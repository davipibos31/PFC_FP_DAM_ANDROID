package com.proyecto_final.ui.profesores

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
import com.proyecto_final.databinding.ProfesoresBinding
import com.proyecto_final.ui.basedatos.DBHelper
import com.proyecto_final.ui.basedatos.DBHelperApplication
import kotlinx.android.synthetic.main.alumnos.*
import kotlinx.android.synthetic.main.profesores.*
import kotlinx.android.synthetic.main.profesores.btnActualizar
import kotlinx.android.synthetic.main.profesores.btnAsignarCurso
import kotlinx.android.synthetic.main.profesores.btnConsultar
import kotlinx.android.synthetic.main.profesores.btnEliminar
import kotlinx.android.synthetic.main.profesores.btnInsertar
import kotlinx.android.synthetic.main.profesores.etApes
import kotlinx.android.synthetic.main.profesores.etNombre
import kotlinx.android.synthetic.main.profesores.tvResult

const val UPDATE = "update"
const val DELETE = "delete"
const val ASIGNAR_CURSO = "asignar_curso"
class ProfesoresFragment : Fragment() {

    private var _binding: ProfesoresBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       val dbHelper = DBHelper(requireContext(), null)
       val list = dbHelper.getProfesor()
       Log.d("DBHELPER", list.toString())
    }

    private fun setListener(){
        btnInsertar.setOnClickListener{
            DBHelperApplication.dataSource.addProfesor(etNombre.text.toString(), etApes.text.toString())
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
        btnAsignarCurso.setOnClickListener {
            solicitaIdentificador2(ASIGNAR_CURSO)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfesoresBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun setData(){
        val profesores = DBHelperApplication.dataSource.getProfesor()
        tvResult.text = ""
        tvResult.append("ID --> NOMBRE --- APELLIDOS --- ID_CURSO\n\n\n")
        profesores.forEach{
            tvResult.append("${it.id} --> ${it.nombre} --- ${it.apellido} --- ${it.id_curso}\n")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dbHelper = DBHelper(requireContext(), null)
        val list = dbHelper.getProfesor()
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
                        val nombre = etNombre.text.toString()
                        val apellido = etApes.text.toString()
                        val alertDialog = AlertDialog.Builder(requireContext())

                        alertDialog.apply {
                            setTitle(getString(R.string.titulo2))
                            setMessage(getString(R.string.mensaje))
                            setPositiveButton(getString(R.string.dialog_button_positivo)) {_,_->
                                Snackbar.make(root_layout3,
                                    "Seleccionado eliminado con ID: $identificador", Snackbar.LENGTH_LONG).show()
                                myToast(
                                    "Actualizado/s " +
                                            "${DBHelperApplication.dataSource.updateProfesor(identificador, nombre, apellido)} " +
                                            "registro/s"
                                )
                                etNombre.text.clear()
                                etApes.text.clear()
                                setData()
                            }
                            setNegativeButton(getString(R.string.dialog_button_negativo)) {_,_->
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
                            setPositiveButton(getString(R.string.dialog_button_positivo)) {_,_->
                                Snackbar.make(root_layout3,
                                    "Seleccionado eliminado con ID: $identificador", Snackbar.LENGTH_LONG).show()
                                myToast(
                                    "Eliminado/s " +
                                            "${DBHelperApplication.dataSource.delProfesor(identificador)} " +
                                            "registro/s"
                                )
                                setData()
                            }
                            setNegativeButton(getString(R.string.dialog_button_negativo)) {_,_->
                                myToast("Cancelado. No Eliminado!!")
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
    private fun solicitaIdentificador2(accion: String) {

        // Se infla la vista para el diálogo.
        val myDialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialogo, null)
        myDialogView.findViewById<EditText>(R.id.identificador).hint = "Introduce el identificador de Curso"

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
                    ASIGNAR_CURSO -> {
                        val identificador = id_Profesor.text.toString().toInt()
                        val idCurso = myDialogView.findViewById<EditText>(R.id.identificador).text.toString().toInt()

                        DBHelperApplication.dataSource.asignarProfesorACurso(identificador, idCurso)

                        setData()
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