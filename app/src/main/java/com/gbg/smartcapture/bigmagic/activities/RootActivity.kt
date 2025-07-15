package com.gbg.smartcapture.bigmagic.activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gbg.smartcapture.bigmagic.R
import com.gbg.smartcapture.bigmagic.compositions.AppSelectionView
import com.gbg.smartcapture.bigmagic.compositions.DocumentCameraResultView
import com.gbg.smartcapture.bigmagic.compositions.FaceResultView
import com.gbg.smartcapture.bigmagic.compositions.SettingsView
import com.gbg.smartcapture.bigmagic.compositions.VersionNumberView
import com.gbg.smartcapture.commons.SmartCaptureException
import com.gbg.smartcapture.commons.compositions.PreviewView
import com.gbg.smartcapture.commons.getInsetPadding
import com.gbg.smartcapture.commons.theme.SmartCaptureUiTheme
import com.gbg.smartcapture.documentcamera.DocumentCameraActivity
import com.gbg.smartcapture.documentcamera.DocumentProcessingMetadata
import com.gbg.smartcapture.documentcamera.DocumentProcessingState
import com.gbg.smartcapture.documentcamera.DocumentScannerConfig
import com.gbg.smartcapture.facecamera.FaceCameraActivity
import com.gbg.smartcapture.facecamera.models.FaceCameraResult

class RootActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private var destination = mutableStateOf ("")

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)

        setContent {
            SmartCaptureUiTheme {
                val dest = remember { destination }

                Box(
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
                ) {
                    RootScreen()
                }

                LaunchedEffect(dest.value) {
                    if (dest.value != "") {
                        navController.navigate(dest.value)
                        dest.value = ""
                    }
                }
            }
        }
    }

    @Composable
    private fun RootScreen() {
        navController = rememberNavController()

        val insetPadding = LocalLayoutDirection.current.getInsetPadding()
        Column (
            modifier = Modifier.fillMaxSize().padding(insetPadding)
        ) {
            Surface (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(9f, fill = true)
            ) {
                NavHost(
                    navController = navController,
                    modifier = Modifier.fillMaxSize(),
                    startDestination = NavigationItem.LandingScreen.route
                ) {
                    composable(NavigationItem.LandingScreen.route) {
                        AppSelectionView(
                            onFaceCamera = ::onFaceCamera,
                            onDocumentCamera = ::onDocumentCamera,
                            onSettings = ::onSettings,
                        )
                    }
                    composable(NavigationItem.SettingsScreen.route) {
                        SettingsView(
                        )
                    }
                    composable(NavigationItem.FaceResultScreen.route) {
                        faceResult?.let { faceResult ->
                            FaceResultView(faceResult)
                        } ?: run {
                            Log.e("Sample App", "Result was null")
                            navController.popBackStack()
                        }
                    }
                    composable(NavigationItem.DocumentCameraResultScreen.route) {
                        documentCameraResult?.let {
                            DocumentCameraResultView(
                                result = it,
                                metadata = documentCameraMetadata,
                                modifier = Modifier.fillMaxSize()
                            )
                        } ?: navController.popBackStack()
                    }
                }
            }
            Surface (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true)
            ){
                VersionNumberView()
            }
        }
    }

    private val documentCameraLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                documentCameraResult = DocumentCameraActivity.latestResult
                documentCameraMetadata = DocumentCameraActivity.latestMetadata
                destination.value = NavigationItem.DocumentCameraResultScreen.route
            }
            RESULT_CANCELED -> {
                // Cancelled
            }
            else -> {
                // Unexpected result
            }
        }
    }

    private fun onDocumentCamera() {
        val intent = DocumentCameraActivity.getIntent(this, DocumentScannerConfig())
        documentCameraLauncher.launch(intent)
    }

    private fun onFaceCamera() {
        faceResult = null
        val intent = Intent(this, FaceCameraActivity::class.java)
        faceCameraLauncher.launch(intent)
    }


    private var faceCameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                val res = FaceCameraActivity.getResult()
                res?.let {
                    faceResult = it
                    destination.value = NavigationItem.FaceResultScreen.route
                }
            }
            RESULT_CANCELED -> {
                Toast.makeText(this, getString(R.string.user_canceled), Toast.LENGTH_SHORT).show()
            }
            else -> {
                val error: SmartCaptureException =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        result.data?.getSerializableExtra(
                            FaceCameraActivity.ERROR_OBJECT,
                            SmartCaptureException::class.java
                        ) ?: SmartCaptureException(
                            SmartCaptureException.ErrorCode.Unknown,
                            "Unknown Error"
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        result.data?.getSerializableExtra(
                            FaceCameraActivity.ERROR_OBJECT
                        ) as SmartCaptureException? ?: SmartCaptureException(
                            SmartCaptureException.ErrorCode.Unknown,
                            "Unknown Error"
                        )
                    }
                error.printStackTrace()
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.error_title))
                    .setMessage(error.message)
                    .setIcon(com.gbg.smartcapture.facecamera.R.drawable.triangle)
                    .show()
            }
        }
    }

    private fun onSettings() {
        navController.navigate(NavigationItem.SettingsScreen.route)
    }

    private enum class Screen {
        LANDING,
        SETTINGS,
        FACE_RESULT,
        DOCUMENT_CAMERA_RESULT
    }

    private sealed class NavigationItem(val route: String) {
        data object LandingScreen : NavigationItem(Screen.LANDING.name)
        data object SettingsScreen : NavigationItem(Screen.SETTINGS.name)
        data object FaceResultScreen : NavigationItem(Screen.FACE_RESULT.name)
        data object DocumentCameraResultScreen : NavigationItem(Screen.DOCUMENT_CAMERA_RESULT.name)
    }

    companion object {
        private var faceResult: FaceCameraResult? = null
        private var documentCameraResult: DocumentProcessingState.Result? = null
        private var documentCameraMetadata: DocumentProcessingMetadata? = null
    }

    @Preview(showBackground = true)
    @Composable
    private fun Preview() {
        PreviewView {
            RootScreen()
        }
    }
}
