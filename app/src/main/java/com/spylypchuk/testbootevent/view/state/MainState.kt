package com.spylypchuk.testbootevent.view.state

import com.spylypchuk.testbootevent.model.BootEvent

sealed interface MainState {
    data object NoData : MainState
    data class HasData(val data: List<BootEvent>) : MainState
}