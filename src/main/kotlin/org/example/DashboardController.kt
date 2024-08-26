package org.example

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class DashboardController(
    private val serverDashboardService: ServerDashboardService,
    private val settingsService: SettingsService
) {

    @GetMapping("/")
    fun showDashboard(model: Model): String {
        val urlStatuses = serverDashboardService.getUrlStatuses()
        model.addAttribute("urlStatuses", urlStatuses)
        model.addAttribute("emailEnabled", settingsService.isEmailEnabled())
        return "dashboard"
    }

    @PostMapping("/update-settings")
    fun updateSettings(@RequestParam(name = "emailEnabled", required = false) emailEnabled: Boolean?): String {
        settingsService.updateEmailEnabled(emailEnabled ?: false)
        return "redirect:/"
    }

    @PostMapping("/add-website")
    fun addWebsite(@RequestParam(name = "newWebsite") newWebsite: String): String {
        serverDashboardService.addWebsite(newWebsite)
        return "redirect:/"
    }
}
