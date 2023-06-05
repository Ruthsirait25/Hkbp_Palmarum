package com.example.hkbptarutung

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hkbptarutung.adapters.Adapter
import com.example.hkbptarutung.model.DataItem
import com.example.hkbptarutung.databinding.ActivityWartaJemaatBinding

class WartaJemaat : AppCompatActivity() {
    lateinit var binding: ActivityWartaJemaatBinding
    private val item = ArrayList<DataItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWartaJemaatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.wartaJemaat)

        binding.rcWarta.setHasFixedSize(true)
        item.addAll(getListUser())
        showList()
    }

    private fun showList() {
        binding.rcWarta.layoutManager = LinearLayoutManager(this)
        val userAdapter = Adapter(item)
        binding.rcWarta.adapter = userAdapter
    }

    private fun getListUser(): Collection<DataItem> {
        val dataItem = resources.getStringArray(R.array.itemList)
        val listItem = ArrayList<DataItem>()
        for (i in dataItem.indices){
            val item = DataItem(dataItem[i])
            listItem.add(item)
        }
        return listItem
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
    }
}