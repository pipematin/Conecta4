package com.sergioteso.conecta4.models

import android.content.Context
import com.sergioteso.conecta4.database.DatabaseC4
import com.sergioteso.conecta4.firebase.FRDataBase
import java.lang.Exception

/**
 * Objeto que crea la factoria donde se almacenaran todas las rondas
 */
object RoundRepositoryFactory {
    var LOCAL = true
    /**
     * Funcion que accede a la database de la factoria devolviendo el repositorio
     *
     * @param context Contexto de la aplicacion
     */
    fun createRepository(context: Context): RoundRepository? {
        val repository = if (LOCAL) DatabaseC4(context) else FRDataBase(context)
        try{
            repository.open()
        } catch (e: Exception){
            return null
        }
        return repository
    }

    fun setLocal(local: Boolean){
        LOCAL = local
    }
}