package org.iskopasi.salchart

import android.content.Context
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import org.iskopasi.salchart.room.MoneyData

/**
 * Created by cora32 on 28.07.2017.
 */
open class BaseSalChart : View {
    protected var xFactor = 1f
    protected var yFactor = 1f
    protected val path = Path()
    protected var max = 1f
    var data: List<MoneyData> = ArrayList()
        set(value) {
            field = value
            max = data.maxBy { it.value }?.value!!
            requestLayout()
        }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    protected open fun setFrameIndexes(leftIndex: Int, rightIndex: Int) {}
}
