package com.nimbletest.app.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.nimbletest.app.util.NavigationDestinations

/**
 *
 * Routes helper class to handle navigation routes, destinations and arguments
 *
 * @property baseRoute the base route of the destination
 * @property route the full route of the destination
 * @property args the list of arguments for the destination
 *
 * */
sealed class NavigationHelper(
    private val baseRoute: String,
    private val navArgs: List<NavArgs> = emptyList(),
) {
    val route = run {
        val argKeys = navArgs.map { "{${it.key}}" }
        listOf(baseRoute).plus(argKeys).joinToString("/")
    }

    val args = navArgs.map {
        navArgument(name = it.key) {
            this.type = it.navType
            this.defaultValue = it.defaultValue
            this.nullable = it.nullable
        }
    }

    //** here you can add your destination objects **//
    // auth
    data object LoginScreen : NavigationHelper(NavigationDestinations.LOGIN_SCREEN)
    data object ForgotPasswordScreen :
        NavigationHelper(NavigationDestinations.FORGOT_PASSWORD_SCREEN)

    // home
    data object HomeScreen : NavigationHelper(NavigationDestinations.HOME_SCREEN)
    data object SurveyDetailsScreen : NavigationHelper(NavigationDestinations.SURVEY_DETAILS_SCREEN)
}

enum class NavArgs(
    val key: String,
    val navType: NavType<*>,
    val nullable: Boolean = false,
    val defaultValue: Any? = null,
) {
    //** here you can add your navArgs with the key, type and default value **//
}
