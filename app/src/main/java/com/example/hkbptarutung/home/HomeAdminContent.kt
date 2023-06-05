package com.example.hkbptarutung.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hkbptarutung.LoginPage
import com.example.hkbptarutung.Profile
import com.example.hkbptarutung.R
import com.example.hkbptarutung.databinding.FragmentHomeAdminBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeAdminContent : Fragment() {

    private var _binding: FragmentHomeAdminBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.ivIconMore.setOnClickListener {
            (activity as HomeAdmin).openDrawer()
        }
        binding.clFooter.findViewById<View>(R.id.ll_logout).setOnClickListener {
            activity?.apply {
                Firebase.auth.signOut()
                finishAffinity()
                startActivity(Intent(this, LoginPage::class.java))
            }
        }
        binding.clFooter.findViewById<View>(R.id.ll_profile).setOnClickListener {
            activity?.apply {
                startActivity(Intent(this, Profile::class.java))
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}