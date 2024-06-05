package ru.mirea.guseva.fitpet.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.firebase.firestore.IgnoreExtraProperties
import ru.mirea.guseva.fitpet.utils.Converters

@Entity(tableName = "articles")
@IgnoreExtraProperties
@TypeConverters(Converters::class)
data class Article(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String = "",
    val content: String = "",
    val imageUrl: String? = null,
    val isFavorite: Boolean = false,
    val tags: List<String> = emptyList(),
    val userId: String? = null // Для Firestore
) {
    constructor() : this(0, "", "", null, false, emptyList(), null)
}
