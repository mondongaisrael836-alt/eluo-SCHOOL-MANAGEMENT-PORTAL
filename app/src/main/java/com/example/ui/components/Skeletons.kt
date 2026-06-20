package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShimmerItem(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val alphaAnim by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = alphaAnim * 0.12f))
    )
}

@Composable
fun SkeletonCard(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            ShimmerItem(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                ShimmerItem(
                    modifier = Modifier
                        .width(140.dp)
                        .height(20.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                ShimmerItem(
                    modifier = Modifier
                        .width(80.dp)
                        .height(14.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        ShimmerItem(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        )
    }
}

@Composable
fun SkeletonDashboardGrid(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ShimmerItem(modifier = Modifier.weight(1f).height(100.dp))
            ShimmerItem(modifier = Modifier.weight(1f).height(100.dp))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ShimmerItem(modifier = Modifier.weight(1f).height(100.dp))
            ShimmerItem(modifier = Modifier.weight(1f).height(100.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        ShimmerItem(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        SkeletonCard()
    }
}

@Composable
fun SkeletonLoadingOverlay(
    message: String = "Fetching records...",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp,
                modifier = Modifier
                    .size(56.dp)
                    .testTag("loading_spinner")
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                ),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Securing administrative pipe",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        }
    }
}
