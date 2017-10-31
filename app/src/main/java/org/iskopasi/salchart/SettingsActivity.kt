package org.iskopasi.salchart

import android.Manifest
import android.app.AlertDialog
import android.content.ComponentName
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.*
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import org.iskopasi.salchart.databinding.ActivitySettingsBinding
import org.iskopasi.salchart.receivers.SMSReceiver
import org.iskopasi.salchart.utils.Consts
import org.iskopasi.salchart.utils.PrefHelper
import org.iskopasi.salchart.utils.RegexUtils
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

    @Inject lateinit var repository: SalaryRepository
    private var firstConstraint: String = ""
    private var secondConstraint: String = ""
    private lateinit var binding: ActivitySettingsBinding
    private var receiver: SMSReceiver? = null
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            match()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_toolbar, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (isRegexpCLVisible())
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
            if (b)
                checkPermissions(b)
            else {
                enableSMSFeature(b)
            }
        })
        val isReadSms = PrefHelper.getBool(Consts.READ_SMS, false)
        binding.readSmsSwitch.isChecked = isReadSms

        binding.addButton.setOnClickListener({
            supportActionBar?.title = getString(R.string.add_new_regexp)
            Utils.revealView(binding.addButton, binding.addCl, null)
            Utils.showKeyboard(this, binding.sampleEt)
            match()
        })

        binding.leftConstraintTil.editText?.addTextChangedListener(textWatcher)
        binding.rightConstraintTil.editText?.addTextChangedListener(textWatcher)
        binding.sampleEt.addTextChangedListener(textWatcher)
    }

    private fun enableSMSFeature(b: Boolean) {
        PrefHelper.saveBoolean(Consts.READ_SMS, b)
        showRegexpPanel(b)
        enableSMSReceiver(b)
    }

    private fun checkPermissions(b: Boolean) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_SMS),
                    Consts.PERMISSIONS_REQUEST_READ_SMS)
        } else {
            enableSMSFeature(b)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            Consts.PERMISSIONS_REQUEST_READ_SMS -> {
                val enable = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED

                enableSMSFeature(enable)
                binding.readSmsSwitch.isChecked = enable
            }
        }
    }

    private fun enableSMSReceiver(isEnabled: Boolean) {
        enableBroadcastReceiver(isEnabled)
        if (isEnabled) {
            receiver = SMSReceiver()
            registerReceiver(receiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
        } else {
            unregisterSMSReceiver()
        }
    }

    private fun unregisterSMSReceiver() {
        if (receiver != null)
            unregisterReceiver(receiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterSMSReceiver()
    }

    private fun match() {
        firstConstraint = RegexUtils
                .escapeSpecialRegexChars(binding.leftConstraintTil.editText?.text.toString())
                .replace(" ", "\\s")
        secondConstraint = RegexUtils.escapeSpecialRegexChars(binding.rightConstraintTil.editText?.text.toString())
                .replace(" ", "\\s")

        val text = binding.sampleEt.text
//        val regexp = "(?<=$firstConstraint).*?(?=$secondConstraint)"
        var regexp = ".*"
        if (!firstConstraint.isEmpty())
            regexp = "(?<=$firstConstraint)$regexp"
        if (!secondConstraint.isEmpty())
            regexp += "?(?=$secondConstraint)"
        val matcher = Pattern.compile(regexp, Pattern.MULTILINE).matcher(text)

        if (matcher.find()) {
            val match = matcher.group()
            val regionStart = matcher.start()
            val regionEnd = matcher.end()
            val newSpannableString = SpannableString(text)
            newSpannableString.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(this, R.color.textColor3)),
                    0, regionStart, 0)
            newSpannableString.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(this, R.color.textColor3)),
                    regionEnd, newSpannableString.length, 0)
            newSpannableString.setSpan(UnderlineSpan(), regionStart, regionEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            binding.resultTv.text = newSpannableString
        } else
            binding.resultTv.text = text
        binding.regexpTv.text = regexp
    }

    private fun enableBroadcastReceiver(isEnabled: Boolean) {
        val receiver = ComponentName(this, SMSReceiver::class.java)

        if (isEnabled) {
            packageManager.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP)
        } else {
            packageManager.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP)
        }
    }

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
        if (isRegexpCLVisible())
            super.onBackPressed()
        else
            hideAddCl()
    }

    private fun hideAddCl() {
        supportActionBar?.title = getString(R.string.settings)
        Utils.hideView(binding.addButton, binding.addCl)
        Utils.hideKeyboard(this)
    }

    private fun isRegexpCLVisible(): Boolean = binding.addCl.visibility != View.VISIBLE
}