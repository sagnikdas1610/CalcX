package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calculator.CalculatorAction

@Composable
fun CalculatorKeypad(
    onAction: (CalculatorAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // ROW 1: AC, (), %, ÷
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalculatorButton(
                text = "AC",
                onClick = { onAction(CalculatorAction.Clear) },
                onLongClick = { onAction(CalculatorAction.ClearAll) },
                buttonType = ButtonType.DANGER,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "( )",
                onClick = { onAction(CalculatorAction.Parenthesis) },
                buttonType = ButtonType.OPERATOR,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "%",
                onClick = { onAction(CalculatorAction.Percentage) },
                buttonType = ButtonType.OPERATOR,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "÷",
                onClick = { onAction(CalculatorAction.Operator("÷")) },
                buttonType = ButtonType.OPERATOR,
                modifier = Modifier.weight(1f)
            )
        }

        // ROW 2: 7, 8, 9, ×
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalculatorButton(
                text = "7",
                onClick = { onAction(CalculatorAction.Number(7)) },
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "8",
                onClick = { onAction(CalculatorAction.Number(8)) },
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "9",
                onClick = { onAction(CalculatorAction.Number(9)) },
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "×",
                onClick = { onAction(CalculatorAction.Operator("×")) },
                buttonType = ButtonType.OPERATOR,
                modifier = Modifier.weight(1f)
            )
        }

        // ROW 3: 4, 5, 6, −
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalculatorButton(
                text = "4",
                onClick = { onAction(CalculatorAction.Number(4)) },
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "5",
                onClick = { onAction(CalculatorAction.Number(5)) },
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "6",
                onClick = { onAction(CalculatorAction.Number(6)) },
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "−",
                onClick = { onAction(CalculatorAction.Operator("−")) },
                buttonType = ButtonType.OPERATOR,
                modifier = Modifier.weight(1f)
            )
        }

        // ROW 4: 1, 2, 3, +
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalculatorButton(
                text = "1",
                onClick = { onAction(CalculatorAction.Number(1)) },
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "2",
                onClick = { onAction(CalculatorAction.Number(2)) },
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "3",
                onClick = { onAction(CalculatorAction.Number(3)) },
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "+",
                onClick = { onAction(CalculatorAction.Operator("+")) },
                buttonType = ButtonType.OPERATOR,
                modifier = Modifier.weight(1f)
            )
        }

        // ROW 5: ±, 0, ., =
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalculatorButton(
                text = "±",
                onClick = { onAction(CalculatorAction.TogglePlusMinus) },
                buttonType = ButtonType.NEUTRAL,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "0",
                onClick = { onAction(CalculatorAction.Number(0)) },
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = ".",
                onClick = { onAction(CalculatorAction.Decimal) },
                buttonType = ButtonType.NEUTRAL,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "=",
                onClick = { onAction(CalculatorAction.Calculate) },
                buttonType = ButtonType.ACTION,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
