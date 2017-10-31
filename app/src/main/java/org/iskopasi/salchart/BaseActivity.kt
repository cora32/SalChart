package org.iskopasi.salchart

import android.arch.lifecycle.LifecycleRegistry
import android.support.v7.app.AppCompatActivity

/**
 * Created by cora32 on 29.08.2017.
 */
open class BaseActivity : AppCompatActivity() {
    private val registry by lazy { LifecycleRegistry(this) }
    override fun getLifecycle(): LifecycleRegistry = registry
}