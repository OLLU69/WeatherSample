package ollu.dp.ua.weather_test;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * ----
 * Created by Лукащук Олег(master) on 18.03.18.
 */

public class BindingAdapters {
    @BindingAdapter("url")
    public static void imageUrlBinding(ImageView view, String url) {
        if (url == null) return;
        Uri uri = Uri.parse(url);
        Glide.with(view).load(uri).into(view);
    }
}
