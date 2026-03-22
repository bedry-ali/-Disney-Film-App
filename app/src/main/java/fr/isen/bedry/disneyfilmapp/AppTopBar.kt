package fr.isen.bedry.disneyfilmapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppTopBar(
    title: String,
    showBack: Boolean,
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit,
    onLogoutClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, start = 12.dp, end = 12.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (showBack) {
                Text(
                    text = "Back",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { onBackClick() }
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Profile",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier
                    .clickable { onProfileClick() }
                    .padding(horizontal = 6.dp)
            )

            if (onLogoutClick != null) {
                Text(
                    text = "Logout",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .clickable { onLogoutClick() }
                        .padding(horizontal = 6.dp)
                )
            }
        }
    }
}