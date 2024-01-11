package com.placek.maja.smartcam

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.placek.maja.smartcam.ui.theme.SmartCamTheme
import com.placek.maja.smartcam.viewmodels.TextRecognitionViewModel
import com.placek.maja.smartcam.viewmodels.TextRecognitionViewModelFactory

class MainActivity : ComponentActivity(){
    private val textRecognizer = TextRecognizer()

    // Create the ViewModel using the factory
    private val textRecognitionViewModel: TextRecognitionViewModel by viewModels {
        TextRecognitionViewModelFactory(textRecognizer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartCamTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainActivityView(textRecognitionViewModel, this)
                }
            }
        }
    }
}

@Composable
fun MainActivityView(textRecognitionViewModel: TextRecognitionViewModel, context: Context) {
    val currentImageResId by textRecognitionViewModel.currentImageResId.collectAsState()
    val recognizedText by textRecognitionViewModel.recognizedText.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Image(
                painter = painterResource(id = currentImageResId),
                contentDescription = null, // TODO: Provide a meaningful description
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Fit
            )
            Button(
                onClick = {
                    textRecognitionViewModel.switchToNextImage()
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
            ) {
                Text(text = "Switch Image", style = MaterialTheme.typography.headlineMedium)
            }

            // Button to detect text
            Button(
                onClick = {
                    textRecognitionViewModel.detectText(context)
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
            ) {
                Text(text = "Detect Text", style = MaterialTheme.typography.headlineMedium)
            }

            // Display the recognized text
            Text(
                text = recognizedText ?: "Recognized text will appear here",
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}