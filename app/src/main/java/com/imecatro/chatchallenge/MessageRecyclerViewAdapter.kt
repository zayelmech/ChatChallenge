package com.imecatro.chatchallenge

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import com.imecatro.chatchallenge.databinding.FragmentMessageBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MessageRecyclerViewAdapter(
    private val values: MutableList<MessageFragment.FastChat>
) : RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder>() {

    fun updateList(newList: List<MessageFragment.FastChat>) {
        values.clear()
        values.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.message
        if (item.user == "A") {
            holder.itemView.setBackgroundColor(
                getColor(
                    holder.itemView.context,
                    R.color.teal_700
                )
            )
        } else {
            holder.itemView.setBackgroundColor(
                getColor(
                    holder.itemView.context,
                    R.color.purple_200
                )
            )
        }


        // holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        //val contentView: TextView = binding.content

//        override fun toString(): String {
//            return super.toString() + " '" + contentView.text + "'"
//        }
    }

}