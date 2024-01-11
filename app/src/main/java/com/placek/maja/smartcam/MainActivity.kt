package com.placek.maja.smartcam

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.placek.maja.smartcam.ui.theme.SmartCamTheme
import java.io.IOException

class MainActivity : ComponentActivity(), TextRecognitionCallback{
    private val textRecognizer = TextRecognizer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_my)

        // Assuming you have a drawable resource named 'my_image'
        textRecognizer.recognizeText(this, R.drawable.img, this)
    }

    override fun onTextRecognized(text: String) {
        println(text)
        // Use the recognized text here
        runOnUiThread {
            // Update UI with recognized text
            // Example: textView.text = text
        }
    }

    override fun onError(e: Exception) {
        // Handle the error here
        e.printStackTrace()
    }

}

@Composable
fun MainActivityView() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image view
            Image(
                painter = painterResource(id = R.drawable.hello_world),
                contentDescription = null, // TODO: Provide a meaningful description
                modifier = Modifier
                    .size(200.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            // Text view
            Text(
                text = "Your Extracted Text",
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge
            )

            // Button to capture image
            Button(
                onClick = { /* TODO: Implement image capture */ },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
            ) {
                Text(text = "Snap", style = MaterialTheme.typography.headlineMedium)
            }

            // Button to detect text
            Button(
                onClick = { /* TODO: Implement text detection */ },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
            ) {
                Text(text = "Detect", style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}