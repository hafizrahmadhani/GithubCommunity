package com.hafizrahmadhani.github.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Switch
import com.hafizrahmadhani.github.R
import com.hafizrahmadhani.github.alarm.AlarmReceiver

class SettingsActivity : AppCompatActivity() {

    private lateinit var alarmReceiver: AlarmReceiver
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switch : Switch
    private lateinit var button : Button

    companion object{
        private const val PREFERENCE = "Preference"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharePreference = applicationContext.getSharedPreferences(PREFERENCE, MODE_PRIVATE)

        alarmReceiver = AlarmReceiver()
        button = findViewById(R.id.button_language)
        switch = findViewById(R.id.alarm_switch)

        switch.isChecked = sharePreference.getBoolean(PREFERENCE, false)
        switch.setOnCheckedChangeListener { _, i -> val text = sharePreference.edit()
            text.putBoolean(PREFERENCE, i)
            text.apply()
            when(i){
                true -> alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_REPEATING)
                false -> alarmReceiver.cancelAlarm(this)

            }
        }

        button.setOnClickListener{
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
    }
}