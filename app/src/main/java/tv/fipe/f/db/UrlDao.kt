package tv.fipe.f.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UrlDao {
    @Insert
     fun insertUrl(url: UrlEntity)

    @Query("SELECT * FROM url_table LIMIT 1")
    fun getUrl(): UrlEntity?
}