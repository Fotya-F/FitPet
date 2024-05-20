package ru.mirea.guseva.fitpet.data.ui.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.mirea.guseva.fitpet.R
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url: String?) {
    url?.let {
        Glide.with(this.context)
            .load(it)
            .apply(RequestOptions()
                .placeholder(R.drawable.ic_pet_placeholder)
                .error(R.drawable.ic_pet_placeholder))
            .into(this)
    }
}

@BindingAdapter("imageResource")
fun ImageView.setImageResource(resource: Int) {
    this.setImageResource(resource)
}

fun String.formatDate(): String {
    return try {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val targetFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = originalFormat.parse(this)
        targetFormat.format(date ?: Date())
    } catch (e: Exception) {
        this
    }
}

fun String.formatTime(): String {
    return try {
        val originalFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val targetFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val time = originalFormat.parse(this)
        targetFormat.format(time ?: Date())
    } catch (e: Exception) {
        this
    }
}
