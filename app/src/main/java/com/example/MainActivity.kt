package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.screens.AboutAbenixScreen
import com.example.ui.screens.AssistantScreen
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.QuoteBuilderScreen
import com.example.ui.screens.SavedInquiriesScreen
import com.example.ui.theme.AbenixTheme
import com.example.ui.theme.GeometricBlue
import com.example.ui.theme.GeometricBlueLight
import com.example.ui.theme.GeometricSurface
import com.example.ui.theme.GeometricTextPrimary
import com.example.ui.theme.GeometricTextSecondary
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AbenixTheme {
                val selectedTab by viewModel.selectedTab.collectAsState()

                Scaffold(
                    bottomBar = {
                        AbenixBottomBar(
                            selectedTab = selectedTab,
                            onTabSelected = { viewModel.setSelectedTab(it) }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (selectedTab) {
                            0 -> AssistantScreen(viewModel = viewModel)
                            1 -> CatalogScreen(viewModel = viewModel)
                            2 -> QuoteBuilderScreen(viewModel = viewModel)
                            3 -> SavedInquiriesScreen(viewModel = viewModel)
                            4 -> AboutAbenixScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AbenixBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = GeometricSurface,
        contentColor = GeometricTextPrimary,
        tonalElevation = 2.dp
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = { Icon(Icons.Default.SmartToy, contentDescription = "AI Assistant") },
            label = { Text("Assistant", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GeometricBlue,
                selectedTextColor = GeometricBlue,
                indicatorColor = GeometricBlueLight,
                unselectedIconColor = GeometricTextSecondary,
                unselectedTextColor = GeometricTextSecondary
            ),
            modifier = Modifier.testTag("nav_tab_assistant")
        )

        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = { Icon(Icons.Default.MedicalServices, contentDescription = "Catalog") },
            label = { Text("Catalog", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GeometricBlue,
                selectedTextColor = GeometricBlue,
                indicatorColor = GeometricBlueLight,
                unselectedIconColor = GeometricTextSecondary,
                unselectedTextColor = GeometricTextSecondary
            ),
            modifier = Modifier.testTag("nav_tab_catalog")
        )

        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            icon = { Icon(Icons.Default.RequestQuote, contentDescription = "Request Quote") },
            label = { Text("Quote", fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GeometricBlue,
                selectedTextColor = GeometricBlue,
                indicatorColor = GeometricBlueLight,
                unselectedIconColor = GeometricTextSecondary,
                unselectedTextColor = GeometricTextSecondary
            ),
            modifier = Modifier.testTag("nav_tab_quote")
        )

        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            icon = { Icon(Icons.Default.FolderOpen, contentDescription = "Saved") },
            label = { Text("Saved", fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GeometricBlue,
                selectedTextColor = GeometricBlue,
                indicatorColor = GeometricBlueLight,
                unselectedIconColor = GeometricTextSecondary,
                unselectedTextColor = GeometricTextSecondary
            ),
            modifier = Modifier.testTag("nav_tab_saved")
        )

        NavigationBarItem(
            selected = selectedTab == 4,
            onClick = { onTabSelected(4) },
            icon = { Icon(Icons.Default.Info, contentDescription = "About Abenix") },
            label = { Text("About", fontWeight = if (selectedTab == 4) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GeometricBlue,
                selectedTextColor = GeometricBlue,
                indicatorColor = GeometricBlueLight,
                unselectedIconColor = GeometricTextSecondary,
                unselectedTextColor = GeometricTextSecondary
            ),
            modifier = Modifier.testTag("nav_tab_about")
        )
    }
}
