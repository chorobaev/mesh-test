package io.flaterlab.meshexam.feature.meshroom

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MeshRoomLauncher(
    val examId: String,
): Parcelable