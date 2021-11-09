package stas.batura.stepteacker.di

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn (ApplicationComponent::class)
class DataModule {

    @Provides
    fun provideProtoData(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return appContext.createDataStore(
            name = "settings"
        )

    }

}