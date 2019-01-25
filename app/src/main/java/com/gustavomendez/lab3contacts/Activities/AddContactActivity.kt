package com.gustavomendez.lab3contacts.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gustavomendez.lab3contacts.Models.Contact
import com.gustavomendez.lab3contacts.MyApplication
import com.gustavomendez.lab3contacts.R
import kotlinx.android.synthetic.main.activity_add_contact.*

class AddContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        btn_create_contact.setOnClickListener {
            val contactName = et_contact_name.text.toString()
            val contactPhone = et_contact_phone.text.toString()
            val contactEmail = et_contact_email.text.toString()

            if(contactName.isNotEmpty() && contactPhone.isNotEmpty() && contactEmail.isNotEmpty()) {
                val newContact = Contact(contactName, contactPhone, contactEmail)
                MyApplication.addContact(newContact)
                Toast.makeText(this, "¡Contacto creado!", Toast.LENGTH_LONG).show()
                MainActivity.adapter.notifyDataSetChanged()

                et_contact_name.text.clear()
                et_contact_phone.text.clear()
                et_contact_email.text.clear()

            } else {
                Toast.makeText(this, "Todos los campos deben ser llenados...", Toast.LENGTH_LONG).show()
            }

        }

        btn_back.setOnClickListener {
            this.finish()
        }
    }
}
