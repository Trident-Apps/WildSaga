package tv.fipe.f.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "url_table")
data class UrlEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var url: String? = null
)
