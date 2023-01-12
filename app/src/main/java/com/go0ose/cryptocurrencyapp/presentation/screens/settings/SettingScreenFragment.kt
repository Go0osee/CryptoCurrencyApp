package com.go0ose.cryptocurrencyapp.presentation.screens.settings

import android.icu.text.DateFormat
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.go0ose.cryptocurrencyapp.R
import com.go0ose.cryptocurrencyapp.databinding.FragmentSettingScreenBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingScreenFragment : Fragment(R.layout.fragment_setting_screen) {

    private val binding: FragmentSettingScreenBinding by viewBinding()
    private val viewModel: SettingScreenViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
    }

    private fun initClickListeners() {

        with(binding) {
            dayOfBirth.setOnClickListener {
                showDatePicker()
            }
            photoButton.setOnClickListener {
                showAlertChoosePhoto()
            }
            toolbar.menu.findItem(R.id.save).setOnMenuItemClickListener {
                viewModel.updateUser()
                true
            }
        }
    }

    private fun showAlertChoosePhoto() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.download_photo))
            .setItems(
                arrayOf(
                    getString(R.string.take_picture),
                    getString(R.string.pick_from_gallery)
                )
            ) { _, id ->
                when (id) {
                    0 -> takePicture()
                    1 -> pickFromGallery()
                }
            }.show()
    }

    private fun pickFromGallery() {

    }

    private fun takePicture() {

    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.date_of_birth))
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
            .build()
        datePicker.addOnPositiveButtonClickListener {
            binding.dayOfBirth.setText(DateFormat.getDateInstance().format(it))
        }
        datePicker.show(requireActivity().supportFragmentManager, "")
    }
}