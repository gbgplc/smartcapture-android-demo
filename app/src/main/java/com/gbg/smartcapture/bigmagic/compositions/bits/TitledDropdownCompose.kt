package com.gbg.smartcapture.bigmagic.compositions.bits

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> TitledDropdown(
    title: String,
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    itemLabel: @Composable (T) -> String = { it.toString() }
) {
    Text(
        text = title,
    )
    Spacer(modifier = Modifier.padding(vertical = 2.dp))
    SimpleDropdown(
        items = items,
        selectedItem = selectedItem,
        onItemSelected = onItemSelected,
        itemLabel = itemLabel,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
}