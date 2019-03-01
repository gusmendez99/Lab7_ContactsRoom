package com.gustavomendez.lab3contacts.Activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gustavomendez.lab3contacts.R
import android.content.Intent
import android.net.Uri
import com.google.android.material.snackbar.Snackbar
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.gustavomendez.lab3contacts.Activities.MainActivity.Companion.SAVED_CONTACT_ID
import com.gustavomendez.lab3contacts.Activities.MainActivity.Companion.SAVED_CONTACT_NAME
import com.gustavomendez.lab3contacts.Models.Contact
import com.gustavomendez.lab3contacts.ViewModel.ContactViewModel
import kotlinx.android.synthetic.main.activity_contact_info.*
import kotlinx.android.synthetic.main.toolbar.*


class ContactInfoActivity : AppCompatActivity() {

    companion object {
        const val EDIT_CONTACT_REQUEST = 3
    }

    private lateinit var contactViewModel:ContactViewModel
    private lateinit var currentImage: ByteArray
    private var currentId:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_info)
        my_toolbar.title = "Ver Contacto"
        setSupportActionBar(my_toolbar)

        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)

        tv_contact_name.text = intent.getStringExtra(MainActivity.SAVED_CONTACT_NAME)
        tv_contact_phone.text = intent.getStringExtra(MainActivity.SAVED_CONTACT_PHONE)
        tv_contact_email.text = intent.getStringExtra(MainActivity.SAVED_CONTACT_EMAIL)
        tv_contact_priority.text = intent.getIntExtra(MainActivity.SAVED_CONTACT_PRIORITY, 1).toString()

        currentId = intent.getIntExtra(MainActivity.SAVED_CONTACT_ID, 1)
        currentImage = intent.getByteArrayExtra(MainActivity.SAVED_CONTACT_IMAGE)

        Glide.with(this).load(currentImage).into(iv_contact)




        tv_contact_email.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND)

            //Making the intent for email
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.type = "text/plain"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, intent.getStringExtra(MainActivity.SAVED_CONTACT_EMAIL))
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Mi nombre es Gus Mendez, y mi teléfono es 32349997")

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."))
                Log.i("Enviando correo...", "")
            } catch (ex: android.content.ActivityNotFoundException) {
                Snackbar.make(parent_view, "No hay cliente de email...", Snackbar.LENGTH_LONG).show()
            }
        }

        tv_contact_phone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${intent.getStringExtra(MainActivity.SAVED_CONTACT_PHONE)}"))
            startActivity(intent)
        }

        btn_back.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        btn_edit.setOnClickListener {
            /*val intent = Intent(this, SaveContactActivity::class.java)
            intent.putExtra(MainActivity.SAVED_CONTACT_ID, savedContactId)
            startActivity(intent)
            this.finish()*/
            var intent = Intent(baseContext, SaveContactActivity::class.java)

            intent.putExtra(MainActivity.SAVED_CONTACT_ID, currentId)
            intent.putExtra(MainActivity.SAVED_CONTACT_NAME, tv_contact_name.text.toString())
            intent.putExtra(MainActivity.SAVED_CONTACT_PHONE, tv_contact_phone.text.toString())
            intent.putExtra(MainActivity.SAVED_CONTACT_EMAIL, tv_contact_email.text.toString())
            intent.putExtra(MainActivity.SAVED_CONTACT_PRIORITY, tv_contact_priority.text.toString())
            intent.putExtra(MainActivity.SAVED_CONTACT_IMAGE, currentImage)



            //Glide.with(this).load()).into(iv_contact)



            startActivityForResult(intent, EDIT_CONTACT_REQUEST)
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()

        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EDIT_CONTACT_REQUEST && resultCode == Activity.RESULT_OK) {
            val id = data?.getIntExtra(MainActivity.SAVED_CONTACT_ID, -1)

            val updateContact = Contact(
                data!!.getStringExtra(MainActivity.SAVED_CONTACT_NAME),
                data!!.getStringExtra(MainActivity.SAVED_CONTACT_PHONE),
                data!!.getStringExtra(MainActivity.SAVED_CONTACT_EMAIL),
                data.getIntExtra(MainActivity.SAVED_CONTACT_PRIORITY, 1)
            )

            updateContact.image = data.getByteArrayExtra(MainActivity.SAVED_CONTACT_IMAGE)
            updateContact.id = id!!
            contactViewModel.update(updateContact)

            //Updating contact
            tv_contact_name.text = updateContact.name
            tv_contact_phone.text = updateContact.phone
            tv_contact_email.text = updateContact.email
            tv_contact_priority.text = updateContact.priority.toString()

            currentId = updateContact.id
            currentImage = updateContact.image!!

            Glide.with(this).load(currentImage).into(iv_contact)


            Snackbar.make(parent_view, "Contacto actualizado", Snackbar.LENGTH_LONG).show()

        } else {
            Snackbar.make(parent_view, "Error al actualizar...", Snackbar.LENGTH_LONG).show()
        }


    }

}
