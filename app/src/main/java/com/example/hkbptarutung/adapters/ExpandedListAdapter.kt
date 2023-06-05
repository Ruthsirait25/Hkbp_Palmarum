package com.example.hkbptarutung.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.TextView
import com.example.hkbptarutung.R
import com.example.hkbptarutung.model.ExpandedMenuModel
import com.example.hkbptarutung.registrasi.ActivityApprovalRegistrasi


class ExpandableListAdapter(
    private val mContext: Context,
    listDataHeader: List<ExpandedMenuModel>,
    listChildData: HashMap<ExpandedMenuModel, List<String>>,
    mView: ExpandableListView
) :
    BaseExpandableListAdapter() {
    private val mListDataHeader // header titles
            : List<ExpandedMenuModel>

    // child data in format of header title, child title
    private val mListDataChild: HashMap<ExpandedMenuModel, List<String>>
    var expandList: ExpandableListView

    init {
        mListDataHeader = listDataHeader
        mListDataChild = listChildData
        expandList = mView
    }

    override fun getGroupCount(): Int {
        val i = mListDataHeader.size
        Log.d("GROUPCOUNT", i.toString())
        return mListDataHeader.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        var childCount = 0
        if (groupPosition != 2) {
            childCount = mListDataChild[mListDataHeader[groupPosition]]?.size ?: 0
        }
        return childCount
    }

    override fun getGroup(groupPosition: Int): Any {
        return mListDataHeader[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        Log.d(
            "CHILD", mListDataChild[mListDataHeader[groupPosition]]
                ?.get(childPosition) ?: "null"
        )
        return mListDataChild[mListDataHeader[groupPosition]]
            ?.get(childPosition) ?: "bye"
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertViewParam: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertViewParam
        val headerTitle: ExpandedMenuModel = getGroup(groupPosition) as ExpandedMenuModel
        if (convertView == null) {
            val infalInflater = mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.listheader, null)
        }
        val lblListHeader = convertView?.findViewById<View>(R.id.submenu) as TextView
        lblListHeader.setTypeface(null, Typeface.BOLD)
        lblListHeader.text = headerTitle.iconName
        return convertView
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertViewParam: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertViewParam
        val childText = getChild(groupPosition, childPosition) as String
        if (convertView == null) {
            val infalInflater = mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.list_submenu, null)
        }
        val txtListChild = convertView
            ?.findViewById<View>(R.id.submenu) as TextView
        txtListChild.text = childText
        txtListChild.setOnClickListener {
            it.context.apply {
                startActivity(Intent(this, ActivityApprovalRegistrasi::class.java).apply{
                    putExtra("type", childText)
                })
            }
        }
        return convertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}