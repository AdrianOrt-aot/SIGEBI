package com.example.sigebi.Adaptador

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.sigebi.R

import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

// Carga la portada del libro (URL_Imagen de la base de datos) con Glide.
// Si el libro no tiene portada o la imagen no carga, se muestra el icono de libros.
fun ImageView.cargarPortada(url: String?) {
    if (url.isNullOrBlank()) {
        Glide.with(this).clear(this)
        setImageResource(R.drawable.ic_libros)
        scaleType = ImageView.ScaleType.CENTER_INSIDE
        return
    }

    scaleType = ImageView.ScaleType.CENTER_CROP

    Glide.with(this)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .placeholder(R.drawable.ic_libros)
        .error(R.drawable.ic_libros)
        .centerCrop()
        .listener(object : RequestListener<Drawable> {

            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                Log.e("GLIDE_ERROR", "URL: $url", e)
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                Log.d("GLIDE_OK", "Imagen cargada: $url")
                return false
            }
        })
        .into(this)

}