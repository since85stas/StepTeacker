package stas.batura.stepteacker.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import stas.batura.stepteacker.data.IRep
import stas.batura.stepteacker.data.Repository

@Module
@InstallIn(ServiceComponent::class)
abstract class ServiceModule {

    @Binds
    abstract fun bindReposserv(repository: Repository): IRep

}