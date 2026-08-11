package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.InstrumentCatalogItem
import com.example.ui.theme.GeometricBg
import com.example.ui.theme.GeometricBlue
import com.example.ui.theme.GeometricBlueLight
import com.example.ui.theme.GeometricBorder
import com.example.ui.theme.GeometricSurface
import com.example.ui.theme.GeometricTextMuted
import com.example.ui.theme.GeometricTextPrimary
import com.example.ui.theme.GeometricTextSecondary
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(viewModel: MainViewModel) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedItem by viewModel.selectedCatalogItem.collectAsState()
    val bookmarkedList by viewModel.bookmarkedProducts.collectAsState()

    val categories = listOf("All", "Surgical", "Dental", "Orthopedic", "TC", "Vascular", "Surgical Sets", "Custom")

    val filteredItems = viewModel.catalogItems.filter { item ->
        val matchesCategory = (selectedCategory == "All" || item.category == selectedCategory)
        val query = searchQuery.trim().lowercase()
        val matchesSearch = query.isEmpty() ||
                item.name.lowercase().contains(query) ||
                item.shortDescription.lowercase().contains(query) ||
                item.category.lowercase().contains(query)
        matchesCategory && matchesSearch
    }

    val sheetState = rememberModalBottomSheetState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GeometricBg)
    ) {
        // Top Search Bar Surface
        Surface(
            color = GeometricSurface,
            shadowElevation = 1.dp,
            border = BorderStroke(1.dp, GeometricBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Abenix Instrument Catalog",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = GeometricTextPrimary
                )
                Text(
                    text = "Browse certified surgical & dental export range",
                    style = MaterialTheme.typography.bodySmall,
                    color = GeometricTextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("Search scissors, forceps, needle holders...", color = GeometricTextMuted) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GeometricTextSecondary) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear search", tint = GeometricTextSecondary)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("catalog_search_bar"),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GeometricBlue,
                        unfocusedBorderColor = GeometricBorder,
                        focusedContainerColor = GeometricBg,
                        unfocusedContainerColor = GeometricBg
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Category Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { category ->
                        val isSelected = selectedCategory == category
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setCategory(category) },
                            label = { Text(category) },
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, if (isSelected) GeometricBlue else GeometricBorder),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GeometricBlue,
                                selectedLabelColor = Color.White,
                                containerColor = GeometricSurface,
                                labelColor = GeometricTextSecondary
                            )
                        )
                    }
                }
            }
        }

        // Product Catalog List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Banner Header Item
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = GeometricSurface),
                    border = BorderStroke(1.dp, GeometricBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                color = GeometricBlue,
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = "EXPORTS & OEM",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "High Grade Surgical Steel",
                                style = MaterialTheme.typography.titleMedium,
                                color = GeometricTextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Autoclave tested, anti-corrosive stainless steel tools manufactured in Sialkot, Pakistan.",
                                style = MaterialTheme.typography.bodySmall,
                                color = GeometricTextSecondary
                            )
                        }
                    }
                }
            }

            if (filteredItems.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = GeometricTextMuted
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No instruments found matching criteria", fontWeight = FontWeight.Medium, color = GeometricTextPrimary)
                        Text("Try searching another keyword or selecting 'All' category", fontSize = 12.sp, color = GeometricTextSecondary)
                    }
                }
            } else {
                items(filteredItems, key = { it.id }) { item ->
                    val isBookmarked = bookmarkedList.any { it.id == item.id }
                    CatalogItemCard(
                        item = item,
                        isBookmarked = isBookmarked,
                        onItemClick = { viewModel.selectCatalogItem(item) },
                        onBookmarkToggle = { viewModel.toggleBookmark(item) },
                        onRequestQuote = { viewModel.prefillQuoteForProduct(item) }
                    )
                }
            }
        }
    }

    // Detail Bottom Sheet
    if (selectedItem != null) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.selectCatalogItem(null) },
            sheetState = sheetState,
            containerColor = GeometricSurface
        ) {
            val item = selectedItem!!
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = GeometricBlueLight,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = item.category.uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = GeometricBlue,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = "Ref: ${item.id}",
                        style = MaterialTheme.typography.labelSmall,
                        color = GeometricTextMuted
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = GeometricTextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.shortDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    color = GeometricTextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    color = GeometricBg,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, GeometricBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Technical Specifications",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = GeometricTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = item.fullSpecs, style = MaterialTheme.typography.bodySmall, color = GeometricTextSecondary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Recommended Material: ${item.recommendedMaterial}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = GeometricBlue
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Key Features:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = GeometricTextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                item.features.forEach { feature ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = GeometricBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = feature, style = MaterialTheme.typography.bodySmall, color = GeometricTextPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val selected = selectedItem
                        viewModel.selectCatalogItem(null)
                        if (selected != null) {
                            viewModel.prefillQuoteForProduct(selected)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GeometricBlue),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.RequestQuote, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Prepare Quotation Request", fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun CatalogItemCard(
    item: InstrumentCatalogItem,
    isBookmarked: Boolean,
    onItemClick: () -> Unit,
    onBookmarkToggle: () -> Unit,
    onRequestQuote: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = GeometricSurface),
        border = BorderStroke(1.dp, GeometricBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .testTag("catalog_card_${item.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        color = GeometricBlueLight,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = item.category,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = GeometricBlue,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GeometricTextPrimary
                    )
                }

                IconButton(
                    onClick = onBookmarkToggle,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark item",
                        tint = if (isBookmarked) GeometricBlue else GeometricTextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.shortDescription,
                style = MaterialTheme.typography.bodySmall,
                color = GeometricTextSecondary,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = GeometricBg,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, GeometricBorder)
                ) {
                    Text(
                        text = item.recommendedMaterial,
                        style = MaterialTheme.typography.labelSmall,
                        color = GeometricTextSecondary,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Button(
                    onClick = onRequestQuote,
                    colors = ButtonDefaults.buttonColors(containerColor = GeometricBlue),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.height(38.dp)
                ) {
                    Icon(Icons.Default.RequestQuote, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Request Quote", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

