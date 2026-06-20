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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.StoreProduct

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(
    products: List<StoreProduct>,
    cart: Map<Int, Int>, // ID -> Qty
    onAddToCart: (Int) -> Unit,
    onRemoveFromCart: (Int) -> Unit,
    onCheckout: (buyerName: String, buyerPhone: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCheckoutDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Uniforms", "Textbooks", "Technology", "Equipment")

    val filteredProducts = if (selectedCategory == "All") {
        products
    } else {
        products.filter { it.category == selectedCategory }
    }

    val cartTotalCount = cart.values.sum()
    val cartTotalPrice = cart.entries.sumOf { (id, qty) ->
        val item = products.find { it.id == id }
        (item?.price ?: 0.0) * qty
    }

    Scaffold(
        modifier = modifier.testTag("store_screen"),
        floatingActionButton = {
            if (cartTotalCount > 0) {
                ExtendedFloatingActionButton(
                    text = { Text("Checkout basket (${cartTotalCount} items • \$${String.format("%.2f", cartTotalPrice)})") },
                    icon = { Icon(Icons.Default.ShoppingCartCheckout, contentDescription = "Checkout") },
                    onClick = { showCheckoutDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("store_checkout_fab"),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        },
        contentWindowInsets = WindowInsets(0,0,0,0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Category scrollable filter bar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                ScrollableTabRow(
                    selectedTabIndex = categories.indexOf(selectedCategory).coerceAtLeast(0),
                    edgePadding = 16.dp,
                    indicator = {},
                    divider = {},
                    modifier = Modifier.testTag("store_filters")
                ) {
                    categories.forEach { tab ->
                        val isSelected = selectedCategory == tab
                        Tab(
                            selected = isSelected,
                            onClick = { selectedCategory = tab },
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(100.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = tab,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Products list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("product_list_view"),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredProducts) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("product_card_${item.id}"),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Category Badge Illustration
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                val vectorIcon = when (item.category) {
                                    "Uniforms" -> Icons.Default.Checkroom
                                    "Textbooks" -> Icons.Default.MenuBook
                                    "Technology" -> Icons.Default.Computer
                                    "Equipment" -> Icons.Default.Engineering
                                    else -> Icons.Default.ShoppingBag
                                }
                                Icon(
                                    imageVector = vectorIcon,
                                    contentDescription = item.category,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(34.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f),
                                    contentColor = MaterialTheme.colorScheme.secondary
                                ) {
                                    Text(
                                        text = item.category.uppercase(),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = item.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "\$${String.format("%.2f", item.price)}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )
                            }

                            // Basket controls
                            val qtyInCart = cart[item.id] ?: 0
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.width(60.dp)
                            ) {
                                if (qtyInCart > 0) {
                                    IconButton(
                                        onClick = { onRemoveFromCart(item.id) },
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f), CircleShape)
                                            .size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Remove,
                                            contentDescription = "Remove item",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Text(
                                        text = qtyInCart.toString(),
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { onAddToCart(item.id) },
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), CircleShape)
                                        .size(36.dp)
                                        .testTag("add_item_${item.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add item",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // --- CHECKOUT MOBILE FUNDS REGISTERED DIALOG ---
    if (showCheckoutDialog) {
        var buyerName by remember { mutableStateOf("Israel Mondonga") }
        var buyerPhone by remember { mutableStateOf("+243 897 1234") }
        var studentClassRole by remember { mutableStateOf("Class 12-A Student") }
        var paymentMethodSelection by remember { mutableStateOf("Mobile Money Pay") }
        var checkoutSuccessMessage by remember { mutableStateOf<String?>(null) }
        var isSplashingCheckout by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { 
                if (!isSplashingCheckout) showCheckoutDialog = false 
            },
            title = {
                Text(
                    text = if (checkoutSuccessMessage != null) "Digital Receipt Secure" else "Online Escrow Purchase Form",
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.testTag("checkout_dialog_title")
                )
            },
            text = {
                if (checkoutSuccessMessage != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth().padding(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = "Complete",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = checkoutSuccessMessage!!,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else if (isSplashingCheckout) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Awaiting Digital Mobile POS validation prompt at phone billing...",
                            style = MaterialTheme.typography.titleSmall,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Please verify Orange/M-Pesa credentials",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "Subtotal: \$${String.format("%.2f", cartTotalPrice)}",
                                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "Payment routed automatically to school designated wallet",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }

                        item {
                            OutlinedTextField(
                                value = buyerName,
                                onValueChange = { buyerName = it },
                                label = { Text("Buyer Guardian Name") },
                                modifier = Modifier.fillMaxWidth().testTag("checkout_buyer_name"),
                                singleLine = true
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = buyerPhone,
                                onValueChange = { buyerPhone = it },
                                label = { Text("Mobile Money Payee Phone Number") },
                                modifier = Modifier.fillMaxWidth().testTag("checkout_buyer_phone"),
                                singleLine = true
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = studentClassRole,
                                onValueChange = { studentClassRole = it },
                                label = { Text("Beneficiary Student Designation") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }

                        item {
                            Text(
                                text = "School wallet setup complies with cellular microfinance guidelines. Secure pin prompt on phone is required.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                if (checkoutSuccessMessage != null) {
                    Button(
                        onClick = { 
                            showCheckoutDialog = false 
                        }
                    ) {
                        Text("Dismiss Receipt")
                    }
                } else if (!isSplashingCheckout) {
                    Button(
                        onClick = {
                            if (buyerName.isNotBlank() && buyerPhone.isNotBlank()) {
                                isSplashingCheckout = true
                                // Simulate digital checkout
                                val messageStr = "Secure purchase complete! Total \$${String.format("%.2f", cartTotalPrice)} has been charged and wired to the School's official Ledger via $buyerPhone."
                                checkoutSuccessMessage = messageStr
                                // Pass to viewmodel to execute transactions
                                onCheckout(buyerName, buyerPhone)
                                isSplashingCheckout = false
                            }
                        },
                        modifier = Modifier.testTag("checkout_confirm_pay_button")
                    ) {
                        Text("Verify Payment & Secure Kit")
                    }
                }
            },
            dismissButton = {
                if (checkoutSuccessMessage == null && !isSplashingCheckout) {
                    TextButton(onClick = { showCheckoutDialog = false }) {
                        Text("Cancel")
                    }
                }
            }
        )
    }
}
