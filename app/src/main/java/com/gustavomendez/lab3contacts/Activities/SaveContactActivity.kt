package com.gustavomendez.lab3contacts.Activities

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.gustavomendez.lab3contacts.Models.Contact
import com.gustavomendez.lab3contacts.Providers.ContactsProvider
import com.gustavomendez.lab3contacts.R
import kotlinx.android.synthetic.main.activity_save_contact.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class SaveContactActivity : AppCompatActivity() {


    companion object {
        private const val IMAGE_DIRECTORY = "/demonuts"
        private const val GALLERY = 1
        private const val CAMERA = 2
    }

    private var photoPath:String = ""
    private val EXTERNAL_WRITE_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_contact)

        val savedContactId = intent.getIntExtra(MainActivity.SAVED_CONTACT_ID, -1)

        if (savedContactId != -1) {
            my_toolbar.title = "Editar Contacto"
            btn_save_contact.text = getString(R.string.save)

            //Get only one contact
            val currentContact = getContact(savedContactId)
            et_contact_name.setText(currentContact!!.name)
            et_contact_phone.setText(currentContact!!.phone)
            et_contact_email.setText(currentContact!!.email)

            setupPermissions()

            if(currentContact.imagePath.isNotEmpty()) {
                photoPath = currentContact.imagePath
                Glide.with(this).load(currentContact.imagePath).into(iv_contact)
            }


        } else {
            my_toolbar.title = "Nuevo Contacto"
        }

        setSupportActionBar(my_toolbar)

        btn_save_contact.setOnClickListener {
            val contactName = et_contact_name.text.toString()
            val contactPhone = et_contact_phone.text.toString()
            val contactEmail = et_contact_email.text.toString()

            if (contactName.isNotEmpty() && contactPhone.isNotEmpty() && contactEmail.isNotEmpty()) {
                //val newContact = Contact(contactName, contactPhone, contactEmail)
                //MyApplication.addContact(newContact)

                val values = ContentValues()
                values.put(ContactsProvider.NAME, contactName)
                values.put(ContactsProvider.PHONE, contactPhone)
                values.put(ContactsProvider.EMAIL, contactEmail)
                Log.d("VALUE OF PATH", photoPath)
                values.put(ContactsProvider.IMAGE_PATH, photoPath) //TODO: Set the path of the ImageView

                if (savedContactId != -1) {
                    val uri = contentResolver.update(
                        Uri.parse("${ContactsProvider.URL}/$savedContactId"),
                        values, null, null
                    )
                    Snackbar.make(parent_view, "¡Contacto actualizado!", Snackbar.LENGTH_LONG).show()


                } else {
                    val uri = contentResolver.insert(ContactsProvider.CONTENT_URI, values)
                    et_contact_name.text.clear()
                    et_contact_phone.text.clear()
                    et_contact_email.text.clear()

                    Snackbar.make(parent_view, "¡Contacto creado!", Snackbar.LENGTH_LONG).show()
                }
                //Toast.makeText(baseContext, uri!!.toString(), Toast.LENGTH_LONG).show()

            } else {
                Snackbar.make(parent_view, "Todos los campos deben ser llenados...", Snackbar.LENGTH_LONG).show()
            }

        }

        iv_contact.setOnClickListener {
            showPictureDialog()
        }

        btn_back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.RECORD_AUDIO)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("SaveContactActivity", "Permission to record denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            EXTERNAL_WRITE_REQUEST_CODE)
    }

    private fun showPictureDialog() {

        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    photoPath = saveImage(bitmap)
                    Toast.makeText(this@SaveContactActivity, "Imagen establecida exitosamente!", Toast.LENGTH_SHORT)
                        .show()
                    iv_contact!!.setImageBitmap(bitmap)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@SaveContactActivity, "Carga fallida!", Toast.LENGTH_SHORT).show()
                }

            }

        } else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            iv_contact!!.setImageBitmap(thumbnail)
            photoPath = saveImage(thumbnail)
            Toast.makeText(this@SaveContactActivity, "Imagen guardada!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY
        )
        // have the object build the directory structure, if needed.
        Log.d("fee", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {

            wallpaperDirectory.mkdirs()
        }

        try {
            Log.d("heel", wallpaperDirectory.toString())
            val f = File(
                wallpaperDirectory, ((Calendar.getInstance()
                    .timeInMillis).toString() + ".jpg")
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                this,
                arrayOf(f.path),
                arrayOf("image/jpeg"), null
            )
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.absolutePath)

            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }


    private fun getContact(id: Int): Contact? {
        var currentContact: Contact? = null
        // Retrieve student records
        val URL = "content://com.gustavomendez.ContactsProvider/contacts/$id"
        val contact = Uri.parse(URL)
        val c = contentResolver.query(contact, null, null, null, "name")

        if (c.moveToFirst()) {
            currentContact = Contact(
                c.getColumnIndex(ContactsProvider._ID),
                c.getString(c.getColumnIndex(ContactsProvider.NAME)),
                c.getString(c.getColumnIndex(ContactsProvider.PHONE)),
                c.getString(c.getColumnIndex(ContactsProvider.EMAIL)),
                c.getString(c.getColumnIndex(ContactsProvider.IMAGE_PATH))
            )

        }
        c.close()

        return currentContact
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            EXTERNAL_WRITE_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("SaveContactActivity", "Permission has been denied by user")
                } else {
                    Log.i("SaveContactActivity", "Permission has been granted by user")
                }
            }
        }
    }
}
