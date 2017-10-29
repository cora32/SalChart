package org.iskopasi.salchart

import android.app.AlertDialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import org.iskopasi.salchart.databinding.ActivitySettingsBinding
import org.iskopasi.salchart.utils.Consts
import org.iskopasi.salchart.utils.PrefHelper
import org.iskopasi.salchart.utils.Utils
import org.jetbrains.anko.doAsync
import java.util.regex.Pattern
import javax.inject.Inject


/**
 * Created by cora32 on 29.08.2017.
 */
class SettingsActivity : BaseActivity() {
    init {
        MainActivity.daggerGraph.inject(this)
    }

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_toolbar, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (backPressed())
                    finish()
                else
                    hideAddCl()
            }
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

        binding.readSmsSwitch.setOnCheckedChangeListener({ _, b
            ->
            PrefHelper.saveBoolean(Consts.READ_SMS, b)
            showRegexpPanel(b)
        })
        val isReadSms = PrefHelper.getBool(Consts.READ_SMS, false)
        binding.readSmsSwitch.isChecked = isReadSms

        binding.addButton.setOnClickListener({
            Utils.revealView(binding.addButton, binding.addCl, null)
            Utils.showKeyboard(this, binding.sampleEt)
        })

        binding.leftConstraintTil.editText?.addTextChangedListener(textWatcher)
        binding.rightConstraintTil.editText?.addTextChangedListener(textWatcher)
        binding.sampleEt.addTextChangedListener(textWatcher)

    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            match()
        }
    }

    private fun match() {
        firstConstraint = binding.leftConstraintTil.editText?.text.toString()
        secondConstraint = binding.rightConstraintTil.editText?.text.toString()

        val text = binding.sampleEt.text.toString()
//        val regexp = "(?<=$firstConstraint)(.*)(?=$secondConstraint)"
        var regexp = "(.*)"
        if (!firstConstraint.isEmpty())
            regexp = "(?<=$firstConstraint)$regexp"
        if (!secondConstraint.isEmpty())
            regexp += "(?=$secondConstraint)"
        val matcher = Pattern.compile(regexp, Pattern.MULTILINE).matcher(text)

        if (matcher.find())
            binding.resultTv.text = matcher.group()
        binding.regexpTv.text = regexp
    }

    private var firstConstraint: String = ""
    private var secondConstraint: String = " "

    @Inject lateinit var repository: SalaryRepository
    private fun showRegexpPanel(b: Boolean) {
        binding.regexpCl.visibility = if (b) View.VISIBLE else View.GONE
        TransitionManager.beginDelayedTransition(binding.scrollView3, AutoTransition())
        if (b) {
            doAsync {
                val regexpList = repository.getRegexpList()
                if (regexpList.isEmpty())
                    binding.noRegexTv.visibility = View.VISIBLE
                else
                    binding.regexpLl.visibility = View.VISIBLE
            }
        }
    }

    override fun onBackPressed() {
        if (backPressed())
            super.onBackPressed()
        else
            hideAddCl()
    }

    private fun hideAddCl() {
        Utils.hideView(binding.addButton, binding.addCl)
        Utils.hideKeyboard(this)
    }

    private fun backPressed(): Boolean = binding.addCl.visibility != View.VISIBLE
}