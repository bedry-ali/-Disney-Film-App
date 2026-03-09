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
fun SagasScreen(
    franchiseName: String,
    sagas: List<SagaItem>,
    onBackClick: () -> Unit,
    onSagaClick: (SagaItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Button(onClick = { onBackClick() }) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            franchiseName,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(sagas) { saga ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable { onSagaClick(saga) }
                ) {
                    Text(
                        text = saga.name,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}