package com.gustavomendez.lab3contacts.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gustavomendez.lab3contacts.MyApplication
import com.gustavomendez.lab3contacts.R
import kotlinx.android.synthetic.main.activity_contact_info.*

class ContactInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_info)

        val savedContactName = intent.getStringExtra(MyApplication.SAVED_CONTACT_NAME)
        val savedContactPhone = intent.getStringExtra(MyApplication.SAVED_CONTACT_PHONE)
        val savedContactEmail = intent.getStringExtra(MyApplication.SAVED_CONTACT_EMAIL)

        tv_contact_name.text = savedContactName
        tv_contact_phone.text = savedContactPhone
        tv_contact_email.text = savedContactEmail

        btn_back.setOnClickListener {
            this.finish()
        }

    }
}
