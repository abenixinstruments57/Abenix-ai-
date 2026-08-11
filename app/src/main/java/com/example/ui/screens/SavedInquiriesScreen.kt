package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookmarkedProduct
import com.example.data.model.CustomSpecInquiry
import com.example.data.model.QuotationInquiry
import com.example.ui.theme.GeometricBg
import com.example.ui.theme.GeometricBlue
import com.example.ui.theme.GeometricBorder
import com.example.ui.theme.GeometricSurface
import com.example.ui.theme.GeometricTextMuted
import com.example.ui.theme.GeometricTextPrimary
import com.example.ui.theme.GeometricTextSecondary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun SavedInquiriesScreen(viewModel: MainViewModel) {
    var selectedSubTab by remember { mutableIntStateOf(0) }
    val quotations by viewModel.quotations.collectAsState()
    val customSpecs by viewModel.customSpecs.collectAsState()
    val bookmarkedProducts by viewModel.bookmarkedProducts.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GeometricBg)
    ) {
        // Top Bar
        Surface(
            color = GeometricSurface,
            shadowElevation = 1.dp,
            border = BorderStroke(1.dp, GeometricBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Saved Requests & Inquiries",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = GeometricTextPrimary
                )
                Text(
                    text = "Manage your saved quote requests and OEM drafts",
                    style = MaterialTheme.typography.bodySmall,
                    color = GeometricTextSecondary
                )
            }
        }

        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = GeometricSurface,
            contentColor = GeometricBlue,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedSubTab]),
                    color = GeometricBlue
                )
            }
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = {
                    Text(
                        "Quotations (${quotations.size})",
                        fontSize = 12.sp,
                        fontWeight = if (selectedSubTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedSubTab == 0) GeometricBlue else GeometricTextSecondary
                    )
                }
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = {
                    Text(
                        "OEM Specs (${customSpecs.size})",
                        fontSize = 12.sp,
                        fontWeight = if (selectedSubTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedSubTab == 1) GeometricBlue else GeometricTextSecondary
                    )
                }
            )
            Tab(
                selected = selectedSubTab == 2,
                onClick = { selectedSubTab = 2 },
                text = {
                    Text(
                        "Bookmarks (${bookmarkedProducts.size})",
                        fontSize = 12.sp,
                        fontWeight = if (selectedSubTab == 2) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedSubTab == 2) GeometricBlue else GeometricTextSecondary
                    )
                }
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (selectedSubTab) {
                0 -> {
                    if (quotations.isEmpty()) {
                        EmptySavedState("No saved quotation requests yet", "Use the 'Request Quote' tab to draft a price inquiry.")
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(quotations, key = { it.id }) { item ->
                                QuotationSavedCard(
                                    item = item,
                                    onDelete = { viewModel.deleteQuotation(item.id) },
                                    onCopy = {
                                        val text = "Abenix Quote Request: ${item.productName}, Qty: ${item.quantity}, Country: ${item.destinationCountry}, Material: ${item.material}"
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        clipboard.setPrimaryClip(ClipData.newPlainText("Quote Request", text))
                                        Toast.makeText(context, "Copied quote details", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    }
                }

                1 -> {
                    if (customSpecs.isEmpty()) {
                        EmptySavedState("No saved custom OEM spec drafts", "Use the 'Custom OEM Specs' form to specify custom tools.")
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(customSpecs, key = { it.id }) { item ->
                                CustomSpecSavedCard(
                                    item = item,
                                    onDelete = { viewModel.deleteCustomSpec(item.id) },
                                    onCopy = {
                                        val text = "Abenix OEM Spec: ${item.instrumentType}, Dimensions: ${item.dimensions}, Material: ${item.materialRequirements}"
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        clipboard.setPrimaryClip(ClipData.newPlainText("OEM Spec", text))
                                        Toast.makeText(context, "Copied OEM spec details", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    }
                }

                2 -> {
                    if (bookmarkedProducts.isEmpty()) {
                        EmptySavedState("No bookmarked catalog instruments", "Browse the catalog and tap the bookmark icon on tools of interest.")
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(bookmarkedProducts, key = { it.id }) { item ->
                                BookmarkedCard(
                                    item = item,
                                    onRequestQuote = {
                                        viewModel.updateQuoteProductName(item.name)
                                        viewModel.setSelectedTab(2)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptySavedState(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.FolderOpen,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = GeometricTextMuted
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = GeometricTextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = GeometricTextSecondary)
    }
}

@Composable
fun QuotationSavedCard(
    item: QuotationInquiry,
    onDelete: () -> Unit,
    onCopy: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = GeometricSurface),
        border = BorderStroke(1.dp, GeometricBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                        text = item.status.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Row {
                    IconButton(onClick = onCopy) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy quote details", tint = GeometricBlue)
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete quote request", tint = Color(0xFFDC2626))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = item.productName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = GeometricTextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Qty: ${item.quantity} Pcs | Country: ${item.destinationCountry}", style = MaterialTheme.typography.bodyMedium, color = GeometricTextSecondary)
            Text(text = "Material: ${item.material} | Finish: ${item.finish}", style = MaterialTheme.typography.bodySmall, color = GeometricTextMuted)
        }
    }
}

@Composable
fun CustomSpecSavedCard(
    item: CustomSpecInquiry,
    onDelete: () -> Unit,
    onCopy: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = GeometricSurface),
        border = BorderStroke(1.dp, GeometricBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                        text = "CUSTOM OEM SPEC",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Row {
                    IconButton(onClick = onCopy) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy spec details", tint = GeometricBlue)
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete OEM spec", tint = Color(0xFFDC2626))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = item.instrumentType, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = GeometricTextPrimary)
            if (item.dimensions.isNotBlank()) {
                Text(text = "Dimensions: ${item.dimensions}", style = MaterialTheme.typography.bodySmall, color = GeometricTextSecondary)
            }
            if (item.materialRequirements.isNotBlank()) {
                Text(text = "Material: ${item.materialRequirements}", style = MaterialTheme.typography.bodySmall, color = GeometricTextMuted)
            }
        }
    }
}

@Composable
fun BookmarkedCard(
    item: BookmarkedProduct,
    onRequestQuote: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = GeometricSurface),
        border = BorderStroke(1.dp, GeometricBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = GeometricTextPrimary)
                Text(text = "${item.category} • ${item.material}", style = MaterialTheme.typography.bodySmall, color = GeometricTextSecondary)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onRequestQuote,
                colors = ButtonDefaults.buttonColors(containerColor = GeometricBlue),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Quote", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

