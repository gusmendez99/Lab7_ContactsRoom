package com.gustavomendez.lab3contacts.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gustavomendez.lab3contacts.MyApplication
import com.gustavomendez.lab3contacts.R
import android.widget.Toast
import android.content.Intent
import android.net.Uri
import android.support.design.widget.Snackbar
import android.util.Log
import kotlinx.android.synthetic.main.activity_contact_info.*
import kotlinx.android.synthetic.main.toolbar.*


class ContactInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_info)
        my_toolbar.title = "Ver Contacto"
        setSupportActionBar(my_toolbar)

        val savedContactName = intent.getStringExtra(MyApplication.SAVED_CONTACT_NAME)
        val savedContactPhone = intent.getStringExtra(MyApplication.SAVED_CONTACT_PHONE)
        val savedContactEmail = intent.getStringExtra(MyApplication.SAVED_CONTACT_EMAIL)

        tv_contact_name.text = savedContactName
        tv_contact_phone.text = savedContactPhone
        tv_contact_email.text = savedContactEmail

        tv_contact_email.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND)

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
            this.finish()
        }

    }


}
