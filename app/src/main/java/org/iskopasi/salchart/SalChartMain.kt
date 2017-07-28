package org.iskopasi.salchart

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet

/**
 * Created by cora32 on 25.07.2017.
 */
class SalChartMain : BaseSalChart {
    private var leftIndex = 0
    private var rightIndex = 0
    private val paintBackground = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintFill = Paint(Paint.ANTI_ALIAS_FLAG.or(Paint.FILTER_BITMAP_FLAG))
    private val paintLine = Paint(Paint.ANTI_ALIAS_FLAG.or(Paint.FILTER_BITMAP_FLAG))

    init {
        paintBackground.color = ContextCompat.getColor(context, R.color.textColor1)
        paintBackground.strokeWidth = .1f

        paintFill.color = ContextCompat.getColor(context, R.color.textColor1)
        paintFill.style = Paint.Style.FILL_AND_STROKE
        paintFill.strokeWidth = 2f
        paintFill.pathEffect = CornerPathEffect(4f)

        val transparentDark = ContextCompat.getColor(context, R.color.transparent_dark_2)
        paintFill.shader = LinearGradient(0f, 0f, 0f, 0f, transparentDark, transparentDark, Shader.TileMode.CLAMP)

        paintLine.color = ContextCompat.getColor(context, R.color.textColor1)
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 4f
        paintLine.pathEffect = CornerPathEffect(4f)

        paintLine.shader = LinearGradient(0f, 0f, 0f, 0f, transparentDark, transparentDark, Shader.TileMode.CLAMP)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        path.reset()
        pathLine.reset()
        drawBackground(canvas)
        drawChart(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (data.size > 1)
            xFactor = measuredWidth / (rightIndex - leftIndex).toFloat()
        yFactor = measuredHeight / max
    }

    private val pathLine = Path()
    private fun drawChart(canvas: Canvas) {
        if (data.isEmpty())
            return

        data[leftIndex].value.let {
            path.moveTo(0f, measuredHeight.toFloat() + 20)
            pathLine.moveTo(0f, measuredHeight.toFloat() + 20)
        }
        data.slice(leftIndex..rightIndex)
                .forEachIndexed { index, i ->
                    val x = index.toFloat() * xFactor
                    val y = measuredHeight.toFloat() - i.value * yFactor
                    path.lineTo(x, y)
                    pathLine.lineTo(x, y)
                }
        path.lineTo(measuredWidth.toFloat(), measuredHeight.toFloat() + 20)
        pathLine.lineTo(measuredWidth.toFloat(), measuredHeight.toFloat() + 20)
        path.close()
        pathLine.close()

        canvas.drawPath(path, paintFill)
        canvas.drawPath(pathLine, paintLine)
    }

    private fun drawBackground(canvas: Canvas) {
        val step = measuredHeight / 10
        for (i in 0..measuredHeight step step)
            canvas.drawLine(0f, i.toFloat(), measuredWidth.toFloat(), i.toFloat(), paintBackground)
    }

    public override fun setFrameIndexes(leftIndex: Int, rightIndex: Int) {
        this.leftIndex = leftIndex
        this.rightIndex = rightIndex
        if (data.size > 1)
            xFactor = measuredWidth / (rightIndex - leftIndex).toFloat()
        invalidate()
    }
}