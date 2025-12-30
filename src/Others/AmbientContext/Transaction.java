package Others.AmbientContext;

import java.util.UUID;

/**
 * Transaction
 *
 * Represents a database transaction.
 * Managed by TransactionContext (Ambient Context).
 */
public class Transaction {
    private String id;
    private boolean isActive;
    private boolean isCommitted;
    private boolean isRolledBack;

    public Transaction() {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.isActive = true;
        this.isCommitted = false;
        this.isRolledBack = false;
        log("Transaction created");
    }

    /**
     * Commit the transaction
     */
    public void commit() {
        if (!isActive) {
            throw new IllegalStateException("Transaction is not active");
        }
        if (isCommitted) {
            throw new IllegalStateException("Transaction already committed");
        }
        if (isRolledBack) {
            throw new IllegalStateException("Transaction already rolled back");
        }

        log("Committing transaction...");
        simulateDelay(50);
        isCommitted = true;
        isActive = false;
        log("Transaction committed successfully ✓");
    }

    /**
     * Rollback the transaction
     */
    public void rollback() {
        if (!isActive) {
            log("Transaction already closed, nothing to rollback");
            return;
        }
        if (isCommitted) {
            throw new IllegalStateException("Cannot rollback committed transaction");
        }

        log("Rolling back transaction...");
        simulateDelay(30);
        isRolledBack = true;
        isActive = false;
        log("Transaction rolled back ↩️");
    }

    /**
     * Dispose the transaction (cleanup)
     */
    public void dispose() {
        if (isActive) {
            log("Transaction not committed, auto-rollback");
            rollback();
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isCommitted() {
        return isCommitted;
    }

    public boolean isRolledBack() {
        return isRolledBack;
    }

    /**
     * Simulate database delay
     */
    private void simulateDelay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Log transaction operations
     */
    private void log(String message) {
        System.out.println("[Transaction:" + id + "] " + message);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", active=" + isActive +
                ", committed=" + isCommitted +
                ", rolledBack=" + isRolledBack +
                '}';
    }
}
