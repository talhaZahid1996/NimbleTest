package com.nimbletest.app.util

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.nimbletest.app.navigation.NavigationHelper

fun NavController.safeNavigate(route: String) {
    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
    }
}

fun NavController.safeNavigateWithPopupTo(route: String, inclusive: Boolean = false) {
    this.navigate(route) {
        popUpTo(route) {
            this.inclusive = inclusive
        }
    }
}

fun NavOptionsBuilder.popUpToTop(navController: NavController) {
    popUpTo(navController.graph.id) {
        inclusive = true
    }
}

fun NavGraphBuilder.composable(
    navItem: NavigationHelper,
    enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
    exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
    popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = enterTransition,
    popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = exitTransition,
    content: @Composable (NavBackStackEntry) -> Unit,
) {
    composable(
        route = navItem.route,
        arguments = navItem.args,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
    ) {
        content(it)
    }
}