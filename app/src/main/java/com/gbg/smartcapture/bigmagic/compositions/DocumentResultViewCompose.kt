package com.gbg.smartcapture.bigmagic.compositions

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gbg.smartcapture.bigmagic.R
import com.gbg.smartcapture.commons.compositions.components.PrimaryButton
import com.gbg.smartcapture.documentcamera.DocumentProcessingMetadata
import com.gbg.smartcapture.documentcamera.DocumentProcessingState
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun DocumentCameraResultView(
    result: DocumentProcessingState.Result,
    metadata: DocumentProcessingMetadata?,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        when (result) {
            is DocumentProcessingState.Failure -> {
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.Center),
                    text = "$result"
                )
            }

            is DocumentProcessingState.Success ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val context = WeakReference(LocalContext.current)
                    PrimaryButton(text = stringResource(R.string.sample_app_download)) {
                        context.get()?.let {
                            saveImageToGallery(it, result.image.image)
                        } ?: run {
                            throw IllegalStateException("Context was null at a time when it can never be null. This should never get thrown.")
                        }
                    }
                    ResultEntryView(stringResource(R.string.sample_app_label_good), result.image.isGood)
                    ResultEntryView(stringResource(R.string.sample_app_label_sharp), result.image.isSharp)
                    ResultEntryView(stringResource(R.string.sample_app_label_glare_free), result.image.isGlareFree)
                    ResultEntryView(stringResource(R.string.sample_app_label_adequate_resolution), result.image.isAdequateDpi)
                    HorizontalDivider()
                    ResultEntryView(stringResource(R.string.sample_app_label_width), result.image.width)
                    ResultEntryView(stringResource(R.string.sample_app_label_height), result.image.height)
                    ResultEntryView(stringResource(R.string.sample_app_label_rotation), result.image.rotation)
                    metadata?.let {
                        HorizontalDivider()
                        ResultEntryView(stringResource(R.string.sample_app_label_blurry_frame_count), it.blurryFrameCount)
                        ResultEntryView(stringResource(R.string.sample_app_label_glare_frame_count), it.glareFrameCount)
                        ResultEntryView(stringResource(R.string.sample_app_label_low_rez_frame_count), it.lowResFrameCount)
                        ResultEntryView(stringResource(R.string.sample_app_label_out_of_bounds_frame_count), it.documentBoundaryFrameCount)
                        ResultEntryView(stringResource(R.string.sample_app_label_total_frame_count), it.processedFrameCount)
                        ResultEntryView(stringResource(R.string.sample_app_label_has_disabled_autocapture), it.hasDisabledAutoCapture)
                        ResultEntryView(stringResource(R.string.sample_app_label_capture_duration_sec), it.captureDuration.inWholeSeconds)
                    }
                    HorizontalDivider()
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp),
                        bitmap = result.image.toBitmap(rotate = true).asImageBitmap(),
                        contentDescription = null,
                    )
                }
        }
    }
}

private fun saveImageToGallery(context: Context, image: ByteArray) {
    val contentValues = ContentValues().apply {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        val current = formatter.format(time)
        put(MediaStore.Images.Media.DISPLAY_NAME, "$current.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        put(MediaStore.Images.Media.IS_PENDING, 1)
    }

    val resolver = context.contentResolver
    val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    imageUri?.let { uri ->
        resolver.openOutputStream(uri)?.use { outputStream ->
            outputStream.write(image)
        } ?: run {
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
        }

        contentValues.clear()
        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
        resolver.update(uri, contentValues, null, null)
        Toast.makeText(context, "Saved image to pictures", Toast.LENGTH_SHORT).show()
    } ?: run {
        Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
    }
}
