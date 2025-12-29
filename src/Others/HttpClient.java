package Others;

import java.util.Map;

/**
 * Options Pattern kullanarak yapılandırılan HTTP Client
 *
 * Bu sınıf, HttpClientOptions kullanarak esnek yapılandırma sağlar
 */
public class HttpClient {
    private final HttpClientOptions options;

    public HttpClient(HttpClientOptions options) {
        if (options == null) {
            throw new IllegalArgumentException("Options cannot be null");
        }
        this.options = options;
        initialize();
    }

    private void initialize() {
        if (options.isEnableLogging()) {
            log("HTTP Client initialized with options: " + options);
        }

        // Proxy yapılandırması
        if (options.getProxyConfig() != null) {
            configureProxy(options.getProxyConfig());
        }

        // Default header'ları ayarla
        if (!options.getDefaultHeaders().isEmpty()) {
            configureDefaultHeaders(options.getDefaultHeaders());
        }
    }

    private void configureProxy(HttpClientOptions.ProxyConfig proxyConfig) {
        log("Configuring proxy: " + proxyConfig.getHost() + ":" + proxyConfig.getPort());
        if (proxyConfig.hasCredentials()) {
            log("Proxy authentication enabled");
        }
    }

    private void configureDefaultHeaders(Map<String, String> headers) {
        log("Configuring " + headers.size() + " default headers");
    }

    /**
     * HTTP GET request simülasyonu
     */
    public String get(String endpoint) {
        String url = options.getBaseUrl() + endpoint;
        log("GET " + url);

        // Retry mekanizması
        int attempts = 0;
        while (attempts <= options.getMaxRetries()) {
            try {
                return performRequest("GET", url);
            } catch (Exception e) {
                attempts++;
                if (attempts > options.getMaxRetries()) {
                    throw new RuntimeException("Failed after " + attempts + " attempts", e);
                }
                log("Retry attempt " + attempts + "/" + options.getMaxRetries());
            }
        }

        return null;
    }

    /**
     * HTTP POST request simülasyonu
     */
    public String post(String endpoint, String body) {
        String url = options.getBaseUrl() + endpoint;
        log("POST " + url);
        log("Body: " + body);

        return performRequest("POST", url);
    }

    private String performRequest(String method, String url) {
        // Gerçek HTTP request simülasyonu
        log("Executing " + method + " request to: " + url);
        log("Timeout: " + options.getTimeout().getSeconds() + "s");
        log("Connection Timeout: " + options.getConnectionTimeout().getSeconds() + "s");
        log("User-Agent: " + options.getUserAgent());
        log("Follow Redirects: " + options.isFollowRedirects());

        // Simüle edilmiş response
        return "{\"status\": \"success\", \"message\": \"Response from " + url + "\"}";
    }

    private void log(String message) {
        if (options.isEnableLogging()) {
            System.out.println("[HttpClient] " + message);
        }
    }

    /**
     * Mevcut client için yeni options ile yeni bir client oluşturur
     */
    public HttpClient withOptions(HttpClientOptions newOptions) {
        return new HttpClient(newOptions);
    }

    public HttpClientOptions getOptions() {
        return options;
    }
}
