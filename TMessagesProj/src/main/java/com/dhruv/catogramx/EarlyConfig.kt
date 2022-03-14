package com.dhruv.catogramx

import android.app.Activity
import android.content.SharedPreferences
import org.telegram.messenger.ApplicationLoader
import ua.itaysonlab.catogram.CatogramConfig
import ua.itaysonlab.catogram.preferences.ktx.boolean

object EarlyConfig {
	private val sharedPreferences: SharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("earlyconfig", Activity.MODE_PRIVATE)
	var enableSaf by sharedPreferences.boolean("cx_enable_saf", false)
	var alwaysShowDoubleBottom by sharedPreferences.boolean("cx_force_double_bottom", false)
}