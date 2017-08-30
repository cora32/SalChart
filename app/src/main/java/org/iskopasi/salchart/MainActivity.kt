package org.iskopasi.salchart

import android.arch.lifecycle.ViewModelProviders
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.util.SortedList
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import org.iskopasi.salchart.dagger.DaggerMainComponent
import org.iskopasi.salchart.dagger.MainComponent
import org.iskopasi.salchart.databinding.ActivityMainBinding
import org.iskopasi.salchart.databinding.MoneyListitemBinding
import org.iskopasi.salchart.receivers.SMSReceiver
import org.iskopasi.salchart.room.MoneyData
import java.util.*


class MainActivity : BaseActivity() {
    companion object {
        @JvmStatic lateinit var daggerGraph: MainComponent
    }

    private lateinit var model: SalaryViewModel
    private val adapter = Adapter(this)
    private lateinit var binding: ActivityMainBinding

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        daggerGraph = DaggerMainComponent
                .builder()
                .build()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.salchartMap.mainView = binding.salchartMain
        model = ViewModelProviders.of(this).get(SalaryViewModel::class.java)
        model.data.observe(this, android.arch.lifecycle.Observer<List<MoneyData>> { model ->
            if (model == null) return@Observer

            binding.salchartMap.data = model
            binding.salchartMain.data = model
            adapter.dataList.clear()
            adapter.dataList.addAll(model)

            supportActionBar?.title = getString(R.string.current_balance) +
                    (if (model.isNotEmpty()) model.last().value.toString() else "0") + "$"
        })

        model.saveData()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "--"

        adapter.setHasStableIds(true)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.setHasFixedSize(true)
        binding.rv.itemAnimator = null
        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(this, (binding.rv.layoutManager as LinearLayoutManager).orientation))

        enableBroadcastReceiver()
    }

    private fun enableBroadcastReceiver() {
        val receiver = ComponentName(this, SMSReceiver::class.java)
        val pm = this.packageManager

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP)
    }

    class Adapter(context: Context) : RecyclerView.Adapter<Adapter.ViewHolder>() {
        private val context by lazy { context }
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder =
                ViewHolder(MoneyListitemBinding.inflate(LayoutInflater.from(context), parent, false))

        override fun getItemCount(): Int = dataList.size()

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(dataList.get(position))
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        val dataList: SortedList<MoneyData> = SortedList(MoneyData::class.java, object : SortedList.Callback<MoneyData>() {
            override fun onChanged(position: Int, count: Int) {
                notifyItemRangeRemoved(position, count)
            }

            override fun onInserted(position: Int, count: Int) {
                notifyItemRangeRemoved(position, count)
            }

            override fun onMoved(position: Int, count: Int) {
                notifyItemRangeRemoved(position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                notifyItemRangeRemoved(position, count)
            }

            override fun areContentsTheSame(item1: MoneyData?, item2: MoneyData?): Boolean {
                return false
            }

            override fun compare(a: MoneyData?, b: MoneyData?): Int =
                    Comparator<MoneyData> { (id), (id2) -> id2.compareTo(id) }.compare(a, b)

            override fun areItemsTheSame(item1: MoneyData?, item2: MoneyData?): Boolean = item1?.equals(item2) as Boolean

        })

        class ViewHolder(private val binding: MoneyListitemBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(model: MoneyData) {
                binding.model = model
            }
        }
    }
}
