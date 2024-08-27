package org.websitedashboard

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import java.net.URI

/**
 * ServerDashboardService is the central service responsible for monitoring websites.
 * It checks the availability of the configured URLs and optionally sends email notifications on errors.
 */
@Service
class ServerDashboardService(
    private val mailSender: JavaMailSender?,
    private val restTemplate: RestTemplate,
    private val settingsService: SettingsService
) {

    @Value("\${app.email.recipient}")
    private lateinit var recipientEmail: String

    @Value("\${app.website.urls}")
    private val websiteUrls: MutableList<String> = mutableListOf()

    private val urlStatuses: MutableMap<String, Boolean> = mutableMapOf()

    /**
     * Scheduled task that periodically checks the availability of websites.
     * The checks are performed at a fixed interval defined in the application properties.
     */
    @Scheduled(fixedDelayString = "\${app.check.interval}")
    fun checkWebsites() {
        println("Checking websites...")
        websiteUrls.forEach { url ->
            try {
                println("Checking URL: $url")
                val response = restTemplate.getForEntity(URI(url), String::class.java)
                println("Response from $url: ${response.statusCode}")
                urlStatuses[url] = response.statusCode.is2xxSuccessful
                if (!response.statusCode.is2xxSuccessful && settingsService.isEmailEnabled()) {
                    sendFailureNotification(url, response.statusCode)
                }
            } catch (ex: HttpClientErrorException) {
                println("Client error when checking $url: ${ex.statusCode.value()}")
                urlStatuses[url] = false
                if (settingsService.isEmailEnabled()) {
                    sendFailureNotification(url, ex.statusCode.value())
                }
            } catch (ex: HttpServerErrorException) {
                println("Server error when checking $url: ${ex.statusCode.value()}")
                urlStatuses[url] = false
                if (settingsService.isEmailEnabled()) {
                    sendFailureNotification(url, ex.statusCode.value())
                }
            } catch (ex: ResourceAccessException) {
                println("Timeout or connection issue when checking $url: ${ex.message}")
                urlStatuses[url] = false
                if (settingsService.isEmailEnabled()) {
                    sendFailureNotification(url, "Timeout or Connection issue")
                }
            } catch (ex: Exception) {
                println("Exception when checking $url: ${ex.message}")
                urlStatuses[url] = false
                if (settingsService.isEmailEnabled()) {
                    sendFailureNotification(url, null)
                }
            }
        }
    }

    /**
     * Adds a new website to the monitoring list and immediately checks its availability.
     * @param newWebsite - The website URL to be added.
     */
    fun addWebsite(newWebsite: String) {
        websiteUrls.add(newWebsite)
        checkWebsites()
    }

    /**
     * Returns the current status of all monitored URLs.
     * @return a map associating each URL with its last known status.
     */
    fun getUrlStatuses(): Map<String, Boolean> = urlStatuses

    /**
     * Sends an email notification if a website check fails.
     * @param url - The URL that failed the check.
     * @param statusCode - The HTTP status code or type of error.
     */
    private fun sendFailureNotification(url: String, statusCode: Any?) {
        if (mailSender == null) {
            println("MailSender is not configured. Email notifications are disabled.")
            return
        }

        // Create and configure the email message
        val message = SimpleMailMessage()
        message.setTo(recipientEmail)
        message.subject = "Website Check Failed"
        message.text = "The website $url is not reachable. Status code: ${statusCode ?: "Unknown"}"

        // Send the email
        mailSender.send(message)
    }
}
