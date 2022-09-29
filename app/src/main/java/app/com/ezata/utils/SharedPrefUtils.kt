package app.com.ezata.utils

import android.content.Context
import android.content.SharedPreferences

const val SHARED_PREF_NAME = "app_shared_pref"

object SharedPrefKey {
    const val STORE_OBJECT_ID = "store_object_id"
}

object SharedPrefUtils {

    private fun getSharedPref(context: Context?): SharedPreferences? {
        return context?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun getSharedPrefEditor(context: Context?): SharedPreferences.Editor? {
        return getSharedPref(context)?.edit()
    }

    fun <T> save(context: Context?, key: String, value: T) {
        val editor = getSharedPrefEditor(context)
        when (value) {
            is String -> editor?.putString(key, value)?.apply()
            is Boolean -> editor?.putBoolean(key, value)?.apply()
            is Int -> editor?.putInt(key, value)?.apply()
            is Double -> editor?.putFloat(key, value.toFloat())?.apply()
            is Float -> editor?.putFloat(key, value)?.apply()
            is Long -> editor?.putLong(key, value)?.apply()
        }
    }


//    fun saveInt(context: Context?, keyValue: SharedPrefIntKey) {
//        getSharedPrefEditor(context)?.putInt(keyValue.name, keyValue.value)?.apply()
//    }
//
//    fun saveBoolean(context: Context?, keyValue: SharedPrefBooleanKey) {
//        getSharedPrefEditor(context)?.putBoolean(keyValue.name, keyValue.value)?.apply()
//    }
//
//    fun saveString(context: Context?, keyValue: SharedPrefStringKey) {
//        getSharedPrefEditor(context)?.putString(keyValue.name, keyValue.value)?.apply()
//    }

    fun getInt(context: Context?, key: String, default: Int = -1): Int {
        return getSharedPref(context)?.getInt(key, default) ?: default
    }

    fun getString(context: Context?, key: String, default: String = ""): String {
        return getSharedPref(context)?.getString(key, default) ?: default
    }

    fun getBoolean(context: Context?, key: String, default: Boolean = false): Boolean {
        return getSharedPref(context)?.getBoolean(key, default) ?: default
    }
}