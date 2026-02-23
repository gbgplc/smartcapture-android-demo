package com.gbg.smartcapture.bigmagic.compositions.mjcs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gbg.smartcapture.bigmagic.R
import com.gbg.smartcapture.bigmagic.compositions.bits.HeadingView
import com.gbg.smartcapture.bigmagic.compositions.bits.ListDividerView
import com.gbg.smartcapture.bigmagic.data.SampleAppCredential
import com.gbg.smartcapture.commons.compositions.GBGPreviewView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CredentialsListView(
    credentialSet: State<Set<String>>,
    allowDelete: Boolean = false,
    onDeleteCredential: (credential: SampleAppCredential) -> Unit = {credential->},
    onSelectCredential: (credential: SampleAppCredential) -> Unit = { credential->},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        HeadingView(R.string.list_of_saved_credentials_title)
        ListDividerView()

        for (credentialJson in credentialSet.value) {
            CredentialRow(
                credentialJson = credentialJson,
                allowDelete = allowDelete,
                onDeleteCredential = onDeleteCredential,
                onSelectCredential = onSelectCredential
            )
            ListDividerView()
        }
    }
}

@Composable
fun CredentialRow(
    credentialJson: String,
    allowDelete: Boolean,
    onDeleteCredential: (credential: SampleAppCredential) -> Unit = {credential->},
    onSelectCredential: (credential: SampleAppCredential) -> Unit = { credential->},
) {
    if (credentialJson.isNotBlank()) {
        val credential: SampleAppCredential? = SampleAppCredential.fromJson(credentialJson)
        if (credential != null) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable {
                        if (allowDelete) {
                            onDeleteCredential(credential)
                        } else {
                            onSelectCredential(credential)
                        }
                    }
            ) {
                Text(
                    text = "${credential.username} | ${credential.hiddenPassword()} | ${credential.baseUrl}",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
    }
}

@Suppress("unused")
@Preview(showBackground = true)
@Composable
private fun Preview() {
    val credJson = """
            { 
                "username": "user", 
                "password": "****", 
                "baseUrl": "test" 
            }        
    """.trimIndent()
    val stringSet = setOf(credJson)
    val credentialSet: StateFlow<Set<String>> = MutableStateFlow(stringSet)

    GBGPreviewView {
        CredentialsListView(credentialSet.collectAsStateWithLifecycle())
    }
}
