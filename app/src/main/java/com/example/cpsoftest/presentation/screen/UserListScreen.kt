package com.example.cpsoftest.presentation.screen


import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cpsoftest.presentation.component.CityFilterChip
import com.example.cpsoftest.presentation.component.EmptyState
import com.example.cpsoftest.presentation.component.ErrorState
import com.example.cpsoftest.presentation.component.MainSearchBar
import com.example.cpsoftest.presentation.component.ShimmerUserCard
import com.example.cpsoftest.presentation.component.SortButton
import com.example.cpsoftest.presentation.component.UserCard
import com.example.cpsoftest.presentation.component.*
import com.example.cpsoftest.ui.theme.*
import com.example.cpsoftest.presentation.viewmodel.SortOrder
import com.example.cpsoftest.presentation.viewmodel.UiState
import com.example.cpsoftest.presentation.viewmodel.UserViewModel

@Composable
fun UserListScreen(
    onNavigateToAdd: () -> Unit,
    viewModel: UserViewModel = hiltViewModel()
) {
    val filteredUsers by viewModel.filteredUsers.collectAsState()
    val uiState by viewModel.usersUiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()
    val cities by viewModel.cities.collectAsState()

    val sortLabel = when (sortOrder) {
        SortOrder.ASCENDING  -> "A → Z"
        SortOrder.DESCENDING -> "Z → A"
        SortOrder.NONE       -> "Urutkan"
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = ColorPrimary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.PersonAdd, contentDescription = null)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorBackground)
                .padding(innerPadding)
        ) {
            //  Header ──────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(ColorPrimary, ColorSecondary)
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                "CPSSOFT",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 2.sp
                            )
                            Text(
                                "Daftar User",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        // User count badge
                        AnimatedContent(
                            targetState = filteredUsers.size,
                            transitionSpec = {
                                (slideInVertically { -it } + fadeIn()) togetherWith
                                        (slideOutVertically { it } + fadeOut())
                            },
                            label = "user_count"
                        ) { count ->
                            Box(
                                modifier = Modifier
                                    .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "$count User",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Search bar
                    MainSearchBar(
                        query = searchQuery,
                        onQueryChange = viewModel::updateSearch
                    )
                }
            }

            //  Filter & Sort Bar ────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColorCard)
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sort button
                SortButton(sortLabel = sortLabel, onClick = viewModel::toggleSort)

                // Divider
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .width(1.dp)
                        .background(ColorGrayLight)
                )

                // "Semua" chip
                CityFilterChip(
                    label = "Semua Kota",
                    selected = selectedCity == null,
                    onClick = { viewModel.selectCity(null) }
                )

                // City chips
                cities.forEach { city ->
                    CityFilterChip(
                        label = city.name,
                        selected = selectedCity == city.name,
                        onClick = {
                            viewModel.selectCity(
                                if (selectedCity == city.name) null else city.name
                            )
                        }
                    )
                }
            }

            // Active filter indicator
            AnimatedVisibility(
                visible = selectedCity != null || searchQuery.isNotBlank(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ColorSecondary.copy(alpha = 0.08f))
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = buildString {
                            if (searchQuery.isNotBlank()) append("\"$searchQuery\"")
                            if (selectedCity != null) {
                                if (searchQuery.isNotBlank()) append(" · ")
                                append("Kota: $selectedCity")
                            }
                        },
                        fontSize = 12.sp,
                        color = ColorSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    TextButton(
                        onClick = {
                            viewModel.updateSearch("")
                            viewModel.selectCity(null)
                        },
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        Text("Reset", fontSize = 12.sp, color = ColorSecondary)
                    }
                }
            }

            //  List Content ──────────────────────────────────────────────────
            when (uiState) {
                is UiState.Loading -> {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(6) { ShimmerUserCard() }
                    }
                }

                is UiState.Error -> {
                    ErrorState(
                        message = "Jaringan terputus, refresh untuk akses secara offline",
                        onRetry = viewModel::loadUsers,
                        modifier = Modifier.weight(1f)
                    )
                }

                else -> {
                    if (filteredUsers.isEmpty()) {
                        EmptyState(
                            message = if (searchQuery.isBlank() && selectedCity == null)
                                "Belum ada user terdaftar"
                            else
                                "Tidak ada user yang cocok dengan filter",
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(
                                horizontal = 16.dp,
                                vertical = 16.dp
//                                top = 12.dp,
//                                bottom = 100.dp  // FAB clearance
                            ),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(
                                items = filteredUsers,
                                key = { it.id }
                            ) { user ->
                                AnimatedVisibility(
                                    visible = true,
                                    enter = fadeIn() + slideInVertically()
                                ) {
                                    UserCard(user = user)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}