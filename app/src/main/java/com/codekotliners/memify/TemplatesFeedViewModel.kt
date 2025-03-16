package com.codekotliners.memify

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class TemplatesFeedViewModel : ViewModel() {
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab : StateFlow<Int> = _selectedTab.asStateFlow()

    private val _templates = MutableStateFlow(mutableListOf(
        R.drawable.placeholder400x400,
        R.drawable.placeholder600x400,
        R.drawable.placeholder600x400,
        R.drawable.placeholder600x400,
        R.drawable.placeholder400x400,
        R.drawable.placeholder400x400,
        R.drawable.placeholder400x400,
        R.drawable.placeholder600x400,)
    )

    val bestTemplates : StateFlow<List<Int>> = _templates.asStateFlow()
    val newTemplates : StateFlow<List<Int>> = _templates.asStateFlow()
    val favouriteTemplates : StateFlow<List<Int>> = _templates.asStateFlow()

    fun selectTab(index: Int){
        _selectedTab.update { index }
    }


}