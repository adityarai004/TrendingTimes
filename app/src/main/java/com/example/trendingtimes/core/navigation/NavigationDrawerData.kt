package com.example.trendingtimes.core.navigation

data class NavigationDrawerData(
    val route: String,
    val displayText: String = route,
    val iconResId: Int? = null,
    val additionalFuncOnClick: () -> Unit = {}
) : NavigationDrawerItem
