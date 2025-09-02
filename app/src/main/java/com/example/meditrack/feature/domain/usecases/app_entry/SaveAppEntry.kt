package com.example.meditrack.feature.domain.usecases.app_entry

import com.example.meditrack.feature.domain.LocalUserManger


class SaveAppEntry(
    private val localUserManger: LocalUserManger,
) {

    suspend operator fun invoke() {
        localUserManger.saveAppEntry()
    }

}