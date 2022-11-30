package com.test.myplaylist.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.test.myplaylist.domain.Music
import javax.inject.Inject

class NormalSharedPreferences @Inject constructor(
    val moshi: Moshi,
    private val applicationContext: Context,
    private val defaultSharedPreferences: SharedPreferences
) : BaseSharedPreferences() {

    init {
        useDefaultSharedPreferences()
    }

    fun useDefaultSharedPreferences() {
        sharedPreferences = defaultSharedPreferences
    }

    // Use this function for creating a custom sharedPreferences if needed
    fun useCustomSharedPreferences(name: String) {
        sharedPreferences = applicationContext.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun <T> setValue(key: String, value: T) {
        set(key, value)
    }

    fun setValueInt(key: String, value: Int) {
        setInt(key, value)
    }

    inline fun <reified T> getValue(key: String): T? = get(key) as T?

    fun setMusicData(music: Music) {
        val moshiAdapter = moshi.adapter(Music::class.java)
        val data = moshiAdapter.toJson(music)
        setValue("Data", data ?: moshiAdapter.toJson(Music()))    }

    fun getMusicData(): Music {
        val moshiAdapter = moshi.adapter(Music::class.java)
        get<String>("Data")?.apply {
            if (this.isNotEmpty()) {
                return moshiAdapter.fromJson(this) ?: Music()
            }
        }
        return Music()    }
}
