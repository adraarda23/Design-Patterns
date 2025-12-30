package Others.UnitOfWork;

import java.util.*;

/**
 * Unit of Work Pattern Implementation
 *
 * Tracks changes to objects and coordinates the writing out of changes
 * in a single transaction.
 *
 * Key Responsibilities:
 * 1. Track new objects (to be inserted)
 * 2. Track dirty objects (to be updated)
 * 3. Track deleted objects (to be removed)
 * 4. Commit all changes in a single transaction
 * 5. Rollback on failure
 */
public class UnitOfWork {

    // Lists to track different types of changes
    private List<Object> newObjects = new ArrayList<>();
    private List<Object> dirtyObjects = new ArrayList<>();
    private List<Object> deletedObjects = new ArrayList<>();

    // Transaction state
    private boolean isActive = false;

    /**
     * Register a new object to be inserted
     *
     * @param obj The object to insert
     */
    public void registerNew(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Cannot register null object");
        }

        if (!newObjects.contains(obj) && !dirtyObjects.contains(obj)) {
            newObjects.add(obj);
            log("Registered NEW: " + obj.getClass().getSimpleName() + " - " + obj);
        }
    }

    /**
     * Register a modified object to be updated
     *
     * @param obj The object to update
     */
    public void registerDirty(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Cannot register null object");
        }

        // Don't mark new objects as dirty (they'll be inserted anyway)
        if (!dirtyObjects.contains(obj) && !newObjects.contains(obj)) {
            dirtyObjects.add(obj);
            log("Registered DIRTY: " + obj.getClass().getSimpleName() + " - " + obj);
        }
    }

    /**
     * Register an object to be deleted
     *
     * @param obj The object to delete
     */
    public void registerDeleted(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Cannot register null object");
        }

        // Remove from new or dirty if present
        newObjects.remove(obj);
        dirtyObjects.remove(obj);

        if (!deletedObjects.contains(obj)) {
            deletedObjects.add(obj);
            log("Registered DELETED: " + obj.getClass().getSimpleName() + " - " + obj);
        }
    }

    /**
     * Commit all changes in a single transaction
     *
     * This is where all tracked changes are persisted to the database.
     * Either all changes succeed or all fail (atomicity).
     */
    public void commit() {
        log("\n" + "═".repeat(60));
        log("COMMIT STARTED");
        log("═".repeat(60));
        log("Changes to commit:");
        log("  - New objects:     " + newObjects.size());
        log("  - Dirty objects:   " + dirtyObjects.size());
        log("  - Deleted objects: " + deletedObjects.size());
        log("─".repeat(60));

        try {
            beginTransaction();

            // 1. INSERT new objects
            if (!newObjects.isEmpty()) {
                log("\n[Phase 1: INSERT Operations]");
                for (Object obj : newObjects) {
                    insertObject(obj);
                }
            }

            // 2. UPDATE dirty objects
            if (!dirtyObjects.isEmpty()) {
                log("\n[Phase 2: UPDATE Operations]");
                for (Object obj : dirtyObjects) {
                    updateObject(obj);
                }
            }

            // 3. DELETE deleted objects
            if (!deletedObjects.isEmpty()) {
                log("\n[Phase 3: DELETE Operations]");
                for (Object obj : deletedObjects) {
                    deleteObject(obj);
                }
            }

            commitTransaction();
            clear();

            log("─".repeat(60));
            log("✓ COMMIT SUCCESSFUL - All changes persisted");
            log("═".repeat(60) + "\n");

        } catch (Exception e) {
            rollbackTransaction();
            log("─".repeat(60));
            log("✗ COMMIT FAILED - All changes rolled back");
            log("═".repeat(60) + "\n");
            throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
        }
    }

    /**
     * Rollback all pending changes
     */
    public void rollback() {
        log("\n" + "═".repeat(60));
        log("ROLLBACK - Discarding all pending changes");
        log("═".repeat(60) + "\n");
        clear();
    }

    /**
     * Clear all tracked objects
     */
    private void clear() {
        newObjects.clear();
        dirtyObjects.clear();
        deletedObjects.clear();
    }

    /**
     * Begin database transaction (simulated)
     */
    private void beginTransaction() {
        log("\n[DB] BEGIN TRANSACTION");
        isActive = true;
        simulateDelay(50);
    }

    /**
     * Commit database transaction (simulated)
     */
    private void commitTransaction() {
        log("\n[DB] COMMIT TRANSACTION");
        isActive = false;
        simulateDelay(50);
    }

    /**
     * Rollback database transaction (simulated)
     */
    private void rollbackTransaction() {
        log("\n[DB] ROLLBACK TRANSACTION");
        isActive = false;
        clear();
        simulateDelay(50);
    }

    /**
     * Insert object into database (simulated)
     */
    private void insertObject(Object obj) {
        log("  [DB] INSERT INTO " + getTableName(obj) + ": " + obj);
        simulateDelay(20);
    }

    /**
     * Update object in database (simulated)
     */
    private void updateObject(Object obj) {
        log("  [DB] UPDATE " + getTableName(obj) + ": " + obj);
        simulateDelay(20);
    }

    /**
     * Delete object from database (simulated)
     */
    private void deleteObject(Object obj) {
        log("  [DB] DELETE FROM " + getTableName(obj) + ": " + obj);
        simulateDelay(20);
    }

    /**
     * Get table name from object class (simulated)
     */
    private String getTableName(Object obj) {
        return obj.getClass().getSimpleName().toLowerCase() + "s";
    }

    /**
     * Simulate database operation delay
     */
    private void simulateDelay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Log message
     */
    private void log(String message) {
        System.out.println("[UnitOfWork] " + message);
    }

    /**
     * Get count of pending changes
     */
    public int getPendingChangesCount() {
        return newObjects.size() + dirtyObjects.size() + deletedObjects.size();
    }

    /**
     * Check if there are pending changes
     */
    public boolean hasPendingChanges() {
        return getPendingChangesCount() > 0;
    }

    /**
     * Print current state
     */
    public void printState() {
        System.out.println("\n=== Unit of Work State ===");
        System.out.println("New objects:     " + newObjects.size());
        System.out.println("Dirty objects:   " + dirtyObjects.size());
        System.out.println("Deleted objects: " + deletedObjects.size());
        System.out.println("Total pending:   " + getPendingChangesCount());
        System.out.println("Transaction active: " + isActive);
        System.out.println("==========================\n");
    }
}
