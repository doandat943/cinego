package com.joverse.cinego.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.joverse.cinego.R
import com.joverse.cinego.databinding.FragmentProfileBinding
import com.joverse.cinego.model.User

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var database: FirebaseDatabase
    private var fbUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        database = FirebaseDatabase.getInstance()
        initUI()

        return binding.root
    }

    private fun loadFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun initUI() {

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            binding.tvName.text = user.displayName
            binding.tvEmail.text = user.email

            val myRef: DatabaseReference = database.getReference("Users").child(user.uid)

            // Lấy dữ liệu User từ Firebase
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    fbUser = dataSnapshot.getValue(User::class.java) ?: User()
                    binding.tvMoney.text = "${fbUser!!.Money} $"
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Lỗi: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })

            binding.btnNap1.setOnClickListener {
                updateMoney(50000, myRef)
            }

            binding.btnNap2.setOnClickListener {
                updateMoney(100000, myRef)
            }
        }

        binding.btnUpdateProfile.setOnClickListener {
            loadFragment(UpdateProfileFragment())
        }

        binding.btnUpdatePassword.setOnClickListener {
            loadFragment(UpdatePasswordFragment())
        }
    }

    private fun updateMoney(amount: Int, myRef: DatabaseReference) {
        fbUser?.let {
            it.Money += amount
            myRef.setValue(it).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Đã nạp $amount", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Lỗi khi nạp tiền", Toast.LENGTH_SHORT).show()
                }
            }
        } ?: Toast.makeText(requireContext(), "Dữ liệu người dùng chưa sẵn sàng", Toast.LENGTH_SHORT).show()
    }

}