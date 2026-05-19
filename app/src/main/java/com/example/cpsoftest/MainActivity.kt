package com.example.cpsoftest

import com.example.cpsoftest.ui.theme.CPSOFTESTTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cpsoftest.presentation.screen.AddUserScreen
import com.example.cpsoftest.presentation.screen.UserListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CPSOFTESTTheme {
                val navController = rememberNavController()
                NavHost(navController = navController)
            }
        }
    }
}

sealed class Screen(val route: String) {
    object UserList : Screen("user_list")
    object AddUser  : Screen("add_user")
}

@Composable
fun NavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.UserList.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it / 3 },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(150))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(150))
        }
    ) {
        composable(Screen.UserList.route) {
            UserListScreen(
                onNavigateToAdd = { navController.navigate(Screen.AddUser.route) }
            )
        }
        composable(Screen.AddUser.route) {
            AddUserScreen(
                onBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }
    }
}