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
fun SagasScreen(
    franchiseName: String,
    sagas: List<SagaItem>,
    onBackClick: () -> Unit,
    onSagaClick: (SagaItem) -> Unit,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    UiScreen(
        title = franchiseName,
        showBack = true,
        onBackClick = onBackClick,
        onProfileClick = onProfileClick,
        onLogoutClick = onLogoutClick
    ) {
        LazyColumn {
            items(sagas) { saga ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable { onSagaClick(saga) }
                ) {
                    Text(
                        text = saga.name,
                        color = WhiteText,
                        modifier = Modifier.padding(18.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}