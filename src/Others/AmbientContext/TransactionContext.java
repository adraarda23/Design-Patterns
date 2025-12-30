package Others.AmbientContext;

/**
 * Transaction Context - Ambient Context Pattern Implementation
 *
 * Provides thread-local storage for the current database transaction.
 * Works with TransactionScope for automatic transaction management.
 *
 * Key Features:
 * - ThreadLocal storage (each thread has its own transaction)
 * - Auto-commit/rollback with TransactionScope
 * - Nested transaction support
 *
 * Usage:
 * <pre>
 * try (TransactionScope scope = TransactionContext.begin()) {
 *     // Do database work
 *     // Transaction is available via TransactionContext.getCurrent()
 *
 *     scope.complete(); // Mark as successful
 * } // Auto-commit if complete() was called, auto-rollback otherwise
 * </pre>
 */
public class TransactionContext {

    // ThreadLocal storage - each thread has its own Transaction
    private static final ThreadLocal<Transaction> currentTransaction = new ThreadLocal<>();

    /**
     * Private constructor - this is a static utility class
     */
    private TransactionContext() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Begin a new transaction scope
     *
     * This is the primary way to start a transaction.
     * Use with try-with-resources for automatic cleanup.
     *
     * @return A new TransactionScope
     */
    public static TransactionScope begin() {
        // Check if there's already a transaction
        Transaction existing = currentTransaction.get();

        if (existing != null && existing.isActive()) {
            log("⚠️  Warning: Transaction already exists in context. Creating nested scope.");
            // In a real implementation, you might want to handle nested transactions differently
        }

        // Create new transaction
        Transaction transaction = new Transaction();
        currentTransaction.set(transaction);

        log("Transaction scope created (Thread: " + Thread.currentThread().getName() + ")");

        return new TransactionScope(transaction);
    }

    /**
     * Get the current transaction for this thread
     *
     * @return The current transaction, or null if not set
     */
    public static Transaction getCurrent() {
        return currentTransaction.get();
    }

    /**
     * Get the current transaction, throw exception if not set
     *
     * @return The current transaction
     * @throws IllegalStateException if no transaction is active
     */
    public static Transaction getCurrentRequired() {
        Transaction transaction = currentTransaction.get();

        if (transaction == null) {
            throw new IllegalStateException(
                "No transaction in context. Did you forget to call TransactionContext.begin()?"
            );
        }

        return transaction;
    }

    /**
     * Check if there is a current transaction
     *
     * @return true if transaction exists, false otherwise
     */
    public static boolean hasCurrentTransaction() {
        return currentTransaction.get() != null;
    }

    /**
     * Check if the current transaction is active
     *
     * @return true if transaction is active, false otherwise
     */
    public static boolean isTransactionActive() {
        Transaction tx = currentTransaction.get();
        return tx != null && tx.isActive();
    }

    /**
     * Clear the current transaction from this thread
     *
     * This is typically called by TransactionScope.close()
     */
    static void clear() {
        Transaction tx = currentTransaction.get();

        if (tx != null) {
            log("Transaction context cleared (Thread: " + Thread.currentThread().getName() + ")");
        }

        currentTransaction.remove();
    }

    /**
     * Execute code within a transaction
     *
     * Automatically creates transaction scope and commits if successful.
     *
     * @param action The action to execute
     * @throws Exception if the action fails
     */
    public static void executeInTransaction(Runnable action) {
        try (TransactionScope scope = begin()) {
            action.run();
            scope.complete();
        }
    }

    /**
     * Get the current transaction ID
     *
     * @return Transaction ID, or "none" if no transaction
     */
    public static String getCurrentTransactionId() {
        Transaction tx = getCurrent();
        return tx != null ? tx.getId() : "none";
    }

    /**
     * Log context operations
     */
    private static void log(String message) {
        System.out.println("[TransactionContext] " + message);
    }
}
