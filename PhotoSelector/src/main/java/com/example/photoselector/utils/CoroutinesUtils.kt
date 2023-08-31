/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.photoselector.utils

import kotlinx.coroutines.flow.SharingStarted

private const val StopTimeoutMillis: Long = 5000

/**
 * 用于 SharingStarted 与 StateFlow 一起使用以向 UI 公开数据
 *
 * 当 UI 停止观察时，上游流会保持活动状态一段时间，以允许系统从短期配置更改（例如轮换）中恢复过来。
 * 如果 UI 停止观察的时间较长，则会保留缓存，但会停止上游流。
 * 当 UI 返回时，将重播最新值，并再次执行上游流。
 * 这样做是为了在应用处于后台时节省资源，但允许用户在应用之间快速切换。
 */
val WhileUiSubscribed: SharingStarted = SharingStarted.WhileSubscribed(StopTimeoutMillis)
