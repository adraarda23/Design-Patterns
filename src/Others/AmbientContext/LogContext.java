package Others.AmbientContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Log Context - Ambient Context Pattern Implementation
 *
 * Provides thread-local storage for logging context information.
 * This includes correlation ID, user info, and custom properties.
 *
 * Similar to MDC (Mapped Diagnostic Context) in Log4j/SLF4J.
 *
 * Key Features:
 * - ThreadLocal storage
 * - Correlation ID for request tracking
 * - Automatic user/transaction info
 * - Custom properties
 *
 * Usage:
 * <pre>
 * // Set correlation ID (e.g., at request entry)
 * LogContext.setCorrelationId("REQ-12345");
 *
 * // Log from anywhere
 * LogContext.info("Processing order");
 * // Output: [2024-01-15 10:30:45] [REQ-12345] [john] INFO: Processing order
 *
 * // Clear when done
 * LogContext.clear();
 * </pre>
 */
public class LogContext {

    // ThreadLocal storage for correlation ID
    private static final ThreadLocal<String> correlationId = new ThreadLocal<>();

    // ThreadLocal storage for custom properties
    private static final ThreadLocal<Map<String, String>> properties = ThreadLocal.withInitial(HashMap::new);

    // Date formatter
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Private constructor - this is a static utility class
     */
    private LogContext() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Set correlation ID for this thread
     *
     * @param id The correlation ID
     */
    public static void setCorrelationId(String id) {
        correlationId.set(id);
        System.out.println("[LogContext] Correlation ID set: " + id);
    }

    /**
     * Generate and set a new correlation ID
     *
     * @return The generated correlation ID
     */
    public static String generateCorrelationId() {
        String id = "CID-" + UUID.randomUUID().toString().substring(0, 8);
        setCorrelationId(id);
        return id;
    }

    /**
     * Get the current correlation ID
     *
     * @return Correlation ID, or "none" if not set
     */
    public static String getCorrelationId() {
        String id = correlationId.get();
        return id != null ? id : "none";
    }

    /**
     * Set a custom property
     *
     * @param key Property key
     * @param value Property value
     */
    public static void setProperty(String key, String value) {
        properties.get().put(key, value);
    }

    /**
     * Get a custom property
     *
     * @param key Property key
     * @return Property value, or null if not set
     */
    public static String getProperty(String key) {
        return properties.get().get(key);
    }

    /**
     * Clear all context (correlation ID and properties)
     */
    public static void clear() {
        String id = correlationId.get();
        if (id != null) {
            System.out.println("[LogContext] Context cleared: " + id);
        }

        correlationId.remove();
        properties.remove();
    }

    /**
     * Log INFO level message
     *
     * @param message The message to log
     */
    public static void info(String message) {
        log("INFO", message);
    }

    /**
     * Log DEBUG level message
     *
     * @param message The message to log
     */
    public static void debug(String message) {
        log("DEBUG", message);
    }

    /**
     * Log WARN level message
     *
     * @param message The message to log
     */
    public static void warn(String message) {
        log("WARN", message);
    }

    /**
     * Log ERROR level message
     *
     * @param message The message to log
     */
    public static void error(String message) {
        log("ERROR", message);
    }

    /**
     * Log ERROR level message with exception
     *
     * @param message The message to log
     * @param throwable The exception
     */
    public static void error(String message, Throwable throwable) {
        log("ERROR", message + " - Exception: " + throwable.getMessage());
    }

    /**
     * Internal log method with full context
     *
     * Format: [timestamp] [correlationId] [user] [transaction] LEVEL: message
     */
    private static void log(String level, String message) {
        StringBuilder sb = new StringBuilder();

        // Timestamp
        sb.append("[").append(LocalDateTime.now().format(TIME_FORMATTER)).append("] ");

        // Correlation ID
        sb.append("[").append(getCorrelationId()).append("] ");

        // User (from UserContext)
        if (UserContext.hasCurrentUser()) {
            sb.append("[User:").append(UserContext.getCurrentUsername()).append("] ");
        }

        // Transaction (from TransactionContext)
        if (TransactionContext.hasCurrentTransaction()) {
            sb.append("[TX:").append(TransactionContext.getCurrentTransactionId()).append("] ");
        }

        // Level and message
        sb.append(level).append(": ").append(message);

        System.out.println(sb);
    }

    /**
     * Execute code with a specific correlation ID
     *
     * Automatically sets and clears the correlation ID.
     *
     * @param id The correlation ID
     * @param action The action to execute
     */
    public static void executeWithCorrelationId(String id, Runnable action) {
        String previousId = correlationId.get();

        try {
            setCorrelationId(id);
            action.run();
        } finally {
            if (previousId != null) {
                correlationId.set(previousId);
            } else {
                clear();
            }
        }
    }

    /**
     * Print current context information
     */
    public static void printContext() {
        System.out.println("\n=== Log Context Info ===");
        System.out.println("Correlation ID: " + getCorrelationId());
        System.out.println("User: " + UserContext.getCurrentUsername());
        System.out.println("Transaction: " + TransactionContext.getCurrentTransactionId());

        Map<String, String> props = properties.get();
        if (!props.isEmpty()) {
            System.out.println("Properties:");
            props.forEach((k, v) -> System.out.println("  " + k + " = " + v));
        }

        System.out.println("========================\n");
    }
}
