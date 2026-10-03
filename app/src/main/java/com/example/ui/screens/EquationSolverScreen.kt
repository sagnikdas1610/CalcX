package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.sp
import com.example.equation.EquationSolver
import com.example.equation.LinearSolution
import com.example.equation.QuadraticSolution
import com.example.ui.theme.CalcXPink
import com.example.ui.theme.CalcXPurple

enum class EquationMode {
    LINEAR, QUADRATIC
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquationSolverScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onNavigateBack() }

    var mode by remember { mutableStateOf(EquationMode.LINEAR) }

    // Linear: ax + b = c
    var linearA by remember { mutableStateOf("2") }
    var linearB by remember { mutableStateOf("5") }
    var linearC by remember { mutableStateOf("15") }

    // Quadratic: ax² + bx + c = 0
    var quadA by remember { mutableStateOf("1") }
    var quadB by remember { mutableStateOf("-5") }
    var quadC by remember { mutableStateOf("6") }

    var stepsExpanded by remember { mutableStateOf(true) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("screen_equation_solver"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Equation Solver",
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
            // Mode Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilterChip(
                    selected = mode == EquationMode.LINEAR,
                    onClick = { mode = EquationMode.LINEAR },
                    label = { Text("Linear (ax + b = c)") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CalcXPurple.copy(alpha = 0.2f),
                        selectedLabelColor = CalcXPurple
                    )
                )
                FilterChip(
                    selected = mode == EquationMode.QUADRATIC,
                    onClick = { mode = EquationMode.QUADRATIC },
                    label = { Text("Quadratic (ax² + bx + c = 0)") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CalcXPurple.copy(alpha = 0.2f),
                        selectedLabelColor = CalcXPurple
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (mode == EquationMode.LINEAR) {
                // Linear Input Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Form: ax + b = c",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CalcXPurple
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = linearA,
                                onValueChange = { linearA = it },
                                label = { Text("a") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f).testTag("linear_input_a"),
                                singleLine = true
                            )
                            Text("x +", fontWeight = FontWeight.Bold)
                            OutlinedTextField(
                                value = linearB,
                                onValueChange = { linearB = it },
                                label = { Text("b") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f).testTag("linear_input_b"),
                                singleLine = true
                            )
                            Text("=", fontWeight = FontWeight.Bold)
                            OutlinedTextField(
                                value = linearC,
                                onValueChange = { linearC = it },
                                label = { Text("c") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f).testTag("linear_input_c"),
                                singleLine = true
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Solve Linear
                val a = linearA.toDoubleOrNull() ?: 0.0
                val b = linearB.toDoubleOrNull() ?: 0.0
                val c = linearC.toDoubleOrNull() ?: 0.0
                val solution = EquationSolver.solveLinear(a, b, c)

                SolutionDisplayCard(
                    title = "Linear Solution",
                    resultText = when (solution) {
                        is LinearSolution.Single -> "x = ${solution.formattedX}"
                        is LinearSolution.Infinite -> solution.message
                        is LinearSolution.NoSolution -> solution.message
                        is LinearSolution.Error -> solution.message
                    },
                    steps = when (solution) {
                        is LinearSolution.Single -> solution.steps
                        is LinearSolution.Infinite -> solution.steps
                        is LinearSolution.NoSolution -> solution.steps
                        is LinearSolution.Error -> emptyList()
                    },
                    stepsExpanded = stepsExpanded,
                    onToggleSteps = { stepsExpanded = !stepsExpanded }
                )

            } else {
                // Quadratic Input Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Form: ax² + bx + c = 0",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CalcXPink
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = quadA,
                                onValueChange = { quadA = it },
                                label = { Text("a") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f).testTag("quad_input_a"),
                                singleLine = true
                            )
                            Text("x² +", fontWeight = FontWeight.Bold)
                            OutlinedTextField(
                                value = quadB,
                                onValueChange = { quadB = it },
                                label = { Text("b") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f).testTag("quad_input_b"),
                                singleLine = true
                            )
                            Text("x +", fontWeight = FontWeight.Bold)
                            OutlinedTextField(
                                value = quadC,
                                onValueChange = { quadC = it },
                                label = { Text("c") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f).testTag("quad_input_c"),
                                singleLine = true
                            )
                            Text("= 0", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Solve Quadratic
                val a = quadA.toDoubleOrNull() ?: 1.0
                val b = quadB.toDoubleOrNull() ?: 0.0
                val c = quadC.toDoubleOrNull() ?: 0.0
                val solution = EquationSolver.solveQuadratic(a, b, c)

                val resultText = when (solution) {
                    is QuadraticSolution.TwoRealRoots -> "x₁ = ${solution.formattedX1}\nx₂ = ${solution.formattedX2}"
                    is QuadraticSolution.OneRealRoot -> "x = ${solution.formattedX}"
                    is QuadraticSolution.ComplexRoots -> "x₁ = ${solution.formattedX1}\nx₂ = ${solution.formattedX2}"
                    is QuadraticSolution.DegenerateLinear -> when (val lin = solution.linear) {
                        is LinearSolution.Single -> "x = ${lin.formattedX}"
                        else -> "Degenerate linear equation"
                    }
                    is QuadraticSolution.Error -> solution.message
                }

                val steps = when (solution) {
                    is QuadraticSolution.TwoRealRoots -> solution.steps
                    is QuadraticSolution.OneRealRoot -> solution.steps
                    is QuadraticSolution.ComplexRoots -> solution.steps
                    is QuadraticSolution.DegenerateLinear -> solution.linear.let {
                        if (it is LinearSolution.Single) it.steps else emptyList()
                    }
                    is QuadraticSolution.Error -> emptyList()
                }

                SolutionDisplayCard(
                    title = "Quadratic Roots",
                    resultText = resultText,
                    steps = steps,
                    stepsExpanded = stepsExpanded,
                    onToggleSteps = { stepsExpanded = !stepsExpanded }
                )
            }
        }
    }
}

@Composable
private fun SolutionDisplayCard(
    title: String,
    resultText: String,
    steps: List<String>,
    stepsExpanded: Boolean,
    onToggleSteps: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = resultText,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = CalcXPurple,
                modifier = Modifier.testTag("equation_solution_text")
            )

            if (steps.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onToggleSteps)
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Step-by-step solution",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        imageVector = if (stepsExpanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                        contentDescription = "Toggle steps"
                    )
                }

                AnimatedVisibility(visible = stepsExpanded) {
                    Column(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        steps.forEachIndexed { index, step ->
                            Row(verticalAlignment = Alignment.Top) {
                                Text(
                                    text = "${index + 1}. ",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = CalcXPurple
                                )
                                Text(
                                    text = step,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
