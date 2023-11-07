package com.nimbletest.app.ui.screens.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nimbletest.app.R
import com.nimbletest.app.ui.components.NimbleButton

@Composable
fun SurveyDetailsScreen(
    onBackPressed: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.nimble_survey_bg_opacity),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                    }
                }
                Text(
                    text = stringResource(R.string.survey_details_name_label),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                )
                Text(
                    text = stringResource(R.string.survey_details_description_label),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                NimbleButton(
                    buttonText = stringResource(R.string.survey_details_btn_start_survey)
                )
            }
        }
    }
}