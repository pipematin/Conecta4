package com.sergioteso.conecta4.models

import com.sergioteso.conecta4.views.ViewC4
import es.uam.eps.multij.*

/**
 * Clase que modeliza un jugador local en la app. Extiende la clase Jugador e impllementa
 * la interfaz de OnPlayListener de la ViewC4
 *
 * @property name String con el nombre del Jugador
 * @property game Partida en curso
 */
class RemotePlayerC4(var name: String, public var turno: Int) : Jugador, ViewC4.OnPlayListener {
    private lateinit var game: Partida

    /**
     * Funcion que realiza un MovimientoC4 en la columna pasada
     *
     * @param column Int con la columna en la que realizar el movimiento
     */
    override fun onPlay(column: Int) {
        return
    }

    /**
     * Función setter de la propiedad game de la clase
     *
     * @param game Partida a asignar
     */
    fun setPartida(game: Partida) {
        this.game = game
    }

    /**
     * Función getter del nombre del jugador
     *
     * @return String con el nombre
     */
    override fun getNombre(): String = name

    /**
     * Función que indica si el jugador puede jugar en el tablero
     *
     * @param p0 Tablero sobre el cual se mira si puede jugar
     * @return Boolean que indica si puede o no
     */
    override fun puedeJugar(p0: Tablero?): Boolean = false

    override fun onCambioEnPartida(p0: Evento?) {}

}