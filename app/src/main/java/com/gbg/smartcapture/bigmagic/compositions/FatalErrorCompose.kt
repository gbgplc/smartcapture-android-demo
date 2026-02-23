package com.gbg.smartcapture.bigmagic.compositions

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun GenricFatalErrorView(text: String) {

    val message = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        URLDecoder.decode(text, StandardCharsets.UTF_8.name())
    } else {
        @Suppress("DEPRECATION")
        URLDecoder.decode(text)
    }

    Box (
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.errorContainer)
            .clip(MaterialTheme.shapes.medium)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text (
            text = message,
            color = MaterialTheme.colorScheme.onErrorContainer,
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
    }
}