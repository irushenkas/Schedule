package com.example.schedule.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.schedule.R
import com.example.schedule.databinding.FragmentEventDetailsBinding
import com.example.schedule.util.BooleanArg
import com.example.schedule.util.IntArg
import com.example.schedule.util.LongArg
import com.example.schedule.util.StringArg
import com.example.schedule.viewmodel.EventViewModel

class EventDetailsFragment : Fragment() {

    private val eventViewModel: EventViewModel by viewModels()

    companion object {
        var Bundle.id: Long? by LongArg
        var Bundle.name: String? by StringArg
        var Bundle.description: String? by StringArg
        var Bundle.timeStart: String? by StringArg
        var Bundle.timeFinish: String? by StringArg
        var Bundle.repeat: Boolean? by BooleanArg
        var Bundle.repeatAfterMinutes: Int? by IntArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEventDetailsBinding.inflate(
            inflater,
            container,
            false
        )

        binding.name.text = arguments?.name!!
        if(arguments?.description != null) {
            binding.description.text = arguments?.description
        }
        binding.time.text = this.resources.getString(R.string.event_time,
            arguments?.timeStart!!, arguments?.timeFinish!!)

        if(arguments?.repeat != null && arguments?.repeat!!) {
            eventViewModel.repeatNotification(arguments?.id!!, arguments?.repeatAfterMinutes!!)
        }

        return binding.root
    }
}