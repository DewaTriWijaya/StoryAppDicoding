package com.andflube.storyapp.model

import android.content.Context

class UserPreference(context: Context) {

    companion object {
        const val PREFS_NAME = "user_pref"
        const val NAME_KEY = "name"
        const val EMAIL_KEY = "email"
        const val USER_ID_KEY = "user_id"
        const val TOKEN_KEY = "token"
        const val IS_LOGIN = "isLogin"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setStringPreference(prefKey: String, value: String) {
        val editor = preferences.edit()
        editor.putString(prefKey, value)
        editor.apply()
    }

    fun setBooleanPreference(prefKey: String, value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(prefKey, value)
        editor.apply()
    }

    fun clearPreferences() {
        val editor = preferences.edit()
        editor.clear().apply()
    }

    val token = preferences.getString(TOKEN_KEY, "")
    val isLogin = preferences.getBoolean(IS_LOGIN, false)
    val name = preferences.getString(NAME_KEY, "")

}
