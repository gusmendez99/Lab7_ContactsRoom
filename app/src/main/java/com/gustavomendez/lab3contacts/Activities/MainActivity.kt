package com.gustavomendez.lab3contacts.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.gustavomendez.lab3contacts.Adapters.ContactAdapter
import com.gustavomendez.lab3contacts.MyApplication
import com.gustavomendez.lab3contacts.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var adapter: ContactAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Creates a vertical Layout Manager
        rv_contact_list.layoutManager = LinearLayoutManager(this)

        // Access the RecyclerView Adapter and load the data into it
        adapter = ContactAdapter(MyApplication.getContacts(), this) { contact ->
            run {
                //TODO: show clicked contact
                val intent = Intent(this, ContactInfoActivity::class.java)
                intent.putExtra(MyApplication.SAVED_CONTACT_NAME, contact.name)
                intent.putExtra(MyApplication.SAVED_CONTACT_PHONE, contact.phone)
                intent.putExtra(MyApplication.SAVED_CONTACT_EMAIL, contact.email)

                startActivity(intent)

            }
        }

        rv_contact_list.adapter = adapter

        btn_add_contact.setOnClickListener {
            startActivity(Intent(this, AddContactActivity::class.java))
        }
    }


}
