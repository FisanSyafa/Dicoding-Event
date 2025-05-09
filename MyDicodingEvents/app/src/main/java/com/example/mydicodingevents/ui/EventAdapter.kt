package com.example.mydicodingevents.ui

import com.example.mydicodingevents.data.remote.response.ListEventsItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mydicodingevents.databinding.ItemRowEventBinding

class EventAdapter(private val eventList: OnItemClickCallback) : ListAdapter<ListEventsItem, EventAdapter.EventViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemRowEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding, eventList)
    }
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }
    class EventViewHolder(private val binding: ItemRowEventBinding, private val listener: OnItemClickCallback) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem){
            val context = binding.root.context

            binding.tvItemName.text = event.name
            Glide.with(context)
                .load(event.imageLogo)
                .into(binding.imgEventPhoto)
            binding.root.setOnClickListener {
                listener.onItemClick(event)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClick(event: ListEventsItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}