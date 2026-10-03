package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CalcXPurple
import com.example.ui.theme.DarkOutline
import kotlin.math.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphCalculatorScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onNavigateBack() }

    var selectedFunction by remember { mutableStateOf("sin(x)") }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("screen_graph_calc"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Graph Calculator",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Function selector chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("sin(x)", "cos(x)", "x²", "x³").forEach { fn ->
                    FilterChip(
                        selected = selectedFunction == fn,
                        onClick = { selectedFunction = fn },
                        label = { Text("y = $fn") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CalcXPurple.copy(alpha = 0.2f),
                            selectedLabelColor = CalcXPurple
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Canvas Plotting Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        val width = size.width
                        val height = size.height
                        val centerX = width / 2f
                        val centerY = height / 2f

                        // Draw Grid & Axes
                        val axisColor = Color.Gray.copy(alpha = 0.4f)
                        drawLine(
                            color = axisColor,
                            start = Offset(0f, centerY),
                            end = Offset(width, centerY),
                            strokeWidth = 2f
                        )
                        drawLine(
                            color = axisColor,
                            start = Offset(centerX, 0f),
                            end = Offset(centerX, height),
                            strokeWidth = 2f
                        )

                        // Plot Function Curve
                        val xMin = -10.0
                        val xMax = 10.0
                        val scaleX = width / (xMax - xMin)
                        val scaleY = height / 10.0 // 10 units vertically

                        val path = Path()
                        var firstPoint = true

                        val step = (xMax - xMin) / 300.0
                        var x = xMin
                        while (x <= xMax) {
                            val y = when (selectedFunction) {
                                "sin(x)" -> sin(x)
                                "cos(x)" -> cos(x)
                                "x²" -> (x * x) * 0.1
                                "x³" -> (x * x * x) * 0.05
                                else -> sin(x)
                            }

                            val px = (centerX + (x * scaleX)).toFloat()
                            val py = (centerY - (y * scaleY)).toFloat()

                            if (firstPoint) {
                                path.moveTo(px, py)
                                firstPoint = false
                            } else {
                                path.lineTo(px, py)
                            }

                            x += step
                        }

                        drawPath(
                            path = path,
                            color = CalcXPurple,
                            style = Stroke(width = 4f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Viewing: y = $selectedFunction on domain [-10, 10]",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
