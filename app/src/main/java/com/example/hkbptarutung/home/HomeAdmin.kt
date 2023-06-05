package com.example.hkbptarutung.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ExpandableListView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.hkbptarutung.R
import com.example.hkbptarutung.adapters.ExpandableListAdapter
import com.example.hkbptarutung.model.ExpandedMenuModel
import com.google.android.material.navigation.NavigationView

class HomeAdmin : AppCompatActivity() {

    private var mDrawerLayout: DrawerLayout? = null
    var mMenuAdapter: ExpandableListAdapter? = null
    var expandableList: ExpandableListView? = null
    var listDataHeader: ArrayList<ExpandedMenuModel>? = null
    var listDataChild: HashMap<ExpandedMenuModel, List<String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.hide()
        val ab = supportActionBar
        /* to set the menu icon image*/if (ab != null) {
            ab.setHomeAsUpIndicator(android.R.drawable.ic_menu_add)
            ab.setDisplayHomeAsUpEnabled(true)
        }
        supportActionBar?.hide()
        mDrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        expandableList = findViewById<View>(R.id.navigationmenu) as ExpandableListView
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.let { setupDrawerContent(it) }
        prepareListData()
        mMenuAdapter = ExpandableListAdapter(
            this,
            listDataHeader!!, listDataChild!!, expandableList!!
        )

        // setting list adapter
        expandableList!!.setAdapter(mMenuAdapter)
        expandableList!!.setOnChildClickListener { expandableListView, view, i, i1, l -> //Log.d("DEBUG", "submenu item clicked");
            false
        }
        expandableList!!.setOnGroupClickListener { expandableListView, view, i, l -> //Log.d("DEBUG", "heading clicked");
            false
        }
    }

    private fun prepareListData() {
        listDataHeader = ArrayList()
        listDataChild = HashMap()
        val item1 = ExpandedMenuModel()
        item1.iconName = "Approval"
        // Adding data header
        listDataHeader?.add(item1)
        val item2 = ExpandedMenuModel()
        item2.iconName = "Update Warta"
        listDataHeader?.add(item2)
        val item3 = ExpandedMenuModel()
        item3.iconName = "Update Acara Minggu"
        listDataHeader?.add(item3)

        val list = resources.getStringArray(R.array.menu_registrasi)
        listDataHeader?.get(0)?.apply {
            listDataChild!![this] = list.toList()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    fun openDrawer() {
        mDrawerLayout!!.openDrawer(GravityCompat.START)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        //revision: this don't works, use setOnChildClickListener() and setOnGroupClickListener() above instead
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            mDrawerLayout!!.closeDrawers()
            true
        }
    }
}