package edu.unikom.suweorajamuapps.view

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dagger.hilt.android.AndroidEntryPoint
import edu.unikom.suweorajamuapps.R

@AndroidEntryPoint
class InfoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return TextView(requireContext()).apply {
            text = "Aplikasi Warung Jamu Suwe Ora Jamu\n Created BY: Rama\n Email: Rama@unikom.ac.id"
            setPadding(16, 16, 16, 16)
            textSize = 20f
            gravity = Gravity.CENTER
        }
    }

}