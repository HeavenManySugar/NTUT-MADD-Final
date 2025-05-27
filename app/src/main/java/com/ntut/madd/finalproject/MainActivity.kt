package com.ntut.madd.finalproject

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ntut.madd.finalproject.data.model.ErrorMessage
import com.ntut.madd.finalproject.ui.home.HomeRoute
import com.ntut.madd.finalproject.ui.home.HomeScreen
import com.ntut.madd.finalproject.ui.settings.SettingsRoute
import com.ntut.madd.finalproject.ui.settings.SettingsScreen
import com.ntut.madd.finalproject.ui.signin.SignInRoute
import com.ntut.madd.finalproject.ui.signin.SignInScreen
import com.ntut.madd.finalproject.ui.signup.SignUpRoute
import com.ntut.madd.finalproject.ui.signup.SignUpScreen
import com.ntut.madd.finalproject.ui.theme.MakeItSoTheme
import com.ntut.madd.finalproject.ui.todoitem.TodoItemRoute
import com.ntut.madd.finalproject.ui.todoitem.TodoItemScreen
import com.ntut.madd.finalproject.ui.todolist.TodoListRoute
import com.ntut.madd.finalproject.ui.todolist.TodoListScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setSoftInputMode()

        setContent {
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }
            val navController = rememberNavController()

            MakeItSoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = TodoListRoute,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable<HomeRoute> { HomeScreen(
                                openSettingsScreen = {
                                    navController.navigate(SettingsRoute) { launchSingleTop = true }
                                }
                            ) }
                            composable<TodoListRoute> { TodoListScreen(
                                openSettingsScreen = {
                                    navController.navigate(SettingsRoute) { launchSingleTop = true }
                                },
                                openTodoItemScreen = { itemId ->
                                    navController.navigate(TodoItemRoute(itemId)) { launchSingleTop = true }
                                }
                            ) }
                            composable<SettingsRoute> { SettingsScreen(
                                openHomeScreen = {
                                    navController.navigate(TodoListRoute) { launchSingleTop = true }
                                },
                                openSignInScreen = {
                                    navController.navigate(SignInRoute) { launchSingleTop = true }
                                }
                            ) }
                            composable<SignInRoute> { SignInScreen(
                                openHomeScreen = {
                                    navController.navigate(TodoListRoute) { launchSingleTop = true }
                                },
                                openSignUpScreen = {
                                    navController.navigate(SignUpRoute) { launchSingleTop = true }
                                },
                                showErrorSnackbar = { errorMessage ->
                                    val message = getErrorMessage(errorMessage)
                                    scope.launch { snackbarHostState.showSnackbar(message) }
                                }
                            ) }
                            composable<SignUpRoute> { SignUpScreen(
                                openHomeScreen = {
                                    navController.navigate(TodoListRoute) { launchSingleTop = true }
                                },
                                showErrorSnackbar = { errorMessage ->
                                    val message = getErrorMessage(errorMessage)
                                    scope.launch { snackbarHostState.showSnackbar(message) }
                                }
                            ) }
                            composable<TodoItemRoute> { TodoItemScreen(
                                openTodoListScreen = {
                                    navController.navigate(TodoListRoute) { launchSingleTop = true }
                                },
                                showErrorSnackbar = { errorMessage ->
                                    val message = getErrorMessage(errorMessage)
                                    scope.launch { snackbarHostState.showSnackbar(message) }
                                }
                            ) }
                        }
                    }
                }
            }
        }
    }

    private fun setSoftInputMode() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    private fun getErrorMessage(error: ErrorMessage): String {
        return when (error) {
            is ErrorMessage.StringError -> error.message
            is ErrorMessage.IdError -> this@MainActivity.getString(error.message)
        }
    }
}