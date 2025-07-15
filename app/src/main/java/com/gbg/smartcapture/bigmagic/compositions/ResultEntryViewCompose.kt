package com.gbg.smartcapture.bigmagic.compositions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResultEntryView(label: String, value: Boolean?) {
    ResultEntryView(label, value.toString())
}

@Composable
fun ResultEntryView(label: String, value: Number) {
    ResultEntryView(label, value.toString())
}

@Composable
fun ResultEntryView(label: String, value: String) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = label
        )
        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = value
        )
    }
}
