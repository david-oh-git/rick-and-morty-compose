/*
 * MIT License
 *
 * Copyright (c) 2024   David Osemwota.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.davidosemwota.ui.components

import android.R.attr.text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.davidosemwota.ui.GeneralPreview
import io.davidosemwota.ui.PreviewComposable

@Composable
fun JerryCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 8.dp,
    cardColour: Color = MaterialTheme.colorScheme.surfaceVariant,
    shape: Shape = CardDefaults.shape,
    content: @Composable (ColumnScope.() -> Unit),
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation,
        ),
        colors = CardDefaults.cardColors(
            containerColor = cardColour,
        ),
        modifier = modifier
            .fillMaxWidth(),
        shape = shape,
        content = content,
    )
}

@Composable
fun BethCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    JerryCard(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
            )

            HorizontalDivider(
                thickness = 2.dp,
            )

            Text(
                text = description,
                style = MaterialTheme.typography.headlineSmall,
            )
        }
    }
}

@GeneralPreview
@Composable
fun BethCardPreview(
    modifier: Modifier = Modifier,
) {
    PreviewComposable {
        BethCard(
            title = "Sleepy Gary",
            description = "what can I say",
            modifier = modifier,
        )
    }
}
