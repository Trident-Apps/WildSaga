package tv.fipe.f.repositories

import androidx.lifecycle.LiveData
import tv.fipe.f.db.UrlEntity

interface MyRepositoryInt {

     fun insertUrl(urlEntity: UrlEntity)

    fun getUrl(): UrlEntity?
}