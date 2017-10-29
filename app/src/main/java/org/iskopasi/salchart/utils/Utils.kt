package org.iskopasi.salchart.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewAnimationUtils
import android.view.inputmethod.InputMethodManager

/**
 * Created by cora32 on 29.10.2017.
 */
object Utils {
    fun revealView(positionView: View, targetView: View, runnable: Runnable?) {
        val array = IntArray(2)
        positionView.getLocationInWindow(array)
        val cx = (positionView.x + positionView.width / 2).toInt()
        val cy = array[1] - positionView.bottom / 2

        val finalRadius = Math.max(targetView.width, targetView.height)

        var anim: Animator? = null
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (positionView.isAttachedToWindow && targetView.isAttachedToWindow)
                anim = ViewAnimationUtils.createCircularReveal(targetView, cx, cy, 0f, finalRadius.toFloat())
        }

        targetView.visibility = View.VISIBLE
        if (anim != null)
            anim.start()

        if (runnable != null)
            Handler().postDelayed(runnable, 2000L)
    }

    fun revealView(cx: Int, cy: Int, targetView: View, runnable: Runnable?) {
        val finalRadius = Math.max(targetView.width, targetView.height)

        var anim: Animator? = null
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (targetView.isAttachedToWindow)
                anim = ViewAnimationUtils.createCircularReveal(targetView, cx, cy, 0f, finalRadius.toFloat())
        }

        targetView.visibility = View.VISIBLE
        if (anim != null)
            anim.start()

        if (runnable != null)
            Handler().postDelayed(runnable, 2000L)
    }

    fun hideView(positionView: View, targetView: View) {
        val array = IntArray(2)
        positionView.getLocationInWindow(array)
        val cx = (positionView.left + positionView.right) / 2
        val cy = array[1] - positionView.bottom / 2

        val initialRadius = targetView.width

        var anim: Animator? = null
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (positionView.isAttachedToWindow && targetView.isAttachedToWindow)
                anim = ViewAnimationUtils.createCircularReveal(targetView, cx, cy, initialRadius.toFloat(), 0f)
        }

        if (anim != null) {
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    targetView.visibility = View.GONE
                }
            })

            anim.start()
        } else {
            targetView.visibility = View.GONE
        }
    }

    fun hideKeyboard(context: Context) {
        val view = (context as AppCompatActivity).currentFocus
        if (view != null) {
            view.clearFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        view.requestFocus()
        imm.showSoftInput(view, 0)
    }
}