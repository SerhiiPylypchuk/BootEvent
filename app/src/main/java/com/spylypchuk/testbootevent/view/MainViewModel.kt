package com.spylypchuk.testbootevent.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spylypchuk.testbootevent.data.BootEventRepository
import com.spylypchuk.testbootevent.view.state.MainState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    val repo: BootEventRepository
): ViewModel() {

    val _state = MutableStateFlow<MainState>(MainState.NoData)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.getAllBootEvents()
                    .collect { list ->
                        if (list.isNotEmpty())
                            _state.value = MainState.HasData(list)
                        else
                            _state.value = MainState.NoData
                    }
            }
        }
    }
}