package com.example.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CalcXPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalcXTopBar(
    onOpenHistory: () -> Unit,
    onToggleScientific: () -> Unit,
    onOpenModules: () -> Unit,
    onOpenSettings: () -> Unit,
    isScientificActive: Boolean = false,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = buildAnnotatedString {
                        append("Calc")
                        withStyle(style = SpanStyle(color = CalcXPurple, fontWeight = FontWeight.ExtraBold)) {
                            append("X")
                        }
                    },
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.testTag("app_branding_title")
                )
            }
        },
        actions = {
            IconButton(
                onClick = onToggleScientific,
                modifier = Modifier.testTag("topbar_btn_scientific")
            ) {
                Icon(
                    imageVector = Icons.Outlined.Science,
                    contentDescription = "Toggle Scientific Keypad",
                    tint = if (isScientificActive) CalcXPurple else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick = onOpenHistory,
                modifier = Modifier.testTag("topbar_btn_history")
            ) {
                Icon(
                    imageVector = Icons.Outlined.History,
                    contentDescription = "History",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick = onOpenModules,
                modifier = Modifier.testTag("topbar_btn_modules")
            ) {
                Icon(
                    imageVector = Icons.Outlined.Apps,
                    contentDescription = "All Tools",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick = onOpenSettings,
                modifier = Modifier.testTag("topbar_btn_settings")
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier
    )
}
