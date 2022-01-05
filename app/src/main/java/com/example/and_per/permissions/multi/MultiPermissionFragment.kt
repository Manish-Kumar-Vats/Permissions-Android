package com.example.and_per.permissions.multi

import android.Manifest.permission.*
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.and_per.databinding.FragmentMultiPermissionBinding
import com.example.and_per.utils.hasPermission
import com.google.android.material.snackbar.Snackbar

class MultiPermissionFragment : Fragment() {

    private var _binding: FragmentMultiPermissionBinding? = null
    private val binding get() = _binding!!

    private val tempPermissionArray = mutableListOf<String>()

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    private val requestMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            if (!it.value) {
                binding.textbox.text = "${it.key} permission denied"
                askPermissionAgain(it.key)
                return@registerForActivityResult
            }
        }
        binding.textbox.text = "All permissions granted"
    }

    private fun askPermissionAgain(key: String) {
        //below line means, the permission is permanently denied
        if (!shouldShowRequestPermissionRationale(key)) {
            binding.textbox.text = "$key permission permanently denied"
            AlertDialog.Builder(requireContext())
                .setMessage("You have permanently denied location permission, go to settings to enable this permission")
                .setPositiveButton("Go to settings") { _: DialogInterface?, _: Int -> openSettings() }
                .setCancelable(false)
                .show()
        } else {
            Snackbar.make(
                binding.root,
                "Permissions are required for XYZ features",
                Snackbar.LENGTH_INDEFINITE
            ).setAction("Enable") {
                requestMultiplePermissions.launch(tempPermissionArray.toTypedArray())
            }.show()
        }
    }

    private fun openSettings() {
        resultLauncher.launch(Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", requireContext().packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMultiPermissionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListener()
    }

    private fun initClickListener() {
        binding.btnPermissions.setOnClickListener {
            checkAndRequestPermissions(
                //Add or Remove the permissions as per your request
                mutableListOf(
                    CAMERA,
                    READ_PHONE_STATE,
                    READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE,
                    READ_EXTERNAL_STORAGE,
                    ACCESS_FINE_LOCATION,
                )
            )
        }
    }


    private fun checkAndRequestPermissions(permissionsArray: MutableList<String>) {
        permissionsArray.forEach {
            if (!requireContext().hasPermission(it))
                tempPermissionArray.add(it)
        }
        if (tempPermissionArray.isNotEmpty())
            requestMultiplePermissions.launch(tempPermissionArray.toTypedArray())
        else binding.textbox.text = "already granted"
    }

}