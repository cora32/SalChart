package org.iskopasi.salchart

import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.util.SortedList
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import org.iskopasi.salchart.dagger.DaggerMainComponent
import org.iskopasi.salchart.dagger.MainComponent
import org.iskopasi.salchart.dagger.MainModule
import org.iskopasi.salchart.databinding.ActivityMainBinding
import org.iskopasi.salchart.databinding.MoneyListitemBinding
import org.iskopasi.salchart.room.MoneyData
import java.util.*


class MainActivity : LifecycleActivity() {
    companion object {
        @JvmStatic lateinit var daggerGraph: MainComponent
    }

    private lateinit var model: SalaryViewModel
    val adapter = Adapter(this)
    private lateinit var binding: ActivityMainBinding

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        daggerGraph = DaggerMainComponent
                .builder()
                .mainModule(MainModule(this))
                .build()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.salchartMap.mainView = binding.salchartMain
        model = ViewModelProviders.of(this).get(SalaryViewModel::class.java)
        model.data.observe(this, android.arch.lifecycle.Observer<List<MoneyData>> { model ->
            Log.e("Observer", "model: " + model)

            if (model == null)
                return@Observer

            binding.salchartMap.data = model
            binding.salchartMain.data = model
            adapter.dataList.clear()
            adapter.dataList.addAll(model)
        })

        model.saveData()

        setActionBar(binding.toolbar)
        actionBar?.title = "Salary accountant"

        adapter.setHasStableIds(true)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.setHasFixedSize(true)
        binding.rv.itemAnimator = null
        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(this, (binding.rv.layoutManager as LinearLayoutManager).orientation))
    }

    class Adapter(context: Context) : RecyclerView.Adapter<Adapter.ViewHolder>() {
        val context by lazy { context }
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder =
                ViewHolder(MoneyListitemBinding.inflate(LayoutInflater.from(context), parent, false))

        override fun getItemCount(): Int = dataList.size()

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(dataList.get(position))
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
                    Comparator<MoneyData> { (id), (id2) -> id.compareTo(id2) }.compare(a, b)

            override fun areItemsTheSame(item1: MoneyData?, item2: MoneyData?): Boolean = item1?.equals(item2) as Boolean

        })

        class ViewHolder(val binding: MoneyListitemBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(model: MoneyData) {
                binding.model = model
            }
        }
    }
}
