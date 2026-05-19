package com.example.cpsoftest.presentation.screen

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.text.KeyboardOptions
import com.example.cpsoftest.domain.model.AddUserRequest
import com.example.cpsoftest.ui.theme.*
import com.example.cpsoftest.presentation.viewmodel.UiState
import com.example.cpsoftest.presentation.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: UserViewModel = hiltViewModel()
) {
    val addUserState by viewModel.addUserState.collectAsState()
    val cities by viewModel.cities.collectAsState()

    // Form state
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("") }
    var gender by remember { mutableIntStateOf(0) }
    var cityExpanded by remember { mutableStateOf(false) }

    // Validation errors
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var cityError by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Handle state changes
    LaunchedEffect(addUserState) {
        when (val state = addUserState) {
            is UiState.Success -> {
                viewModel.resetAddUserState()
                onSuccess()
            }
            is UiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetAddUserState()
            }
            else -> {}
        }
    }

    fun validate(): Boolean {
        nameError = if (name.isBlank()) "Nama tidak boleh kosong" else null
        emailError = when {
            email.isBlank() -> "Email tidak boleh kosong"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Format email tidak valid"
            else -> null
        }
        phoneError = when {
            phoneNumber.isBlank() -> "Nomor telepon tidak boleh kosong"
            phoneNumber.length < 9 -> "Nomor telepon terlalu pendek"
            else -> null
        }
        cityError = if (selectedCity.isBlank()) "Pilih kota terlebih dahulu" else null
        return nameError == null && emailError == null && phoneError == null && cityError == null
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorBackground)
                .padding(padding)
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
                    .padding(horizontal = 8.dp, vertical = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    Column {
                        Text(
                            "Tambah User",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Isi data user baru",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            //  Form ────────────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                FormSection(title = "Informasi Pribadi") {
                    FormField(
                        value = name,
                        onValueChange = { name = it; nameError = null },
                        label = "Nama Lengkap",
                        icon = Icons.Outlined.Person,
                        isError = nameError != null,
                        errorMessage = nameError
                    )
                    FormField(
                        value = email,
                        onValueChange = { email = it; emailError = null },
                        label = "Email",
                        icon = Icons.Outlined.Email,
                        keyboardType = KeyboardType.Email,
                        isError = emailError != null,
                        errorMessage = emailError
                    )
                    FormField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it; phoneError = null },
                        label = "Nomor Telepon",
                        icon = Icons.Outlined.Phone,
                        keyboardType = KeyboardType.Phone,
                        isError = phoneError != null,
                        errorMessage = phoneError
                    )
                }

                FormSection(title = "Jenis Kelamin") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        GenderOption(
                            label = "Laki-laki",
                            icon = Icons.Filled.Male,
                            selected = gender == 0,
                            selectedColor = ColorSecondary,
                            onClick = { gender = 0 },
                            modifier = Modifier.weight(1f)
                        )
                        GenderOption(
                            label = "Perempuan",
                            icon = Icons.Filled.Female,
                            selected = gender == 1,
                            selectedColor = Color(0xFFEC4899),
                            onClick = { gender = 1 },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                FormSection(title = "Alamat") {
                    FormField(
                        value = address,
                        onValueChange = { address = it },
                        label = "Alamat",
                        icon = Icons.Outlined.Home
                    )

                    // City dropdown
                    ExposedDropdownMenuBox(
                        expanded = cityExpanded,
                        onExpandedChange = { cityExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedCity,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Kota") },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.LocationCity,
                                    contentDescription = null,
                                    tint = if (cityError != null) ColorError else ColorGrayMed
                                )
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityExpanded) },
                            isError = cityError != null,
                            supportingText = cityError?.let { { Text(it, color = ColorError, fontSize = 11.sp) } },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ColorSecondary,
                                unfocusedBorderColor = ColorGrayLight,
                                errorBorderColor = ColorError
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = cityExpanded,
                            onDismissRequest = { cityExpanded = false }
                        ) {
                            cities.forEach { city ->
                                DropdownMenuItem(
                                    text = { Text(city.name) },
                                    onClick = {
                                        selectedCity = city.name
                                        cityError = null
                                        cityExpanded = false
                                    }
                                )
                            }
                            if (cities.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("Tidak ada kota tersedia", color = ColorGrayMed) },
                                    onClick = { cityExpanded = false }
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
            }

            //  Submit Button ────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColorCard)
                    .padding(16.dp)
            ) {
                val isLoading = addUserState is UiState.Loading

                Button(
                    onClick = {
                        if (validate()) {
                            viewModel.addUser(
                                AddUserRequest(
                                    name = name.trim(),
                                    address = address.trim(),
                                    email = email.trim(),
                                    phoneNumber = phoneNumber.trim(),
                                    city = selectedCity,
                                    gender = gender
                                )
                            )
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorPrimary,
                        disabledContainerColor = ColorGrayLight
                    )
                ) {
                    AnimatedContent(
                        targetState = isLoading,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "btn_state"
                    ) { loading ->
                        if (loading) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp
                                )
                                Text("Menyimpan...", color = Color.White)
                            }
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Filled.Save, contentDescription = null)
                                Text(
                                    "Simpan User",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

//  Form Section ─────────────────────────────────────────────────────────────

@Composable
private fun FormSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ColorCard),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = ColorPrimary,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

//  Form Field ──────────────────────────────────────────────────────────────

@Composable
private fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 14.sp) },
        leadingIcon = {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isError) ColorError else ColorGrayMed,
                modifier = Modifier.size(20.dp)
            )
        },
        isError = isError,
        supportingText = errorMessage?.let {
            { Text(it, color = ColorError, fontSize = 11.sp) }
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ColorSecondary,
            unfocusedBorderColor = ColorGrayLight,
            errorBorderColor = ColorError
        )
    )
}

//  Gender Option ────────────────────────────────────────────────────────────

@Composable
private fun GenderOption(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (selected) selectedColor.copy(alpha = 0.1f) else Color.Transparent
    val borderColor = if (selected) selectedColor else ColorGrayLight
    val textColor = if (selected) selectedColor else ColorGrayDark

    Card(
        modifier = modifier
            .border(
                width = if (selected) 1.5.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                label,
                color = textColor,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 14.sp
            )
        }
    }
}