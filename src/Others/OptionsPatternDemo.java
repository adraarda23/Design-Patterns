package Others;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Options Pattern Demonstration
 *
 * Options Pattern'in farklı kullanım senaryolarını gösterir
 */
public class OptionsPatternDemo {

    public static void main(String[] args) {
        System.out.println("=== OPTIONS PATTERN DEMO ===\n");

        // Scenario 1: Minimal Configuration
        minimalConfiguration();

        System.out.println("\n" + "=".repeat(50) + "\n");

        // Scenario 2: Full Configuration
        fullConfiguration();

        System.out.println("\n" + "=".repeat(50) + "\n");

        // Scenario 3: Production Configuration
        productionConfiguration();

        System.out.println("\n" + "=".repeat(50) + "\n");

        // Scenario 4: Development Configuration
        developmentConfiguration();
    }

    /**
     * Senaryo 1: Minimal yapılandırma - sadece zorunlu alanlar
     */
    private static void minimalConfiguration() {
        System.out.println("--- Scenario 1: Minimal Configuration ---");

        // Sadece base URL ile client oluştur (diğer tüm ayarlar varsayılan)
        HttpClientOptions options = new HttpClientOptions.Builder("https://api.example.com")
                .build();

        HttpClient client = new HttpClient(options);

        System.out.println("Options: " + options);
        System.out.println("\nMaking request...");
        String response = client.get("/users");
        System.out.println("Response: " + response);
    }

    /**
     * Senaryo 2: Tam yapılandırma - tüm özellikler aktif
     */
    private static void fullConfiguration() {
        System.out.println("--- Scenario 2: Full Configuration ---");

        // Tüm özellikleri içeren yapılandırma
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");

        HttpClientOptions.ProxyConfig proxy = new HttpClientOptions.ProxyConfig(
            "proxy.company.com",
            8080,
            "user",
            "pass"
        );

        HttpClientOptions options = new HttpClientOptions.Builder("https://api.example.com")
                .timeout(Duration.ofSeconds(60))
                .connectionTimeout(Duration.ofSeconds(20))
                .maxRetries(5)
                .followRedirects(true)
                .addHeaders(headers)
                .addHeader("Authorization", "Bearer token123")
                .enableLogging(true)
                .userAgent("MyApp/2.0")
                .proxy(proxy)
                .build();

        HttpClient client = new HttpClient(options);

        System.out.println("\nMaking request...");
        String response = client.get("/api/data");
        System.out.println("Response: " + response);
    }

    /**
     * Senaryo 3: Production ortamı yapılandırması
     */
    private static void productionConfiguration() {
        System.out.println("--- Scenario 3: Production Configuration ---");

        HttpClientOptions options = new HttpClientOptions.Builder("https://api.production.com")
                .timeout(Duration.ofSeconds(45))
                .connectionTimeout(Duration.ofSeconds(15))
                .maxRetries(3)
                .followRedirects(true)
                .addHeader("X-Environment", "production")
                .addHeader("X-API-Version", "v1")
                .enableLogging(false)  // Production'da loglama kapalı
                .userAgent("ProductionApp/1.0")
                .build();

        HttpClient client = new HttpClient(options);

        System.out.println("Options: " + options);
        System.out.println("\nMaking production request...");
        String response = client.post("/api/analytics", "{\"event\": \"page_view\"}");
        System.out.println("Response: " + response);
    }

    /**
     * Senaryo 4: Development ortamı yapılandırması
     */
    private static void developmentConfiguration() {
        System.out.println("--- Scenario 4: Development Configuration ---");

        HttpClientOptions options = new HttpClientOptions.Builder("http://localhost:3000")
                .timeout(Duration.ofMinutes(5))  // Uzun timeout (debugging için)
                .connectionTimeout(Duration.ofSeconds(30))
                .maxRetries(1)  // Az retry (hızlı fail için)
                .followRedirects(false)
                .addHeader("X-Environment", "development")
                .addHeader("X-Debug", "true")
                .enableLogging(true)  // Development'ta loglama açık
                .userAgent("DevApp/0.1-SNAPSHOT")
                .build();

        HttpClient client = new HttpClient(options);

        System.out.println("\nMaking development request...");
        String response = client.get("/api/test");
        System.out.println("Response: " + response);

        // Development'ta farklı ayarlarla denemeler yapabilme
        System.out.println("\n--- Testing with different timeouts ---");

        HttpClientOptions quickOptions = new HttpClientOptions.Builder("http://localhost:3000")
                .timeout(Duration.ofSeconds(5))
                .enableLogging(true)
                .build();

        HttpClient quickClient = new HttpClient(quickOptions);
        String quickResponse = quickClient.get("/api/quick");
        System.out.println("Quick Response: " + quickResponse);
    }
}
