package Others.AmbientContext;

/**
 * Transaction Scope
 *
 * Provides automatic transaction management using try-with-resources.
 * This is a key component of Ambient Context pattern.
 *
 * Usage:
 * <pre>
 * try (TransactionScope scope = TransactionContext.begin()) {
 *     // Do work
 *     scope.complete(); // Mark as successful
 * } // Auto-commit if complete() called, auto-rollback otherwise
 * </pre>
 */
public class TransactionScope implements AutoCloseable {
    private final Transaction transaction;
    private boolean isCompleted;

    /**
     * Constructor - package private, created by TransactionContext
     */
    TransactionScope(Transaction transaction) {
        this.transaction = transaction;
        this.isCompleted = false;
    }

    /**
     * Mark the transaction scope as complete (ready to commit)
     *
     * If this is not called, the transaction will be rolled back on close()
     */
    public void complete() {
        if (isCompleted) {
            throw new IllegalStateException("Transaction scope already completed");
        }
        isCompleted = true;
        log("Scope marked as complete");
    }

    /**
     * Get the current transaction
     */
    public Transaction getTransaction() {
        return transaction;
    }

    /**
     * Auto-close: Commit if complete, rollback otherwise
     *
     * This is called automatically when exiting try-with-resources block
     */
    @Override
    public void close() {
        try {
            if (isCompleted && transaction.isActive()) {
                log("Scope closing - committing transaction");
                transaction.commit();
            } else if (transaction.isActive()) {
                log("Scope closing - transaction not completed, rolling back");
                transaction.rollback();
            }
        } finally {
            // Clear from ambient context
            TransactionContext.clear();
        }
    }

    /**
     * Log scope operations
     */
    private void log(String message) {
        System.out.println("[TransactionScope] " + message);
    }
}
