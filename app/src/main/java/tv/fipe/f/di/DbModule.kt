package tv.fipe.f.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import tv.fipe.f.db.UrlDao
import tv.fipe.f.db.UrlDatabase
import tv.fipe.f.utils.Constants
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): UrlDatabase {
        return Room.databaseBuilder(context, UrlDatabase::class.java, Constants.DATABASE_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun provideDao(db: UrlDatabase): UrlDao {
        return db.dao
    }
}