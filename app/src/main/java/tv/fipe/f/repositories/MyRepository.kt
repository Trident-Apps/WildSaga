package tv.fipe.f.repositories

import androidx.lifecycle.LiveData
import tv.fipe.f.db.UrlDao
import tv.fipe.f.db.UrlEntity
import javax.inject.Inject

class MyRepository @Inject constructor(val dao: UrlDao) : MyRepositoryInt {

    @Inject
    lateinit var urlDao: UrlDao

    override  fun insertUrl(urlEntity: UrlEntity) = urlDao.insertUrl(urlEntity)

    override fun getUrl(): UrlEntity? = urlDao.getUrl()
}