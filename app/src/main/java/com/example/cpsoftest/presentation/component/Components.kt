package com.example.cpsoftest.presentation.component

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cpsoftest.domain.model.User
import com.example.cpsoftest.ui.theme.*

// User Card

@Composable
fun UserCard(user: User, modifier: Modifier = Modifier) {
    val genderIcon = if (user.gender == 0) Icons.Filled.Male else Icons.Filled.Female
    val genderColor = if (user.gender == 0) ColorSecondary else Color(0xFFEC4899)
    val genderLabel = if (user.gender == 0) "Laki-laki" else "Perempuan"
    val initials = user.name.split(" ")
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ColorCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(ColorPrimary, ColorSecondary)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials.ifEmpty { "?" },
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = user.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = ColorText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    // Gender badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(genderColor.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = genderIcon,
                            contentDescription = genderLabel,
                            tint = genderColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(Modifier.width(3.dp))
                        Text(
                            text = genderLabel,
                            fontSize = 11.sp,
                            color = genderColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                UserInfoRow(Icons.Outlined.Email, user.email)
                Spacer(Modifier.height(2.dp))
                UserInfoRow(Icons.Outlined.Phone, user.phoneNumber)
                Spacer(Modifier.height(2.dp))
                UserInfoRow(Icons.Outlined.LocationOn, "${user.address}, ${user.city}")
            }
        }
    }
}

@Composable
private fun UserInfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ColorGrayMed,
            modifier = Modifier.size(13.dp)
        )
        Spacer(Modifier.width(5.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = ColorGrayDark,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// Search Bar

@Composable
fun MainSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                "Cari nama, email, kota...",
                color = ColorGrayMed,
                fontSize = 14.sp
            )
        },
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = "Cari", tint = ColorGrayMed)
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Filled.Close, contentDescription = "Hapus", tint = ColorGrayMed)
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ColorSecondary,
            unfocusedBorderColor = ColorGrayLight,
            focusedContainerColor = ColorCard,
            unfocusedContainerColor = ColorCard
        )
    )
}

//  Filter Chip Row
@Composable
fun CityFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) ColorPrimary else ColorCard,
            contentColor = if (selected) Color.White else ColorGrayDark
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) ColorPrimary else ColorGrayLight
        )
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(4.dp))
        }
        Text(label, fontSize = 12.sp)
    }
}


//  Sort Button ─────────────────────────────────────────────────────────────

@Composable
fun SortButton(
    sortLabel: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            width = 1.dp
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = ColorPrimary
        )
    ) {
        Icon(
            imageVector = Icons.Filled.SortByAlpha,
            contentDescription = "Urutkan",
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(sortLabel, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

//  Empty State ─────────────────────────────────────────────────────────────

@Composable
fun EmptyState(message: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.PersonSearch,
            contentDescription = null,
            tint = ColorGrayLight,
            modifier = Modifier.size(72.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = message,
            color = ColorGrayMed,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

//  Error State ─────────────────────────────────────────────────────────────

@Composable
fun ErrorState(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = null,
            tint = ColorError,
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = message,
            color = ColorGrayDark,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Refresh")
        }
    }
}

//  Loading Shimmer ─────────────────────────────────────────────────────────

@Composable
fun ShimmerUserCard(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ColorCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(52.dp).clip(CircleShape)
                    .background(ColorGrayLight.copy(alpha = alpha))
            )
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Box(modifier = Modifier.fillMaxWidth(0.6f).height(14.dp).clip(RoundedCornerShape(4.dp))
                    .background(ColorGrayLight.copy(alpha = alpha)))
                Spacer(Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth(0.85f).height(11.dp).clip(RoundedCornerShape(4.dp))
                    .background(ColorGrayLight.copy(alpha = alpha)))
                Spacer(Modifier.height(6.dp))
                Box(modifier = Modifier.fillMaxWidth(0.5f).height(11.dp).clip(RoundedCornerShape(4.dp))
                    .background(ColorGrayLight.copy(alpha = alpha)))
            }
        }
    }
}