package ollu.dp.ua.weather

import android.databinding.BindingAdapter
import android.net.Uri
import android.widget.ImageView

import com.bumptech.glide.Glide

/**
 * ----
 * Created by Лукащук Олег(master) on 18.03.18.
 */

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("url")
    fun imageUrlBinding(view: ImageView, url: String?) {
        if (url == null) return
        val uri = Uri.parse(url)
        Glide.with(view).load(uri).into(view)
    }
}
