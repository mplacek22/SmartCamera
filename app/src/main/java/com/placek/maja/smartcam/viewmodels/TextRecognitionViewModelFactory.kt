package com.placek.maja.smartcam.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.placek.maja.smartcam.TextRecognizer

class TextRecognitionViewModelFactory(private val textRecognizer: TextRecognizer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TextRecognitionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TextRecognitionViewModel(textRecognizer) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}