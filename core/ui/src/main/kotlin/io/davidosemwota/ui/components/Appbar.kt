/*
 * MIT License
 *
 * Copyright (c) 2025   David Osemwota.
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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import io.davidosemwota.ui.GeneralPreview
import io.davidosemwota.ui.PreviewComposable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummerAppBar(
    modifier: Modifier = Modifier,
    actionIcon: @Composable (() -> Unit),
    navigationIcon: @Composable (() -> Unit),
    title: String = "",
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    CenterAlignedTopAppBar(
        title = {
            if (title.isNotEmpty()) {
                Text(text = title)
            }
        },
        actions = {
            actionIcon()
        },
        navigationIcon = navigationIcon,
        colors = colors,
        modifier = modifier,
        windowInsets = windowInsets,
        scrollBehavior = scrollBehavior,
    )
}

@Composable
fun AppbarIcon(
    icon: ImageVector,
    iconContentDescription: String,
    onIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 32.dp,
) {
    IconButton(
        modifier = modifier,
        onClick = onIconClick,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconContentDescription,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(iconSize),
        )
    }
}

@Composable
fun AppBarImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
    )

    when (painter.state) {
        AsyncImagePainter.State.Empty,
        is AsyncImagePainter.State.Loading,
        -> { }
        is AsyncImagePainter.State.Error -> { }
        is AsyncImagePainter.State.Success -> {
            Image(
                painter = painter,
                contentDescription = "Appbar image icon",
                modifier = modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .border(BorderStroke(1.dp, Color.Gray)),
            )
        }
    }
}

@GeneralPreview
@Composable
private fun PreviewAppBarImage(
    modifier: Modifier = Modifier,
) {
    AppBarImage(
        imageUrl = "",
    )
}

@GeneralPreview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PreviewSummerAppBar(
    modifier: Modifier = Modifier,
) {
    PreviewComposable {
        SummerAppBar(
            modifier = modifier,
            actionIcon = {
                AppbarIcon(
                    icon = Icons.AutoMirrored.Rounded.Send,
                    iconContentDescription = "",
                    onIconClick = { },
                )
            },
            navigationIcon = {
                AppbarIcon(
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    iconContentDescription = "",
                    onIconClick = { },
                )
            },
            title = "Summer appbar",
        )
    }
}
