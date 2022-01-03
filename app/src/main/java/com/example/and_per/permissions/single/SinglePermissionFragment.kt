package com.example.and_per.permissions.single

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.fragment.app.Fragment
import com.example.and_per.BuildConfig.APPLICATION_ID
import com.example.and_per.databinding.FragmentSinglePermissionBinding
import com.example.and_per.utils.hasPermission
import com.example.and_per.utils.showToast
import com.google.android.material.snackbar.Snackbar

class SinglePermissionFragment : Fragment() {

    private var _binding: FragmentSinglePermissionBinding? = null
    private val binding get() = _binding!!

    private val singlePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        //the permission is granted on permission dialog
        if (it) {
            showToast("Permission granted")
            binding.text.text = "Permission granted"
        }
        //the permission is permanently denied by user
        else if (!shouldShowRequestPermissionRationale(requireActivity(), ACCESS_FINE_LOCATION))
            showSnackBar()
        //the permission is denied on permission dialog
        else {
            showToast("Permission denied")
            binding.text.text = "Permission denied"
        }

    }

    private fun showSnackBar() {
        Snackbar.make(
            binding.root,
            "You have permanently denied the ABC Permission, you can enable it manually in system settings",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Enable") {
//            // Build intent that displays the App settings screen.
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.fromParts("package", APPLICATION_ID, null)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSinglePermissionBinding.inflate(inflater, container, false)
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
        binding.btnPermissions.setOnClickListener { requestPermissions() }
    }


    private fun requestPermissions() {
        val hasLocationForegroundPermission = requireContext().hasPermission(ACCESS_FINE_LOCATION)
        if (!hasLocationForegroundPermission) singlePermissionLauncher.launch(ACCESS_FINE_LOCATION)
        else {
            binding.text.text = "Already Granted"
            showToast("Already granted!")
        }
    }


}