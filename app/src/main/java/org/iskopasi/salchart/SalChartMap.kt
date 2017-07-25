package org.iskopasi.salchart

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


/**
 * Created by cora32 on 25.07.2017.
 */
class SalChartMap : View {
    private val frameWidth = 200f
    private var leftRect = RectF(0f, 0f, 600f, 600f)
    private var rightRect = RectF(800f, 0f, 1000f, 600f)
    var data: List<MoneyData>? = null
        set(value) {
            field = value
            max = data?.maxBy { it.value }?.value!!
            invalidate()
        }
    private var max = 1f
    private val paintBackground = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val framePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var xFactor = 1f
    private var yFactor = 1f
    private val path = Path()
    private var cacheBitmap: Bitmap? = null

    init {
        paintBackground.color = ContextCompat.getColor(context, R.color.textColor1)
        paintBackground.strokeWidth = .1f
        paint.color = ContextCompat.getColor(context, R.color.red)
        paint.strokeWidth = .1f
        framePaint.color = ContextCompat.getColor(context, R.color.transparent_dark)
        framePaint.strokeWidth = .1f

        isDrawingCacheEnabled = true
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (data != null && data!!.size > 1)
            xFactor = measuredWidth / (data!!.size - 1).toFloat()
        yFactor = measuredHeight / max

        rightRect.right = measuredWidth.toFloat()
        rightRect.bottom = measuredHeight.toFloat()
        leftRect.right = rightRect.left - frameWidth
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        getDrawingCache(true)
        canvas.drawColor(Color.WHITE)

        if (cacheBitmap == null) {
            path.reset()
            drawBackground(canvas)
            drawChart(canvas)

            val bm = getDrawingCache(true)
            cacheBitmap?.recycle()
            cacheBitmap = bm.copy(bm.config, false)
        } else {
            canvas.drawBitmap(cacheBitmap, 0f, 0f, null)
        }
        drawFrame(canvas)
    }

    private fun drawFrame(canvas: Canvas) {
        canvas.drawRect(leftRect, framePaint)
        canvas.drawRect(rightRect, framePaint)
    }

    private fun drawChart(canvas: Canvas) {
        if (data == null || data?.size == 0)
            return

        data?.get(0)?.value?.let { path.moveTo(0f, measuredHeight.toFloat()) }
        data?.forEachIndexed { index, i ->
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
            canvas.drawLine(0f, i.toFloat(), measuredWidth.toFloat(), i.toFloat(), paintBackground)
    }

    var initialX = 0f
    var currentLeftRectRightConstraintX = 0f
    var currentRightRectLeftConstraintX = 0f
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = event.rawX

                rightRect.left = initialX + frameWidth / 2f
                leftRect.right = initialX - frameWidth / 2f

                checkFrameConstraints()

                currentLeftRectRightConstraintX = leftRect.right
                currentRightRectLeftConstraintX = rightRect.left

                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = initialX - event.rawX

                leftRect.right = currentLeftRectRightConstraintX - deltaX
                rightRect.left = currentRightRectLeftConstraintX - deltaX

                checkFrameConstraints()

                invalidate()
            }
        }

        return true
    }

    private fun checkFrameConstraints() {
        if (leftRect.right < 0) {
            leftRect.right = 0f
            rightRect.left = frameWidth
        } else if (rightRect.left > measuredWidth) {
            rightRect.left = measuredWidth.toFloat()
            leftRect.right = measuredWidth - frameWidth
        }
    }
}