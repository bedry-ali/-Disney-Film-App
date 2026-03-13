package fr.isen.bedry.disneyfilmapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategoriesScreen(
    categories: List<CategoryItem>,
    onCategoryClick: (CategoryItem) -> Unit,
    onProfileClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Categories",
                style = MaterialTheme.typography.headlineMedium
            )

            Button(onClick = { onProfileClick() }) {
                Text("Profile")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(categories) { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable { onCategoryClick(category) }
                ) {
                    Text(
                        text = category.name,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}