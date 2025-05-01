package com.codekotliners.memify.features.templates.presentation.state

import com.codekotliners.memify.core.models.Template

data class TemplatesPageState(
    val selectedTab: Tab,
    val bestTemplatesState: TabState = TabState.None,
    val favouriteTemplatesState: TabState = TabState.None,
    val newTemplatesState: TabState = TabState.None,
) {
    fun getTabs(): List<Tab> = Tab.entries.toList()

    fun getCurrentState(): TabState =
        when (selectedTab) {
            Tab.BEST -> bestTemplatesState
            Tab.NEW -> newTemplatesState
            Tab.FAVOURITE -> favouriteTemplatesState
        }

    fun currentContent(state: TabState): List<Template> {
        return when (state) {
            is TabState.None -> emptyList<Template>()
            is TabState.Loading -> emptyList<Template>()
            is TabState.Error -> emptyList<Template>()
            is TabState.Content -> {
                return state.templates
            }
            is TabState.Empty -> emptyList<Template>()
        }
    }

    fun updatedCurrentTabState(newState: TabState): TemplatesPageState =
        when (selectedTab) {
            Tab.BEST -> copy(bestTemplatesState = newState)
            Tab.NEW -> copy(newTemplatesState = newState)
            Tab.FAVOURITE -> copy(favouriteTemplatesState = newState)
        }

    fun updatedCurrentContent(newTemplate: Template): TemplatesPageState {
        val updatedContent = { currentState: TabState -> TabState.Content(currentContent(currentState) + newTemplate) }
        return when (selectedTab) {
            Tab.BEST -> copy(bestTemplatesState = updatedContent(bestTemplatesState))
            Tab.NEW -> copy(newTemplatesState = updatedContent(newTemplatesState))
            Tab.FAVOURITE -> copy(favouriteTemplatesState = updatedContent(favouriteTemplatesState))
        }
    }
}
