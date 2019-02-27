package com.gustavomendez.lab3contacts.Adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gustavomendez.lab3contacts.Models.Contact
import com.gustavomendez.lab3contacts.R
import kotlinx.android.synthetic.main.contact_list_item.view.*

class ContactAdapter(
    private val items : ArrayList<Contact>, private val context: Context,
    private val listener: (Contact, Boolean) -> Unit) : androidx.recyclerview.widget.RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    // Gets the number of contatcs in the list
    override fun getItemCount(): Int {
        return items.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.contact_list_item, parent, false))
    }

    // Binds each contact in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position], listener)
    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        //Binding each item, with a listener
        fun bindItems(contact: Contact, listener: (Contact, Boolean) -> Unit) = with(itemView) {

            itemView.tv_contact_info.text = contact.toString()
            //Listener return false if there's a single click
            setOnClickListener { listener(contact, false) }
            //Listener return true if there's a long click
            setOnLongClickListener {
                listener(contact, true)
                items.removeAt(adapterPosition)
                notifyDataSetChanged()
                return@setOnLongClickListener true
            }
        }
    }


}

