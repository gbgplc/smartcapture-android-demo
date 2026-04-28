package com.gbg.smartcapture.bigmagic.compositions

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gbg.smartcapture.bigmagic.R
import com.gbg.smartcapture.bigmagic.compositions.bits.ResultEntryView
import com.gbg.smartcapture.commons.compositions.components.PrimaryButton
import com.gbg.smartcapture.commons.utils.gbgSaveImageToGallery
import com.gbg.smartcapture.documentcamera.DocumentProcessingMetadata
import java.lang.ref.WeakReference

@Composable
fun DocumentCameraResultView(
    resultBytes: ByteArray,
    previewBitmap: Bitmap?,
    metadata: DocumentProcessingMetadata?,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
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
                    gbgSaveImageToGallery(it, resultBytes)
                } ?: run {
                    throw IllegalStateException("Context was null at a time when it can never be null. This should never get thrown.")
                }
            }
            HorizontalDivider()
            metadata?.let {
                HorizontalDivider()
                ResultEntryView(
                    stringResource(R.string.sample_app_label_blurry_frame_count),
                    it.blurryFrameCount
                )
                ResultEntryView(
                    stringResource(R.string.sample_app_label_glare_frame_count),
                    it.glareFrameCount
                )
                ResultEntryView(
                    stringResource(R.string.sample_app_label_low_rez_frame_count),
                    it.lowResFrameCount
                )
                ResultEntryView(
                    stringResource(R.string.sample_app_label_high_rez_frame_count),
                    it.highResFrameCount
                )
                ResultEntryView(
                    stringResource(R.string.sample_app_label_not_aligned_frame_count),
                    it.notAlignedFrameCount
                )
                ResultEntryView(
                    stringResource(R.string.sample_app_label_not_parallel_frame_count),
                    it.notParallelFrameCount
                )
                ResultEntryView(
                    stringResource(R.string.sample_app_label_out_of_bounds_frame_count),
                    it.documentBoundaryFrameCount
                )
                ResultEntryView(
                    stringResource(R.string.sample_app_label_total_frame_count),
                    it.processedFrameCount
                )
                ResultEntryView(
                    stringResource(R.string.sample_app_label_has_disabled_autocapture),
                    it.hasDisabledAutoCapture
                )
                ResultEntryView(
                    stringResource(R.string.sample_app_label_capture_duration_sec),
                    it.captureDuration.inWholeSeconds
                )
            }
            previewBitmap?.let {
                HorizontalDivider()
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp),
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                )
            }
        }
    }
}