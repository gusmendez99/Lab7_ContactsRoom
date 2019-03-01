package com.gustavomendez.lab3contacts.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gustavomendez.lab3contacts.R
import android.content.Intent
import android.net.Uri
import com.google.android.material.snackbar.Snackbar
import android.util.Log
import com.bumptech.glide.Glide
import com.gustavomendez.lab3contacts.Activities.MainActivity.Companion.SAVED_CONTACT_ID
import com.gustavomendez.lab3contacts.Models.Contact
import kotlinx.android.synthetic.main.activity_contact_info.*
import kotlinx.android.synthetic.main.toolbar.*


class ContactInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_info)
        my_toolbar.title = "Ver Contacto"
        setSupportActionBar(my_toolbar)

        //Parsing data from the intent
        val savedContactId = intent.getIntExtra(SAVED_CONTACT_ID, -1)
        val savedContact = getContact(savedContactId)

        tv_contact_name.text = savedContact?.name
        tv_contact_phone.text = savedContact?.phone
        tv_contact_email.text = savedContact?.email

        if(savedContact!!.imagePath.isNotEmpty()){
            Glide.with(this).load(savedContact!!.imagePath).into(iv_contact)
        }

        tv_contact_email.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND)

            //Making the intent for email
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.type = "text/plain"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, savedContact!!.email)
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Mi nombre es Gus Mendez, y mi teléfono es 32349997")

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."))
                Log.i("Enviando correo...", "")
            } catch (ex: android.content.ActivityNotFoundException) {
                Snackbar.make(parent_view, "No hay cliente de email...", Snackbar.LENGTH_LONG).show()
            }
        }

        tv_contact_phone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${savedContact.phone}"))
            startActivity(intent)
        }

        btn_back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        btn_edit.setOnClickListener {
            val intent = Intent(this, SaveContactActivity::class.java)
            intent.putExtra(MainActivity.SAVED_CONTACT_ID, savedContactId)
            startActivity(intent)
            this.finish()
        }


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

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

}
