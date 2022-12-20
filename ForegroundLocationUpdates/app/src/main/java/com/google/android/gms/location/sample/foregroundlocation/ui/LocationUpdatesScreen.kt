/*
 * Copyright (C) 2021 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.gms.location.sample.foregroundlocation.ui

import android.location.Location
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.sample.foregroundlocation.R
import com.google.android.gms.location.sample.foregroundlocation.ui.theme.ForegroundLocationTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.sample.foregroundlocation.*

var cache = UserMetadata()
@Composable
fun LocationUpdatesScreen(
    showDegradedExperience: Boolean,
    needsPermissionRationale: Boolean,
    onButtonClick: () -> Unit,
    isLocationOn: Boolean,
    location: Location?
) {
    cache.addLocation(location)
    var showRationaleDialog by remember { mutableStateOf(false) }
    if (showRationaleDialog) {
        PermissionRationaleDialog(
            onConfirm = {
                showRationaleDialog = false
                onButtonClick()
            },
            onDismiss = { showRationaleDialog = false }
        )
    }

    fun onClick() {
        if (needsPermissionRationale) {
            showRationaleDialog = true
        } else {
            onButtonClick()
        }
    }

    val message = when {
        isLocationOn -> if (location != null) {
            stringResource(
                id = R.string.location_lat_lng,
                location.latitude,
                location.longitude
            )
        } else {
            stringResource(id = R.string.waiting_for_location)
        }
        showDegradedExperience -> stringResource(id = R.string.please_allow_permission)
        else -> stringResource(id = R.string.not_started)
    }
    var stuff = "timestamp, latitude, longitude, @handle, email\n"
    if(isLocationOn){}
    else {
        stuff = cache.printUserMetadata()
        cache.writeToFile(stuff, LocalContext.current)

//        I may come back to this later on
//        This is a stub for one of the test email services
//        val emailService = EmailService("yourSmtpServer", 587)
//        emailService.usage(stuff)

    }
    val log = when {
        isLocationOn -> if (location != null) {
            //state 1 context LocalContext.current.applicationInfo.dataDir
            "1. Data will be saved to /data/data/com.google.android.gms.location.sample.foregroundlocation/files/location_data.csv" //LocalContext.current.applicationInfo.dataDir
        } else {
            //state 2 context LocalContext.current.applicationInfo.dataDir
            "2. Data will be saved to /data/data/com.google.android.gms.location.sample.foregroundlocation/files/location_data.csv"
        }
        else -> //"state 3"
        stringResource(
        id=R.string.log,
        stuff
    )
    }
    val labelResId = if (isLocationOn) R.string.stop else R.string.start

    // Creating a variable to store toggle state
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TextField(
            value = cache.username,
            onValueChange = {
                cache.username = it
            },
            label = { Text("Username") }
        )
        TextField(
            value = cache.password,
            onValueChange = {
                cache.password = it
            },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.ArrowForward
                else Icons.Filled.ArrowBack

                // Localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                // Toggle button to hide or display password
                IconButton(onClick = {passwordVisible = !passwordVisible}){
                    Icon(imageVector  = image, description)
                }
            }
        )
        TextField(
            value = cache.twitter_handle,
            onValueChange = {
                cache.twitter_handle = it
            },
            label = { Text("Twitter Handle") }
        )
        TextField(
            value = cache.email,
            onValueChange = {
                cache.email = it
            },
            label = { Text("Email") }
        )
        TextField(
            value = cache.runname,
            onValueChange = {
                cache.runname = it
            },
            label = { Text("Email") }
        )
        Text(
            text = message,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
        Button(onClick = { onClick() }) {
            Text(text = stringResource(id = labelResId))
        }
        Text(
            text = log,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PermissionRationaleDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.permission_rationale_dialog_title))
        },
        text = {
            Text(text = stringResource(id = R.string.permission_rationale_dialog_message))
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = stringResource(id = android.R.string.ok))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(id = android.R.string.cancel))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun LocationUpdatesScreenPreview() {
    ForegroundLocationTheme {
        LocationUpdatesScreen(
            showDegradedExperience = false,
            needsPermissionRationale = false,
            onButtonClick = {},
            isLocationOn = true,
            location = null,
        )
    }
}
