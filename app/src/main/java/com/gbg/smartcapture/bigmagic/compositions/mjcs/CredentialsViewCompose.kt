package com.gbg.smartcapture.bigmagic.compositions.mjcs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gbg.smartcapture.bigmagic.R
import com.gbg.smartcapture.bigmagic.compositions.bits.HeadingView
import com.gbg.smartcapture.commons.compositions.GBGPreviewView
import com.gbg.smartcapture.commons.compositions.components.PrimaryButton

@Composable
fun CredentialsView(
    onSubmit: (baseUrl: String, username: String, password: String, save: Boolean) -> Unit = {baseUrl,username,password,save -> },
    onCancel: () -> Unit = {},
) {
    var baseUrl by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val baseUrlFocus = remember { FocusRequester() }
    val usernameFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(rememberScrollState()),
    ) {
        HeadingView(R.string.new_credential_title)
        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(stringResource(R.string.credentials_base_url))
        TextField(
            value = baseUrl,
            singleLine = true,
            onValueChange = { baseUrl = it },
            modifier = Modifier.focusRequester(baseUrlFocus),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { usernameFocus.requestFocus() }
            )
        )
        Text(stringResource(R.string.credentials_username))
        TextField(
            value = username,
            singleLine = true,
            onValueChange = { username = it },
            modifier = Modifier.focusRequester(usernameFocus),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { passwordFocus.requestFocus() }
            )
        )
        Text(stringResource(R.string.credentials_password))
        TextField(
            value = password,
            singleLine = true,
            onValueChange = { password = it },
            modifier = Modifier.focusRequester(passwordFocus),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        var save by remember { mutableStateOf(true) }   // default ON

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = save,
                onCheckedChange = { save = it }
            )
            Text(
                text = stringResource(R.string.save_credential),
                modifier = Modifier.clickable { save = !save }
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            PrimaryButton(
                text = stringResource(id = R.string.cancel_button),
                onSubmit = onCancel
            )
            PrimaryButton(
                text = stringResource(id = R.string.submit_button),
                onSubmit = {
                    onSubmit(
                    baseUrl, username, password, save
                    )
                }
            )
        }
        Spacer(
            modifier = Modifier.height(32.dp)
        )
    }

}

@Suppress("unused")
@Preview(showBackground = true)
@Composable
private fun Preview() {
    GBGPreviewView {
        CredentialsView()
    }
}
