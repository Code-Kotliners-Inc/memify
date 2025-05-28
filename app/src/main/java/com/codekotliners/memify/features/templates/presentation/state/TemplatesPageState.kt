package com.codekotliners.memify.features.templates.presentation.state

import com.codekotliners.memify.core.models.Template

data class TemplatesPageState(
    val refreshing: Boolean,
    val selectedTab: Tab,
    val bestTemplatesState: TabState = TabState.None,
    val favouriteTemplatesState: TabState = TabState.None,
    val newTemplatesState: TabState = TabState.None,
    val vkTemplatesState: TabState = TabState.None,
) {
    fun getTabs(): List<Tab> = Tab.entries.toList()

    fun getCurrentTabState(): TabState =
        when (selectedTab) {
            Tab.BEST -> bestTemplatesState
            Tab.NEW -> newTemplatesState
            Tab.FAVOURITE -> favouriteTemplatesState
            Tab.VK_IMAGES -> vkTemplatesState
        }

    fun getIsLoadingMoreByState(state: TabState): Boolean =
        when (state) {
            is TabState.Content -> state.isLoadingMore
            else -> false
        }

    fun getReachedEndByState(state: TabState): Boolean =
        when (state) {
            is TabState.Content -> state.reachedEnd
            else -> false
        }

    fun getTemplatesByState(state: TabState): List<Template> {
        return when (state) {
            is TabState.Content -> {
                return state.templates
            }
            else -> emptyList<Template>()
        }
    }

    fun getTemplatesOfSelectedState(): List<Template> {
        val state = getCurrentTabState()
        return getTemplatesByState(state)
    }

    fun updatedCurrentTabState(newState: TabState): TemplatesPageState =
        when (selectedTab) {
            Tab.BEST -> copy(bestTemplatesState = newState)
            Tab.NEW -> copy(newTemplatesState = newState)
            Tab.FAVOURITE -> copy(favouriteTemplatesState = newState)
            Tab.VK_IMAGES -> copy(vkTemplatesState = newState)
        }

    fun updatedCurrentContent(
        newTemplates: List<Template>,
        loadMode: Boolean? = null,
    ): TemplatesPageState {
        val updatedContent = { currentState: TabState ->
            val currentTemplates = getTemplatesByState(currentState)
            TabState.Content(
                currentTemplates + (newTemplates - currentTemplates),
                loadMode ?: getIsLoadingMoreByState(currentState),
                false,
            )
        }

        return updatedCurrentTabState(
            updatedContent(getCurrentTabState()),
        )
    }
}
