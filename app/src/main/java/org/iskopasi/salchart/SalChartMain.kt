package org.iskopasi.salchart

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent

/**
 * Created by cora32 on 25.07.2017.
 */
class SalChartMain : BaseSalChart {
    private var leftIndex = 0
    private var rightIndex = 0
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintCircle = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintPoint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintBackground = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintFill = Paint(Paint.ANTI_ALIAS_FLAG.or(Paint.FILTER_BITMAP_FLAG))
    private val paintLine = Paint(Paint.ANTI_ALIAS_FLAG.or(Paint.FILTER_BITMAP_FLAG))
    private val circleRadius by lazy { context.resources.getDimension(R.dimen.circle_radius) }
    private val textSize by lazy { context.resources.getDimension(R.dimen.chart_text_size) }
    private val baseTopOffset by lazy { context.resources.getDimension(R.dimen.base_top_offset_y) }
    private val baseBottomOffset by lazy { context.resources.getDimension(R.dimen.base_bottom_offset_y) }
    private val textBounds: Rect = Rect()
    private val pathLine = Path()
    private var clickPosition: Int = -1

    init {
        paintText.color = ContextCompat.getColor(context, R.color.black)
        paintText.textSize = textSize

        paintPoint.color = ContextCompat.getColor(context, R.color.red)
        paintPoint.strokeWidth = .8f
        paintPoint.style = Paint.Style.STROKE

        paintCircle.color = ContextCompat.getColor(context, R.color.textColor1)
        paintCircle.strokeWidth = .6f
        paintCircle.style = Paint.Style.STROKE

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
        if (data.isEmpty())
            return

        canvas.drawColor(Color.WHITE)
        path.reset()
        pathLine.reset()
        drawBackground(canvas)
        drawChart(canvas)
        drawPoint(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (data.size > 1)
            xFactor = measuredWidth / (rightIndex - leftIndex).toFloat()
        yFactor = (measuredHeight - (baseTopOffset + baseBottomOffset)) / max
    }

    private fun drawChart(canvas: Canvas) {
//        val bottom = measuredHeight - baseBottomOffset
        val bottom = measuredHeight.toFloat()
        data[leftIndex].value.let {
            path.moveTo(0f, bottom)
            pathLine.moveTo(0f, bottom)
        }
        data.slice(leftIndex..rightIndex)
                .forEachIndexed { index, i ->
                    val x = index * xFactor
                    val y = bottom - i.value * yFactor
                    path.lineTo(x, y)
                    pathLine.lineTo(x, y)
                    canvas.drawCircle(x, y, circleRadius, paintCircle)
                }
        path.lineTo(measuredWidth.toFloat(), bottom)
        pathLine.lineTo(measuredWidth.toFloat(), bottom)
        path.close()
        pathLine.close()

        canvas.drawPath(path, paintFill)
        canvas.drawPath(pathLine, paintLine)
    }

    private fun drawBackground(canvas: Canvas) {
        val step = ((measuredHeight - (baseTopOffset + baseBottomOffset)) / 10).toInt()
        val moneyValueStep = max / 10f
        var moneyValueAcc = 0f
        val top = baseTopOffset.toInt()
        val bottom = (measuredHeight - baseBottomOffset).toInt()
        for (i in top..bottom step step) {
            canvas.drawLine(0f, i.toFloat(), measuredWidth.toFloat(), i.toFloat(), paintBackground)

            //Draw values
            moneyValueAcc += moneyValueStep
            val text = "%.1f".format(moneyValueAcc)
            paintText.getTextBounds(text, 0, text.length, textBounds)
            val textWidthOffset = (textBounds.right - textBounds.left)
            canvas.drawText(text,
                    (measuredWidth - (textWidthOffset + (textWidthOffset shr 2))).toFloat(),
                    ((measuredHeight - i) - (textBounds.top shr 1)).toFloat(), paintText)
        }
    }

    private fun drawPoint(canvas: Canvas) {
        if (clickPosition > -1) {
            val index = clickPosition + leftIndex
            val x = clickPosition * xFactor
            val y = measuredHeight.toFloat() - data[index].value * yFactor
            canvas.drawCircle(x, y, 19f, paintPoint)
            canvas.drawText("Date: " + data[index].date, (measuredWidth shr 1).toFloat(), 20f, paintText)
            canvas.drawText("Value: " + data[index].value, (measuredWidth shr 1).toFloat(), 40f, paintText)
        }
    }

    public override fun setFrameIndexes(leftIndex: Int, rightIndex: Int) {
        Log.e("setting index", " $leftIndex $rightIndex")
        this.leftIndex = leftIndex
        this.rightIndex = rightIndex
        if (data.size > 1)
            xFactor = measuredWidth / (rightIndex - leftIndex).toFloat()
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_MOVE -> {
                val x = event.rawX
                val position = x / xFactor
                clickPosition = Math.round(position)
                invalidate()
                return true
            }
        }
        return true
    }
}