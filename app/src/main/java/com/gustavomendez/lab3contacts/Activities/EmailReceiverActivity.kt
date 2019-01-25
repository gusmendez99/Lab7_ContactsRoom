package com.gustavomendez.lab3contacts.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.Toast
import com.gustavomendez.lab3contacts.R
import kotlinx.android.synthetic.main.activity_email_receiver.*
import kotlinx.android.synthetic.main.toolbar.*

class EmailReceiverActivity : AppCompatActivity() {

    lateinit var sender:String
    lateinit var receiver:String
    lateinit var message:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_receiver)
        my_toolbar.title = "Enviar Correo"
        setSupportActionBar(my_toolbar)


        when {
            intent?.action == Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    handleSendText(intent) // Handle text being sent
                }
            }
        }

        btn_send.setOnClickListener {
            sender = et_email_sender.text.toString()
            receiver = et_email_receiver.text.toString()
            message = et_email_message.text.toString()

            Snackbar.make(parent_view, "$message enviado desde $sender hacia $receiver", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun handleSendText (intent: Intent) {

        message = intent.getStringExtra(Intent.EXTRA_TEXT)
        receiver = intent.getStringExtra(Intent.EXTRA_EMAIL)

        et_email_message.setText(message)
        et_email_receiver.setText(receiver)
    }

}
