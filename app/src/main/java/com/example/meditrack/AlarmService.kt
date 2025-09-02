package com.example.meditrack

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.meditrack.feature.domain.model.Medicine
import com.example.meditrack.feature.domain.repository.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.Duration
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class AlarmService : Service() {
    private var player: MediaPlayer? = null

    @Inject
    lateinit var notify: NotificationCompat.Builder

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    @Inject
    lateinit var repository: Repository

    var countDownTimer: CountDownTimer? = null

    override fun onBind(p0: Intent?): IBinder? {
        return AlarmBinder()
    }


    var zero = ""

    var timeValue = mutableStateOf("$zero$zero:$zero$zero:$zero$zero")
        private set

    var medicineId = mutableIntStateOf(0)
        private set

    override fun onCreate() {
        super.onCreate()
        zero = String.format(getString(R.string.numberRes), 0)
        timeValue.value = "$zero$zero:$zero$zero:$zero$zero"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val medicine = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("medicine", Medicine::class.java) ?: Medicine()
        } else {
            intent?.getParcelableExtra<Medicine>("medicine") ?: Medicine()
        }
        startForeground(1, notify.build())
        Log.e(
            "Yz", "medicine in service$medicine"
        )

        medicineId.intValue = medicine.id

        when (intent?.action) {
            "ACTION_MISSED" -> {
                val idFromDataStore = runBlocking {
                    dataStore.data.first()[intPreferencesKey("medicineId")] ?: 0
                }
                medicineId.intValue = idFromDataStore

                Log.e("AlarmService", "ACTION_TAKEN medicineId: ${medicineId.intValue}")

                countDownTimer?.cancel()
                countDownTimer = null
                player?.stop()
                player?.release()
                player = null

                CoroutineScope(Dispatchers.IO).launch {
                    repository.markMedicineMISSED(medicineId.intValue)
                    App.manager.cancel(medicineId.intValue)
                }
                notify.clearActions()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }

            "ACTION_SNOOZE" -> {
                Log.e("AlarmService", "تأجيل ضغط")
                countDownTimer?.cancel()
                countDownTimer = null
            }


            "ACTION_TAKEN" -> {
                val idFromDataStore = runBlocking {
                    dataStore.data.first()[intPreferencesKey("medicineId")] ?: 0
                }
                medicineId.intValue = idFromDataStore

                Log.e("AlarmService", "ACTION_TAKEN medicineId: ${medicineId.intValue}")

                countDownTimer?.cancel()
                countDownTimer = null
                player?.stop()
                player?.release()
                player = null

                CoroutineScope(Dispatchers.IO).launch {
                    repository.markMedicineCompleted(medicineId.intValue)
                    App.manager.cancel(medicineId.intValue)
                }
                notify.clearActions()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }


//            "stop_reminder" -> {
//                player?.stop()
//                player?.release()
//                player = null
//                CoroutineScope(Dispatchers.IO).launch {
//                    dataStore.data.first().also { preferences ->
//                        medicineId.intValue = preferences[intPreferencesKey("medicineId")] ?: 0
//                    }
//                    withContext(Dispatchers.Main) {
//                        removeStopBtn()
//                    }
//                }
//            }

            "stop_service" -> {
                player?.stop()
                player?.release()
                player = null
                countDownTimer?.cancel()
                countDownTimer = null
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }

            "start_service" -> start(true, medicine) { value ->
                notify.setContentTitle(value)
                    .setContentText("\"${medicine.name}\" " + getString(R.string.notification_msg))
                App.manager.notify(medicineId.intValue, notify.build())
            }

            "restart_service" -> {
                notify.clearActions()
                start(false, medicine) { value ->
                    notify.setContentTitle(value)
                        .setContentText("\"${medicine.name}\" " + getString(R.string.notification_msg))
                    App.manager.notify(medicineId.intValue, notify.build())
                }
            }
        }
        return START_REDELIVER_INTENT
    }


    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
        player?.release()
        player = null

        countDownTimer?.cancel()
        countDownTimer = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun start(isPlay: Boolean, medicine: Medicine, onTick: (String) -> Unit) {
        if (isPlay) {
            player = MediaPlayer.create(this, R.raw.ots_l).apply {
                isLooping = true
                start()
            }
//            Log.e("Yz", "inside isplay block")
//            val intentForActionBtn = Intent(this, AlarmService::class.java).apply {
//                action = "stop_reminder"
//            }
//            val pendingIntentForAction = PendingIntent.getForegroundService(
//                this,
//                1,
//                intentForActionBtn,
//                PendingIntent.FLAG_IMMUTABLE
//            )
            // --- أزرار الإشعار ---
            val missedIntent =
                Intent(this, AlarmService::class.java).apply { action = "ACTION_MISSED" }
            val snoozeIntent =
                Intent(this, AlarmService::class.java).apply { action = "ACTION_SNOOZE" }
            val takenIntent =
                Intent(this, AlarmService::class.java).apply { action = "ACTION_TAKEN" }

            val missedPending =
                PendingIntent.getService(this, 1, missedIntent, PendingIntent.FLAG_IMMUTABLE)
            val snoozePending =
                PendingIntent.getService(this, 2, snoozeIntent, PendingIntent.FLAG_IMMUTABLE)
            val takenPending =
                PendingIntent.getService(this, 3, takenIntent, PendingIntent.FLAG_IMMUTABLE)

            notify.addAction(R.drawable.ic_home, "فاتني", missedPending)
                .addAction(R.drawable.ic_home, "تأجيل", snoozePending)
                .addAction(R.drawable.ic_home, "تم الأخذ", takenPending)
        }


        var isFirstTime = true
        val period = Duration.between(medicine.startTime, medicine.startTime.plusMinutes(10))
        val timeInMillis = period.seconds * 1000
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                preferences[intPreferencesKey("medicineId")] = medicine.id
            }
        }

        countDownTimer = object : CountDownTimer(timeInMillis, 1000L) {
            override fun onTick(millis: Long) {

                val time = String.format(
                    Locale.getDefault(), "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) % 60,
                    TimeUnit.MILLISECONDS.toSeconds(millis) % 60
                )
                if (isFirstTime) {
                    startForeground(medicine.id, notify.build())
                    isFirstTime = false
                }
                timeValue.value = time
                onTick(time)

            }

            override fun onFinish() {
                Log.e("ks", "inside onFinish")
                //here make same scope
                CoroutineScope(Dispatchers.IO).launch {
                    repository.insertMedicine(
                        medicine.copy(
                            isTimerFinished = true,
                            isMissed = true
                        )
                    )
                    withContext(Dispatchers.Main) {
                        player!!.stop()
                        stopForeground(STOP_FOREGROUND_REMOVE)
                        stopSelf()
                    }
                }
            }

        }.start()

    }


    inner class AlarmBinder : Binder() {
        fun getService(): AlarmService = this@AlarmService
    }

    private fun removeStopBtn() {
        notify.clearActions()
        startForeground(medicineId.intValue, notify.build())
    }

    private fun getFormattedTime(i: Long): String {
        return if (i > 60) {
            val hour = i / 60
            val minutes = i % 60
            if (minutes >= 10) "$hour:$minutes" else "$hour:0$minutes"
        } else {
            i.toString()
        }
    }
}