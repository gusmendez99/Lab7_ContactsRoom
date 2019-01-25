package com.gustavomendez.lab3contacts

import android.app.Application
import com.gustavomendez.lab3contacts.Models.Contact

class MyApplication: Application() {

    companion object {
        //Constants for the intent keys
        const val SAVED_CONTACT_NAME = "savedContactName"
        const val SAVED_CONTACT_PHONE = "savedContactPhone"
        const val SAVED_CONTACT_EMAIL = "savedContactEmail"

        //Dummy data
        private val myContacts = arrayListOf<Contact>(
            Contact("Luis Urbina", "54786521", "urbina212@gmail.com"),
            Contact("Mario Perdomo", "41256323", "mario@gmailc.com"),
            Contact("Diego Estrada", "41257895", "diego@gmail.com")
        )

        fun addContact(newContact: Contact) {
            myContacts.add(newContact)
        }

        fun getContacts():ArrayList<Contact> {
            return myContacts
        }

    }





}