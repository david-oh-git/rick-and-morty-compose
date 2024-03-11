/*
 * MIT License
 *
 * Copyright (c) 2024   David Osemwota.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.davidosemwota.rickandmorty.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.davidosemwota.rickandmorty.R
import io.davidosemwota.rickandmorty.navigation.MainScreenDestinations
import io.davidosemwota.rickandmorty.navigation.RickAndMortyNavHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RickAndMortyApp(
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            RickAndMortyBottomNavigation(
                navController = navController,
                destinations = MainScreenDestinations.entries,
            )
        },
    ) { padding ->
        RickAndMortyNavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
        )
    }
}

@Composable
private fun RickAndMortyBottomNavigation(
    navController: NavHostController,
    destinations: List<MainScreenDestinations>,
) {
    NavigationBar(
        modifier = Modifier
            .padding(8.dp)
            .clip(shape = RoundedCornerShape(8.dp)),
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        destinations.forEach { destination ->
            val selected = currentDestination?.hierarchy?.any { it.route?.contains(destination.name, true) ?: false } ?: false
            val icon = if (selected) destination.selectedIcon else destination.unSelectedIcon
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route?.contains(destination.name, true) ?: false } ?: false,
                onClick = {
                    navController.navigate(destination.name) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(R.string.main_screen_bottom_nav_unselected_icon_desc),
                    )
                },
                label = { Text(text = stringResource(id = destination.iconTitleId)) },

            )
        }
    }
}
