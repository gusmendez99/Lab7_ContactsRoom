package com.gustavomendez.lab3contacts.Adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.gustavomendez.lab3contacts.Models.Contact
import com.gustavomendez.lab3contacts.R
import kotlinx.android.synthetic.main.contact_list_item.view.*

class ContactAdapter(private val listener: (Contact) -> Unit): ListAdapter<Contact, ContactAdapter.ViewHolder>(DIFF_CALLBACK) {


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Contact>() {
            override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem.name == newItem.name && oldItem.phone == newItem.phone
                        && oldItem.priority == newItem.priority && oldItem.email == newItem.email
            }
        }
    }


    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.contact_list_item, parent, false))
    }

    // Binds each contact in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentContact: Contact = getItem(position)
        holder.bindItems(currentContact, listener)
    }

    fun getContactAt(position: Int): Contact {
        return getItem(position)
    }


    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        //Binding each item, with a listener
        fun bindItems(contact: Contact, listener: (Contact) -> Unit) = with(itemView) {

            itemView.text_view_priority.text = contact.priority.toString()
            itemView.text_view_title.text = contact.name
            itemView.text_view_phone.text = contact.phone

            //Listener return false if there's a single click
            setOnClickListener { listener(contact) }
            //Listener return true if there's a long click
            /*setOnLongClickListener {
                listener(contact)
                items.removeAt(adapterPosition)
                notifyDataSetChanged()
                return@setOnLongClickListener true
            }*/
        }
    }


}

