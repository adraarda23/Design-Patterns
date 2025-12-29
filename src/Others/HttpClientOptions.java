package Others;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Options Pattern Example - HTTP Client Configuration
 *
 * Options Pattern, bir nesnenin yapılandırılmasını esnek ve okunabilir hale getiren bir tasarım desenidir.
 * Builder pattern ile birlikte kullanılarak fluent API oluşturur.
 */
public class HttpClientOptions {
    private final String baseUrl;
    private final Duration timeout;
    private final Duration connectionTimeout;
    private final int maxRetries;
    private final boolean followRedirects;
    private final Map<String, String> defaultHeaders;
    private final boolean enableLogging;
    private final String userAgent;
    private final ProxyConfig proxyConfig;

    private HttpClientOptions(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.timeout = builder.timeout;
        this.connectionTimeout = builder.connectionTimeout;
        this.maxRetries = builder.maxRetries;
        this.followRedirects = builder.followRedirects;
        this.defaultHeaders = new HashMap<>(builder.defaultHeaders);
        this.enableLogging = builder.enableLogging;
        this.userAgent = builder.userAgent;
        this.proxyConfig = builder.proxyConfig;
    }

    // Getters
    public String getBaseUrl() { return baseUrl; }
    public Duration getTimeout() { return timeout; }
    public Duration getConnectionTimeout() { return connectionTimeout; }
    public int getMaxRetries() { return maxRetries; }
    public boolean isFollowRedirects() { return followRedirects; }
    public Map<String, String> getDefaultHeaders() { return new HashMap<>(defaultHeaders); }
    public boolean isEnableLogging() { return enableLogging; }
    public String getUserAgent() { return userAgent; }
    public ProxyConfig getProxyConfig() { return proxyConfig; }

    /**
     * Builder sınıfı - Fluent API ile options oluşturulmasını sağlar
     */
    public static class Builder {
        // Zorunlu alanlar
        private final String baseUrl;

        // Opsiyonel alanlar - varsayılan değerlerle
        private Duration timeout = Duration.ofSeconds(30);
        private Duration connectionTimeout = Duration.ofSeconds(10);
        private int maxRetries = 3;
        private boolean followRedirects = true;
        private Map<String, String> defaultHeaders = new HashMap<>();
        private boolean enableLogging = false;
        private String userAgent = "HttpClient/1.0";
        private ProxyConfig proxyConfig = null;

        public Builder(String baseUrl) {
            if (baseUrl == null || baseUrl.trim().isEmpty()) {
                throw new IllegalArgumentException("Base URL cannot be null or empty");
            }
            this.baseUrl = baseUrl;
        }

        public Builder timeout(Duration timeout) {
            if (timeout.isNegative() || timeout.isZero()) {
                throw new IllegalArgumentException("Timeout must be positive");
            }
            this.timeout = timeout;
            return this;
        }

        public Builder connectionTimeout(Duration connectionTimeout) {
            if (connectionTimeout.isNegative() || connectionTimeout.isZero()) {
                throw new IllegalArgumentException("Connection timeout must be positive");
            }
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            if (maxRetries < 0) {
                throw new IllegalArgumentException("Max retries cannot be negative");
            }
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder followRedirects(boolean followRedirects) {
            this.followRedirects = followRedirects;
            return this;
        }

        public Builder addHeader(String key, String value) {
            this.defaultHeaders.put(key, value);
            return this;
        }

        public Builder addHeaders(Map<String, String> headers) {
            this.defaultHeaders.putAll(headers);
            return this;
        }

        public Builder enableLogging(boolean enableLogging) {
            this.enableLogging = enableLogging;
            return this;
        }

        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public Builder proxy(ProxyConfig proxyConfig) {
            this.proxyConfig = proxyConfig;
            return this;
        }

        public HttpClientOptions build() {
            return new HttpClientOptions(this);
        }
    }

    /**
     * Proxy yapılandırması için nested class
     */
    public static class ProxyConfig {
        private final String host;
        private final int port;
        private final String username;
        private final String password;

        public ProxyConfig(String host, int port) {
            this(host, port, null, null);
        }

        public ProxyConfig(String host, int port, String username, String password) {
            this.host = host;
            this.port = port;
            this.username = username;
            this.password = password;
        }

        public String getHost() { return host; }
        public int getPort() { return port; }
        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public boolean hasCredentials() { return username != null && password != null; }
    }

    @Override
    public String toString() {
        return "HttpClientOptions{" +
                "baseUrl='" + baseUrl + '\'' +
                ", timeout=" + timeout.getSeconds() + "s" +
                ", connectionTimeout=" + connectionTimeout.getSeconds() + "s" +
                ", maxRetries=" + maxRetries +
                ", followRedirects=" + followRedirects +
                ", headerCount=" + defaultHeaders.size() +
                ", enableLogging=" + enableLogging +
                ", userAgent='" + userAgent + '\'' +
                ", hasProxy=" + (proxyConfig != null) +
                '}';
    }
}
