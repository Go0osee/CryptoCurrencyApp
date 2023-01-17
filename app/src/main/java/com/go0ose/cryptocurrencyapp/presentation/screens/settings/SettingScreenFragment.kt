package com.go0ose.cryptocurrencyapp.presentation.screens.settings

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.DateFormat
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.go0ose.cryptocurrencyapp.R
import com.go0ose.cryptocurrencyapp.databinding.FragmentSettingScreenBinding
import com.go0ose.cryptocurrencyapp.utils.setImageByUri
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class SettingScreenFragment : Fragment(R.layout.fragment_setting_screen) {

    private val binding: FragmentSettingScreenBinding by viewBinding()
    private val viewModel: SettingScreenViewModel by viewModel()

    private lateinit var file: File

    private val pickFromGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {

                viewModel.imageUri = it.data?.data.toString()
                binding.avatar.setImageByUri(viewModel.imageUri)
            }
        }

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {

                viewModel.imageUri = file.toUri().toString()
                binding.avatar.setImageByUri(viewModel.imageUri)
            }
        }

    private val requestPermissionCameraLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.permission_is_required),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private val requestPermissionReadStorageLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.permission_is_required),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initValidation()
        initObservers()
    }

    private fun initObservers() {
        with(binding) {
            lifecycleScope.launchWhenStarted {
                viewModel.stateSaveButton.collectLatest { state ->
                    when (state) {
                        true -> {
                            toolbar.menu.findItem(R.id.save).isEnabled = true
                            toolbar.menu.findItem(R.id.save).setIcon(R.drawable.ic_save_unlock)
                        }
                        false -> {
                            toolbar.menu.findItem(R.id.save).isEnabled = false
                            toolbar.menu.findItem(R.id.save).setIcon(R.drawable.ic_save_lock)
                        }
                    }
                }
            }
            lifecycleScope.launchWhenStarted {
                viewModel.user.collectLatest { user ->
                    if (user.avatar.isNotEmpty()) {
                        binding.avatar.setImageByUri(user.avatar)
                    } // TODO!@# use construction with(...) { ... } in all places of project where it is possible
                    binding.firstName.setText(user.firstName, TextView.BufferType.EDITABLE)
                    binding.lastName.setText(user.lastName, TextView.BufferType.EDITABLE)
                    binding.dayOfBirth.setText(user.dayOfBirth, TextView.BufferType.EDITABLE)
                }
            }
            lifecycleScope.launchWhenStarted {
                viewModel.stateSave.collectLatest { state ->
                    if(state){
                        Snackbar.make(binding.root, getString(R.string.saved), Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initListeners() {
        with(binding) {

            dayOfBirth.setOnClickListener {
                showDatePicker()
            }
            photoButton.setOnClickListener {
                showAlertChoosePhoto()
            }
            toolbar.menu.findItem(R.id.save).setOnMenuItemClickListener {
                viewModel.saveToDataBase(
                    binding.firstName.text.toString(),
                    binding.lastName.text.toString(),
                    binding.dayOfBirth.text.toString(),
                )
                true
            }
        }
    }

    private fun initValidation() {
        with(binding) {

            firstName.addTextChangedListener {
                if (firstName.text.length in 1..20 && lastName.text.length in 1..20) {
                    viewModel.setState(true)
                } else {
                    viewModel.setState(false)
                }
            }

            lastName.addTextChangedListener {
                if (firstName.text.length in 1..20 && lastName.text.length in 1..20) {
                    viewModel.setState(true)
                } else {
                    viewModel.setState(false)
                }
            }
        }
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
        checkPermissionReadStorage {
            openGallery()
        }
    }

    private fun checkPermissionReadStorage(function: () -> Unit) {
        if (ContextCompat.checkSelfPermission(
                this.requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionReadStorageLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            function()
        }
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickFromGalleryLauncher.launch(intent)
    }


    private fun takePicture() {
        checkPermissionCamera {
            openCamera()
        }
    }

    private fun checkPermissionCamera(function: () -> Unit) {
        if (ContextCompat.checkSelfPermission(this.requireActivity(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionCameraLauncher.launch(Manifest.permission.CAMERA)
        } else {
            function()
        }
    }

    private fun openCamera() {
        file = File.createTempFile(
            "avatar",
            ".jpeg",
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
        val fileProvider = FileProvider.getUriForFile(
            requireContext(),
            "com.go0ose.cryptocurrencyapp.provider",
            file
        )
        val intent = Intent().apply {
            action = MediaStore.ACTION_IMAGE_CAPTURE
            putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        }

        takePictureLauncher.launch(intent)
    }
}