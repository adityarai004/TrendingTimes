package com.example.trendingtimes.core.navigation

import com.example.trendingtimes.R
import com.example.trendingtimes.core.navigation.NavConstants.LOGIN
import com.example.trendingtimes.core.navigation.NavConstants.LOGIN_ABOUT

sealed class NavDes(val data: NavigationDrawerItem,val customAppBarStringId: Int? = null){

    data object Login: NavDes(NavigationDrawerData(LOGIN,LOGIN_ABOUT), R.string.app_name)
    fun route() = (data as NavigationDrawerData).route
    fun displayText() = (data as NavigationDrawerData).displayText
    companion object {
        val startDestination = Login
    }
}