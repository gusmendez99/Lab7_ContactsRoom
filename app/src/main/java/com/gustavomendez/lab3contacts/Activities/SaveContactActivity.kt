package com.gustavomendez.lab3contacts.Activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.google.android.material.snackbar.Snackbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.gustavomendez.lab3contacts.Models.Contact
import com.gustavomendez.lab3contacts.R
import com.gustavomendez.lab3contacts.ViewModel.ContactViewModel
import kotlinx.android.synthetic.main.activity_save_contact.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.ByteArrayOutputStream
import java.io.IOException

class SaveContactActivity : AppCompatActivity() {

    companion object {
        //private const val IMAGE_DIRECTORY = "/demonuts"
        private const val EXTERNAL_WRITE_REQUEST_CODE = 112
        private const val GALLERY = 1
        private const val CAMERA = 2
    }

    private lateinit var contactViewModel: ContactViewModel
    private var currentId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_contact)

        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)

        //Specific setup of view
        number_picker_priority.minValue = 1
        number_picker_priority.maxValue = 10

        val savedContactId = intent.getIntExtra(MainActivity.SAVED_CONTACT_ID, -1)

        if (savedContactId != -1) {
            my_toolbar.title = "Editar Contacto"
            btn_save_contact.text = getString(R.string.save)


            et_contact_name.setText(intent.getStringExtra(MainActivity.SAVED_CONTACT_NAME))
            et_contact_phone.setText(intent.getStringExtra(MainActivity.SAVED_CONTACT_PHONE))
            et_contact_email.setText(intent.getStringExtra(MainActivity.SAVED_CONTACT_EMAIL))
            number_picker_priority.value = intent.getIntExtra(MainActivity.SAVED_CONTACT_PRIORITY, 1)

            currentId = savedContactId

            setupPermissions()

            //Setting up the image with Glide, if the path is not null
            Glide.with(this).load(intent.getByteArrayExtra(MainActivity.SAVED_CONTACT_IMAGE)).into(iv_contact)


        } else {
            my_toolbar.title = "Nuevo Contacto"
        }


        //General setup of view
        setSupportActionBar(my_toolbar)

        btn_save_contact.setOnClickListener {

            val bitmap = (iv_contact.drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            val contactImage = stream.toByteArray()

            if (et_contact_name.text.toString().isNotEmpty() && et_contact_phone.text.toString().isNotEmpty()
                && et_contact_email.text.toString().isNotEmpty()) {
                //val newContact = Contact(contactName, contactPhone, contactEmail)
                //MyApplication.addContact(newContact)

                    val data = Intent().apply {
                        putExtra(MainActivity.SAVED_CONTACT_NAME, et_contact_name.text.toString())
                        putExtra(MainActivity.SAVED_CONTACT_PHONE, et_contact_phone.text.toString())
                        putExtra(MainActivity.SAVED_CONTACT_EMAIL, et_contact_email.text.toString())
                        putExtra(MainActivity.SAVED_CONTACT_PRIORITY, number_picker_priority.value)
                        putExtra(MainActivity.SAVED_CONTACT_IMAGE, contactImage)
                        if (currentId != -1) {
                            putExtra(MainActivity.SAVED_CONTACT_ID, currentId)
                        }
                    }

                    setResult(Activity.RESULT_OK, data)
                    finish()

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
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("SaveContactActivity", "Permission to write declined")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            EXTERNAL_WRITE_REQUEST_CODE)
    }

    private fun showPictureDialog() {
        //Creating a new dialog, no permissions needed
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallery()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun choosePhotoFromGallery() {
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
            //To get the contact photo from the gallery
            if (data != null) {
                val contentURI = data!!.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    //saveImage(bitmap)
                    Toast.makeText(this@SaveContactActivity, "Imagen establecida exitosamente!", Toast.LENGTH_SHORT)
                        .show()
                    iv_contact!!.setImageBitmap(bitmap)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@SaveContactActivity, "Carga fallida!", Toast.LENGTH_SHORT).show()
                }

            }

        } else if (requestCode == CAMERA) {
            //For get the contact photo from the camera
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            iv_contact!!.setImageBitmap(thumbnail)
            //saveImage(thumbnail)
            Toast.makeText(this@SaveContactActivity, "Imagen guardada!", Toast.LENGTH_SHORT).show()
        }
    }

    /*private fun saveImage(myBitmap: Bitmap): String {
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
            //Creating the image on a temp directory
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
    }*/


    /**
     * Method to accept/decline permission on real time
     */
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

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}
