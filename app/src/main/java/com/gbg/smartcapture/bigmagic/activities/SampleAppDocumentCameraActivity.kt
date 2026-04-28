package com.gbg.smartcapture.bigmagic.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.gbg.smartcapture.bigmagic.compositions.DocumentCameraResultView
import com.gbg.smartcapture.commons.compositions.components.PrimaryButton
import com.gbg.smartcapture.commons.compositions.components.SecondaryButton
import com.gbg.smartcapture.commons.theme.SmartCaptureUiTheme
import com.gbg.smartcapture.documentcamera.AutoCaptureToggleConfig
import com.gbg.smartcapture.documentcamera.DocumentCameraActivity
import com.gbg.smartcapture.documentcamera.DocumentCameraView
import com.gbg.smartcapture.documentcamera.DocumentCameraViewModel
import com.gbg.smartcapture.documentcamera.DocumentProcessingState
import com.gbg.smartcapture.documentcamera.DocumentScannerConfig
import com.gbg.smartcapture.documentcamera.DocumentSide
import com.gbg.smartcapture.documentcamera.DocumentType
import com.gbgplc.idscan.stickman.SmartCapture
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//todo I think it might make sense to break out the face, ozone, and mjcs into similar sample
// activities, our root activity is too crowded and difficult to parse making it not the best as
// sample code. This will also allow for more buttons for testing (Launch via activity, launch via
// compose, via compose taking up 60% of the screen, etc) without cluttering the root screen with
// dozens of buttons. Not urgent, can be done when we have time for tech debt.
/**
 * This activity demonstrates how to reactively utilize the document camera by interacting with its
 * viewmodel. It also offers a basic example of how to launch it via activity (backwards
 * compatibility option).
 */
class SampleAppDocumentCameraActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.Companion.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.Companion.dark(Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)

        SmartCapture.init(application)

        val viewModel: DocumentCameraViewModel by viewModels<DocumentCameraViewModel>()

        viewModel.setDocumentScannerConfig(
            DocumentScannerConfig(
                autoCaptureToggleConfig = AutoCaptureToggleConfig.ShowDelayed(7000),
                documentSide = DocumentSide.BACK,
                documentType = DocumentType.PASSPORT,
            )
        )

        setContent {
            SmartCaptureUiTheme {
                var buttonEnabled by remember { mutableStateOf(true) }
                val isCameraOpen by viewModel.isCameraOpen.collectAsStateWithLifecycle()
                val bytes by viewModel.capturedBytes.collectAsStateWithLifecycle()
                val preview by viewModel.previewBitmap.collectAsStateWithLifecycle()
                val error by viewModel.error.collectAsStateWithLifecycle()

                val context = LocalContext.current
                val lifecycleOwner = LocalLifecycleOwner.current

                LaunchedEffect(bytes) {
                    if (bytes != null)
                        viewModel.closeCamera()
                }

                LaunchedEffect(error) {
                    if (error != null) {
                        viewModel.closeCamera()
                    }
                }

                BackHandler(bytes != null || error != null) {
                    viewModel.clearPreviousCapture()
                }

                Box(
                    modifier = Modifier.Companion
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RectangleShape
                        )
                        .safeContentPadding()
                        .padding(16.dp),
                ) {
                    if (isCameraOpen) {
                        DocumentCameraView(
                            modifier = Modifier.Companion
                                .fillMaxSize(),
                            viewModel = viewModel
                        )
                    } else if (error != null) {
                        Column(
                            modifier = Modifier.Companion
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Companion.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier.Companion.padding(8.dp),
                                text = "Runtime Error",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = error?.errorCode?.name ?: "",
                                modifier = Modifier.Companion.padding(4.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = error?.message ?: "",
                                modifier = Modifier.Companion.padding(4.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Press back to dismiss...",
                                modifier = Modifier.Companion.padding(16.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    } else if (bytes != null) {
                        bytes?.let {
                            DocumentCameraResultView(
                                it,
                                preview,
                                viewModel.documentProcessingMetadata
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier.Companion
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Companion.CenterHorizontally
                        ) {
                            if (buttonEnabled) {
                                PrimaryButton(
                                    text = "Start Camera"
                                ) {
                                    lifecycleScope.launch {
                                        buttonEnabled = false
                                        viewModel.openCamera(context, lifecycleOwner)
                                        delay(1500)
                                        buttonEnabled = true
                                    }
                                }
                                SecondaryButton(
                                    text = "Start Camera via Activity (backwards compat option)"
                                ) {
                                    lifecycleScope.launch {
                                        buttonEnabled = false
                                        openActivityCamera(context)
                                        delay(1500)
                                        buttonEnabled = true
                                    }
                                }
                            } else {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun openActivityCamera(context: Context) {
        val intent = DocumentCameraActivity.Companion.getIntent(context)
        activityResultLauncher.launch(intent)
    }

    private val activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                val result = DocumentCameraActivity.Companion.latestResult
                val metadata = DocumentCameraActivity.Companion.latestMetadata
                if (result != null) {
                    if (result is DocumentProcessingState.Success) {
                        //use bytes and preview as needed
                        if (metadata != null) {
                            Toast.makeText(this, "Success and has metadata", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(this, "Success with no metadata", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else if (result is DocumentProcessingState.Failure) {
                        Toast.makeText(this, "Error: " + result.message, Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Error: Result was null, this should not happen", Toast.LENGTH_LONG).show()
                }
            }
            RESULT_CANCELED -> {
                Toast.makeText(this, "User cancelled", Toast.LENGTH_SHORT).show()

            }
        }
    }
}