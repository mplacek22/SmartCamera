package com.placek.maja.smartcam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.placek.maja.smartcam.ui.theme.SmartCamTheme
import com.placek.maja.smartcam.viewmodels.MainViewModel
import com.placek.maja.smartcam.viewmodels.MainViewModelFactory

class MainActivity : ComponentActivity(){
    // Create the ViewModel using the factory
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory(applicationContext)
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
                    MainActivityView(mainViewModel)
                }
            }
        }
    }
}

@Composable
fun MainActivityView(mainViewModel: MainViewModel) {
    val currentImageResId by mainViewModel.currentImageResId.collectAsState()
    val recognizedText by mainViewModel.recognizedText.collectAsState()
    val recognizedLabels by mainViewModel.recognizedLabels.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn {
            item {
                Image(
                    painter = painterResource(id = currentImageResId),
                    contentDescription = "Displayed Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.image_height))
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Fit
                )
            }
            item { Spacer(Modifier.height(dimensionResource(id = R.dimen.padding_large))) }
            item {
                Button(
                    onClick = {
                                mainViewModel.switchToNextImage()
                                mainViewModel.detect()
                              },
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.padding_large))
                        .fillMaxWidth(),
                ) {
                    Text(text = "Switch Image", style = MaterialTheme.typography.headlineMedium)
                }
            }
            item { Spacer(Modifier.height(dimensionResource(id = R.dimen.padding_large))) }
            item {
                Text(
                    text = "Recognized Text:",
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
                    style = MaterialTheme.typography.headlineMedium
                )
                recognizedText?.let {
                    Text(
                        text = it.ifEmpty { "No text detected" },
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.padding_large))
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (recognizedText.isNullOrEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            item {
                Text(
                    text = "Recognized Labels:",
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
                    style = MaterialTheme.typography.headlineMedium
                )
                LabelsTable(recognizedLabels)
            }
        }
    }
}

@Composable
fun LabelsTable(labels: Map<String, Float>) {
    Column(
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding_large))
            .background(MaterialTheme.colorScheme.surface)
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.padding_medium)))
    ) {
        if (labels.isEmpty()) {
            Text(
                text = "No labels detected",
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        } else {
            TableHeader()
            Divider()
            labels.forEach { (label, confidence) ->
                TableRow(label, confidence)
            }
        }
    }
}

@Composable
fun TableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        Text(
            text = "Label",
            modifier = Modifier.weight(2f),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Confidence",
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
fun TableRow(label: String, confidence: Float) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = dimensionResource(id = R.dimen.padding_small),
                horizontal = dimensionResource(id = R.dimen.padding_large)
            )
    ) {
        Text(text = label, modifier = Modifier.weight(2f))
        Text(text = String.format("%.2f%%", confidence * 100), modifier = Modifier.weight(1f))
    }
}
