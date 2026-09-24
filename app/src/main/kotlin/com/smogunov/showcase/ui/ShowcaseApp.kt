package com.smogunov.showcase.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.smogunov.showcase.navigation.ShowcaseNavHost
import com.smogunov.showcase.navigation.TopLevelDestination

@Composable
fun ShowcaseApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val showBottomBar = TopLevelDestination.entries.any { currentDestination.isInHierarchy(it) }

    Scaffold(
        modifier = modifier,
        // Each screen draws its own top app bar and handles the status bar inset itself.
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (showBottomBar) {
                ShowcaseBottomBar(
                    currentDestination = currentDestination,
                    onNavigate = { navController.navigateToTopLevel(it) },
                )
            }
        },
    ) { padding ->
        ShowcaseNavHost(
            navController = navController,
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding),
        )
    }
}

@Composable
private fun ShowcaseBottomBar(
    currentDestination: NavDestination?,
    onNavigate: (TopLevelDestination) -> Unit,
) {
    NavigationBar {
        TopLevelDestination.entries.forEach { destination ->
            val label = stringResource(destination.labelRes)
            NavigationBarItem(
                selected = currentDestination.isInHierarchy(destination),
                onClick = { onNavigate(destination) },
                icon = { Icon(destination.icon, contentDescription = null) },
                label = { Text(label) },
            )
        }
    }
}

private fun NavDestination?.isInHierarchy(destination: TopLevelDestination): Boolean =
    this?.hierarchy?.any { it.hasRoute(destination.route::class) } ?: false

/** Standard bottom-navigation behaviour: single top, saved and restored back stack per tab. */
private fun NavHostController.navigateToTopLevel(destination: TopLevelDestination) {
    navigate(destination.route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
