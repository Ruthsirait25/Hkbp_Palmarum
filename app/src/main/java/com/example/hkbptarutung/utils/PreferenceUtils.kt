package com.example.hkbptarutung.utils

import android.content.Context

class PreferenceUtils {
    companion object {

        fun getPref(context: Context) = context.getSharedPreferences("app", Context.MODE_PRIVATE)

        fun setAdmin(context: Context, value: Boolean = true) {
            getPref(context).edit().putBoolean("isAdmin", value).apply()
        }

        fun isAdmin(context: Context) = getPref(context).getBoolean("isAdmin", false)
    }
}