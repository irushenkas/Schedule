package com.example.schedule.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.schedule.databinding.FragmentNewScheduleBinding
import com.example.schedule.util.AndroidUtils
import com.example.schedule.viewmodel.ScheduleViewModel

class NewScheduleFragment : Fragment() {

    private val scheduleViewModel: ScheduleViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewScheduleBinding.inflate(
            inflater,
            container,
            false
        )

        binding.ok.setOnClickListener {
            scheduleViewModel.changeContent(binding.edit.text.toString())
            scheduleViewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }
        return binding.root
    }
}