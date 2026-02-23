package com.gbg.smartcapture.bigmagic.compositions

import android.content.ContentValues
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gbg.smartcapture.bigmagic.R
import com.gbg.smartcapture.commons.compositions.components.PrimaryButton
import com.gbg.smartcapture.commons.compositions.components.SecondaryButton
import com.gbg.smartcapture.facecamera.models.FaceCameraResult
import java.io.ByteArrayOutputStream
import java.io.IOException

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
                    SaveButtons(faceCameraResult)
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
            SaveButtons(faceCameraResult)
        }
    }
}

@Composable
private fun SaveButtons(faceCameraResult: FaceCameraResult) {
    val context = LocalContext.current

    PrimaryButton(
        stringResource(R.string.save_encrypted_blob)
    ) {
        saveFile(
            context,
            "${System.currentTimeMillis()}.raw",
            "application/octet-stream",
            faceCameraResult.encryptedBlob
        )
    }
    SecondaryButton(
        stringResource(R.string.save_preview_photo)
    ) {
        val stream = ByteArrayOutputStream()
        faceCameraResult.previewPhoto.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()

        saveFile(
            context,
            "${System.currentTimeMillis()}.jpg",
            "image/jpeg",
            byteArray
        )
    }
}

private fun saveFile(
    context: Context,
    filename: String,
    mimeType: String,
    bytes: ByteArray
) {
    try {
        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, filename)
            put(MediaStore.Downloads.MIME_TYPE, mimeType)
            put(MediaStore.Downloads.IS_PENDING, 1)
        }

        val resolver = context.contentResolver
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            Toast.makeText(
                context,
                "Sample app can only save files post android 10. Does not affect SDK",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val uri = resolver.insert(collection, values) ?: return

        resolver.openOutputStream(uri)?.use { output ->
            output.write(bytes)
        }

        values.clear()
        values.put(MediaStore.Downloads.IS_PENDING, 0)
        resolver.update(uri, values, null, null)

        Toast.makeText(
            context,
            "Saving $filename in downloads",
            Toast.LENGTH_SHORT
        ).show()
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(
            context,
            "Failed to save $filename",
            Toast.LENGTH_SHORT
        ).show()
    }
}