package stas.batura.stepteacker.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import stas.batura.stepteacker.data.Repository
import stas.batura.stepteacker.data.RepositoryImpl

@Module
@InstallIn(ActivityComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepos(repositoryImpl: RepositoryImpl): Repository

}