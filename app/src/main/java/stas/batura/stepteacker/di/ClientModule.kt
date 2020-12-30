package stas.batura.stepteacker.di

import android.content.Context
import android.hardware.SensorManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ClientModule {


//    @Provides
//    @Singleton
//    fun provideSingAccount(@ApplicationContext appContext: Context): SensorManager {
//
//        return sensorManager
//    }

}