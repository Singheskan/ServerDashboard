package org.websitedashboard

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

/**
 * DashboardController manages the web interface, handling the display of the dashboard
 * and actions for adding URLs and updating settings.
 */
@Controller
class DashboardController(
    private val serverDashboardService: ServerDashboardService,
    private val settingsService: SettingsService
) {

    /**
     * Displays the dashboard, which includes the status of all monitored URLs.
     * @param model - The model passed to the view, containing the necessary data.
     * @return the name of the view ("dashboard") to be rendered.
     */
    @GetMapping("/")
    fun showDashboard(model: Model): String {
        val urlStatuses = serverDashboardService.getUrlStatuses()
        model.addAttribute("urlStatuses", urlStatuses)
        model.addAttribute("emailEnabled", settingsService.isEmailEnabled())
        model.addAttribute("canToggleEmail", settingsService.canToggleEmail())
        return "dashboard"
    }

    /**
     * Updates the user settings, specifically whether email notifications are enabled.
     * @param emailEnabled - Indicates whether email notifications should be enabled.
     * @return a redirect to the main dashboard page.
     */
    @PostMapping("/update-settings")
    fun updateSettings(@RequestParam(name = "emailEnabled", required = false) emailEnabled: Boolean?): String {
        settingsService.updateEmailEnabled(emailEnabled ?: false)
        return "redirect:/"
    }

    /**
     * Adds a new website to the monitoring list and returns the current status of all URLs.
     * This method is called when a user adds a new URL.
     * @param newWebsite - The URL to be added to the monitoring list.
     * @return a list of maps containing the URLs and their current status.
     */
    @PostMapping("/add-website")
    @ResponseBody
    fun addWebsite(@RequestParam(name = "newWebsite") newWebsite: String): List<Map<String, Any>> {
        serverDashboardService.addWebsite(newWebsite)
        return serverDashboardService.getUrlStatuses().map { (url, status) ->
            mapOf("url" to url, "status" to status)
        }
    }
}
