package com.udacity.asteroidradar


import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

/**
 * Sets the picture of the day and it's content description to the proper view
 */
@BindingAdapter("pictureOfTheDay")
fun bindPictureOfDay(imageView: ImageView, imgInfo: PictureOfDay?) {
    imgInfo?.let {
        if (imgInfo.mediaType == "image") {
            Picasso.get().load(imgInfo.url).placeholder(R.drawable.placeholder_picture_of_day)
                .error(R.drawable.placeholder_picture_of_day).into(imageView)
            val context = imageView.context
            imageView.contentDescription = context.getString(
                R.string.nasa_picture_of_day_content_description_format,
                imgInfo.title
            )
        } else {
            imageView.setImageResource(R.drawable.placeholder_picture_of_day)
        }
    }
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription = R.string.potentially_hazardous_asteroid_image.toString()
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription = R.string.not_hazardous_asteroid_image.toString()
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription = R.string.potentially_hazardous_asteroid_image.toString()
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = R.string.not_hazardous_asteroid_image.toString()
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}
