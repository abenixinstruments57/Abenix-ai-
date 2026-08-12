package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.data.AbenixSettings
import com.example.data.Local.AbenixProductStore
import com.example.data.Model.AbenixProduct
import java.util.UUID

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
        AdminDashboard(onBack = onBack)
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

        Text("Enter your Admin PIN")

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

        OutlinedButton(
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
    val context = LocalContext.current

    val settings = remember {
        AbenixSettings(context)
    }

    val productStore = remember {
        AbenixProductStore(context)
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

    var products by remember {
        mutableStateOf(productStore.getProducts())
    }

    var showProductDialog by remember {
        mutableStateOf(false)
    }

    var editingProduct by remember {
        mutableStateOf<AbenixProduct?>(null)
    }

    var savedMessage by remember {
        mutableStateOf("")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        item {
            Text(
                text = "Abenix AI Admin Panel",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
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
        }

        item {
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

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
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
                Text("Save Company & AI Settings")
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Text(
                text = "Products / Instruments",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    editingProduct = null
                    showProductDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("＋ Add Instrument")
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        if (products.isEmpty()) {
            item {
                Text(
                    text = "No instruments added yet.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        items(
            items = products,
            key = { it.id }
        ) { product ->

            ProductAdminCard(
                product = product,

                onEdit = {
                    editingProduct = product
                    showProductDialog = true
                },

                onDelete = {
                    productStore.deleteProduct(product.id)
                    products = productStore.getProducts()
                }
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            if (savedMessage.isNotEmpty()) {
                Text(
                    text = savedMessage,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    if (showProductDialog) {
        ProductEditorDialog(
            existingProduct = editingProduct,

            onDismiss = {
                showProductDialog = false
            },

            onSave = { product ->

                if (editingProduct == null) {
                    productStore.addProduct(product)
                } else {
                    productStore.updateProduct(product)
                }

                products = productStore.getProducts()
                showProductDialog = false
            }
        )
    }
}

@Composable
private fun ProductAdminCard(
    product: AbenixProduct,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = product.category,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Price: ${product.price} ${product.currency}",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (product.available)
                    "Available"
                else
                    "Unavailable"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Edit")
                }

                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
private fun ProductEditorDialog(
    existingProduct: AbenixProduct?,
    onDismiss: () -> Unit,
    onSave: (AbenixProduct) -> Unit
) {
    var name by remember {
        mutableStateOf(existingProduct?.name ?: "")
    }

    var category by remember {
        mutableStateOf(existingProduct?.category ?: "")
    }

    var description by remember {
        mutableStateOf(existingProduct?.description ?: "")
    }

    var price by remember {
        mutableStateOf(existingProduct?.price ?: "")
    }

    var currency by remember {
        mutableStateOf(existingProduct?.currency ?: "USD")
    }

    var available by remember {
        mutableStateOf(existingProduct?.available ?: true)
    }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text(
                if (existingProduct == null)
                    "Add Instrument"
                else
                    "Edit Instrument"
            )
        },

        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Instrument Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = currency,
                    onValueChange = { currency = it },
                    label = { Text("Currency") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            available = !available
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (available)
                                "Available ✓"
                            else
                                "Unavailable"
                        )
                    }
                }
            }
        },

        confirmButton = {
            TextButton(
                onClick = {

                    if (name.isNotBlank()) {

                        val product = AbenixProduct(
                            id = existingProduct?.id
                                ?: UUID.randomUUID().toString(),
                            name = name.trim(),
                            category = category.trim(),
                            description = description.trim(),
                            price = price.trim(),
                            currency = currency.trim().ifEmpty { "USD" },
                            available = available
                        )

                        onSave(product)
                    }
                }
            ) {
                Text("Save")
            }
        },

        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}
