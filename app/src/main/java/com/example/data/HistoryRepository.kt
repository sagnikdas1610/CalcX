package com.example.data

import com.example.model.CalculationHistoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface HistoryRepository {
    val historyFlow: StateFlow<List<CalculationHistoryItem>>
    suspend fun addCalculation(expression: String, result: String)
    suspend fun deleteItem(id: String)
    suspend fun clearHistory()
}

class InMemoryHistoryRepository : HistoryRepository {
    private val _historyFlow = MutableStateFlow<List<CalculationHistoryItem>>(emptyList())
    override val historyFlow: StateFlow<List<CalculationHistoryItem>> = _historyFlow.asStateFlow()

    override suspend fun addCalculation(expression: String, result: String) {
        val newItem = CalculationHistoryItem(
            expression = expression,
            result = result,
            timestamp = System.currentTimeMillis()
        )
        // Keep top 100 items
        _historyFlow.value = (listOf(newItem) + _historyFlow.value).take(100)
    }

    override suspend fun deleteItem(id: String) {
        _historyFlow.value = _historyFlow.value.filter { it.id != id }
    }

    override suspend fun clearHistory() {
        _historyFlow.value = emptyList()
    }
}
