# Server Dashboard

## Overview

Server Dashboard is a Kotlin-based Spring Boot application designed to monitor the availability of configured websites. The application periodically checks the status of websites and provides a web-based GUI to view the status in real-time. Additionally, it can send email notifications if a website becomes unreachable, although the email functionality is optional and can be disabled if email credentials are not provided.

## Features

- **Website Monitoring**: Periodically checks the status of configured websites.
- **Web Interface**: Provides a simple web-based GUI to view the status of websites.
- **Email Notifications**: Sends email notifications when a website becomes unreachable (optional).
- **Dynamic Configuration**: Add new websites via the web interface.
- **Graceful Degradation**: The application runs even if email configuration is missing or incorrect, disabling email notifications automatically.

## Technologies Used

- **Kotlin**: The programming language used for development.
- **Spring Boot**: The framework used to create the application.
- **Thymeleaf**: Used for rendering the web-based GUI.
- **JavaMailSender**: Used to send email notifications (optional).
- **Gradle**: Build and dependency management.

## Setup Instructions

### Prerequisites

- **JDK 17 or higher**: Ensure that Java is installed and configured.
- **Gradle**: Ensure Gradle is installed or use the Gradle wrapper provided with the project.

### Configuration

1. **Clone the repository**:

    ```bash
    git clone https://github.com/yourusername/server-dashboard.git
    cd server-dashboard
    ```

2. **Create an `application.properties` file**:

   Copy the example properties file and configure it according to your needs:

    ```bash
    cp src/main/resources/application.properties.example src/main/resources/application.properties
    ```

   Edit the `application.properties` file to set up your website URLs and (optionally) email configuration:

    ```properties
    # List of websites to monitor
    app.website.urls=https://example.com,https://another-example.com

    # Email configuration (optional)
    spring.mail.host=smtp.example.com
    spring.mail.port=587
    spring.mail.username=your-email@example.com
    spring.mail.password=your-email-password

    # Email recipient
    app.email.recipient=recipient@example.com

    # Interval between checks (in milliseconds)
    app.check.interval=60000
    ```

   **Note**: If email credentials are missing or incorrect, the application will still run, but email notifications will be disabled.

3. **Build and Run the Application**:

   You can run the application using Gradle:

    ```bash
    ./gradlew bootRun
    ```

   Or build the project and run the JAR file:

    ```bash
    ./gradlew build
    java -jar build/libs/server-dashboard-0.0.1-SNAPSHOT.jar
    ```

### Usage

1. **Accessing the Dashboard**:

   Once the application is running, open your web browser and navigate to:

    ```
    http://localhost:8081/
    ```

2. **Managing Websites**:

    - **Adding a Website**: Use the "Add New Website" form to add a new URL. The status of the new URL will be displayed immediately.
    - **Viewing Status**: The status of each website (Online/Offline) is shown in a table on the dashboard.

3. **Email Notifications**:

    - If email is configured correctly, the application will send notifications when a website becomes unreachable.
    - If email is not configured, the email checkbox on the dashboard will be disabled.

## Troubleshooting

- **Application Fails to Start**: Ensure that you have JDK 17 or higher installed and properly configured.
- **No Email Notifications**: Check the `application.properties` file to ensure email settings are correctly configured. If the credentials are missing or incorrect, the email functionality will be disabled, but the application will continue to run.
- **Adding Websites**: If the website is not appearing in the dashboard immediately after adding, ensure the application is running correctly and that there are no errors in the console.

## Contributing

If you'd like to contribute to this project, please fork the repository and use a feature branch. Pull requests are welcome.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

