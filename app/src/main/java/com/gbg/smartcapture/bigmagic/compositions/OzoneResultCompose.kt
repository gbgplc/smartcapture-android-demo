package com.gbg.smartcapture.bigmagic.compositions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gbg.smartcapture.bigmagic.R
import com.gbg.smartcapture.ozone.model.OzoneResultData

@Composable
internal fun OzoneResultView(result: OzoneResultData) {
    Column (
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        result.faceImage?.asImageBitmap()?.let {
            Image (
                modifier = Modifier.fillMaxWidth(0.5f),
                contentScale = ContentScale.FillWidth,
                bitmap = it,
                contentDescription = stringResource(R.string.face_image_extracted_from_echip))
        }
        result.signatureImage?.asImageBitmap()?.let {
            Image (
                modifier = Modifier.fillMaxWidth(0.5f),
                contentScale = ContentScale.FillWidth,
                bitmap = it,
                contentDescription = stringResource(R.string.sig_image_extracted_from_echip))
        }
        val map = result.toMap()
        map.forEach { entry ->
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(entry.key, modifier = Modifier.padding(5.dp))
                Text(entry.value.toString(), modifier = Modifier.padding(5.dp))
            }
        }
    }
}