package com.codekotliners.memify.features.templates.presentation.state

data class TemplatesPageState(
    val selectedTab: Tab,
    val bestTemplatesState: TabState = TabState.Idle,
    val favouriteTemplatesState: TabState = TabState.Idle,
    val newTemplatesState: TabState = TabState.Idle,
) {
    fun getTabs(): List<Tab> = Tab.entries.toList()

    fun getCurrentState(): TabState =
        when (selectedTab) {
            Tab.BEST -> bestTemplatesState
            Tab.NEW -> newTemplatesState
            Tab.FAVOURITE -> favouriteTemplatesState
        }

    fun updatedCurrentTabState(newState: TabState): TemplatesPageState =
        when (selectedTab) {
            Tab.BEST -> copy(bestTemplatesState = newState)
            Tab.NEW -> copy(newTemplatesState = newState)
            Tab.FAVOURITE -> copy(favouriteTemplatesState = newState)
        }
}
