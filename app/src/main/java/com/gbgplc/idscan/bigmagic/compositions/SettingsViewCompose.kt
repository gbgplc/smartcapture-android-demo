package com.gbgplc.idscan.bigmagic.compositions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.gbgplc.idscan.commons.compositions.PreviewView

@Composable
fun SettingsView(
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewView {
        SettingsView()
    }
}