package com.gbg.smartcapture.bigmagic.compositions.bits

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.gbg.smartcapture.bigmagic.R

@Composable
fun HeadingView(
    stringResource: Int,
    fontWeight: FontWeight = FontWeight.Bold
) {
    Text(
        text = stringResource(stringResource),
        fontWeight = fontWeight
    )
}

@Suppress("unused")
@Preview(showBackground = true)
@Composable
private fun Preview() {
    HeadingView(R.string.app_name)
}
