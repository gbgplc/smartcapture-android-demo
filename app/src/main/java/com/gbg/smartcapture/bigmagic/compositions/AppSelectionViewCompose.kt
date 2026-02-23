package com.gbg.smartcapture.bigmagic.compositions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gbg.smartcapture.bigmagic.R
import com.gbg.smartcapture.bigmagic.compositions.bits.SpacedListDividerView
import com.gbg.smartcapture.commons.compositions.GBGPreviewView
import com.gbg.smartcapture.commons.compositions.components.PrimaryButton
import com.gbg.smartcapture.commons.compositions.components.SecondaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectionView(
    onFaceCamera: () -> Unit = {},
    onDocumentCamera: () -> Unit = {},
    onOzoneMrz: () -> Unit = {},
    onOzoneManual: () -> Unit = {},
    onMJCSJourney: () -> Unit = {},
    onSettings: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PrimaryButton(
            text = stringResource(id = R.string.mjcs_journey_button),
            onSubmit = onMJCSJourney
        )

        SpacedListDividerView(
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        PrimaryButton(
            text = stringResource(id = R.string.face_camera_button),
            onSubmit = onFaceCamera
        )
        PrimaryButton(
            text = stringResource(id = R.string.document_camera_button),
            onSubmit = onDocumentCamera
        )
        Row {
            PrimaryButton(
                text = stringResource(id = R.string.ozone_mrz_button),
                onSubmit = onOzoneMrz
            )
            SecondaryButton(
                text = stringResource(id = R.string.ozone_manual_button),
                onSubmit = onOzoneManual
            )
        }

        SpacedListDividerView(
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        SecondaryButton(
            text = stringResource(id = R.string.settings_button),
            onSubmit = onSettings
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    GBGPreviewView {
        AppSelectionView()
    }
}
