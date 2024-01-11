package com.placek.maja.smartcam

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException

interface TextRecognitionCallback {
    fun onTextRecognized(text: String)
    fun onError(e: Exception)
}

class TextRecognizer {
    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private fun imageFromResource(context: Context, resourceId: Int): InputImage? {
        return try {
            val drawable = ContextCompat.getDrawable(context, resourceId)
            val bitmap = (drawable as BitmapDrawable).bitmap
            InputImage.fromBitmap(bitmap, 0)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun recognizeText(context: Context, resourceId: Int, callback: TextRecognitionCallback) {
        val image = imageFromResource(context, resourceId) ?: run {
            callback.onError(IOException("Could not create InputImage from resource."))
            return
        }

        textRecognizer.process(image)
            .addOnSuccessListener { visionText ->
                // Task completed successfully
                val resultText = visionText.text
                callback.onTextRecognized(resultText)
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                callback.onError(e)
            }
    }
}
