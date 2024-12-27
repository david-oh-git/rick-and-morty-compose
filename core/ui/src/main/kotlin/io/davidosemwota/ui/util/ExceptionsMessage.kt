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
package io.davidosemwota.ui.util

import android.content.Context
import io.davidosemwota.ui.R
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/**
 * TO easily convert [Exception]s to human readable error messages.
 */
fun getDisplayMessage(
    context: Context,
    throwable: Throwable,
): String {
    Timber.d(throwable)
    return when (throwable) {
        is UnknownHostException -> context.getString(R.string.host_unreachable_msg)
        is SocketTimeoutException -> context.getString(R.string.connection_timed_out_msg)
        is SSLHandshakeException -> context.getString(R.string.ssl_error_msg)
        is ConnectException -> context.getString(R.string.connection_exception_msg)
        is IOException -> context.getString(R.string.io_exception_error)
        else -> context.getString(R.string.unknown_error_msg)
    }
}
