package com.joverse.cinego.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.joverse.cinego.R
import com.joverse.cinego.databinding.FragmentExploreBinding
import com.joverse.cinego.databinding.FragmentUpdatePasswordBinding
import com.joverse.cinego.databinding.FragmentUpdateProfileBinding

class UpdateProfileFragment : Fragment() {
    private lateinit var binding: FragmentUpdateProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        binding.btnSubmit.setOnClickListener {
            val user = Firebase.auth.currentUser

            val profileUpdates = userProfileChangeRequest {
                displayName = binding.edtName.text.toString()
                photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
            }

            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Cập nhật Tên thành công!", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack();
                    }
                }
        }
        return binding.root
    }
}