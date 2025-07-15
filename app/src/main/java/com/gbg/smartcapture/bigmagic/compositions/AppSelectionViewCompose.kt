package com.gbg.smartcapture.bigmagic.compositions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gbg.smartcapture.bigmagic.R
import com.gbg.smartcapture.commons.compositions.PreviewView
import com.gbg.smartcapture.commons.compositions.components.PrimaryButton

@Composable
fun AppSelectionView(
    onFaceCamera: () -> Unit = {},
    onDocumentCamera: () -> Unit = {},
    onSettings: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PrimaryButton(
            text = stringResource(id = R.string.face_camera_button),
            onSubmit = onFaceCamera
        )
        PrimaryButton(
            text = stringResource(id = R.string.document_camera_button),
            onSubmit = onDocumentCamera
        )
//        SecondaryButton(
//            modifier = Modifier.padding(top = 24.dp),
//            text = stringResource(id = R.string.settings_button),
//            onSubmit = onSettings
//        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewView {
        AppSelectionView()
    }
}
