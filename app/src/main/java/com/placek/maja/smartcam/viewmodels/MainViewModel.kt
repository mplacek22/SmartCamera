package com.placek.maja.smartcam.viewmodels

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.placek.maja.smartcam.LabelRecognizer
import com.placek.maja.smartcam.LabelRecognizerCallback
import com.placek.maja.smartcam.R
import com.placek.maja.smartcam.TextRecognitionCallback
import com.placek.maja.smartcam.TextRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException
import kotlin.random.Random

class MainViewModel(private val context: Context): ViewModel(){
    // Create a list of drawable resource IDs to switch between
    private val drawableResIds = listOf(R.drawable.img_1, R.drawable.img_2, R.drawable.img_3, R.drawable.img_4)
    private var currentIndex = 0

    // StateFlow to hold the current image resource ID
    private val _currentImageResId = MutableStateFlow(drawableResIds[currentIndex])
    val currentImageResId = _currentImageResId.asStateFlow()

    fun switchToNextImage() {
        currentIndex = Random.nextInt(drawableResIds.size)
        _currentImageResId.value = drawableResIds[currentIndex]
        // Reset the recognized text when we switch images
        _recognizedText.value = null
        _recognizedLabels.value = emptyMap()
    }

    private val textRecognizer = TextRecognizer()
    private val _recognizedText = MutableStateFlow<String?>(null)
    val recognizedText = _recognizedText.asStateFlow()

    private val labelRecognizer = LabelRecognizer()
    private val _recognizedLabels = MutableStateFlow<Map<String, Float>>(emptyMap())
    val recognizedLabels = _recognizedLabels.asStateFlow()

    private fun currentImageFromResource(): InputImage? {
        return try {
            val drawable = ContextCompat.getDrawable(context, drawableResIds[currentIndex])
            val bitmap = (drawable as BitmapDrawable).bitmap
            InputImage.fromBitmap(bitmap, 0)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun detectText() {
        val img = currentImageFromResource()
        if (img == null) {
            _recognizedText.value = "Could not create InputImage from resource."
            return
        }
        textRecognizer.recognizeText(img, object :
            TextRecognitionCallback {
            override fun onTextRecognized(text: String) {
                _recognizedText.value = text
            }

            override fun onError(e: Exception) {
                e.printStackTrace()
            }
        })
    }

    private fun detectLabels() {
        currentImageFromResource()?.let {
            labelRecognizer.processImage(it, object :
                LabelRecognizerCallback {
                override fun onLabelsRecognized(labels: Map<String, Float>) {
                    _recognizedLabels.value = labels
                }

                override fun onError(e: Exception) {
                    e.printStackTrace()
                }
            })
        }
    }

    fun detect(){
        detectText()
        detectLabels()
    }
}