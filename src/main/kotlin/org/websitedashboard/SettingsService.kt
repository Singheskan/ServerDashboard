package org.websitedashboard

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class SettingsService(
    @Autowired(required = false) private val mailSender: JavaMailSender?  // Inject JavaMailSender if available
) {

    private var emailEnabled: Boolean = mailSender != null

    fun canToggleEmail(): Boolean = emailEnabled

    fun isEmailEnabled(): Boolean = emailEnabled

    fun updateEmailEnabled(enabled: Boolean) {
        if (mailSender != null) {
            this.emailEnabled = enabled
        } else {
            this.emailEnabled = false
        }
    }
}