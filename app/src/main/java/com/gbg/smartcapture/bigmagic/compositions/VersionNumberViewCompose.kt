package com.gbg.smartcapture.bigmagic.compositions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gbg.smartcapture.bigmagic.BuildConfig
import com.gbg.smartcapture.commons.compositions.PreviewView

@Composable
internal fun VersionNumberView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(5.dp)
        )
        Text(
            text = BuildConfig.BUILD_TYPE,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(5.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewView {
        VersionNumberView()
    }
}
