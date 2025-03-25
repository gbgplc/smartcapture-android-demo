package com.gbgplc.idscan.bigmagic.compositions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gbgplc.idscan.bigmagic.R
import com.gbgplc.idscan.commons.compositions.PreviewView
import com.gbgplc.idscan.commons.compositions.components.PrimaryButton

@Composable
fun AppSelectionView(
    onFaceCamera: () -> Unit = {},
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
