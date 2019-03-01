package com.gustavomendez.lab3contacts.Activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.gustavomendez.lab3contacts.Adapters.ContactAdapter
import com.gustavomendez.lab3contacts.Models.Contact
import com.gustavomendez.lab3contacts.R
import com.gustavomendez.lab3contacts.ViewModel.ContactViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {

    private lateinit var contactViewModel: ContactViewModel

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
        rv_contact_list.setHasFixedSize(true)

        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)

        contactViewModel.getAllContacts().observe(this, Observer<List<Contact>> {
            adapter.submitList(it)
        })


        //With Observers...
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                removeContact(viewHolder.adapterPosition)
            }
        }
        ).attachToRecyclerView(rv_contact_list)


        adapter = ContactAdapter { contact ->
            run {
                //Get a callback with the contact info
                val intent = Intent(this, ContactInfoActivity::class.java)
                intent.putExtra(SAVED_CONTACT_ID, contact.id)
                startActivity(intent)
                this.finish()

            }
        }

        //Setting the recycler adapter
        rv_contact_list.adapter = adapter

        btn_add_contact.setOnClickListener {
            startActivity(Intent(this, SaveContactActivity::class.java))
            this.finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.delete_all_notes -> {
                contactViewModel.deleteAllContacts()
                Snackbar.make(parent_view, "Se han eliminado todos los contactos", Snackbar.LENGTH_LONG).show()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun removeContact(position: Int) {
        // Delete contact records
        //TODO: use the 'remove()' function of ContactRepo
        val contactRemoved = adapter.getContactAt(position)
        Snackbar.make(parent_view, "Contacto '${contactRemoved.name}' eliminado", Snackbar.LENGTH_LONG).show()
        contactViewModel.delete(contactRemoved)

    }


}
