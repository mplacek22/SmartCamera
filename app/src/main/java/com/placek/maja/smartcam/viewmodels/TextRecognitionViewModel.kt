package com.placek.maja.smartcam.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.placek.maja.smartcam.R
import com.placek.maja.smartcam.TextRecognitionCallback
import com.placek.maja.smartcam.TextRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TextRecognitionViewModel(private val textRecognizer: TextRecognizer) : ViewModel() {

    // Create a list of drawable resource IDs to switch between
    private val drawableResIds = listOf(R.drawable.img_1, R.drawable.img_2, R.drawable.img_3)
    private var currentIndex = 0

    // StateFlow to hold the current image resource ID
    private val _currentImageResId = MutableStateFlow(drawableResIds[currentIndex])
    val currentImageResId = _currentImageResId.asStateFlow()

    // StateFlow to hold the recognized text
    private val _recognizedText = MutableStateFlow<String?>(null)
    val recognizedText = _recognizedText.asStateFlow()


    fun detectText(context: Context) {
        textRecognizer.recognizeText(context, drawableResIds[currentIndex], object : TextRecognitionCallback {
            override fun onTextRecognized(text: String) {
                _recognizedText.value = text
            }

            override fun onError(e: Exception) {
                e.printStackTrace()
            }
        })
    }

    fun switchToNextImage() {
        currentIndex = (currentIndex + 1) % drawableResIds.size
        _currentImageResId.value = drawableResIds[currentIndex]
        // Reset the recognized text when we switch images
        _recognizedText.value = null
    }
}
