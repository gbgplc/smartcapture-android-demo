package com.gbgplc.idscan.bigmagic.compositions

import android.content.ClipData
import android.content.ClipboardManager
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat
import com.gbgplc.idscan.bigmagic.R
import com.gbgplc.idscan.commons.compositions.components.PrimaryButton

@Composable
fun CopyButtonView(buttonText: String, string: String) {
    val context = LocalContext.current
    val copiedText = stringResource(R.string.copied_base64_string_to_clipboard)
    val failedCopyText = stringResource(R.string.failed_to_copy_base64_string_to_clipboard)

    if (string.length > STEP) {
        Text(
            text = stringResource(R.string.copy_explanation),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
        var i = 0
        var j = 0
        while (i < string.length) {
            val subString = if (i + STEP < string.length) {
                string.substring(i, i + STEP)
            } else {
                string.substring(i)
            }
            i += STEP
            j += 1
            PrimaryButton(text = "$buttonText part $j") {
                val clipboard: ClipboardManager? =
                    ContextCompat.getSystemService(context, ClipboardManager::class.java)
                val clip = ClipData.newPlainText("data", subString)
                clipboard?.let {
                    it.setPrimaryClip(clip)
                    Toast.makeText(
                        context,
                        copiedText,
                        Toast.LENGTH_LONG
                    ).show()
                } ?: run {
                    Toast.makeText(
                        context,
                        failedCopyText,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    } else {
        PrimaryButton(text = buttonText) {
            val clipboard: ClipboardManager? =
                ContextCompat.getSystemService(context, ClipboardManager::class.java)
            val clip = ClipData.newPlainText("data", string)
            clipboard?.let {
                it.setPrimaryClip(clip)
                Toast.makeText(
                    context,
                    copiedText,
                    Toast.LENGTH_LONG
                ).show()
            } ?: run {
                Toast.makeText(
                    context,
                    failedCopyText,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}


private const val STEP = 450000