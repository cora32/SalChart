package org.iskopasi.salchart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet

/**
 * Created by cora32 on 25.07.2017.
 */
class SalChartMain : BaseSalChart {
    private var leftIndex = 0
    private var rightIndex = 0
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.color = ContextCompat.getColor(context, R.color.red)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        path.reset()
        drawBackground(canvas)
        drawChart(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (data.size > 1)
            xFactor = measuredWidth / (rightIndex - leftIndex).toFloat()
        yFactor = measuredHeight / max
    }

    private fun drawChart(canvas: Canvas) {
        if (data.isEmpty())
            return

        data[leftIndex].value.let { path.moveTo(0f, measuredHeight.toFloat()) }
        data.slice(leftIndex..rightIndex)
                .forEachIndexed { index, i ->
                    val x = index.toFloat() * xFactor
                    val y = measuredHeight.toFloat() - i.value * yFactor
                    path.lineTo(x, y)
                }
        path.lineTo(measuredWidth.toFloat(), measuredHeight.toFloat())
        path.close()

        canvas.drawPath(path, paint)
    }

    private fun drawBackground(canvas: Canvas) {
        val step = measuredHeight / 10
        for (i in 0..measuredHeight step step)
            canvas.drawLine(0f, i.toFloat(), measuredWidth.toFloat(), i.toFloat(), paint)
    }

    public override fun setFrameIndexes(leftIndex: Int, rightIndex: Int) {
        this.leftIndex = leftIndex
        this.rightIndex = rightIndex
        if (data.size > 1)
            xFactor = measuredWidth / (rightIndex - leftIndex).toFloat()
        invalidate()
    }
}