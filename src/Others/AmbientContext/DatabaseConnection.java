package Others.AmbientContext;

/**
 * Database Connection (Simulated)
 *
 * Simulates database operations using ambient contexts.
 * Uses TransactionContext, UserContext, and LogContext.
 *
 * This demonstrates how ambient contexts eliminate the need to pass
 * context objects through method parameters.
 */
public class DatabaseConnection {

    /**
     * Execute a SQL query (simulated)
     *
     * Notice: No parameters for user, transaction, or logging!
     * Everything comes from ambient context.
     *
     * @param sql The SQL to execute
     * @return Number of rows affected
     */
    public static int executeUpdate(String sql) {
        // Get context from ambient contexts
        String username = UserContext.getCurrentUsername();
        String txId = TransactionContext.getCurrentTransactionId();

        LogContext.info("Executing SQL: " + sql);
        LogContext.debug("Executed by user: " + username);
        LogContext.debug("In transaction: " + txId);

        // Verify transaction is active
        if (!TransactionContext.isTransactionActive()) {
            LogContext.warn("No active transaction! SQL may not be atomic.");
        }

        // Simulate execution
        simulateDelay(30);

        LogContext.info("SQL executed successfully");
        return 1; // Rows affected
    }

    /**
     * Execute a SQL query (simulated)
     *
     * @param sql The SQL to execute
     * @return Result string
     */
    public static String executeQuery(String sql) {
        LogContext.info("Executing SELECT: " + sql);

        // Simulate execution
        simulateDelay(20);

        String result = "Query result (executed by " + UserContext.getCurrentUsername() + ")";
        LogContext.debug("Query returned: " + result);

        return result;
    }

    /**
     * Insert a record
     *
     * @param table Table name
     * @param data Data to insert
     */
    public static void insert(String table, String data) {
        String sql = "INSERT INTO " + table + " VALUES (" + data + ")";
        executeUpdate(sql);
    }

    /**
     * Update a record
     *
     * @param table Table name
     * @param data Data to update
     * @param condition Where condition
     */
    public static void update(String table, String data, String condition) {
        String sql = "UPDATE " + table + " SET " + data + " WHERE " + condition;
        executeUpdate(sql);
    }

    /**
     * Delete a record
     *
     * @param table Table name
     * @param condition Where condition
     */
    public static void delete(String table, String condition) {
        String sql = "DELETE FROM " + table + " WHERE " + condition;
        executeUpdate(sql);
    }

    /**
     * Simulate database delay
     */
    private static void simulateDelay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
