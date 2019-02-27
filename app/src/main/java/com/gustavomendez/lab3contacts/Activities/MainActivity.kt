package com.gustavomendez.lab3contacts.Activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.LinearLayoutManager
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        my_toolbar.title = "Mis Contactos"
        setSupportActionBar(my_toolbar)

        // Creates a vertical Layout Manager
        rv_contact_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        // Access the RecyclerView Adapter and load the data into it
        adapter = ContactAdapter(getContacts(), this) { contact, isLongClick ->
            run {
                //Get a callback with the contact info
                if(isLongClick){
                    //Long click used to remove contacts
                    if(removeContact(contact._id)){
                        Snackbar.make(parent_view, "Contacto '${contact.name}' eliminado", Snackbar.LENGTH_LONG).show()
                    } else {
                        Snackbar.make(parent_view, "Error al borrar contacto...", Snackbar.LENGTH_LONG).show()
                    }

                } else {
                    //Simple onClickListener
                    val intent = Intent(this, ContactInfoActivity::class.java)
                    intent.putExtra(SAVED_CONTACT_ID, contact._id)
                    startActivity(intent)
                    this.finish()

                }
            }
        }

        //Setting the recycler adapter
        rv_contact_list.adapter = adapter

        btn_add_contact.setOnClickListener {
            startActivity(Intent(this, SaveContactActivity::class.java))
            this.finish()
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

    private fun removeContact(id: Int):Boolean {
        // Delete contact records
        val URL = "content://com.gustavomendez.ContactsProvider/contacts/$id"
        val contact = Uri.parse(URL)
        val count = contentResolver.delete(contact, null, null)

        return count > 0
    }


}
