package com.spylypchuk.testbootevent.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import com.spylypchuk.testbootevent.R
import com.spylypchuk.testbootevent.view.state.MainState
import com.spylypchuk.testbootevent.work.BootNotificationWorker
import kotlinx.coroutines.launch
import org.joda.time.format.DateTimeFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val text = findViewById<TextView>(R.id.textView)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }

        //TODO - add change period of worker

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is MainState.NoData -> {
                            text.text = "No data"
                        }

                        is MainState.HasData -> {
                            text.text = state.data
                                .map {
                                    DateTimeFormat.forPattern("dd.MM.YYYY").print(it.timestamp)
                                }
                                .groupBy {
                                    it
                                }
                                .map {
                                    "${it.key} - ${it.value.size}"
                                }
                                .joinToString("\n")
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("InlinedApi")
    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // DO NOTHING
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            startNotificationWorker()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startNotificationWorker()
        } else {
            Snackbar
                .make(
                    findViewById(R.id.main),
                    "Notification permission failed. This application require notification permission for a work correctly",
                    Snackbar.LENGTH_INDEFINITE
                )
                .setAction("OPEN SETTINGS") { openAppSettings() }
                .show()
            Toast.makeText(
                this,
                "Notification permission failed. This application require notification permission for a work correctly",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun startNotificationWorker() {
        // Schedule periodic worker to show notifications every 15 minutes
        val workRequest = PeriodicWorkRequest.Builder(
            BootNotificationWorker::class.java,
            15, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }

    @SuppressLint("InlinedApi")
    private fun showPermissionExplanation() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Notification Permission Needed")
        builder.setMessage("This app needs notification permission to alert you about boot events. Without it, you won't receive important notifications.")

        // Add OK and Cancel buttons
        builder.setPositiveButton("OK") { dialog, _ ->
            // User agrees, request the permission again
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            // User cancels, handle accordingly (you may disable the feature)
            dialog.dismiss()
        }

        builder.create().show()
    }

    fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }

}