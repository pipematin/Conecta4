package com.sergioteso.conecta4.activities

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.models.Round
import es.uam.eps.multij.Tablero

/**
 * Clase que implementa el ViewHolder de RecyclerView
 */
class RoundViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
    var cardView: CardView
    var idTextView: TextView
    var dateTextView: TextView
    var tableroTextView: TextView

    /**
     * Al inicializar la clase obtiene del layout las views a usar
     */
    init {
        cardView = itemview.findViewById(R.id.list_cardview)
        idTextView = itemview.findViewById(R.id.list_item_id) as TextView
        dateTextView = itemview.findViewById(R.id.list_item_date) as TextView
        tableroTextView = itemview.findViewById(R.id.list_item_tablero) as TextView
    }

    /**
     * funcion que establece los valores de sus views de acorde a la ronda y el listener pasados
     * como parametros
     */
    fun bindRound(round: Round, listener: (Round) -> Unit) {
        idTextView.text = round.title
        dateTextView.text = round.date.substring(0, 19)
        tableroTextView.text = round.tableroc4.tableroInString()
        if (round.tableroc4.estado == Tablero.FINALIZADA) {
            itemView.setBackgroundResource(R.color.darkRed)
        } else if (round.tableroc4.estado == Tablero.TABLAS) {
            itemView.setBackgroundResource(R.color.darkYellow)
        } else {
            itemView.setBackgroundResource(R.color.darkGreen)
        }
        cardView.setOnClickListener { listener(round) }
        idTextView.setOnClickListener { listener(round) }
        tableroTextView.setOnClickListener { listener(round) }
        dateTextView.setOnClickListener { listener(round) }

    }
}

/**
 * Clase que implementa el Adapter de RecyclerView basandose en el Holder previamente implmentado.
 */
class RoundAdapter(var rounds: List<Round>, val listener: (Round) -> Unit) : RecyclerView.Adapter<RoundViewHolder>() {

    /**
     * Funcion que infla el layout del elemento de la lista y devuelve su Holder correspondiente a esa vista.
     */
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RoundViewHolder {
        val layoutInflater = LayoutInflater.from(p0.context)
        val view = layoutInflater.inflate(R.layout.list_item_round, p0, false)
        return RoundViewHolder(view)
    }

    /**
     * Establece el numero de items en el Holder
     */
    override fun getItemCount(): Int = rounds.size

    /**
     * Hace un bind de la ronda en el indice pasado con el RoundViewHolder
     */
    override fun onBindViewHolder(p0: RoundViewHolder, p1: Int) {
        p0.bindRound(rounds[p1], listener)
    }
}