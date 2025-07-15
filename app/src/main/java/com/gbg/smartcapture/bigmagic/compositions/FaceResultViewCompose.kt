package com.gbg.smartcapture.bigmagic.compositions

import android.content.res.Configuration
import android.graphics.Bitmap
import android.util.Base64.NO_WRAP
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gbg.smartcapture.bigmagic.R
import com.gbg.smartcapture.facecamera.models.FaceCameraResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


@Composable
fun FaceResultView(
    faceCameraResult: FaceCameraResult
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val blobString = remember { mutableStateOf("") }
        val faceString = remember { mutableStateOf("") }
        LaunchedEffect(faceCameraResult) {
            blobString.value = ""
            faceString.value = ""
            launch(Dispatchers.Default) {
                blobString.value = android.util.Base64.encodeToString(faceCameraResult.encryptedBlob, NO_WRAP)
                val byteArrayOutputStream = ByteArrayOutputStream()
                faceCameraResult.previewPhoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                faceString.value = android.util.Base64.encodeToString(byteArray, NO_WRAP)
            }
        }
        if (blobString.value == "" || faceString.value == "") {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(vertical = 30.dp),
                color = MaterialTheme.colorScheme.onSurface,
            )
        } else {
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Row (
                    modifier = Modifier
                        .fillMaxSize(),
                ) {

                    Image(
                        modifier = Modifier
                            .height(400.dp)
                            .weight(1f),
                        bitmap = faceCameraResult.previewPhoto.asImageBitmap(),
                        contentDescription = stringResource(R.string.captured_face_image_alt_text),
                        contentScale = ContentScale.FillHeight
                    )
                    Column (
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CopyButtonView(
                            buttonText = stringResource(R.string.copy_encrypted_blob),
                            string = blobString.value
                        )
                        CopyButtonView(
                            buttonText = stringResource(R.string.copy_face_blob),
                            string = faceString.value
                        )
                    }
                }
            } else {
                Image(
                    modifier = Modifier
                        .height(400.dp)
                        .fillMaxWidth(),
                    bitmap = faceCameraResult.previewPhoto.asImageBitmap(),
                    contentDescription = stringResource(R.string.captured_face_image_alt_text),
                    contentScale = ContentScale.FillHeight
                )
                CopyButtonView(
                    buttonText = stringResource(R.string.copy_encrypted_blob),
                    string = blobString.value
                )
                CopyButtonView(
                    buttonText = stringResource(R.string.copy_face_blob),
                    string = faceString.value
                )
            }
        }
    }
}
