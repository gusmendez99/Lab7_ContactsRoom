package com.gustavomendez.lab3contacts.Models

class Contact(val name:String, val phone:String, val email:String){

    override fun toString(): String {
        return "$name - $phone"
    }

}

