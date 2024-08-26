package org.websitedashboard

import org.springframework.stereotype.Service

@Service
class SettingsService {

    private var emailEnabled: Boolean = true

    fun isEmailEnabled(): Boolean = emailEnabled

    fun updateEmailEnabled(enabled: Boolean) {
        this.emailEnabled = enabled
    }
}