package com.example.meditrack.di

import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.meditrack.AlarmService
import com.example.meditrack.App
import com.example.meditrack.MainActivity
import com.example.meditrack.R
import com.example.meditrack.feature.data.LocalUserMangerImpl
import com.example.meditrack.feature.data.alarm.MedicineAlarmScheduler
import com.example.meditrack.feature.data.database.local.AppDatabase
import com.example.meditrack.feature.data.repository.RepositoryImpl
import com.example.meditrack.feature.domain.LocalUserManger
import com.example.meditrack.feature.domain.alarm.AlarmScheduler
import com.example.meditrack.feature.domain.repository.Repository
import com.example.meditrack.feature.domain.usecases.app_entry.AppEntryUseCases
import com.example.meditrack.feature.domain.usecases.app_entry.ReadAppEntry
import com.example.meditrack.feature.domain.usecases.app_entry.SaveAppEntry
import com.example.meditrack.feature.domain.usecases.meditrack.AddMedicine
import com.example.meditrack.feature.domain.usecases.meditrack.AddMedicines
import com.example.meditrack.feature.domain.usecases.meditrack.DeleteMedicine
import com.example.meditrack.feature.domain.usecases.meditrack.DeleteMedicines
import com.example.meditrack.feature.domain.usecases.meditrack.GetMedicine
import com.example.meditrack.feature.domain.usecases.meditrack.GetMedicineId
import com.example.meditrack.feature.domain.usecases.meditrack.GetMedicines
import com.example.meditrack.feature.domain.usecases.meditrack.MedicineUseCases
import com.example.meditrack.feature.domain.usecases.meditrack.ScheduleReminder
import com.example.meditrack.feature.domain.usecases.meditrack.ValidateDate
import com.example.meditrack.feature.domain.usecases.meditrack.ValidateTime
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalUserManager(
        dataStore: DataStore<Preferences>,
    ): LocalUserManger {
        return LocalUserMangerImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideAppEntryUseCases(
        localUserManger: LocalUserManger,
    ): AppEntryUseCases = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManger), saveAppEntry = SaveAppEntry(localUserManger)
    )

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Singleton
    @Provides
    fun provideNotification(@ApplicationContext context: Context): NotificationCompat.Builder {
        val i = Intent(context, MainActivity::class.java)
        // إنشاء أزرار الإشعار
        val missedIntent = Intent(context, AlarmService::class.java).apply {
            action = "ACTION_MISSED"
        }
        val missedPendingIntent = PendingIntent.getService(
            context, 1, missedIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val snoozeIntent = Intent(context, AlarmService::class.java).apply {
            action = "ACTION_SNOOZE"
        }
        val snoozePendingIntent = PendingIntent.getService(
            context, 2, snoozeIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val takenIntent = Intent(context, AlarmService::class.java).apply {
            action = "ACTION_TAKEN"
        }
        val takenPendingIntent = PendingIntent.getService(
            context, 3, takenIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Builder(context, App.CHANNEL_ID).setSmallIcon(R.drawable.time)
            .setOnlyAlertOnce(true).setAutoCancel(false).setOngoing(true)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.time, "فاتني", missedPendingIntent)
            .addAction(R.drawable.time, "تاجيل", snoozePendingIntent)
            .addAction(R.drawable.time, "تم الأخذ", takenPendingIntent)
    }

    @Singleton
    @Provides
    fun provideDatabase(app: Application): AppDatabase = Room.databaseBuilder(
        app, AppDatabase::class.java, AppDatabase.DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideRepository(database: AppDatabase): Repository {
        return RepositoryImpl(database.medicineDao)
    }

    @Singleton
    @Provides
    fun provideTaskUseCase(
        repository: Repository,
        alarmScheduler: AlarmScheduler,
        @ApplicationContext context: Context,
    ): MedicineUseCases {
        return MedicineUseCases(
            addMedicine = AddMedicine(repository, context),
            addMedicines = AddMedicines(repository),
            getMedicine = GetMedicine(repository),
            getMedicines = GetMedicines(repository),
            deleteMedicine = DeleteMedicine(repository),
            deleteMedicines = DeleteMedicines(repository),
            validateTime = ValidateTime(context),
            validateDate = ValidateDate(context),
            getMedicineId = GetMedicineId(repository),
            scheduleReminder = ScheduleReminder(alarmScheduler)
        )
    }

    @Singleton
    @Provides
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmScheduler {
        return MedicineAlarmScheduler(context)
    }

}