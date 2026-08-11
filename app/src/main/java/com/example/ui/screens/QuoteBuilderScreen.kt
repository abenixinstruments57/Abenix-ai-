package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GeometricBg
import com.example.ui.theme.GeometricBlue
import com.example.ui.theme.GeometricBorder
import com.example.ui.theme.GeometricSurface
import com.example.ui.theme.GeometricTextMuted
import com.example.ui.theme.GeometricTextPrimary
import com.example.ui.theme.GeometricTextSecondary
import com.example.ui.theme.GreenSuccess
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteBuilderScreen(viewModel: MainViewModel) {
    var selectedFormTab by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val bannerMessage by viewModel.quoteSuccessBanner.collectAsState()

    val productName by viewModel.quoteProductName.collectAsState()
    val quantity by viewModel.quoteQuantity.collectAsState()
    val country by viewModel.quoteDestinationCountry.collectAsState()
    val material by viewModel.quoteMaterial.collectAsState()
    val finish by viewModel.quoteFinish.collectAsState()
    val customization by viewModel.quoteCustomization.collectAsState()
    val specialSpecs by viewModel.quoteSpecialSpecs.collectAsState()

    val customType by viewModel.customInstrumentType.collectAsState()
    val customPhotoRef by viewModel.customPhotoRef.collectAsState()
    val customDimensions by viewModel.customDimensions.collectAsState()

    val materialsList = listOf(
        "Stainless Steel 410 / 420",
        "Japanese Steel AISI 420",
        "German Stainless Steel Grade",
        "Tungsten Carbide (TC) Tipped",
        "Titanium Alloy Grade"
    )

    val finishList = listOf(
        "Satin / Matte Anti-Glare Finish",
        "Mirror High Polish",
        "Sandblast Finish",
        "Gold Plated Handles / Rings"
    )

    val customizationList = listOf(
        "Standard Export Carton Packing",
        "Laser Engraved Logo & Ref Code",
        "Sterilization Pouch Packing",
        "Custom OEM Box Packaging"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GeometricBg)
    ) {
        // Top Bar Header Surface
        Surface(
            color = GeometricSurface,
            shadowElevation = 1.dp,
            border = BorderStroke(1.dp, GeometricBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Quotation & Custom Spec Builder",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = GeometricTextPrimary
                )
                Text(
                    text = "Abenix Instruments Export Inquiry System",
                    style = MaterialTheme.typography.bodySmall,
                    color = GeometricTextSecondary
                )
            }
        }

        // Form Tabs
        TabRow(
            selectedTabIndex = selectedFormTab,
            containerColor = GeometricSurface,
            contentColor = GeometricBlue,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedFormTab]),
                    color = GeometricBlue
                )
            }
        ) {
            Tab(
                selected = selectedFormTab == 0,
                onClick = { selectedFormTab = 0 },
                text = { Text("Standard Quote Request", fontWeight = if (selectedFormTab == 0) FontWeight.Bold else FontWeight.Normal, color = if (selectedFormTab == 0) GeometricBlue else GeometricTextSecondary) },
                icon = { Icon(Icons.Default.RequestQuote, contentDescription = null, tint = if (selectedFormTab == 0) GeometricBlue else GeometricTextSecondary) }
            )
            Tab(
                selected = selectedFormTab == 1,
                onClick = { selectedFormTab = 1 },
                text = { Text("Custom OEM Specs", fontWeight = if (selectedFormTab == 1) FontWeight.Bold else FontWeight.Normal, color = if (selectedFormTab == 1) GeometricBlue else GeometricTextSecondary) },
                icon = { Icon(Icons.Default.PrecisionManufacturing, contentDescription = null, tint = if (selectedFormTab == 1) GeometricBlue else GeometricTextSecondary) }
            )
        }

        // Form Content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            AnimatedVisibility(visible = bannerMessage != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = GreenSuccess.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, GreenSuccess.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GreenSuccess)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = bannerMessage ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GreenSuccess,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { viewModel.clearBanner() }) {
                            Icon(Icons.Default.Info, contentDescription = "Close banner", tint = GreenSuccess)
                        }
                    }
                }
            }

            if (selectedFormTab == 0) {
                // Standard Quote Form Card
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = GeometricSurface),
                    border = BorderStroke(1.dp, GeometricBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Product Details & Export Quantity",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = GeometricTextPrimary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = productName,
                            onValueChange = { viewModel.updateQuoteProductName(it) },
                            label = { Text("Instrument Name / Code *") },
                            placeholder = { Text("e.g. Mayo Scissors 14cm or Dental Forceps Set", color = GeometricTextMuted) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("quote_product_name_input"),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GeometricBlue,
                                unfocusedBorderColor = GeometricBorder,
                                focusedContainerColor = GeometricBg,
                                unfocusedContainerColor = GeometricBg
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = quantity,
                                onValueChange = { viewModel.updateQuoteQuantity(it) },
                                label = { Text("Quantity (Pcs) *") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("quote_quantity_input"),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GeometricBlue,
                                    unfocusedBorderColor = GeometricBorder,
                                    focusedContainerColor = GeometricBg,
                                    unfocusedContainerColor = GeometricBg
                                ),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            OutlinedTextField(
                                value = country,
                                onValueChange = { viewModel.updateQuoteDestinationCountry(it) },
                                label = { Text("Destination Country *") },
                                placeholder = { Text("e.g. Germany, USA, UAE", color = GeometricTextMuted) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("quote_country_input"),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GeometricBlue,
                                    unfocusedBorderColor = GeometricBorder,
                                    focusedContainerColor = GeometricBg,
                                    unfocusedContainerColor = GeometricBg
                                ),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Manufacturing & Finish Options",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = GeometricTextPrimary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Material Dropdown
                        var materialExpanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = materialExpanded,
                            onExpandedChange = { materialExpanded = !materialExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = material,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Material Grade") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = materialExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GeometricBlue,
                                    unfocusedBorderColor = GeometricBorder,
                                    focusedContainerColor = GeometricBg,
                                    unfocusedContainerColor = GeometricBg
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = materialExpanded,
                                onDismissRequest = { materialExpanded = false }
                            ) {
                                materialsList.forEach { mat ->
                                    DropdownMenuItem(
                                        text = { Text(mat) },
                                        onClick = {
                                            viewModel.updateQuoteMaterial(mat)
                                            materialExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Finish Dropdown
                        var finishExpanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = finishExpanded,
                            onExpandedChange = { finishExpanded = !finishExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = finish,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Surface Finish") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = finishExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GeometricBlue,
                                    unfocusedBorderColor = GeometricBorder,
                                    focusedContainerColor = GeometricBg,
                                    unfocusedContainerColor = GeometricBg
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = finishExpanded,
                                onDismissRequest = { finishExpanded = false }
                            ) {
                                finishList.forEach { fin ->
                                    DropdownMenuItem(
                                        text = { Text(fin) },
                                        onClick = {
                                            viewModel.updateQuoteFinish(fin)
                                            finishExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Customization Dropdown
                        var customExpanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = customExpanded,
                            onExpandedChange = { customExpanded = !customExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = customization,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Branding & Packaging") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = customExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GeometricBlue,
                                    unfocusedBorderColor = GeometricBorder,
                                    focusedContainerColor = GeometricBg,
                                    unfocusedContainerColor = GeometricBg
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = customExpanded,
                                onDismissRequest = { customExpanded = false }
                            ) {
                                customizationList.forEach { cust ->
                                    DropdownMenuItem(
                                        text = { Text(cust) },
                                        onClick = {
                                            viewModel.updateQuoteCustomization(cust)
                                            customExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = specialSpecs,
                            onValueChange = { viewModel.updateQuoteSpecialSpecs(it) },
                            label = { Text("Additional Requirements / Notes") },
                            placeholder = { Text("Specific sizing, jaw serrations, or custom packing notes...", color = GeometricTextMuted) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GeometricBlue,
                                unfocusedBorderColor = GeometricBorder,
                                focusedContainerColor = GeometricBg,
                                unfocusedContainerColor = GeometricBg
                            ),
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { viewModel.submitQuotationInquiry() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("submit_quote_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = GeometricBlue),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate Formal Quotation Ticket", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Preview Ticket
                if (productName.isNotBlank() && country.isNotBlank()) {
                    QuotationTicketCard(
                        product = productName,
                        qty = quantity,
                        country = country,
                        material = material,
                        finish = finish,
                        customization = customization,
                        onCopySummary = { summary ->
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Quotation Packet", summary))
                            Toast.makeText(context, "Quotation summary copied to clipboard", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

            } else {
                // Custom OEM Spec Form Card
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = GeometricSurface),
                    border = BorderStroke(1.dp, GeometricBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Custom Instrument Specification",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = GeometricTextPrimary
                        )
                        Text(
                            text = "Abenix Instruments manufactures custom tools according to customer drawings or physical samples.",
                            style = MaterialTheme.typography.bodySmall,
                            color = GeometricTextSecondary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = customType,
                            onValueChange = { viewModel.updateCustomInstrumentType(it) },
                            label = { Text("Instrument Category / Description *") },
                            placeholder = { Text("e.g. Micro-arthroscopy Punch Scissor or Titanium Spatula", color = GeometricTextMuted) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("custom_type_input"),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GeometricBlue,
                                unfocusedBorderColor = GeometricBorder,
                                focusedContainerColor = GeometricBg,
                                unfocusedContainerColor = GeometricBg
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = customDimensions,
                            onValueChange = { viewModel.updateCustomDimensions(it) },
                            label = { Text("Dimensions & Sizes") },
                            placeholder = { Text("Length: 160mm, Shaft diameter: 3.5mm, Jaw angle: 45°", color = GeometricTextMuted) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GeometricBlue,
                                unfocusedBorderColor = GeometricBorder,
                                focusedContainerColor = GeometricBg,
                                unfocusedContainerColor = GeometricBg
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = customPhotoRef,
                            onValueChange = { viewModel.updateCustomPhotoRef(it) },
                            label = { Text("Photo or Technical Drawing Reference") },
                            placeholder = { Text("e.g. Sample Ref #ABX-OEM-2026 or Image link", color = GeometricTextMuted) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GeometricBlue,
                                unfocusedBorderColor = GeometricBorder,
                                focusedContainerColor = GeometricBg,
                                unfocusedContainerColor = GeometricBg
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = viewModel.customLogoBranding.collectAsState().value,
                            onValueChange = { viewModel.updateCustomLogoBranding(it) },
                            label = { Text("Laser Marking & Branding Requirements") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GeometricBlue,
                                unfocusedBorderColor = GeometricBorder,
                                focusedContainerColor = GeometricBg,
                                unfocusedContainerColor = GeometricBg
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = viewModel.customSpecialInstructions.collectAsState().value,
                            onValueChange = { viewModel.updateCustomSpecialInstructions(it) },
                            label = { Text("Special Technical Instructions") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GeometricBlue,
                                unfocusedBorderColor = GeometricBorder,
                                focusedContainerColor = GeometricBg,
                                unfocusedContainerColor = GeometricBg
                            ),
                            maxLines = 4
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { viewModel.submitCustomSpecInquiry() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("submit_custom_spec_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = GeometricBlue),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(Icons.Default.PrecisionManufacturing, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Save Custom OEM Spec Draft", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuotationTicketCard(
    product: String,
    qty: String,
    country: String,
    material: String,
    finish: String,
    customization: String,
    onCopySummary: (String) -> Unit
) {
    val summaryText = """
        ABENIX INSTRUMENTS - FORMAL QUOTATION REQUEST
        Product: $product
        Quantity: $qty Pcs
        Destination: $country
        Material Grade: $material
        Surface Finish: $finish
        Branding/Packing: $customization
        Status: Request Drafted for Abenix Sales Team Review
        Motto: Choose Perfect, Choose Abenix.
    """.trimIndent()

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = GeometricSurface),
        border = BorderStroke(1.dp, GeometricBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = GeometricBlue,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "QUOTATION TICKET PREVIEW",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                IconButton(onClick = { onCopySummary(summaryText) }) {
                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy ticket summary", tint = GeometricBlue)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Product: $product", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = GeometricTextPrimary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "Quantity: $qty Pcs | Destination: $country", style = MaterialTheme.typography.bodyMedium, color = GeometricTextSecondary)
            Text(text = "Material: $material", style = MaterialTheme.typography.bodySmall, color = GeometricTextSecondary)
            Text(text = "Finish: $finish", style = MaterialTheme.typography.bodySmall, color = GeometricTextSecondary)
            Text(text = "Branding: $customization", style = MaterialTheme.typography.bodySmall, color = GeometricTextSecondary)

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                color = GeometricBg,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, GeometricBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Abenix team will prepare pricing after reviewing quantity and destination shipping requirements.",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(10.dp),
                    color = GeometricTextSecondary
                )
            }
        }
    }
}

