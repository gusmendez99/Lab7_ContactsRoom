package com.gustavomendez.lab3contacts.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gustavomendez.lab3contacts.Models.Contact
import com.gustavomendez.lab3contacts.R
import kotlinx.android.synthetic.main.contact_list_item.view.*

class ContactAdapter(
    private val items : ArrayList<Contact>, private val context: Context,
    private val listener: (Contact) -> Unit) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.contact_list_item, parent, false))
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position],listener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(contact: Contact, listener: (Contact) -> Unit) = with(itemView) {

            itemView.tv_contact_info.text = contact.toString()
            setOnClickListener { listener(contact) }
        }
    }
}

