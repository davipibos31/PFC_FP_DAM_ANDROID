package com.proyecto_final.ui.navegacion

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.proyecto_final.databinding.FragmentNavegacionBinding
import kotlinx.android.synthetic.main.fragment_navegacion.*

class NavegacionFragment : Fragment() {

    private var _binding: FragmentNavegacionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNavegacionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            btn_google_maps.setOnClickListener {
                val myIntent = Intent(requireContext(), MapaGoogle::class.java)
                startActivity(myIntent)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}