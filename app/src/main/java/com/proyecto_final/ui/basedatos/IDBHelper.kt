package com.proyecto_final.ui.basedatos

import com.proyecto_final.ui.alumnos.Alumno
import com.proyecto_final.ui.aulas.Aulas
import com.proyecto_final.ui.curso.Curso
import com.proyecto_final.ui.profesores.Profesor
import com.proyecto_final.ui.users.User

interface IDBHelper {
    fun addAlumnos(name: String, lastname: String)
    fun delAlumnos(id: Int/*, nombre: String*/) : Int
    fun getAlumnos(): List<Alumno>
    fun getAlumnosByID(id: Int): Alumno?
    fun updateAlumnos(id: Int, nombre: String, apellido: String) : Int

    fun addProfesor(name: String, lastname: String)
    fun delProfesor(id: Int/*, nombre: String*/) : Int
    fun getProfesor(): List<Profesor>
    fun getProfesorByID(id: Int): Profesor?
    fun updateProfesor(id: Int, nombre: String, apellido: String) : Int

    fun addCurso(name: String, lastname: String)
    fun delCurso(id: Int/*, nombre: String*/) : Int
    fun getCurso(): List<Curso>
    fun getCursoByID(id: Int): Curso?
    fun updateCurso(id: Int, nombre: String, apellido: String) : Int

    fun asignarProfesorACurso(idProfesor: Int, idCurso: Int)
    fun asignarCursoAProfesor(idCurso: Int, idProfesor: Int)
    fun asignarAlumnoACurso(idAlumno: Int, idCurso: Int)
    fun asignarCursoAAlumno(idAlumno: Int, idCurso: Int)
    fun asignarProfesorAAlumno(idAlumno: Int, idProfesor: Int)
    fun asignarAlumnoAProfesor(idAlumno: Int, idProfesor: Int)
    fun asignarAulaACurso(idCurso: Int, idAula: Int)

    fun addUsers(name: String, password: String)
    fun getUsers(): List<User>
    fun getUsersByNameAndPassword(name: String, password: String): User?
    fun isUsuarioExistente(usuario: String): Boolean

    fun addAulas(name: String)
    fun delAulas(id: Int/*, nombre: String*/) : Int
    fun getAulas(): List<Aulas>
    fun getAulasByID(id: Int): Aulas?
    fun updateAulas(id: Int, nombre: String) : Int
}