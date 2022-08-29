package tv.fipe.f.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tv.fipe.f.repositories.MyRepository
import tv.fipe.f.repositories.MyRepositoryInt
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

interface RepositoryModule {
    @Binds
    @Singleton
    fun bindRepository(
        repository: MyRepository
    ): MyRepositoryInt
}