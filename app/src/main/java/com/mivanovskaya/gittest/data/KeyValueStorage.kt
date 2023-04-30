package com.mivanovskaya.gittest.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

const val AUTH_TOKEN = "auth_token"
const val TOKEN_SHARED_NAME = "token_shared_pref"
const val LOGIN_KEY = "login"

@Singleton
class KeyValueStorage @Inject constructor(@ApplicationContext context: Context) {

    private val prefs: SharedPreferences
    private val editor: SharedPreferences.Editor

    var authToken: String?
        get() = prefs.getString(AUTH_TOKEN, "")
        set(token) = editor.putString(AUTH_TOKEN, token).apply()

    var login: String?
        get() = prefs.getString(LOGIN_KEY, "")
        set(login) = editor.putString(LOGIN_KEY, login).apply()

    private fun createSharedPrefs(context: Context): SharedPreferences {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            EncryptedSharedPreferences.create(
                context,
                TOKEN_SHARED_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } else {
            context.getSharedPreferences(TOKEN_SHARED_NAME, MODE_PRIVATE)
        }
    }

    init {
        prefs = createSharedPrefs(context)
        editor = prefs.edit()
    }
}