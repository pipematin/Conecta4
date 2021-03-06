package com.sergioteso.conecta4.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.design.widget.Snackbar
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.Toast
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.activities.Logger.log
import com.sergioteso.conecta4.activities.setColor
import com.sergioteso.conecta4.models.TableroC4
import es.uam.eps.multij.ExcepcionJuego
import es.uam.eps.multij.Tablero

/**
 * Clase que extiende una view para dibujar la vista del tablero
 *
 * @property backgroundPaint Paint con el color del fondo
 * @property linePaint pintura de la casilla
 * @property heightOfTile tamaño de la altura de la casilla
 * @property widthOfTile tamaño del ancho de la casilla
 * @property radio radio de la casilla
 * @property columns numero de columnas del tablero
 * @property rows numero de filas del tablero
 * @property board TableroC4 en el cula se basa la vista
 * @property onPlayListener Interfaz que implementa el comportamiento al hacer una jugada en el tablero
 * @property myGestureDetector Detector de los eventos generados al usar la pantalla del movil
 */
class ViewC4(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var heightOfTile: Float = 0.toFloat()
    private var widthOfTile: Float = 0.toFloat()
    private var radio: Float = 0.toFloat()
    private var board: TableroC4? = null
    private var onPlayListener: OnPlayListener? = null
    private var myGestureDetector: GestureDetector

    interface OnPlayListener {
        fun onPlay(column: Int)
    }

    init {
        backgroundPaint.color = Color.BLACK
        linePaint.strokeWidth = 2f
        myGestureDetector = GestureDetector(context, MyGestureListener())
    }

    /**
     * Clase interna que modela los eventos de la pantalla
     */
    internal inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            Toast.makeText(context, "onDown: " + e.toString(), Toast.LENGTH_SHORT).show()
            return true
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            Toast.makeText(context, "onDoubleTap: " + e.toString(), Toast.LENGTH_SHORT).show()
            return true
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            Toast.makeText(
                context, "onFling: " + e1.toString() + e2.toString(),
                Toast.LENGTH_SHORT
            ).show()
            return true
        }
    }

    /**
     * Funcion que se ejecuta siempre que se tenga que redibujar la vista la cual calcula las dimensiones de la vista
     *
     * @param widthMeasureSpec Int con el ancho esperado de la vista
     * @param heightMeasureSpec Int con el alto esperado de la vista
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        val desiredWidth = 500
//        val wMode: String
//        val hMode: String
//        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
//        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val width: Int
        val height: Int

        if (widthSize < heightSize) {
            heightSize = widthSize
            height = heightSize
            width = height
        } else {
            widthSize = heightSize
            height = widthSize
            width = height
        }

        setMeasuredDimension(width, height)
    }

    /**
     * Funcion que se ejecuta al dibujar la vista dibujando el tablero
     *
     * @param canvas que dibujara el tablero
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val boardWidth = width.toFloat()
        val boardHeight = height.toFloat()
        canvas.drawRect(0f, 0f, boardWidth, boardHeight, backgroundPaint)
        drawCircles(canvas, linePaint)
    }

    /**
     * Funcion que se ejecuta cuando cambia el tamaño de la vista
     *
     * @param w ancho nuevo
     * @param h alto nuevo
     * @param oldw ancho antiguo
     * @param oldh alto antiguo
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        widthOfTile = (w / board!!.columnas).toFloat()
        heightOfTile = (h / board!!.filas).toFloat()
        if (widthOfTile < heightOfTile)
            radio = widthOfTile * 0.3f
        else
            radio = heightOfTile * 0.3f
        super.onSizeChanged(w, h, oldw, oldh)
    }

    /**
     * Funcion que dibuja todos los circulos de las casillas en el tablero
     *
     * @param canvas Canvas que dibuja los circulos
     * @param paint Paint que guardara los colores para dibujar
     */
    private fun drawCircles(canvas: Canvas, paint: Paint) {
        var centerRow: Float
        var centerColumn: Float
        for (i in 0 until board!!.filas) {
            val pos = board!!.filas - i - 1
            centerRow = heightOfTile * (1 + 2 * pos) / 2f
            for (j in 0 until board!!.columnas) {
                centerColumn = widthOfTile * (1 + 2 * j) / 2f
                paint.setColor(board!!, board!!.filas - 1 - i, j, context)
                canvas.drawCircle(centerColumn, centerRow, radio, paint)
            }
        }
    }

    /**
     * Funcion que se ejecuta cuando se realiza una pulsacion en la vista
     *
     * @param event Evento con los datos de la pulsacion
     * @return Booleano que incdica si ha salido bien o no la funcion
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
//        myGestureDetector.onTouchEvent(event)
//        return true
        if (onPlayListener == null)
            return super.onTouchEvent(event)
        if (board!!.estado != Tablero.EN_CURSO) {
            Snackbar.make(
                this,
                R.string.round_already_finished, Snackbar.LENGTH_SHORT
            ).show()
            return super.onTouchEvent(event)
        }
        if (event.action == MotionEvent.ACTION_DOWN ) {
            try {
                onPlayListener?.onPlay(fromEventToJ(event))
            } catch (e: ExcepcionJuego) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    /**
     * Funcion que dado un evento devuelve la coordenada de la columna pulsada en la vista
     *
     * @param event El evento del cual sacar la columna
     */
    private fun fromEventToJ(event: MotionEvent): Int {
        return (event.x / widthOfTile).toInt()
    }

    /**
     * Setter del OnPLayListener
     *
     * @param listener el listener del cual hacer set
     */
    fun setOnPlayListener(listener: OnPlayListener) {
        this.onPlayListener = listener
    }

    /**
     * Setter del tablero en la clase
     *
     * @param board Tablero del cual hacer set
     */
    fun setBoard(board: TableroC4) {
        this.board = board
    }


    fun print_board(){
        print(this.board)
    }

}
