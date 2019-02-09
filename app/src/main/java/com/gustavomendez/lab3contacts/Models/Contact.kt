package com.gustavomendez.lab3contacts.Models

/**
 * Class for contacts
 */
class Contact(val _id: Int, val name:String, val phone:String, val email:String, val imagePath:String = ""){

    override fun toString(): String {
        //For recycler view purpose
        return "$name - $phone"
    }

}

