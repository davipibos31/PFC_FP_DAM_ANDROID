package com.proyecto_final.ui.aulas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.proyecto_final.databinding.FragmentAulasBinding
import kotlinx.android.synthetic.main.activity_aulas.*
import kotlinx.android.synthetic.main.fragment_aulas.*

class AulasFragment : Fragment() {

    private var _binding: FragmentAulasBinding? = null
    private val binding get() = _binding!!
    private val aulas =
        arrayOf(
            "Aula 1", "Aula 2", "Aula 3", "Aula 4",
            "Aula 5", "Aula 6", "Aula 7", "Aula 10"
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAulasBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
    }

    @SuppressLint("SetTextI18n")
    private fun setView() {
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), android.R.layout.simple_expandable_list_item_1, aulas)
        val listview: ListView = list_aulas
        listview.adapter = arrayAdapter

        listview.onItemClickListener = AdapterView.OnItemClickListener{_, _, position, _ ->
            val myIntent = Intent(requireContext(), AulasActivity::class.java)
            startActivity(myIntent)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun myToast(mensaje: String) {
        Toast.makeText(
            requireContext(),
            mensaje,
            Toast.LENGTH_SHORT
        ).show()
    }
}