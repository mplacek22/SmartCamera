package com.placek.maja.smartcam

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

interface LabelRecognizerCallback {
    fun onLabelsRecognized(labels: Map<String, Float>)
    fun onError(e: Exception)
}
class LabelRecognizer {
    private val labeler: ImageLabeler = ImageLabeling.getClient(
        ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.7f)
            .build()
    )

    fun processImage(image: InputImage, callback: LabelRecognizerCallback) {
        labeler.process(image)
            .addOnSuccessListener { imageLabels ->
                // Task completed successfully
                val resultLabels = imageLabels.map { label ->
                    label.text to label.confidence

                }.toMap()
                callback.onLabelsRecognized(resultLabels)
            }
            .addOnFailureListener { e ->
                // Handle the error
                callback.onError(e)
            }
    }
}