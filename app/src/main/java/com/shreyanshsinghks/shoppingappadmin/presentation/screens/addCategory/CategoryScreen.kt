package com.shreyanshsinghks.shoppingappadmin.presentation.screens.addCategory

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Preview(showBackground = true)
@Composable
fun CategoryScreen(viewModel: AddCategoryViewModel = hiltViewModel()) {
    val categoryState by viewModel.categoryState.collectAsState()

    when {
        categoryState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        categoryState.error.isNotEmpty() -> {
            Toast.makeText(LocalContext.current, categoryState.error, Toast.LENGTH_SHORT).show()
        }

        categoryState.isSuccess -> {
//            Toast.makeText(LocalContext.current, "Category added successfully", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add Category",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = viewModel.category.name,
            onValueChange = { viewModel.updateCategory(it) },
            label = { Text("Category Name") }
        )

        Button(onClick = { viewModel.addCategory() }) {
            Text(text = "Submit")
        }
    }
}
