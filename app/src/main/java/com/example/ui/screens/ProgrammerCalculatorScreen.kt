package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.programmer.NumberBase
import com.example.programmer.ProgrammerEngine
import com.example.ui.theme.CalcXOrange
import com.example.ui.theme.CalcXPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgrammerCalculatorScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onNavigateBack() }

    var selectedBase by remember { mutableStateOf(NumberBase.DEC) }
    var inputString by remember { mutableStateOf("255") }

    val currentLongValue by remember(inputString, selectedBase) {
        derivedStateOf {
            ProgrammerEngine.parseInput(inputString, selectedBase) ?: 0L
        }
    }

    val values by remember(currentLongValue) {
        derivedStateOf {
            ProgrammerEngine.getValues(currentLongValue)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("screen_programmer_calc"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Programmer Calculator",
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Input field with base selector
            OutlinedTextField(
                value = inputString,
                onValueChange = { inputString = it },
                label = { Text("Input (${selectedBase.name})") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (selectedBase == NumberBase.HEX) KeyboardType.Text else KeyboardType.Number
                ),
                textStyle = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("programmer_input")
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Radix Comparison Cards
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RadixRow(
                        label = "HEX",
                        value = values.hex,
                        isSelected = selectedBase == NumberBase.HEX,
                        onSelect = {
                            selectedBase = NumberBase.HEX
                            inputString = values.hex
                        }
                    )
                    RadixRow(
                        label = "DEC",
                        value = values.dec,
                        isSelected = selectedBase == NumberBase.DEC,
                        onSelect = {
                            selectedBase = NumberBase.DEC
                            inputString = values.dec
                        }
                    )
                    RadixRow(
                        label = "OCT",
                        value = values.oct,
                        isSelected = selectedBase == NumberBase.OCT,
                        onSelect = {
                            selectedBase = NumberBase.OCT
                            inputString = values.oct
                        }
                    )
                    RadixRow(
                        label = "BIN",
                        value = values.bin,
                        isSelected = selectedBase == NumberBase.BIN,
                        onSelect = {
                            selectedBase = NumberBase.BIN
                            inputString = values.bin
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bitwise Logic Operations
            Text(
                text = "Bitwise Operations",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CalcXOrange
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        val res = ProgrammerEngine.bitwiseNot(currentLongValue)
                        inputString = when (selectedBase) {
                            NumberBase.HEX -> java.lang.Long.toHexString(res).uppercase()
                            NumberBase.OCT -> java.lang.Long.toOctalString(res)
                            NumberBase.BIN -> java.lang.Long.toBinaryString(res)
                            NumberBase.DEC -> res.toString()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CalcXPurple),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("NOT (~)")
                }

                Button(
                    onClick = {
                        val res = ProgrammerEngine.shiftLeft(currentLongValue, 1)
                        inputString = when (selectedBase) {
                            NumberBase.HEX -> java.lang.Long.toHexString(res).uppercase()
                            NumberBase.OCT -> java.lang.Long.toOctalString(res)
                            NumberBase.BIN -> java.lang.Long.toBinaryString(res)
                            NumberBase.DEC -> res.toString()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CalcXPurple),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("<< 1")
                }

                Button(
                    onClick = {
                        val res = ProgrammerEngine.shiftRight(currentLongValue, 1)
                        inputString = when (selectedBase) {
                            NumberBase.HEX -> java.lang.Long.toHexString(res).uppercase()
                            NumberBase.OCT -> java.lang.Long.toOctalString(res)
                            NumberBase.BIN -> java.lang.Long.toBinaryString(res)
                            NumberBase.DEC -> res.toString()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CalcXPurple),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(">> 1")
                }
            }
        }
    }
}

@Composable
private fun RadixRow(
    label: String,
    value: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) CalcXOrange else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value.ifEmpty { "0" },
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = FontFamily.Monospace,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
