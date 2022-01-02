package com.example.and_per.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.and_per.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListeners()
    }

    private fun clickListeners() {
        binding.btnBasicPer.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToBasicPermissionsFragment()
            findNavController().navigate(action)
        }
        binding.btnSinglePer.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSinglePermissionFragment()
            findNavController().navigate(action)
        }
        binding.btnMultiPer.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToMultiPermissionFragment()
            findNavController().navigate(action)
        }
    }

}