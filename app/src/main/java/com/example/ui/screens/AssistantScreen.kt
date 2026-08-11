package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.ui.theme.GeometricBg
import com.example.ui.theme.GeometricBlue
import com.example.ui.theme.GeometricBlueLight
import com.example.ui.theme.GeometricBorder
import com.example.ui.theme.GeometricSurface
import com.example.ui.theme.GeometricTextMuted
import com.example.ui.theme.GeometricTextPrimary
import com.example.ui.theme.GeometricTextSecondary
import com.example.ui.theme.LiveSupportBg
import com.example.ui.theme.LiveSupportBorder
import com.example.ui.theme.LiveSupportDot
import com.example.ui.theme.LiveSupportText
import com.example.ui.viewmodel.MainViewModel

@Composable
fun AssistantScreen(viewModel: MainViewModel) {
    val messages by viewModel.messages.collectAsState()
    val input by viewModel.chatInput.collectAsState()
    val isSending by viewModel.isSending.collectAsState()
    val context = LocalContext.current
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GeometricBg)
    ) {
        // Header Banner - Geometric Balance Header
        Surface(
            color = GeometricSurface,
            shadowElevation = 1.dp,
            border = BorderStroke(1.dp, GeometricBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(GeometricBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "A",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "ABENIX",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = GeometricTextPrimary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "INSTRUMENTS",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = GeometricBlue,
                            fontSize = 10.sp,
                            letterSpacing = 2.sp
                        )
                    }
                }

                // Live Support Pill Badge
                Surface(
                    color = LiveSupportBg,
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, LiveSupportBorder)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(LiveSupportDot)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Live Support",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = LiveSupportText,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Main Content Area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (messages.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier.height(10.dp))

                    // AI Assistant Card - Hero Callout
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        Card(
                            shape = RoundedCornerShape(28.dp),
                            colors = CardDefaults.cardColors(containerColor = GeometricSurface),
                            border = BorderStroke(1.dp, GeometricBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 24.dp, vertical = 28.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Hello! 👋 Welcome to Abenix Instruments.",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = GeometricTextPrimary,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "I am here to assist with surgical inquiries, custom instrument manufacturing, and international quotations.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = GeometricTextSecondary
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "\"Choose Perfect, Choose Abenix.\"",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Medium,
                                    color = GeometricBlue
                                )
                            }
                        }

                        // Floating AI Assistant Pill Tag
                        Surface(
                            color = GeometricBlue,
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                        ) {
                            Text(
                                text = "AI ASSISTANT",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 10.sp,
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Geometric 2x2 Quick Action Cards
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            QuickActionCard(
                                icon = Icons.Default.MedicalServices,
                                title = "Surgical Sets",
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.sendMessage("Tell me about your Surgical instrument sets.") }
                            )
                            QuickActionCard(
                                icon = Icons.Default.Vaccines,
                                title = "Dental Tools",
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.sendMessage("What dental instruments do you offer?") }
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            QuickActionCard(
                                icon = Icons.Default.RequestQuote,
                                title = "Get Quotation",
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.sendMessage("How do I request a formal price quote for surgical instruments?") }
                            )
                            QuickActionCard(
                                icon = Icons.Default.PrecisionManufacturing,
                                title = "Custom Orders",
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.sendMessage("What are the requirements for custom OEM instrument manufacturing?") }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(messages, key = { it.id }) { message ->
                        ChatMessageItem(
                            message = message,
                            onCopy = { text ->
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Abenix Chat", text)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                            },
                            onRequestQuote = {
                                viewModel.setSelectedTab(2)
                            }
                        )
                    }
                }
            }
        }

        // Quick Category Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val chips = listOf("Orthopedic", "Vascular", "Arthroscopy", "Tungsten Carbide", "Orthodontic")
            items(chips) { category ->
                AssistChip(
                    onClick = { viewModel.sendMessage("Tell me about $category instruments.") },
                    label = { Text(category, fontSize = 12.sp, color = GeometricTextSecondary) },
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, GeometricBorder),
                    colors = AssistChipDefaults.assistChipColors(containerColor = GeometricSurface)
                )
            }
        }

        // Input Bar - Geometric Balance Styling
        Surface(
            color = GeometricSurface,
            shadowElevation = 2.dp,
            border = BorderStroke(1.dp, GeometricBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = GeometricBg,
                    shape = RoundedCornerShape(28.dp),
                    border = BorderStroke(1.dp, GeometricBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = input,
                            onValueChange = { viewModel.updateChatInput(it) },
                            placeholder = { Text("Describe an instrument...", color = GeometricTextMuted) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("chat_input_field"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                disabledBorderColor = Color.Transparent
                            ),
                            maxLines = 3
                        )

                        IconButton(
                            onClick = { viewModel.sendMessage("Can I upload a photo reference for a custom order?") },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(GeometricSurface)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Camera attachment",
                                tint = GeometricTextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        IconButton(
                            onClick = { viewModel.sendMessage() },
                            enabled = input.isNotBlank() && !isSending,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(
                                    if (input.isNotBlank() && !isSending) GeometricBlue
                                    else GeometricBlue.copy(alpha = 0.5f)
                                )
                                .testTag("send_button")
                        ) {
                            if (isSending) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Send message",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Pakistan Based Manufacturer & Exporter",
                    style = MaterialTheme.typography.labelSmall,
                    color = GeometricTextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun QuickActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = GeometricSurface),
        border = BorderStroke(1.dp, GeometricBorder),
        modifier = modifier
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(GeometricBlueLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = GeometricBlue,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = GeometricTextPrimary
            )
        }
    }
}

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    onCopy: (String) -> Unit,
    onRequestQuote: () -> Unit
) {
    val isUser = message.sender == "user"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(GeometricBlue),
                contentAlignment = Alignment.Center
            ) {
                Text("A", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) GeometricBlue else GeometricSurface
            ),
            border = if (!isUser) BorderStroke(1.dp, GeometricBorder) else null,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isUser) Color.White else GeometricTextPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onCopy(message.text) },
                        modifier = Modifier.size(26.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy message",
                            tint = if (isUser) Color.White.copy(alpha = 0.7f) else GeometricTextSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    if (!isUser && (message.text.contains("price", ignoreCase = true) || message.text.contains("quote", ignoreCase = true) || message.text.contains("quantity", ignoreCase = true))) {
                        AssistChip(
                            onClick = onRequestQuote,
                            label = { Text("Build Quote", fontSize = 10.sp, color = Color.White) },
                            shape = RoundedCornerShape(50),
                            colors = AssistChipDefaults.assistChipColors(containerColor = GeometricBlue)
                        )
                    }
                }
            }
        }
    }
}

