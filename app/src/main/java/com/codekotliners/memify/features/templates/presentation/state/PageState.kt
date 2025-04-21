package com.codekotliners.memify.features.templates.presentation.state

data class TemplatesPageState(
    val selectedTab: Tab = Tab.BEST,
    val bestTemplatesState: TabState = TabState.Loading,
    val favouriteTemplatesState: TabState = TabState.Loading,
    val newTemplatesState: TabState = TabState.Loading,
) {
    fun getTabs(): List<Tab> = listOf(Tab.BEST, Tab.NEW, Tab.FAVOURITE)

    fun getCurrentState(): TabState =
        when (selectedTab) {
            Tab.BEST -> bestTemplatesState
            Tab.NEW -> newTemplatesState
            Tab.FAVOURITE -> favouriteTemplatesState
        }
}
