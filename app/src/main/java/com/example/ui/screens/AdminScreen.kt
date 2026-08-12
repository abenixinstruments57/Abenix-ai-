package com.example.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.example.data.AbenixSettings

private const val ADMIN_PIN = "5757"

@Composable
fun AdminScreen(
    onBack: () -> Unit = {}
) {
    var enteredPin by remember { mutableStateOf("") }
    var unlocked by remember { mutableStateOf(false) }

    if (!unlocked) {
        AdminLoginScreen(
            pin = enteredPin,
            onPinChange = { enteredPin = it },
            onLogin = {
                if (enteredPin == ADMIN_PIN) {
                    unlocked = true
                }
            },
            onBack = onBack
        )
    } else {
        AdminDashboard(
            onBack = onBack
        )
    }
}

@Composable
private fun AdminLoginScreen(
    pin: String,
    onPinChange: (String) -> Unit,
    onLogin: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Abenix AI Admin",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Enter your Admin PIN",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = pin,
            onValueChange = {
                if (it.length <= 6) {
                    onPinChange(it)
                }
            },
            label = { Text("Admin PIN") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Unlock Admin Panel")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}

@Composable
private fun AdminDashboard(
    onBack: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    val settings = remember {
        AbenixSettings(context)
    }

    var companyName by remember {
        mutableStateOf(settings.companyName)
    }

    var email1 by remember {
        mutableStateOf(settings.email1)
    }

    var email2 by remember {
        mutableStateOf(settings.email2)
    }

    var phone by remember {
        mutableStateOf(settings.phone)
    }

    var instagram by remember {
        mutableStateOf(settings.instagram)
    }

    var aiInstructions by remember {
        mutableStateOf(settings.aiInstructions)
    }

    var savedMessage by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "Abenix AI Admin Panel",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Company Details",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = companyName,
                    onValueChange = { companyName = it },
                    label = { Text("Company Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = email1,
                    onValueChange = { email1 = it },
                    label = { Text("Email 1") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = email2,
                    onValueChange = { email2 = it },
                    label = { Text("Email 2") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("WhatsApp / Phone") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = instagram,
                    onValueChange = { instagram = it },
                    label = { Text("Instagram Link") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "AI Instructions",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = aiInstructions,
                    onValueChange = { aiInstructions = it },
                    label = { Text("AI Behavior") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 6
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                settings.companyName = companyName
                settings.email1 = email1
                settings.email2 = email2
                settings.phone = phone
                settings.instagram = instagram
                settings.aiInstructions = aiInstructions

                savedMessage = "✓ Changes saved successfully"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }

        if (savedMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = savedMessage,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
