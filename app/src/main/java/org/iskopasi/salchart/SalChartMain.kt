package org.iskopasi.salchart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View

/**
 * Created by cora32 on 25.07.2017.
 */
class SalChartMain : View {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.color = ContextCompat.getColor(context, R.color.red)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawBackground(canvas)
    }

    private fun drawBackground(canvas: Canvas?) {
        val step = measuredHeight / 10
        for (i in 0..measuredHeight step step)
            canvas?.drawLine(0f, i.toFloat(), measuredWidth.toFloat(), i.toFloat(), paint)
    }
}