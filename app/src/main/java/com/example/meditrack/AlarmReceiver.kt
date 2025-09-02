package com.example.meditrack

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.meditrack.feature.domain.model.Medicine
import com.example.meditrack.feature.domain.repository.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: Repository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {

        val medicine = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("medicine", Medicine::class.java)!!.copy(
                isEnteredTime = true
            )
        } else {
            intent?.getParcelableExtra<Medicine>("medicine")!!.copy(
                isEnteredTime = true
            )
        }
        Log.e("Yz", "Enter receiver")

        CoroutineScope(Dispatchers.IO).launch {
            repository.insertMedicine(medicine)

        }
        Log.e("Yz", "the medicine inside receiver: $medicine")

        val i = Intent(context!!, AlarmService::class.java).apply {
            putExtra("medicine", medicine)
            action = "start_service"
        }

        context.startForegroundService(i)

    }
}