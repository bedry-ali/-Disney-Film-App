package fr.isen.bedry.disneyfilmapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.isen.bedry.disneyfilmapp.ui.theme.DarkCard
import fr.isen.bedry.disneyfilmapp.ui.theme.GrayText
import fr.isen.bedry.disneyfilmapp.ui.theme.WhiteText

@Composable
fun CategoriesScreen(
    categories: List<CategoryItem>,
    onCategoryClick: (CategoryItem) -> Unit,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    UiScreen(
        title = "Disney Film App",
        showBack = false,
        onProfileClick = onProfileClick,
        onLogoutClick = onLogoutClick
    ) {
        Text(
            text = "Browse universes and categories",
            color = GrayText,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(categories) { category ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable { onCategoryClick(category) }
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = category.name,
                            color = WhiteText,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }
}