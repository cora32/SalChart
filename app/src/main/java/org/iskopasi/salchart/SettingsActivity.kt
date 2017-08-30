package org.iskopasi.salchart

import android.app.AlertDialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import org.iskopasi.salchart.databinding.ActivitySettingsBinding
import org.iskopasi.salchart.utils.Consts
import org.iskopasi.salchart.utils.PrefHelper


/**
 * Created by cora32 on 29.08.2017.
 */
class SettingsActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_toolbar, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.currencySignValue.text = PrefHelper.getString(Consts.CURRENCY_KEY, getString(R.string.dollar_sign))

        binding.currencySign.setOnClickListener {
            if (isFinishing)
                return@setOnClickListener

            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            AlertDialog.Builder(this)
                    .setTitle("Select currency")
                    .setPositiveButton(getString(R.string.ok), null)
                    .setNegativeButton(getString(R.string.cancel), null)
                    .setView(input)
                    .show()
        }
    }
}