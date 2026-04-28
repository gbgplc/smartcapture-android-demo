package com.gbg.smartcapture.bigmagic.compositions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.gbg.smartcapture.bigmagic.R
import com.gbg.smartcapture.commons.compositions.components.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OzoneDataInput(passNumVal: String = "", dobVal: String = "", doeVal: String = "", onSubmit: (passNum: String, dob: String, doe: String) -> Unit) {
    var passNum by remember { mutableStateOf(TextFieldValue(passNumVal)) }
    var dob by remember { mutableStateOf(TextFieldValue(dobVal)) }
    var doe by remember { mutableStateOf(TextFieldValue(doeVal)) }
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField (
            modifier = Modifier.padding(6.dp),
            value = passNum,
            label = {
                Text(
                    text = stringResource(R.string.pass_num_label),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                capitalization = KeyboardCapitalization.Characters,
                imeAction = ImeAction.Next
            ),
            onValueChange = {
                passNum = it
            }
        )
        OutlinedTextField (
            modifier = Modifier.padding(6.dp),
            value = dob,
            label = {
                Text(
                    text = stringResource(R.string.dob_label),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            onValueChange = {
                dob = it
            }
        )
        OutlinedTextField (
            modifier = Modifier.padding(6.dp),
            value = doe,
            label = {
                Text(
                    text = stringResource(R.string.doe_label),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            onValueChange = {
                doe = it
            }
        )
        PrimaryButton (
            text = stringResource(R.string.submit),
            onSubmit = {
                onSubmit(passNum.text, dob.text, doe.text)
            }
        )
    }
}