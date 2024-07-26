package com.shreyanshsinghks.shoppingappadmin.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shreyanshsinghks.shoppingappadmin.presentation.screens.addProduct.AddProductsScreen
import com.shreyanshsinghks.shoppingappadmin.presentation.screens.addCategory.CategoryScreen
import com.shreyanshsinghks.shoppingappadmin.presentation.screens.DashboardScreen
import com.shreyanshsinghks.shoppingappadmin.presentation.screens.NotificationScreen
import com.shreyanshsinghks.shoppingappadmin.presentation.screens.OrderScreen

@Composable
fun App() {
    val navController = rememberNavController()
    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = Dashboard) {
            composable<Dashboard> {
                DashboardScreen()
            }

            composable<AddProducts> {
                AddProductsScreen()
            }

            composable<Notification> {
                NotificationScreen()
            }

            composable<Category> {
                CategoryScreen()
            }

            composable<Order> {
                OrderScreen()
            }
        }
    }
}