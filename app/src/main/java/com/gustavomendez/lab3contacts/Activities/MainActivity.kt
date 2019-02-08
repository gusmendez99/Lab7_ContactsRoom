package com.gustavomendez.lab3contacts.Activities

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.gustavomendez.lab3contacts.Adapters.ContactAdapter
import com.gustavomendez.lab3contacts.Models.Contact
import com.gustavomendez.lab3contacts.Providers.ContactsProvider
import com.gustavomendez.lab3contacts.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var adapter: ContactAdapter
        //Constants for the intent keys
        const val SAVED_CONTACT_ID = "savedContactId"
        const val SAVED_CONTACT_NAME = "savedContactName"
        const val SAVED_CONTACT_PHONE = "savedContactPhone"
        const val SAVED_CONTACT_EMAIL = "savedContactEmail"
        const val SAVED_CONTACT_IMAGE_PATH = "savedContactImagePath"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        my_toolbar.title = "Mis Contactos"
        setSupportActionBar(my_toolbar)

        // Creates a vertical Layout Manager
        rv_contact_list.layoutManager = LinearLayoutManager(this)
        updateDataSet()

        //Setting the recycler adapter
        rv_contact_list.adapter = adapter

        //TODO: Insert into DB as dummy contacts
        /*
        * Contact("Luis Urbina", "54786521", "urbina212@gmail.com"),
            Contact("Mario Perdomo", "41256323", "mario@gmailc.com"),
            Contact("Diego Estrada", "41257895", "diego@gmail.com")
        * */

        btn_add_contact.setOnClickListener {
            startActivity(Intent(this, SaveContactActivity::class.java))
            this.finish()
        }
    }

    private fun updateDataSet() {

        // Access the RecyclerView Adapter and load the data into it
        adapter = ContactAdapter(getContacts(), this) { contact ->
            run {
                //Get a callback with the contact info
                val intent = Intent(this, ContactInfoActivity::class.java)
                intent.putExtra(SAVED_CONTACT_ID, contact._id)
                intent.putExtra(SAVED_CONTACT_NAME, contact.name)
                intent.putExtra(SAVED_CONTACT_PHONE, contact.phone)
                intent.putExtra(SAVED_CONTACT_EMAIL, contact.email)
                intent.putExtra(SAVED_CONTACT_IMAGE_PATH, contact.imagePath)

                startActivity(intent)
                this.finish()

            }
        }

    }

    private fun getContacts(): ArrayList<Contact> {

        val myContacts = ArrayList<Contact>()
        // Retrieve student records
        val URL = "content://com.gustavomendez.ContactsProvider"
        val contacts = Uri.parse(URL)
        val c = contentResolver.query(contacts, null, null, null, "name")

        if (c.moveToFirst()) {
            do {
                myContacts.add(
                    Contact(
                        c.getInt(c.getColumnIndex(ContactsProvider._ID)),
                        c.getString(c.getColumnIndex(ContactsProvider.NAME)),
                        c.getString(c.getColumnIndex(ContactsProvider.PHONE)),
                        c.getString(c.getColumnIndex(ContactsProvider.EMAIL)),
                        c.getString(c.getColumnIndex(ContactsProvider.IMAGE_PATH))
                    )
                )
            } while (c.moveToNext())
        }
        c.close()
        return myContacts
    }


}
