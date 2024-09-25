package com.example.connections.category

import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connections.api.ApiServiceImpl
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// VER COMMITS DE LA CLASE 6 (1/9/2024)
@HiltViewModel
class CategoryViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiServiceImpl: ApiServiceImpl,
): ViewModel() {
    private var _loadingCategories = MutableStateFlow(false);
    val loadingCategories = _loadingCategories.asStateFlow()

    private var _categories = MutableStateFlow(listOf<CategoryModel>())
    val categories = _categories.asStateFlow()

    private val _showRetry = MutableStateFlow(false)
    val showRetry = _showRetry.asStateFlow()

    init{
        loadCategories();
    }

    fun retryLoadCategories() {
        loadCategories()
    }

    private fun loadCategories() {
        _loadingCategories.value = true
        apiServiceImpl.getWords(
            context = context,
            onSuccess = {
                viewModelScope.launch {
                    _categories.emit(it)
                }
                _showRetry.value = false
            },
            onFail = {
                _showRetry.value = true
            },
            loadingFinished = {
                _loadingCategories.value = false
            }
        )
    }
}