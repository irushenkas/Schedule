package com.example.schedule.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.schedule.R
import com.example.schedule.databinding.FragmentNewEventBinding
import com.example.schedule.util.AndroidUtils
import com.example.schedule.util.LongArg
import com.example.schedule.util.StringArg
import com.example.schedule.viewmodel.EventViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class NewEventFragment : Fragment() {

    private val eventViewModel: EventViewModel by viewModels()

    companion object {
        var Bundle.scheduleId: Long? by LongArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewEventBinding.inflate(
            inflater,
            container,
            false
        )

        binding.start.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val picker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(0)
                        .build()

                picker.show(parentFragmentManager, "DATE_PICKER")

                picker.addOnPositiveButtonClickListener {
                    binding.start.setText(String.format("%02d:%02d", picker.hour, picker.minute))
                }
            }
        }

        binding.finish.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val picker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(0)
                        .build()

                picker.show(parentFragmentManager, "DATE_PICKER")

                picker.addOnPositiveButtonClickListener {
                    binding.finish.setText(String.format("%02d:%02d", picker.hour, picker.minute))
                }
            }
        }

        val minutes = resources.getStringArray(R.array.notify_minutes)

        if (binding.notifyBeforeMinutes != null) {
            val adapter = context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_item, minutes
                )
            }
            binding.notifyBeforeMinutes.adapter = adapter
        }


//        eventViewModel.jobCreated.observe(viewLifecycleOwner) {
//            findNavController().navigateUp()
//        }

        binding.ok.setOnClickListener {
            eventViewModel.changeContent(
                arguments?.scheduleId!!, binding.name.text.toString(),
                binding.description.text.toString(), binding.start.text.toString(),
                binding.finish.text.toString(),
                Integer.parseInt(binding.notifyBeforeMinutes.selectedItem.toString()), true
            )
            eventViewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }
        return binding.root
    }
}