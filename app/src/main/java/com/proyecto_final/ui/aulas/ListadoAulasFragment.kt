package com.proyecto_final.ui.aulas

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.proyecto_final.R
import com.proyecto_final.databinding.AulasBinding
import com.proyecto_final.ui.basedatos.DBHelper
import com.proyecto_final.ui.basedatos.DBHelperApplication
import kotlinx.android.synthetic.main.aulas.*


const val UPDATE = "update"
const val DELETE = "delete"
class ListadoAulasFragment : Fragment() {

    private var _binding: AulasBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dbHelper = DBHelper(requireContext(), null)
        val list = dbHelper.getAulas()
        Log.d("DBHELPER", list.toString())
    }
    private fun setListener(){
        btnInsertar.setOnClickListener{
            DBHelperApplication.dataSource.addAulas(etNombre.text.toString())
            setData()
        }
        btnActualizar.setOnClickListener{
            solicitaIdentificador(UPDATE)
        }
        btnEliminar.setOnClickListener{
            solicitaIdentificador(DELETE)
        }
        btnConsultar.setOnClickListener{
            setData()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AulasBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun setData(){
        val aulas = DBHelperApplication.dataSource.getAulas()
        tvResult.text = ""
        tvResult.append("ID --> AULAS\n\n\n")
        aulas.forEach{
            tvResult.append("${it.id} --> ${it.aula}\n")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dbHelper = DBHelper(requireContext(), null)
        val list = dbHelper.getAulas()
        Log.d("DBHELPER", list.toString())
        setListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("CutPasteId")
    private fun solicitaIdentificador(accion: String) {

        // Se infla la vista para el diálogo.
        val myDialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialogo, null)

        // Se crea el builder.
        val builder = AlertDialog.Builder(requireContext())
            .setView(myDialogView)

        builder.apply {
            setPositiveButton(android.R.string.ok) { _, _ ->
                val valor = myDialogView
                    .findViewById<EditText>(R.id.identificador).text
                val identificador = valor.toString().toInt()

                // Se realiza la acción.
                when (accion) {
                    UPDATE -> {
                        val aula = etNombre.text.toString()
                        val alertDialog = AlertDialog.Builder(requireContext())

                        alertDialog.apply {
                            setTitle(getString(R.string.titulo2))
                            setMessage(getString(R.string.mensaje2))
                            setPositiveButton(getString(R.string.dialog_button_positivo)) { _, _ ->
                                Snackbar.make(root_layout3,
                                    "Seleccionado eliminado con ID: $identificador",
                                    Snackbar.LENGTH_LONG).show()
                                myToast(
                                    "Actualizado/s " +
                                            "${
                                                DBHelperApplication.dataSource.updateAulas(
                                                    identificador,
                                                    aula)
                                            } " +
                                            "registro/s"
                                )
                                etNombre.text.clear()
                                setData()
                            }
                            setNegativeButton(getString(R.string.dialog_button_negativo)) { _, _ ->
                                myToast("Cancelado. No Actualizado!!")
                            }
                            alertDialog.show()
                        }
                    }
                    DELETE -> {
                        val alertDialog = AlertDialog.Builder(requireContext())
                        alertDialog.apply {
                            setTitle(getString(R.string.titulo))
                            setMessage(getString(R.string.mensaje))
                            setPositiveButton(getString(R.string.dialog_button_positivo)) { _, _->
                                Snackbar.make(root_layout3,
                                    "Seleccionado eliminado con ID: $identificador", Snackbar.LENGTH_LONG).show()
                                myToast(
                                    "Eliminado/s " +
                                            "${DBHelperApplication.dataSource.delAulas(identificador)} " +
                                            "registro/s"
                                )
                                setData()
                            }
                            setNegativeButton(getString(R.string.dialog_button_negativo)) { _, _->
                                myToast("Cancelado: No Eliminado!!")
                            }
                            alertDialog.show()
                        }
                    }
                }
            }
            setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }

    private fun myToast(mensaje: String) {
        Toast.makeText(
            requireContext(),
            mensaje,
            Toast.LENGTH_SHORT
        ).show()
    }
}