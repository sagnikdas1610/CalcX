package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.AngleMode
import com.example.calculator.CalculatorAction

@Composable
fun ScientificKeypad(
    angleMode: AngleMode,
    onAction: (CalculatorAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ROW 1: DEG/RAD, sin, cos, tan, ln
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                text = angleMode.name,
                onClick = { onAction(CalculatorAction.ToggleAngleMode) },
                buttonType = ButtonType.SCIENTIFIC,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "sin",
                onClick = { onAction(CalculatorAction.ScientificFunction("sin")) },
                buttonType = ButtonType.SCIENTIFIC,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "cos",
                onClick = { onAction(CalculatorAction.ScientificFunction("cos")) },
                buttonType = ButtonType.SCIENTIFIC,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "tan",
                onClick = { onAction(CalculatorAction.ScientificFunction("tan")) },
                buttonType = ButtonType.SCIENTIFIC,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "ln",
                onClick = { onAction(CalculatorAction.ScientificFunction("ln")) },
                buttonType = ButtonType.SCIENTIFIC,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
        }

        // ROW 2: asin, acos, atan, log, √
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                text = "sin⁻¹",
                onClick = { onAction(CalculatorAction.ScientificFunction("asin")) },
                buttonType = ButtonType.SCIENTIFIC,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "cos⁻¹",
                onClick = { onAction(CalculatorAction.ScientificFunction("acos")) },
                buttonType = ButtonType.SCIENTIFIC,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "tan⁻¹",
                onClick = { onAction(CalculatorAction.ScientificFunction("atan")) },
                buttonType = ButtonType.SCIENTIFIC,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "log",
                onClick = { onAction(CalculatorAction.ScientificFunction("log")) },
                buttonType = ButtonType.SCIENTIFIC,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "√",
                onClick = { onAction(CalculatorAction.ScientificFunction("sqrt")) },
                buttonType = ButtonType.SCIENTIFIC,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
        }

        // ROW 3: x², x³, xʸ, π, e
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                text = "x²",
                onClick = { onAction(CalculatorAction.ScientificFunction("x²")) },
                buttonType = ButtonType.SCIENTIFIC,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "x³",
                onClick = { onAction(CalculatorAction.ScientificFunction("x³")) },
                buttonType = ButtonType.SCIENTIFIC,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "xʸ",
                onClick = { onAction(CalculatorAction.ScientificFunction("xʸ")) },
                buttonType = ButtonType.SCIENTIFIC,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "π",
                onClick = { onAction(CalculatorAction.Constant("π")) },
                buttonType = ButtonType.SCIENTIFIC,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "e",
                onClick = { onAction(CalculatorAction.Constant("e")) },
                buttonType = ButtonType.SCIENTIFIC,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
        }

        // ROW 4: x!, 1/x
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                text = "x!",
                onClick = { onAction(CalculatorAction.ScientificFunction("x!")) },
                buttonType = ButtonType.SCIENTIFIC,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "1/x",
                onClick = { onAction(CalculatorAction.ScientificFunction("1/x")) },
                buttonType = ButtonType.SCIENTIFIC,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
