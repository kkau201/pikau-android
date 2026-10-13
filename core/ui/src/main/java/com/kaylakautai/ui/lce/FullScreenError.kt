package com.kaylakautai.ui.lce

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.kaylakautai.pikau.ui.R
import com.kaylakautai.ui.components.PikauButton
import com.kaylakautai.ui.extensions.Spacer
import com.kaylakautai.ui.theme.Spacing
import com.kaylakautai.ui.util.PikauPreviewSurface
import com.kaylakautai.ui.util.PikauPreviews

@Composable
fun FullScreenError(
    error: DisplayError,
    onRetryClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = Spacing.xlarge),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        error.icon?.let {
            Icon(
                painter = painterResource(id = it),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = Spacing.medium)
            )
        }
        Text(
            text = stringResource(error.title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = Spacing.small)
        )
        Text(
            text = stringResource(error.message),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Spacing.medium)
        onRetryClick?.let {
            PikauButton(
                textRes = R.string.try_again,
                onClick = it,
                modifier = Modifier.padding(Spacing.medium)
            )
        }
    }
}

@PikauPreviews
@Composable
private fun FullScreenErrorPreview() {
    PikauPreviewSurface {
        FullScreenError(
            DisplayError(
                icon = R.drawable.ic_generic_error,
                title = R.string.error_generic_title,
                message = R.string.error_generic_message,
            )
        ) {}
    }
}