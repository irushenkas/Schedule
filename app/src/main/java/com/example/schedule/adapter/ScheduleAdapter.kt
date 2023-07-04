package com.example.schedule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.databinding.ScheduleBinding
import com.example.schedule.dto.Schedule

interface OnInteractionListener {
    fun onShowDetails(schedule: Schedule) {}
    fun onRemove(schedule: Schedule) {}
    fun onSelect(schedule: Schedule) {}
}

class ScheduleAdapter(
    private val onInteractionListener: OnInteractionListener,
    private val context: Context?,
) : ListAdapter<Schedule, ScheduleViewHolder>(ScheduleDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = ScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleViewHolder(binding, onInteractionListener, context)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = getItem(position)
        holder.bind(schedule)
    }
}

class ScheduleViewHolder(
    private val binding: ScheduleBinding,
    private val onInteractionListener: OnInteractionListener,
    private val context: Context?,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(schedule: Schedule) {
        binding.apply {
            name.text = schedule.name

            if(schedule.isActive) {
                context?.resources?.let {
                    card.setBackgroundColor(it.getColor(R.color.purple_light))
                    check.setBackgroundResource(R.drawable.ic_alarm_on_24dp)
                }
            } else {
                context?.resources?.let {
                    check.setBackgroundResource(R.drawable.ic_alarm_24dp)
                }
            }
            check.setOnClickListener {
                if(!schedule.isActive) {
                    context?.resources?.let { check.setBackgroundResource(R.drawable.ic_alarm_on_24dp) }
                } else {
                    context?.resources?.let { check.setBackgroundResource(R.drawable.ic_alarm_24dp) }
                }

                onInteractionListener.onSelect(schedule)
            }
            info.setOnClickListener {
                onInteractionListener.onShowDetails(schedule)
            }
            delete.setOnClickListener {
                onInteractionListener.onRemove(schedule)
            }
        }
    }
}

class ScheduleDiffCallback : DiffUtil.ItemCallback<Schedule>() {
    override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
        return oldItem == newItem
    }
}