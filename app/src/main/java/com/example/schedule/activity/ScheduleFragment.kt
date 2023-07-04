package com.example.schedule.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.schedule.R
import com.example.schedule.adapter.OnInteractionListener
import com.example.schedule.adapter.ScheduleAdapter
import com.example.schedule.databinding.FragmentScheduleBinding
import com.example.schedule.dto.Schedule
import com.example.schedule.service.RemindersManager
import com.example.schedule.viewmodel.ScheduleViewModel

class ScheduleFragment : Fragment() {

    private val scheduleViewModel: ScheduleViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleBinding.inflate(
            inflater,
            container,
            false
        )
        val adapter = ScheduleAdapter(object : OnInteractionListener {
            override fun onShowDetails(schedule: Schedule) {
                val bundle = Bundle()
                bundle.putLong("scheduleId", schedule.id)
                bundle.putString("name", schedule.name)
                findNavController().navigate(R.id.action_scheduleFragment_to_eventFragment, bundle)
            }

            override fun onRemove(schedule: Schedule) {
                scheduleViewModel.removeById(schedule.id)
            }

            override fun onSelect(schedule: Schedule) {
                scheduleViewModel.changeActive(schedule)
            }
        }, context)

        binding.list.adapter = adapter
        scheduleViewModel.data.observe(viewLifecycleOwner) { schedules ->
            adapter.submitList(schedules)
        }

        binding.add.setOnClickListener {
            findNavController().navigate(R.id.action_scheduleFragment_to_newScheduleFragment)
        }

        return binding.root
    }
}