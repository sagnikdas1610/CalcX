package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.AngleMode
import com.example.ui.theme.CalcXPurple
import com.example.ui.theme.CalcXPurpleLight
import com.example.ui.theme.CalcXRed

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalculatorDisplay(
    expression: String,
    result: String,
    previewResult: String,
    error: String?,
    angleMode: AngleMode,
    isScientific: Boolean,
    onResultCopy: (String) -> Unit,
    onBackspace: () -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val exprScrollState = rememberScrollState()

    // Scroll to end as user types expression
    LaunchedEffect(expression) {
        exprScrollState.animateScrollTo(exprScrollState.maxValue)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        // Status Row (e.g. DEG/RAD indicator, scientific pill)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isScientific) {
                    Text(
                        text = angleMode.name,
                        style = MaterialTheme.typography.labelMedium,
                        color = CalcXPurple,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("angle_mode_tag")
                    )
                }
            }

            AnimatedVisibility(
                visible = expression.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onBackspace()
                    },
                    modifier = Modifier.testTag("btn_backspace")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Backspace,
                        contentDescription = "Backspace",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Expression Area (horizontal scrollable)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(exprScrollState, reverseScrolling = false),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = if (expression.isEmpty()) " " else expression,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                textAlign = TextAlign.End,
                modifier = Modifier.testTag("display_expression")
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Live Preview Result (faintly shown while typing)
        if (previewResult.isNotEmpty() && error == null) {
            Text(
                text = "= $previewResult",
                style = MaterialTheme.typography.titleMedium,
                color = CalcXPurpleLight.copy(alpha = 0.8f),
                textAlign = TextAlign.End,
                modifier = Modifier.testTag("display_preview")
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Main Result Area with Double-Tap and Long-Press copy interactions
        val interactionSource = remember { MutableInteractionSource() }

        Text(
            text = error ?: result,
            style = if ((error ?: result).length > 10) {
                MaterialTheme.typography.displaySmall
            } else {
                MaterialTheme.typography.displayLarge
            },
            fontWeight = FontWeight.Bold,
            color = if (error != null) CalcXRed else MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.End,
            modifier = Modifier
                .testTag("display_result")
                .combinedClickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = { /* normal tap */ },
                    onDoubleClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onResultCopy(error ?: result)
                    },
                    onLongClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onResultCopy(error ?: result)
                    }
                )
        )
    }
}
