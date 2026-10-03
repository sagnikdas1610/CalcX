package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

enum class ButtonType {
    NEUTRAL,
    OPERATOR,
    ACTION,
    DANGER,
    SCIENTIFIC
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
    buttonType: ButtonType = ButtonType.NEUTRAL,
    fontSize: TextUnit = 24.sp,
    testTag: String = "btn_$text",
    contentDescriptionText: String = text
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 600f),
        label = "btnScale"
    )

    val isDark = MaterialTheme.colorScheme.background == DarkBackground

    val backgroundColor: Color = when (buttonType) {
        ButtonType.NEUTRAL -> if (isDark) DarkKeyNeutral else LightKeyNeutral
        ButtonType.OPERATOR -> if (isDark) DarkKeyOperator else LightKeyOperator
        ButtonType.ACTION -> CalcXPurple
        ButtonType.DANGER -> if (isDark) DarkKeyDanger else LightKeyDanger
        ButtonType.SCIENTIFIC -> if (isDark) DarkSurfaceElevated else LightSurfaceVariant
    }

    val contentColor: Color = when (buttonType) {
        ButtonType.NEUTRAL -> MaterialTheme.colorScheme.onSurface
        ButtonType.OPERATOR -> CalcXPurpleLight
        ButtonType.ACTION -> Color.White
        ButtonType.DANGER -> if (isDark) DarkKeyDangerText else LightKeyDangerText
        ButtonType.SCIENTIFIC -> if (isDark) CalcXPurpleLight else CalcXPurpleDark
    }

    Box(
        modifier = modifier
            .scale(scale)
            .defaultMinSize(minWidth = 56.dp, minHeight = 56.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(backgroundColor)
            .combinedClickable(
                interactionSource = interactionSource,
                indication = androidx.compose.material3.ripple(),
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                },
                onLongClick = onLongClick?.let {
                    {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        it()
                    }
                }
            )
            .testTag(testTag)
            .semantics { contentDescription = contentDescriptionText },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = fontSize,
            fontWeight = when (buttonType) {
                ButtonType.ACTION -> FontWeight.Bold
                ButtonType.OPERATOR -> FontWeight.SemiBold
                else -> FontWeight.Medium
            }
        )
    }
}
