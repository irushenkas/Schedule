package com.example.schedule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.databinding.EventBinding
import com.example.schedule.dto.Event

interface OnEventInteractionListener {
    fun onShowDetails(event: Event) {}
    fun onRemove(event: Event) {}
    fun onNotify(event: Event) {}
}

class EventAdapter(
    private val onInteractionListener: OnEventInteractionListener,
    private val context: Context?,
) : ListAdapter<Event, EventViewHolder>(EventDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = EventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding, onInteractionListener, context)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }
}

class EventViewHolder(
    private val binding: EventBinding,
    private val onInteractionListener: OnEventInteractionListener,
    private val context: Context?,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(event: Event) {
        binding.apply {
            name.text = event.name
            period.text = String.format("(%s - %s)", event.start, event.finish)

            notify.setOnClickListener {
                onInteractionListener.onNotify(event)
            }
            info.setOnClickListener {
                onInteractionListener.onShowDetails(event)
            }
            delete.setOnClickListener {
                onInteractionListener.onRemove(event)
            }
        }
    }
}

class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }
}