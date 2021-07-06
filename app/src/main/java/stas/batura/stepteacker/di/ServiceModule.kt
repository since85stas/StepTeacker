package stas.batura.stepteacker.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import stas.batura.stepteacker.data.Repository
import stas.batura.stepteacker.data.RepositoryImpl

@Module
@InstallIn(ServiceComponent::class)
abstract class ServiceModule {

    @Binds
    abstract fun bindReposserv(repositoryImpl: RepositoryImpl): Repository

}