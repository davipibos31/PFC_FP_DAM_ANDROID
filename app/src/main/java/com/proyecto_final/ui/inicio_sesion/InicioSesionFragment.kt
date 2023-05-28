package com.proyecto_final.ui.inicio_sesion

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.proyecto_final.R
import com.proyecto_final.databinding.FragmentInicioSesionBinding
import com.proyecto_final.ui.basedatos.DBHelper
import com.proyecto_final.ui.basedatos.DBHelperApplication
import com.proyecto_final.ui.basedatos.IDBHelper
import kotlinx.android.synthetic.main.fragment_inicio_sesion.*

class InicioSesionFragment : Fragment() {

    private var _binding: FragmentInicioSesionBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: IDBHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInicioSesionBinding.inflate(inflater, container, false)

        if (DBHelperApplication.dataSource.getUsers().isEmpty())
            DBHelperApplication.dataSource.addUsers("EDI","admin")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListener() {
        btn_inicio_sesion_entrar.setOnClickListener {
            if (Edi_txt_usuario.text.toString().isEmpty()) {
                myToast("Usuario vacío")
            } else if (editTextTextPassword.text.toString().isEmpty()) {
                myToast("Contraseña vacía")
            } else {
                validateCredentials()

            }
        }

        btn_inicio_sesion_registrar.setOnClickListener {
            if (Edi_txt_usuario.text.toString().isEmpty()) {
                myToast("Usuario vacío")
            } else if (editTextTextPassword.text.toString().isEmpty()) {
                myToast("Contraseña vacía")
            } else if (DBHelperApplication.dataSource.isUsuarioExistente(Edi_txt_usuario.text.toString())) {
                myToast("El usuario ya existe")
            } else {
                DBHelperApplication.dataSource.addUsers(Edi_txt_usuario.text.toString(), editTextTextPassword.text.toString())
                myToast("Se ha creado con éxito")
            }
        }
    }

    private fun myToast(mensaje: String) {
        Toast.makeText(
            requireContext(),
            mensaje,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun validateCredentials() {
        val username = Edi_txt_usuario.text.toString()
        val password = editTextTextPassword.text.toString()

        dbHelper = DBHelper(requireContext(),null)
        val db = DBHelper(requireContext(),null).readableDatabase

        val selection = "${DBHelper.COLUMNA_NOMBRE_USERS} = ? AND ${DBHelper.COLUMNA_PASSWORD_USERS} = ?"
        val selectionArgs = arrayOf(username, password)

        val cursor: Cursor? = db.query(
            DBHelper.TABLA_USERS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController

        // Cambiar el navGraph al nuevo archivo de navegación
        navController.setGraph(R.navigation.mobile_navigation2)
        if (cursor?.count == 1) {
            // Usuario y contraseña válidos
            if (username != "EDI" && password != "admin") {

                val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
                val navController = navHostFragment.navController

                // Cambiar el navGraph al nuevo archivo de navegación
                navController.setGraph(R.navigation.mobile_navigation2)
                myToast("Sesión iniciada...")
                myToast("Bienvenido " + Edi_txt_usuario.text.toString())
                myToast("Menu OP: Inicio sesión, bloqueado")
                myToast("Menu OP: Profesores, bloqueado")
                myToast("Menu OP: Alumnos, bloqueado")
                myToast("Menu OP: Cursos, bloqueado")
                myToast("Menu OP: Aulas, bloqueado")
                // Navegar al siguiente fragmento o realizar alguna acción adicional
            } else if (username == "EDI" && password == "admin"){
                val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
                val navController = navHostFragment.navController

                // Cambiar el navGraph al nuevo archivo de navegación
                navController.setGraph(R.navigation.mobile_navigation)
                myToast("Sesión iniciada...")
                myToast("Bienvenido " + Edi_txt_usuario.text.toString())
            }
        }

        cursor?.close()
        db.close()
    }
}
