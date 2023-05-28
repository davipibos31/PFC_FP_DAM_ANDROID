@file:Suppress("NAME_SHADOWING")

package com.proyecto_final.ui.basedatos

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.proyecto_final.ui.alumnos.Alumno
import com.proyecto_final.ui.aulas.Aulas
import com.proyecto_final.ui.curso.Curso
import com.proyecto_final.ui.profesores.Profesor
import com.proyecto_final.ui.users.User

class DBHelper(context : Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION),
    IDBHelper {
    private val tag = "SQLite"

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "EDI.db"

        const val TABLA_ALUMNOS = "alumnos"
        const val TABLA_PROFESORES = "profesores"
        const val TABLA_CURSOS = "cursos"
        const val TABLA_USERS = "users"
        const val TABLA_AULAS = "aulas"

        const val COLUMNA_ID_ALUMNO = "_ida"
        const val COLUMNA_ID_PROFESOR = "_idp"
        const val COLUMNA_ID_CURSO = "_idc"
        const val COLUMNA_ID_USERS = "_idu"
        const val COLUMNA_ID_AULAS = "_idau"

        const val COLUMNA_NOMBRE_ALUMNO = "nombre"
        const val COLUMNA_NOMBRE_PROFESOR = "nombre"
        const val COLUMNA_NOMBRE_CURSO = "nombre"
        const val COLUMNA_NOMBRE_USERS = "usuario"

        const val COLUMNA_APELLIDOS_ALUMNO = "apellidos"
        const val COLUMNA_APELLIDOS_PROFESOR = "apellidos"
        const val COLUMNA_APELLIDOS_CURSO = "codigo"

        const val COLUMNA_PASSWORD_USERS = "password"
        const val COLUMNA_AULAS_AULAS = "aulas"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            val createTableAlumnos = "CREATE TABLE $TABLA_ALUMNOS " +
                    "($COLUMNA_ID_ALUMNO INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMNA_NOMBRE_ALUMNO TEXT, " +
                    "$COLUMNA_APELLIDOS_ALUMNO TEXT," +
                    "$COLUMNA_ID_PROFESOR INTEGER, " +
                    "$COLUMNA_ID_CURSO INTEGER, " +
                    "FOREIGN KEY($COLUMNA_ID_PROFESOR) REFERENCES $TABLA_PROFESORES($COLUMNA_ID_PROFESOR), " +
                    "FOREIGN KEY($COLUMNA_ID_CURSO) REFERENCES $TABLA_CURSOS($COLUMNA_ID_CURSO))"

            val createTableProfesores = "CREATE TABLE $TABLA_PROFESORES" +
                    "($COLUMNA_ID_PROFESOR INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMNA_NOMBRE_PROFESOR TEXT, " +
                    "$COLUMNA_APELLIDOS_PROFESOR TEXT, "+
                    "$COLUMNA_ID_CURSO INTEGER, " +
                    "FOREIGN KEY($COLUMNA_ID_CURSO) REFERENCES $TABLA_CURSOS($COLUMNA_ID_CURSO))"

            val createTableCursos = "CREATE TABLE $TABLA_CURSOS" +
                    "($COLUMNA_ID_CURSO INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMNA_NOMBRE_CURSO TEXT, " +
                    "$COLUMNA_APELLIDOS_CURSO TEXT," +
                    "$COLUMNA_ID_AULAS INTEGER, " +
            "FOREIGN KEY($COLUMNA_ID_AULAS) REFERENCES $TABLA_AULAS($COLUMNA_ID_AULAS))"


            val createTableUsers = "CREATE TABLE $TABLA_USERS" +
                    "($COLUMNA_ID_USERS INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMNA_NOMBRE_USERS TEXT, " +
                    "$COLUMNA_PASSWORD_USERS TEXT)"

            val createTableAulas = "CREATE TABLE $TABLA_AULAS" +
                    "($COLUMNA_ID_AULAS INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMNA_AULAS_AULAS TEXT)"


            db!!.execSQL(createTableAulas)
            db.execSQL(createTableProfesores)
            db.execSQL(createTableCursos)
            db.execSQL(createTableAlumnos)
            db.execSQL(createTableUsers)
        } catch (e: SQLiteException) {
            Log.e("$tag (onCreate)", e.message.toString())
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLA_ALUMNOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_PROFESORES")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_CURSOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_AULAS")
        onCreate(db)
    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
    }

    override fun addAlumnos(name: String, lastname: String) {
        val data = ContentValues()
        data.put(COLUMNA_NOMBRE_ALUMNO, name)
        data.put(COLUMNA_APELLIDOS_ALUMNO, lastname)
        val db = this.writableDatabase
        db.insert(TABLA_ALUMNOS, null, data)
        db.close()
    }

    override fun delAlumnos(id: Int/*, nombre: String*/): Int {
        val args = arrayOf(id.toString())

        val db = this.writableDatabase
        val result = db.delete(TABLA_ALUMNOS, "$COLUMNA_ID_ALUMNO = ?", args)
        db.close()
        return result
    }

    @SuppressLint("Recycle")
    override fun getAlumnos(): List<Alumno> {

        val result = ArrayList<Alumno>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMNA_ID_ALUMNO, $COLUMNA_NOMBRE_ALUMNO,$COLUMNA_APELLIDOS_ALUMNO,$COLUMNA_ID_PROFESOR,$COLUMNA_ID_CURSO" +
                    " FROM $TABLA_ALUMNOS",
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val apellido = cursor.getString(2)
                val profesor = cursor.getInt(3)
                val curso = cursor.getInt(4)
                val alumno = Alumno(id, nombre, apellido, profesor,curso)
                result.add(alumno)
            } while (cursor.moveToNext())
        }
        return result
    }

    @SuppressLint("Recycle")
    override fun getAlumnosByID(id: Int): Alumno? {
        var alumno: Alumno? = null// le pongo que es nulo de inicio
        val args = arrayOf(id.toString())
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMNA_ID_ALUMNO, $COLUMNA_NOMBRE_ALUMNO,$COLUMNA_APELLIDOS_ALUMNO,$COLUMNA_ID_PROFESOR,$COLUMNA_ID_CURSO" +
                    " FROM $TABLA_ALUMNOS where $COLUMNA_ID_ALUMNO=?",
            args// el args se pasa el id del array
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val apellido = cursor.getString(2)
                val profesor = cursor.getInt(3)
                val curso = cursor.getInt(4)
                alumno = Alumno(id, nombre, apellido, profesor,curso)
            } while (cursor.moveToNext())
        }
        return alumno
    }

    override fun updateAlumnos(id: Int, nombre: String, apellido: String): Int {
        val args = arrayOf(id.toString())
        val db = this.writableDatabase
        val data = ContentValues()

        data.put(COLUMNA_NOMBRE_ALUMNO, nombre)
        data.put(COLUMNA_APELLIDOS_ALUMNO, apellido)

        val res = db.update(TABLA_ALUMNOS, data, "$COLUMNA_ID_ALUMNO = ?", args)
        db.close()
        return res
    }

    override fun addProfesor(name: String, lastname: String) {
        val data = ContentValues()
        data.put(COLUMNA_NOMBRE_PROFESOR, name)
        data.put(COLUMNA_APELLIDOS_PROFESOR, lastname)
        val db = this.writableDatabase
        db.insert(TABLA_PROFESORES, null, data)
        db.close()
    }

    override fun delProfesor(id: Int/*, nombre: String*/): Int {
        val args = arrayOf(id.toString())
        val db = this.writableDatabase
        val result = db.delete(TABLA_PROFESORES, "$COLUMNA_ID_PROFESOR = ?", args)
        db.close()
        return result
    }

    @SuppressLint("Recycle")
    override fun getProfesor(): List<Profesor> {

        val result = ArrayList<Profesor>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMNA_ID_PROFESOR, $COLUMNA_NOMBRE_PROFESOR,$COLUMNA_APELLIDOS_PROFESOR,$COLUMNA_ID_CURSO" +
                    " FROM $TABLA_PROFESORES",
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val apellido = cursor.getString(2)
                val curso = cursor.getInt(3)
                val profesor = Profesor(id, nombre, apellido,curso)
                result.add(profesor)
            } while (cursor.moveToNext())

        }
        return result
    }

    @SuppressLint("Recycle")
    override fun getProfesorByID(id: Int): Profesor? {
        var profesor: Profesor? = null// le pongo que es nulo de inicio
        val args = arrayOf(id.toString())
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMNA_ID_PROFESOR, $COLUMNA_NOMBRE_PROFESOR,$COLUMNA_APELLIDOS_PROFESOR, $COLUMNA_ID_CURSO" +
                    "TABLA_$TABLA_PROFESORES where $COLUMNA_ID_PROFESOR=?",
            args// el args se pasa el id del array
            //
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val apellido = cursor.getString(2)
                val curso = cursor.getInt(3)
                profesor = Profesor(id, nombre, apellido,curso)

            } while (cursor.moveToNext())

        }
        return profesor
    }

    override fun updateProfesor(id: Int, nombre: String, apellido: String): Int {
        val args = arrayOf(id.toString())
        val db = this.writableDatabase
        val data = ContentValues()

        data.put(COLUMNA_NOMBRE_PROFESOR, nombre)
        data.put(COLUMNA_APELLIDOS_PROFESOR, apellido)

        val res = db.update(TABLA_PROFESORES, data, "$COLUMNA_ID_PROFESOR = ?", args)
        db.close()
        return res
    }

    override fun addCurso(name: String, lastname: String) {
        val data = ContentValues()
        data.put(COLUMNA_NOMBRE_CURSO, name)
        data.put(COLUMNA_APELLIDOS_CURSO, lastname)
        val db = this.writableDatabase
        db.insert(TABLA_CURSOS, null, data)
        db.close()
    }

    override fun delCurso(id: Int/*, nombre: String*/): Int {
        val args = arrayOf(id.toString())

        val db = this.writableDatabase
        //val result = db.delete(TABLA_AMIGOS,"$COLUMNA_ID = ? and $COLUMNA_NOMBRE=?", args)
        val result = db.delete(TABLA_CURSOS, "$COLUMNA_ID_CURSO = ?", args)
        db.close()
        return result
    }

    @SuppressLint("Recycle")
    override fun getCurso(): List<Curso> {

        val result = ArrayList<Curso>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMNA_ID_CURSO, $COLUMNA_NOMBRE_CURSO,$COLUMNA_APELLIDOS_CURSO,$COLUMNA_ID_AULAS" +
                    " FROM $TABLA_CURSOS",
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val apellido = cursor.getString(2)
                val aula = cursor.getInt(3)
                val curso = Curso(id, nombre, apellido,aula)
                result.add(curso)
            } while (cursor.moveToNext())

        }
        return result
    }

    @SuppressLint("Recycle")
    override fun getCursoByID(id: Int): Curso? {
        var curso: Curso? = null// le pongo que es nulo de inicio
        val args = arrayOf(id.toString())
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMNA_ID_CURSO, $COLUMNA_NOMBRE_CURSO,$COLUMNA_APELLIDOS_CURSO,$COLUMNA_ID_AULAS" +
                    "TABLA_$TABLA_CURSOS where $COLUMNA_ID_CURSO=?",
            args// el args se pasa el id del array
            //
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val apellido = cursor.getString(2)
                val aula = cursor.getInt(3)
                curso = Curso(id, nombre, apellido,aula)
            } while (cursor.moveToNext())
        }
        return curso
    }

    override fun updateCurso(id: Int, nombre: String, apellido: String): Int {
        val args = arrayOf(id.toString())
        val db = this.writableDatabase
        val data = ContentValues()

        data.put(COLUMNA_NOMBRE_CURSO, nombre)
        data.put(COLUMNA_APELLIDOS_CURSO, apellido)

        val res = db.update(TABLA_CURSOS, data, "$COLUMNA_ID_CURSO = ?", args)
        db.close()
        return res
    }

    override fun addUsers(name: String, password: String) {
        val data = ContentValues()
        data.put(COLUMNA_NOMBRE_USERS, name)
        data.put(COLUMNA_PASSWORD_USERS, password)
        val db = this.writableDatabase
        db.insert(TABLA_USERS, null, data)
        db.close()
    }

    @SuppressLint("Recycle")
    override fun getUsers(): List<User> {
        val result = ArrayList<User>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMNA_ID_USERS, $COLUMNA_NOMBRE_USERS,$COLUMNA_PASSWORD_USERS" +
                    " FROM $TABLA_USERS",
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val password = cursor.getString(2)
                val users = User(id, name, password)
                result.add(users)
            } while (cursor.moveToNext())
        }
        return result
    }

    @SuppressLint("Recycle")
    override fun getUsersByNameAndPassword(name: String, password: String): User? {
        var users: User? = null// le pongo que es nulo de inicio
        val args = arrayOf(name)
        val args2 = arrayOf(password)
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMNA_ID_USERS, $COLUMNA_NOMBRE_USERS,$COLUMNA_PASSWORD_USERS" +
                    "TABLA_$TABLA_CURSOS where $COLUMNA_NOMBRE_USERS=? AND $COLUMNA_PASSWORD_USERS=?",
            args + args2// el args se pasa el id del array
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val password = cursor.getString(2)
                users = User(id, name, password)
            } while (cursor.moveToNext())
        }
        return users
    }

    override fun addAulas(name: String) {
        val data = ContentValues()
        data.put(COLUMNA_AULAS_AULAS, name)
        val db = this.writableDatabase
        db.insert(TABLA_AULAS, null, data)
        db.close()
    }

    override fun delAulas(id: Int): Int {
        val args = arrayOf(id.toString())
        val db = this.writableDatabase
        val result = db.delete(TABLA_AULAS, "$COLUMNA_ID_AULAS = ?", args)
        db.close()
        return result
    }

    @SuppressLint("Recycle")
    override fun getAulas(): List<Aulas> {
        val result = ArrayList<Aulas>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMNA_ID_AULAS, $COLUMNA_AULAS_AULAS" +
                    " FROM $TABLA_AULAS",
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val aulas = Aulas(id, name)
                result.add(aulas)
            } while (cursor.moveToNext())
        }
        return result
    }

    override fun getAulasByID(id: Int): Aulas? {
        var aula: Aulas? = null// le pongo que es nulo de inicio
        val args = arrayOf(id.toString())
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMNA_ID_AULAS, $COLUMNA_AULAS_AULAS" +
                    "TABLA_$TABLA_AULAS where $COLUMNA_ID_AULAS=?",
            args// el args se pasa el id del array
            //
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val nombre = cursor.getString(1)
                aula = Aulas(id, nombre)
            } while (cursor.moveToNext())
        }
        return aula
    }

    override fun updateAulas(id: Int, nombre: String): Int {
        val args = arrayOf(id.toString())
        val db = this.writableDatabase
        val data = ContentValues()

        data.put(COLUMNA_AULAS_AULAS, nombre)

        val res = db.update(TABLA_AULAS, data, "$COLUMNA_ID_AULAS = ?", args)
        db.close()
        return res
    }

    override fun asignarProfesorACurso(idProfesor: Int, idCurso: Int) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMNA_ID_PROFESOR, idProfesor)
        contentValues.put(COLUMNA_ID_CURSO, idCurso)

        val resultado = db.update(TABLA_PROFESORES, contentValues, "$COLUMNA_ID_PROFESOR = ?", arrayOf(idProfesor.toString()))
        if (resultado == 0) {
            // Si no se actualizó ningún registro, significa que no existe el profesor con ese ID.
            Log.d("DBHelper", "No se encontró el profesor con el ID $idProfesor")
        } else {
            Log.d("DBHelper", "Profesor $idProfesor asignado al curso $idCurso")
        }
        db.close()
    }

    override fun asignarCursoAProfesor(idCurso: Int, idProfesor: Int) {
        val values = ContentValues()
        values.put(COLUMNA_ID_CURSO, idCurso)

        val db = writableDatabase
        db.update(TABLA_PROFESORES, values, "$COLUMNA_ID_PROFESOR=?", arrayOf(idProfesor.toString()))
        db.close()
    }

    override fun asignarAlumnoACurso(idAlumno: Int, idCurso: Int) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMNA_ID_ALUMNO, idAlumno)
        contentValues.put(COLUMNA_ID_CURSO, idCurso)

        val resultado = db.update(TABLA_ALUMNOS, contentValues, "$COLUMNA_ID_ALUMNO = ?", arrayOf(idAlumno.toString()))
        if (resultado == 0) {
            // Si no se actualizó ningún registro, significa que no existe el profesor con ese ID.
            Log.d("DBHelper", "No se encontró el profesor con el ID $idAlumno")
        } else {
            Log.d("DBHelper", "Alumno $idAlumno asignado al curso $idCurso")
        }
        db.close()
    }

    override fun asignarCursoAAlumno(idAlumno: Int, idCurso: Int) {
        val values = ContentValues()
        values.put(COLUMNA_ID_CURSO, idCurso)

        val db = writableDatabase
        db.update(TABLA_ALUMNOS, values, "$COLUMNA_ID_ALUMNO=?", arrayOf(idAlumno.toString()))
        db.close()
    }
    override fun asignarProfesorAAlumno(idAlumno: Int, idProfesor: Int) {
        val values = ContentValues()
        values.put(COLUMNA_ID_PROFESOR, idProfesor)

        val db = writableDatabase
        db.update(TABLA_ALUMNOS, values, "$COLUMNA_ID_ALUMNO=?", arrayOf(idAlumno.toString()))
        db.close()
    }
    override fun asignarAlumnoAProfesor(idAlumno: Int, idProfesor: Int) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMNA_ID_ALUMNO, idAlumno)
        contentValues.put(COLUMNA_ID_PROFESOR, idProfesor)

        val resultado = db.update(TABLA_ALUMNOS, contentValues, "$COLUMNA_ID_ALUMNO = ?", arrayOf(idAlumno.toString()))
        if (resultado == 0) {
            // Si no se actualizó ningún registro, significa que no existe el profesor con ese ID.
            Log.d("DBHelper", "No se encontró el profesor con el ID $idAlumno")
        } else {
            Log.d("DBHelper", "Alumno $idAlumno asignado al profesor $idProfesor")
        }
        db.close()
    }

    override fun asignarAulaACurso(idCurso: Int, idAula: Int) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMNA_ID_AULAS, idAula)
        contentValues.put(COLUMNA_ID_CURSO, idCurso)

        val resultado = db.update(TABLA_CURSOS, contentValues, "$COLUMNA_ID_CURSO = ?", arrayOf(idCurso.toString()))
        if (resultado == 0) {
            // Si no se actualizó ningún registro, significa que no existe el profesor con ese ID.
            Log.d("DBHelper", "No se encontró el curso con el ID $idCurso")
        } else {
            Log.d("DBHelper", "Aula $idAula asignada al Curso $idCurso")
        }
        db.close()
    }

    override fun isUsuarioExistente(usuario: String): Boolean {
        // Aquí debes implementar la lógica para consultar la base de datos
        // y verificar si el usuario ya existe. El siguiente código es solo un ejemplo:

        // Supongamos que tienes una lista de usuarios existentes
        val usuariosExistentes = DBHelperApplication.dataSource.getUsers()

        // Verificar si el usuario ya está en la lista de usuarios existentes
        for (existingUser in usuariosExistentes) {
            if (existingUser.user == usuario) {
                return true
            }
        }

        return false
    }


}



