package org.override.apollo.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.override.apollo.ui.screens.home.HomeViewModel
import org.override.apollo.ui.screens.home.screens.ai.AiViewModel
import org.override.apollo.ui.screens.home.screens.task.TaskViewModel
import org.override.apollo.ui.screens.home.screens.tools.ToolsViewModel
import org.override.apollo.ui.screens.start.StartViewModel

val viewModelModule: Module
    get() = module {
        viewModelOf(::StartViewModel)
        viewModelOf(::HomeViewModel)
        viewModelOf(::ToolsViewModel)
        viewModelOf(::AiViewModel)
        viewModelOf(::TaskViewModel)
    }