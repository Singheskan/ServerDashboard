package org.websitedashboard

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

/**
 * Service responsible for managing email notification settings.
 */
@Service
class SettingsService(
    @Autowired(required = false) private val mailSender: JavaMailSender?  // Inject JavaMailSender if available
) {

    private var emailEnabled: Boolean = mailSender != null  // Initialize emailEnabled based on whether mailSender is available

    /**
     * Returns whether the email notifications can be toggled.
     * @return true if email notifications can be enabled/disabled, false otherwise.
     */
    fun canToggleEmail(): Boolean = emailEnabled

    /**
     * Checks if email notifications are currently enabled.
     * @return true if email notifications are enabled, false otherwise.
     */
    fun isEmailEnabled(): Boolean = emailEnabled

    /**
     * Updates the status of email notifications.
     * Only allows enabling if mailSender is configured.
     * @param enabled - The desired state of email notifications.
     */
    fun updateEmailEnabled(enabled: Boolean) {
        if (mailSender != null) {
            this.emailEnabled = enabled
        } else {
            this.emailEnabled = false
        }
    }
}
