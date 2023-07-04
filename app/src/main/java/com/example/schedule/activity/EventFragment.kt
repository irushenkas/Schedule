package com.example.schedule.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.schedule.R
import com.example.schedule.adapter.EventAdapter
import com.example.schedule.adapter.OnEventInteractionListener
import com.example.schedule.databinding.FragmentEventBinding
import com.example.schedule.dto.Event
import com.example.schedule.util.LongArg
import com.example.schedule.viewmodel.EventViewModel


class EventFragment : Fragment() {

    companion object {
        var Bundle.scheduleId: Long? by LongArg
    }

    val eventViewModel: EventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEventBinding.inflate(
            inflater,
            container,
            false
        )
        val adapter = EventAdapter(object : OnEventInteractionListener {
            override fun onShowDetails(event: Event) {
                val bundle = Bundle()
                bundle.putString("name", event.name)
                bundle.putString("description", event.description)
                bundle.putString("timeStart", event.start)
                bundle.putString("timeFinish", event.finish)
                findNavController().navigate(R.id.action_eventFragment_to_eventDetailsFragment, bundle)
            }

            override fun onRemove(event: Event) {
                eventViewModel.removeById(event.id)
            }

            override fun onNotify(event: Event) {
                eventViewModel.notify(event)
            }
        }, context)

        binding.list.adapter = adapter

        eventViewModel.data?.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
        }

        binding.add.setOnClickListener {
            val bundle = Bundle()
            bundle.putLong("scheduleId", arguments?.scheduleId!!)
            findNavController().navigate(R.id.action_eventFragment_to_newEventFragment, bundle)
        }

        return binding.root
    }
}