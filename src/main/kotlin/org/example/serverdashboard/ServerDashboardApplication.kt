package org.example.serverdashboard

// General Imports
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

// Website Checker Service Imports
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.ResourceAccessException
import java.net.URI

// App config imports
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.http.client.SimpleClientHttpRequestFactory

// GUI
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class DashboardController(private val serverDashboardService: ServerDashboardService) {

	@GetMapping("/")
	fun showDashboard(model: Model): String {
		val urlStatuses = serverDashboardService.getUrlStatuses()
		model.addAttribute("urlStatuses", urlStatuses)
		return "dashboard"
	}
}

@Configuration
class AppConfig {

	@Bean
	fun restTemplate(): RestTemplate {
		val factory = SimpleClientHttpRequestFactory()
		factory.setConnectTimeout(10000)  // 5 seconds connection timeout
		factory.setReadTimeout(10000)     // 5 seconds read timeout
		return RestTemplate(factory)
	}
}


@Service
class ServerDashboardService(
	private val mailSender: JavaMailSender,
	private val restTemplate: RestTemplate
) {

	@Value("\${app.email.recipient}")
	private lateinit var recipientEmail: String

	@Value("\${app.website.urls}")
	private lateinit var websiteUrls: List<String>

	private val urlStatuses: MutableMap<String, Boolean> = mutableMapOf()

	@Scheduled(fixedDelayString = "\${app.check.interval}")
	fun checkWebsites() {
		println("Checking websites...")
		websiteUrls.forEach { url ->
			try {
				println("Checking URL: $url")
				val response = restTemplate.getForEntity(URI(url), String::class.java)
				println("Response from $url: ${response.statusCode}")
				if (response.statusCode.is2xxSuccessful) {
					urlStatuses[url] = true
				} else {
					urlStatuses[url] = false
					sendFailureNotification(url, response.statusCodeValue)
				}
			} catch (ex: HttpClientErrorException) {
				println("Client error when checking $url: ${ex.statusCode.value()}")
				urlStatuses[url] = false
				sendFailureNotification(url, ex.statusCode.value())
			} catch (ex: HttpServerErrorException) {
				println("Server error when checking $url: ${ex.statusCode.value()}")
				urlStatuses[url] = false
				sendFailureNotification(url, ex.statusCode.value())
			} catch (ex: ResourceAccessException) {
				println("Timeout or connection issue when checking $url: ${ex.message}")
				urlStatuses[url] = false
				sendFailureNotification(url, "Timeout or Connection issue")
			} catch (ex: Exception) {
				println("Exception when checking $url: ${ex.message}")
				urlStatuses[url] = false
				sendFailureNotification(url, null)
			}
		}
	}

	fun getUrlStatuses(): Map<String, Boolean> {
		return urlStatuses
	}

	private fun sendFailureNotification(url: String, statusCode: Any?) {
		val message = SimpleMailMessage()
		message.setTo(recipientEmail)
		message.subject = "Website Check Failed"
		message.text = "The website $url is not reachable. Status code: ${statusCode ?: "Unknown"}"
		mailSender.send(message)
	}
}



@SpringBootApplication
@EnableScheduling
class ServerDashboardApplication

fun main(args: Array<String>) {
	runApplication<ServerDashboardApplication>(*args)
}
