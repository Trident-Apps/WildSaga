package tv.fipe.f.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [UrlEntity::class],
    version = 1
)
abstract class UrlDatabase : RoomDatabase() {

    abstract val dao: UrlDao

}
