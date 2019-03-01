package com.gustavomendez.lab3contacts.Models

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactDao {

    @Insert
    fun insert(contact: Contact)

    @Update
    fun update(contact: Contact)

    @Delete
    fun delete(contact: Contact)

    @Query("DELETE FROM contacts_table")
    fun deleteAllContacts()

    @Query("SELECT * FROM contacts_table ORDER BY name DESC")
    fun getAllContacts(): LiveData<List<Contact>>

    @Query("SELECT * FROM contacts_table WHERE id == :contactId")
    fun getContact(contactId: Int): List<Contact>

}