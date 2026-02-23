package com.gbg.smartcapture.bigmagic.compositions.bits

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SpacedListDividerView(
    modifier: Modifier = Modifier,
    colour: Color = Color.LightGray,
    thickness: Dp = 1.dp,
    spacing: Dp = 8.dp
) {
    Spacer(
        modifier = Modifier.height(spacing)
    )
    HorizontalDivider(
        modifier = modifier,
        color = colour,
        thickness = thickness
    )
    Spacer(
        modifier = Modifier.height(spacing)
    )
}

@Composable
fun ListDividerView(
    modifier: Modifier = Modifier,
    colour: Color = Color.LightGray,
    thickness: Dp = 1.dp,
) {
    HorizontalDivider(
        modifier = modifier,
        color = colour,
        thickness = thickness
    )
}

@Suppress("unused")
@Preview(showBackground = true)
@Composable
private fun Preview() {
    ListDividerView()
}
