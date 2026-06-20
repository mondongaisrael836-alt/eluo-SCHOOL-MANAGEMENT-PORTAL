package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MessageLog
import com.example.ui.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagingScreen(
    userProfile: UserProfile,
    chatMessages: List<MessageLog>,
    onSignIn: (name: String, email: String, role: String, phone: String) -> Unit,
    onSignOut: () -> Unit,
    onUpdateProfile: (name: String, email: String, phone: String, bio: String) -> Unit,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!userProfile.isLoggedIn) {
        // --- SECURE ACADEMIC SIGN IN FORM ---
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .testTag("signin_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Lock Emblem Motif
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VpnKey,
                            contentDescription = "Security lock symbol",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Core-Connect Intel",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Decrypt secure portal communications logs",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                    }

                    var nameInput by remember { mutableStateOf("Israel Mondonga") }
                    var emailInput by remember { mutableStateOf("mondongaisrael836@gmail.com") }
                    var phoneInput by remember { mutableStateOf("+243 897 1234") }
                    var roleInput by remember { mutableStateOf("Student") }
                    var pinInput by remember { mutableStateOf("1234") }

                    val rolesList = listOf("Student", "Parent", "Teacher", "Administrator")
                    var showDropdown by remember { mutableStateOf(false) }

                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Identity Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") },
                        modifier = Modifier.fillMaxWidth().testTag("signin_name_field"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = { emailInput = it },
                        label = { Text("Portal Email") },
                        leadingIcon = { Icon(Icons.Default.Mail, contentDescription = "Email") },
                        modifier = Modifier.fillMaxWidth().testTag("signin_email_field"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = { phoneInput = it },
                        label = { Text("Mobile Contact") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone") },
                        modifier = Modifier.fillMaxWidth().testTag("signin_phone_field"),
                        singleLine = true
                    )

                    // Role selector
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = roleInput,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Scholastic Role") },
                            leadingIcon = { Icon(Icons.Default.AssignmentInd, contentDescription = "Role") },
                            trailingIcon = {
                                IconButton(onClick = { showDropdown = true }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Open")
                                }
                            },
                            modifier = Modifier.fillMaxWidth().testTag("signin_role_selector")
                        )
                        DropdownMenu(
                            expanded = showDropdown,
                            onDismissRequest = { showDropdown = false }
                        ) {
                            rolesList.forEach { role ->
                                DropdownMenuItem(
                                    text = { Text(role) },
                                    onClick = {
                                        roleInput = role
                                        showDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { pinInput = it },
                        label = { Text("Passcode PIN") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock") },
                        modifier = Modifier.fillMaxWidth().testTag("signin_pin_field"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = {
                            if (nameInput.isNotBlank() && emailInput.isNotBlank()) {
                                onSignIn(nameInput, emailInput, roleInput, phoneInput)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("core_connect_submit"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Authenticate Core-Connect",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    } else {
        // --- AUTHENTICATED CHAT MESSENGER & PROFILE MANIFEST VIEW ---
        var currentPortalTab by remember { mutableStateOf(0) } // 0: Chat logs, 1: Identity Update

        Column(modifier = modifier.fillMaxSize()) {
            TabRow(
                selectedTabIndex = currentPortalTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = currentPortalTab == 0,
                    onClick = { currentPortalTab = 0 },
                    text = { Text("Dispatches Stream") },
                    icon = { Icon(Icons.Default.QuestionAnswer, contentDescription = "Dispatches") }
                )
                Tab(
                    selected = currentPortalTab == 1,
                    onClick = { currentPortalTab = 1 },
                    text = { Text("Update Profile") },
                    icon = { Icon(Icons.Default.ManageAccounts, contentDescription = "Profile Setup") }
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                if (currentPortalTab == 0) {
                    // --- MESSAGING VIEW ---
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Channel Banner Info
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 2.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.secondaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "PEM", // Prof. Eluo Mondonga Sr.
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Prof. Eluo Mondonga Sr. (Faculty Support)",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Faculty Security Officer • Advanced Algorithms Dept",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }

                        // Message Stream
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .testTag("message_stream"),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(chatMessages) { msg ->
                                val isMe = msg.isFromMe
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                                ) {
                                    Card(
                                        shape = RoundedCornerShape(
                                            topStart = 16.dp,
                                            topEnd = 16.dp,
                                            bottomStart = if (isMe) 16.dp else 2.dp,
                                            bottomEnd = if (isMe) 2.dp else 16.dp
                                        ),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isMe) MaterialTheme.colorScheme.primary
                                                             else MaterialTheme.colorScheme.secondaryContainer
                                        ),
                                        modifier = Modifier.widthIn(max = 280.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(
                                                text = if (isMe) "User Dispatch" else msg.senderName,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isMe) Color.White.copy(alpha = 0.8f)
                                                                 else MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                                                )
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = msg.text,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = if (isMe) Color.White else MaterialTheme.colorScheme.onSecondaryContainer
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = msg.time,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = if (isMe) Color.White.copy(alpha = 0.6f)
                                                             else MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f),
                                                modifier = Modifier.align(Alignment.End)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Message Entry field
                        var draftText by remember { mutableStateOf("") }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = draftText,
                                onValueChange = { draftText = it },
                                placeholder = { Text("Draft encrypted dispatch...") },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("chat_input_field"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                            IconButton(
                                onClick = {
                                    if (draftText.isNotBlank()) {
                                        onSendMessage(draftText)
                                        draftText = ""
                                    }
                                },
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                                    .size(48.dp)
                                    .testTag("chat_send_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Send",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                } else {
                    // --- IDENTITY UPDATE PROFILE PORTAL MANAGER ---
                    var editName by remember { mutableStateOf(userProfile.name) }
                    var editEmail by remember { mutableStateOf(userProfile.email) }
                    var editPhone by remember { mutableStateOf(userProfile.phone) }
                    var editBio by remember { mutableStateOf(userProfile.bio) }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .testTag("update_profile_view"),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            // High fidelity avatar card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(20.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(20.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(70.dp)
                                            .clip(CircleShape)
                                            .background(
                                                Brush.radialGradient(
                                                    listOf(
                                                        MaterialTheme.colorScheme.primary,
                                                        MaterialTheme.colorScheme.secondary
                                                    )
                                                )
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = userProfile.name.split(" ").mapNotNull { it.firstOrNull() }.joinToString("").take(2).uppercase(),
                                            style = MaterialTheme.typography.headlineSmall.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color.White
                                            )
                                        )
                                    }

                                    Column {
                                        Surface(
                                            shape = RoundedCornerShape(100.dp),
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                        ) {
                                            Text(
                                                text = userProfile.role.uppercase(),
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp),
                                                color = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = userProfile.name,
                                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = userProfile.email,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(20.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    Text(
                                        text = "Edit Administrative Manifest",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    OutlinedTextField(
                                        value = editName,
                                        onValueChange = { editName = it },
                                        label = { Text("Manifest Legal Name") },
                                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") },
                                        modifier = Modifier.fillMaxWidth().testTag("profile_edit_name"),
                                        singleLine = true
                                    )

                                    OutlinedTextField(
                                        value = editEmail,
                                        onValueChange = { editEmail = it },
                                        label = { Text("Portal Verified Email") },
                                        leadingIcon = { Icon(Icons.Default.Mail, contentDescription = "Email") },
                                        modifier = Modifier.fillMaxWidth().testTag("profile_edit_email"),
                                        singleLine = true
                                    )

                                    OutlinedTextField(
                                        value = editPhone,
                                        onValueChange = { editPhone = it },
                                        label = { Text("Official Mobile Contact") },
                                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone") },
                                        modifier = Modifier.fillMaxWidth().testTag("profile_edit_phone"),
                                        singleLine = true
                                    )

                                    OutlinedTextField(
                                        value = editBio,
                                        onValueChange = { editBio = it },
                                        label = { Text("Academic Biography Manifest") },
                                        leadingIcon = { Icon(Icons.Default.Description, contentDescription = "Bio") },
                                        modifier = Modifier.fillMaxWidth().testTag("profile_edit_bio"),
                                        minLines = 3
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Button(
                                        onClick = {
                                            if (editName.isNotBlank() && editEmail.isNotBlank()) {
                                                onUpdateProfile(editName, editEmail, editPhone, editBio)
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp)
                                            .testTag("profile_update_submit"),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text("Commit Identity Manifest", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                    }

                                    OutlinedButton(
                                        onClick = onSignOut,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp)
                                            .testTag("profile_sign_out"),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(Icons.Default.Logout, contentDescription = "Exit")
                                            Text("Disconnect Session", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
