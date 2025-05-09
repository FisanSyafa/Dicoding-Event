package com.example.mydicodingevents.ui.settings

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.mydicodingevents.R
import com.example.mydicodingevents.ui.scheduler.MyWorker
import com.example.mydicodingevents.ui.preferences.SettingPreferences
import com.example.mydicodingevents.ui.preferences.dataStore
import com.google.android.material.switchmaterial.SwitchMaterial
import java.util.concurrent.TimeUnit

class SettingsActivity : AppCompatActivity() {

    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"

        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_theme)
        val switchNotification: SwitchMaterial = findViewById(R.id.switch_worker)

        workManager = WorkManager.getInstance(this)

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        val pref = SettingPreferences.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, SettingsModelFactory(pref))[SettingsViewModel::class.java]
        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        mainViewModel.getNotificationSettings().observe(this){ isNotifActive: Boolean ->
            if(isNotifActive){
                startPeriodicTask()
                switchNotification.isChecked = true
            }else{
                stopPeriodicTask()
                switchNotification.isChecked = false
            }

        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSettings(isChecked)
        }

        switchNotification.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveNotificationSettings(isChecked)
        }
    }

    private fun stopPeriodicTask() {
        try{
            workManager.cancelUniqueWork("Event_Dicoding_work")
        }catch (e:Exception){
            Log.d("SettingsActivity","periodic work not initialized")

        }
    }

    private fun startPeriodicTask() {

        val data = Data.Builder().build()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        periodicWorkRequest = PeriodicWorkRequest.Builder(MyWorker::class.java, 1, TimeUnit.DAYS)
            .setInputData(data)
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniquePeriodicWork(
            "Event_Dicoding_work",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest)


        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id)
            .observe(this@SettingsActivity) { workInfo ->
                if (workInfo != null) {
                    val status = workInfo.state.name
                    Log.d("Status Worker", "WorkManager Status: $status")
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}