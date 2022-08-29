package tv.fipe.f.repositories

import androidx.lifecycle.LiveData
import tv.fipe.f.db.UrlEntity

interface MyRepositoryInt {

    suspend fun insertUrl(urlEntity: UrlEntity)

    fun getUrl(): LiveData<UrlEntity>
}