package com.gustavomendez.lab3contacts.Models

/**
 * Class for contacts
 */

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "contacts_table")
data class Contact(val name:String, val phone:String, val email:String, var priority:Int = 0){

    //does it matter if these are private or not?
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var image: ByteArray? = null

    override fun toString(): String {
        //For recycler view purpose
        return "$name - $phone"
    }

}

