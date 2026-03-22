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
import fr.isen.bedry.disneyfilmapp.ui.theme.WhiteText

@Composable
fun FranchisesScreen(
    categoryName: String,
    franchises: List<FranchiseItem>,
    onBackClick: () -> Unit,
    onFranchiseClick: (FranchiseItem) -> Unit,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    UiScreen(
        title = categoryName,
        showBack = true,
        onBackClick = onBackClick,
        onProfileClick = onProfileClick,
        onLogoutClick = onLogoutClick
    ) {
        LazyColumn {
            items(franchises) { franchise ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable { onFranchiseClick(franchise) }
                ) {
                    Text(
                        text = franchise.name,
                        color = WhiteText,
                        modifier = Modifier.padding(18.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}