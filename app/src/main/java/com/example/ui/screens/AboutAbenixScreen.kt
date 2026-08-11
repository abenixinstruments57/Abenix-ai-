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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GeometricBg
import com.example.ui.theme.GeometricBlue
import com.example.ui.theme.GeometricBlueLight
import com.example.ui.theme.GeometricBorder
import com.example.ui.theme.GeometricSurface
import com.example.ui.theme.GeometricTextMuted
import com.example.ui.theme.GeometricTextPrimary
import com.example.ui.theme.GeometricTextSecondary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun AboutAbenixScreen(viewModel: MainViewModel) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GeometricBg)
            .verticalScroll(scrollState)
    ) {
        // Hero Card Header
        Surface(
            color = GeometricSurface,
            shadowElevation = 1.dp,
            border = BorderStroke(1.dp, GeometricBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(GeometricBlueLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = null,
                        tint = GeometricBlue,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Abenix Instruments",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = GeometricTextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Manufacturer & Exporter of Surgical & Medical Instruments",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GeometricTextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    color = GeometricBlue,
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Choose Perfect, Choose Abenix.",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // Manufacturing Hub Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = GeometricSurface),
                border = BorderStroke(1.dp, GeometricBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Public, contentDescription = null, tint = GeometricBlue)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Sialkot, Pakistan Manufacturing Heritage",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = GeometricTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Abenix Instruments operates out of Pakistan's world-renowned surgical instrument manufacturing hub. We combine generational craftsman technique with modern CNC & laser technology to fabricate high-precision surgical, dental, orthopedic, and TC instruments.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GeometricTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Website Status Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = GeometricBlueLight),
                border = BorderStroke(1.dp, GeometricBlue.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Language, contentDescription = null, tint = GeometricBlue)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Official Website Status",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = GeometricBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "The Abenix Instruments official web portal is currently under active development. Meanwhile, customers, distributors, and surgical centers can use this AI Assistant app to inquire, prepare quotations, and connect with our sales team.",
                        style = MaterialTheme.typography.bodySmall,
                        color = GeometricTextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Product Specialties List
            Text(
                text = "Export Capabilities & Standards",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GeometricTextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            val capabilities = listOf(
                "Surgical, Dental, Orthopedic & Vascular Instrument Production",
                "Tungsten Carbide (TC) Precision Tipped Tools with Gold Finish",
                "Complete Surgical Set Assembly & Custom Tray Packing",
                "OEM Manufacturing according to Drawing/Sample Specifications",
                "Custom Laser Engraving with Ref Codes & Private Brand Logos",
                "Strict Passivation & Autoclave Quality Inspection"
            )

            capabilities.forEach { cap ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = GeometricBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = cap,
                        style = MaterialTheme.typography.bodyMedium,
                        color = GeometricTextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Direct Export Team Connection Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = GeometricSurface),
                border = BorderStroke(1.dp, GeometricBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Connect with Abenix Export Team",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GeometricTextPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Our human sales team responds promptly to all formal price quote requests, custom OEM inquiries, and distributorship proposals.",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = GeometricTextSecondary
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            val contactInfo = "Abenix Instruments Pakistan\nOfficial Sales & Export Inquiry\nBrand Motto: Choose Perfect, Choose Abenix."
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Abenix Contact", contactInfo))
                            Toast.makeText(context, "Contact details copied", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GeometricBlue),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Copy Company Contact Info", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

