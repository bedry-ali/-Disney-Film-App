package fr.isen.bedry.disneyfilmapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.isen.bedry.disneyfilmapp.ui.theme.BlackBackground

@Composable
fun UiScreen(
    title: String,
    showBack: Boolean,
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit,
    onLogoutClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlackBackground)
    ) {
        AppTopBar(
            title = title,
            showBack = showBack,
            onBackClick = onBackClick,
            onProfileClick = onProfileClick,
            onLogoutClick = onLogoutClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            content = content
        )
    }
}