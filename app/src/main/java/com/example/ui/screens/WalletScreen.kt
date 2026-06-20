package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import com.example.ui.StoreTransaction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    payeePhone: String,
    transactions: List<StoreTransaction>,
    onRegisterPayeePhone: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var editPhoneInput by remember { mutableStateOf(payeePhone) }
    var isEditingMerchant by remember { mutableStateOf(false) }

    val totalIncomingRevenue = transactions.sumOf { it.amountPaid }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("wallet_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- VIRTUAL SMART WALLET BANNER CARD ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.tertiary
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "SCHOOL WALLET LEDGER",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                )
                                Text(
                                    text = "Digital Settlement Escrow",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.5f)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = "Ledger Icon",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "\$${String.format("%.2f", totalIncomingRevenue)}",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color.Green)
                                )
                                Text(
                                    text = "Bound Payee endpoint: $payeePhone",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White.copy(alpha = 0.82f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- MERCHANDISE PAYEE PHONE REGISTRATION FORM ---
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Merchant Gateway Setup",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "All store client sales route to this phone",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        IconButton(
                            onClick = { isEditingMerchant = !isEditingMerchant },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = if (isEditingMerchant) Icons.Default.Close else Icons.Default.Edit,
                                contentDescription = "Edit endpoint"
                            )
                        }
                    }

                    if (isEditingMerchant) {
                        OutlinedTextField(
                            value = editPhoneInput,
                            onValueChange = { editPhoneInput = it },
                            label = { Text("School Money Receiver Mobile Number") },
                            placeholder = { Text("+243 897 5555") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone") },
                            modifier = Modifier.fillMaxWidth().testTag("wallet_phone_input"),
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                if (editPhoneInput.isNotBlank()) {
                                    onRegisterPayeePhone(editPhoneInput)
                                    isEditingMerchant = false
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("wallet_phone_submit"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Commit Merchant Registration", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.03f), RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhoneAndroid,
                                    contentDescription = "Active receiver",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            Column {
                                Text(
                                    text = payeePhone,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Status: ACTIVE AND ACCEPTING DIGITAL DISBURSEMENTS",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- TRANSACTION RECEIPTS LEDGER ---
        item {
            Text(
                text = "Incoming Revenues Receipts Ledger (${transactions.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (transactions.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "No Digital Escrow disbursements registered yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.padding(24.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(transactions) { tx ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ledger_card_${tx.id}"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = tx.id,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = tx.itemsSummary,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF10B981).copy(alpha = 0.12f),
                                contentColor = Color(0xFF10B981)
                            ) {
                                Text(
                                    text = "PAID",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Buyer: ${tx.buyerName} (${tx.buyerPhone})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = "Dest: ${tx.payeePhone}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "+\$${String.format("%.2f", tx.amountPaid)}",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold),
                                    color = Color(0xFF10B981)
                                )
                                Text(
                                    text = tx.timestamp,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
