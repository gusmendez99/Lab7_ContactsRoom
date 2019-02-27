package com.gustavomendez.lab3contacts.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.gustavomendez.lab3contacts.Models.Contact
import com.gustavomendez.lab3contacts.Models.ContactRepository

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: ContactRepository =
        ContactRepository(application)
    private var allContacts: LiveData<List<Contact>> = repository.getAllContacts()

    fun insert(contact: Contact) {
        repository.insert(contact)
    }

    fun update(contact: Contact) {
        repository.update(contact)
    }

    fun delete(contact: Contact) {
        repository.delete(contact)
    }

    fun deleteAllNotes() {
        repository.deleteAllContacts()
    }

    fun getAllContacts(): LiveData<List<Contact>> {
        return allContacts
    }
}