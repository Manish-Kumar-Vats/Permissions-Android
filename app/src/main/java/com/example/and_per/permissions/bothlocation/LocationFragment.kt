package com.example.and_per.permissions.bothlocation

import android.Manifest
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
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.and_per.R
import com.example.and_per.databinding.FragmentLocationBinding
import com.example.and_per.utils.hasPermission

class LocationFragment : Fragment() {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
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
        binding.btnPermissions.setOnClickListener { requestBothLocationPermissions() }
    }

    private fun bothPermissionGiven() {
        binding.textbox.text = "All permissions granted"
    }

    private fun requestBothLocationPermissions() {
        val hasForegroundPermission =
            requireContext().hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        val hasBackgroundPermission =
            requireContext().hasPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)

        val permissionsArray = mutableListOf<String>()
        when {
            hasForegroundPermission && hasBackgroundPermission -> bothPermissionGiven()
            !hasForegroundPermission -> permissionsArray.add(Manifest.permission.ACCESS_FINE_LOCATION)
            !hasBackgroundPermission -> permissionsArray.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
        requestMultiplePermissions.launch(permissionsArray.toTypedArray())
    }


    private val requestMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            if (!it.value) {
                askPermissionAgain(it.key)
                return@registerForActivityResult
            }
        }
        requestBothLocationPermissions()
    }

    private fun askPermissionAgain(key: String) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), key))

        //you can disable the feature or do any thing when the user PERMANENTLY denied for location permission
            showPermanentlyDeniedDialog()
        else {
            AlertDialog.Builder(requireContext())
                .setMessage("${getString(R.string.app_name)} uses location data to enable ABC feature even when app is closed or not in use.")
                .setPositiveButton("Allow") { _: DialogInterface?, _: Int ->
                    requestBothLocationPermissions()
                }
                .setNegativeButton("Cancel") { _: DialogInterface?, _: Int ->
                    //you can disable the feature or do any thing when the user don't wants to give permission
                    findNavController().popBackStack()
                }
                .setCancelable(false)
                .show()
        }
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    private fun openSettings() {
        resultLauncher.launch(Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", requireActivity().packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }


    private fun showPermanentlyDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("You have permanently denied the location permissions, you can enable it manually in system settings")
            .setPositiveButton("Settings") { _: DialogInterface?, _: Int ->
                openSettings()
            }
            .setNegativeButton("Exit") { _: DialogInterface?, _: Int ->
                findNavController().popBackStack()
            }
            .setCancelable(false)
            .show()
    }

}