package com.gustavomendez.lab3contacts.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gustavomendez.lab3contacts.R
import android.content.Intent
import android.net.Uri
import android.support.design.widget.Snackbar
import android.util.Log
import com.bumptech.glide.Glide
import com.gustavomendez.lab3contacts.Activities.MainActivity.Companion.SAVED_CONTACT_EMAIL
import com.gustavomendez.lab3contacts.Activities.MainActivity.Companion.SAVED_CONTACT_ID
import com.gustavomendez.lab3contacts.Activities.MainActivity.Companion.SAVED_CONTACT_IMAGE_PATH
import com.gustavomendez.lab3contacts.Activities.MainActivity.Companion.SAVED_CONTACT_NAME
import com.gustavomendez.lab3contacts.Activities.MainActivity.Companion.SAVED_CONTACT_PHONE
import com.gustavomendez.lab3contacts.Models.Contact
import com.gustavomendez.lab3contacts.Providers.ContactsProvider
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
        //TODO: get this data with Content Provider
        val savedContactName = intent.getStringExtra(SAVED_CONTACT_NAME)
        val savedContactPhone = intent.getStringExtra(SAVED_CONTACT_PHONE)
        val savedContactEmail = intent.getStringExtra(SAVED_CONTACT_EMAIL)
        val savedContactPhotoPath = intent.getStringExtra(SAVED_CONTACT_IMAGE_PATH)

        tv_contact_name.text = savedContactName
        tv_contact_phone.text = savedContactPhone
        tv_contact_email.text = savedContactEmail

        if(savedContactPhotoPath.isNotEmpty()){
            Glide.with(this).load(savedContactPhotoPath).into(iv_contact)
        }

        tv_contact_email.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND)

            //Making the intent for email
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.type = "text/plain"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, savedContactEmail)
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Mi nombre es Gus Mendez, y mi teléfono es 32349997")

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."))
                Log.i("Enviando correo...", "")
            } catch (ex: android.content.ActivityNotFoundException) {
                Snackbar.make(parent_view, "No hay cliente de email...", Snackbar.LENGTH_LONG).show()
            }
        }

        tv_contact_phone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$savedContactPhone"))
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


}
