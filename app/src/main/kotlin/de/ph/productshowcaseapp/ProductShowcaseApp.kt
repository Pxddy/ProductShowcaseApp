package de.ph.productshowcaseapp

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ProductShowcaseApp : Application(), SingletonImageLoader.Factory {

    @Inject
    lateinit var imageLoader: dagger.Lazy<ImageLoader>

    override fun newImageLoader(context: PlatformContext): ImageLoader = imageLoader.get()
}
