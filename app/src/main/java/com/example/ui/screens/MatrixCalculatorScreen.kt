package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.calculator.CalculatorEngine
import com.example.matrix.MatrixEngine
import com.example.ui.theme.CalcXCyan
import com.example.ui.theme.CalcXPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatrixCalculatorScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onNavigateBack() }

    var size by remember { mutableStateOf(2) } // 2 or 3

    // 3x3 grid state strings
    var m00 by remember { mutableStateOf("1") }
    var m01 by remember { mutableStateOf("2") }
    var m02 by remember { mutableStateOf("3") }

    var m10 by remember { mutableStateOf("0") }
    var m11 by remember { mutableStateOf("1") }
    var m12 by remember { mutableStateOf("4") }

    var m20 by remember { mutableStateOf("5") }
    var m21 by remember { mutableStateOf("6") }
    var m22 by remember { mutableStateOf("0") }

    var operationResult by remember { mutableStateOf<String?>(null) }
    var resultMatrix by remember { mutableStateOf<List<List<Double>>?>(null) }

    fun currentMatrix(): List<List<Double>> {
        return if (size == 2) {
            listOf(
                listOf(m00.toDoubleOrNull() ?: 0.0, m01.toDoubleOrNull() ?: 0.0),
                listOf(m10.toDoubleOrNull() ?: 0.0, m11.toDoubleOrNull() ?: 0.0)
            )
        } else {
            listOf(
                listOf(m00.toDoubleOrNull() ?: 0.0, m01.toDoubleOrNull() ?: 0.0, m02.toDoubleOrNull() ?: 0.0),
                listOf(m10.toDoubleOrNull() ?: 0.0, m11.toDoubleOrNull() ?: 0.0, m12.toDoubleOrNull() ?: 0.0),
                listOf(m20.toDoubleOrNull() ?: 0.0, m21.toDoubleOrNull() ?: 0.0, m22.toDoubleOrNull() ?: 0.0)
            )
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("screen_matrix_calc"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Matrix Calculator",
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
            // Size Selection (2x2 vs 3x3)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilterChip(
                    selected = size == 2,
                    onClick = {
                        size = 2
                        operationResult = null
                        resultMatrix = null
                    },
                    label = { Text("2 × 2 Matrix") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CalcXPurple.copy(alpha = 0.2f),
                        selectedLabelColor = CalcXPurple
                    )
                )
                FilterChip(
                    selected = size == 3,
                    onClick = {
                        size = 3
                        operationResult = null
                        resultMatrix = null
                    },
                    label = { Text("3 × 3 Matrix") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CalcXPurple.copy(alpha = 0.2f),
                        selectedLabelColor = CalcXPurple
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Matrix Input Grid Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Matrix A (${size}×${size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CalcXCyan
                    )

                    // Row 0
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = m00,
                            onValueChange = { m00 = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = m01,
                            onValueChange = { m01 = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        if (size == 3) {
                            OutlinedTextField(
                                value = m02,
                                onValueChange = { m02 = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }
                    }

                    // Row 1
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = m10,
                            onValueChange = { m10 = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = m11,
                            onValueChange = { m11 = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        if (size == 3) {
                            OutlinedTextField(
                                value = m12,
                                onValueChange = { m12 = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }
                    }

                    // Row 2 (if 3x3)
                    if (size == 3) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = m20,
                                onValueChange = { m20 = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = m21,
                                onValueChange = { m21 = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = m22,
                                onValueChange = { m22 = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons: Determinant, Inverse, Transpose
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        val mat = currentMatrix()
                        val det = if (size == 2) MatrixEngine.determinant2x2(mat) else MatrixEngine.determinant3x3(mat)
                        operationResult = "Determinant (det A) = ${CalculatorEngine.formatResult(det)}"
                        resultMatrix = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CalcXPurple),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("det(A)")
                }

                Button(
                    onClick = {
                        val mat = currentMatrix()
                        val inv = if (size == 2) MatrixEngine.inverse2x2(mat) else MatrixEngine.inverse3x3(mat)
                        if (inv != null) {
                            operationResult = "Inverse Matrix (A⁻¹):"
                            resultMatrix = inv
                        } else {
                            operationResult = "Matrix is singular (det = 0). No inverse exists."
                            resultMatrix = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CalcXPurple),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("A⁻¹")
                }

                Button(
                    onClick = {
                        val mat = currentMatrix()
                        val trans = MatrixEngine.transpose(mat)
                        operationResult = "Transpose Matrix (Aᵀ):"
                        resultMatrix = trans
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CalcXPurple),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Aᵀ")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Result Display Card
            if (operationResult != null) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = operationResult ?: "",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CalcXCyan
                        )

                        if (resultMatrix != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            resultMatrix?.forEach { row ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    row.forEach { cell ->
                                        Text(
                                            text = CalculatorEngine.formatResult(cell),
                                            style = MaterialTheme.typography.headlineSmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
