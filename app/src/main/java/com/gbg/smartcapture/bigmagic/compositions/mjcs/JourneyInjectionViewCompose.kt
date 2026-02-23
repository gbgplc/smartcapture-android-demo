package com.gbg.smartcapture.bigmagic.compositions.mjcs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gbg.smartcapture.commons.compositions.components.PrimaryButton

@Composable
fun JourneyInjectionView(
    text: String,
    subtitle: String,
    buttonText: String?,
    onClick: (() -> Unit) = {}
) {
    Surface (
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium
            )
            if (buttonText != null) {
                PrimaryButton(
                    text = buttonText,
                    onSubmit = onClick
                )
            }
        }
    }
}