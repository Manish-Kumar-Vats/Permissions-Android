package com.example.and_per.permissions.basicpermission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.example.and_per.R.drawable.ic_round_image
import com.example.and_per.databinding.FragmentBasicPermissionsBinding

class BasicPermissionsFragment : Fragment() {

    private var _binding: FragmentBasicPermissionsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBasicPermissionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
        initCLickListeners()
    }

    private fun initCLickListeners() {
        binding.apply {
            btnBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setUpView() {
        val randomImageUrl = "https://source.unsplash.com/random/200x200"

        binding.imageBox.load(randomImageUrl) {
            transformations(CircleCropTransformation())
            crossfade(true)
            placeholder(ic_round_image)
        }
    }
}