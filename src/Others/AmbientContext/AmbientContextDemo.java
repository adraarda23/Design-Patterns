package Others.AmbientContext;

/**
 * Ambient Context Pattern Demonstration
 *
 * This demo shows different scenarios of using Ambient Context pattern:
 * 1. Basic usage with UserContext
 * 2. Transaction management with TransactionContext
 * 3. Logging with correlation ID
 * 4. Multi-threading behavior (ThreadLocal)
 * 5. Security and permission checks
 */
public class AmbientContextDemo {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║   AMBIENT CONTEXT PATTERN DEMONSTRATION        ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        // Scenario 1: Basic User Context
        scenario1_BasicUserContext();

        System.out.println("\n" + "=".repeat(80) + "\n");

        // Scenario 2: Transaction Context with Auto-Commit
        scenario2_TransactionAutoCommit();

        System.out.println("\n" + "=".repeat(80) + "\n");

        // Scenario 3: Transaction Rollback
        scenario3_TransactionRollback();

        System.out.println("\n" + "=".repeat(80) + "\n");

        // Scenario 4: Logging with Correlation ID
        scenario4_LoggingWithCorrelationId();

        System.out.println("\n" + "=".repeat(80) + "\n");

        // Scenario 5: Security - Permission Denied
        scenario5_SecurityCheck();

        System.out.println("\n" + "=".repeat(80) + "\n");

        // Scenario 6: Multi-threading (ThreadLocal behavior)
        scenario6_MultiThreading();
    }

    /**
     * SCENARIO 1: Basic User Context
     *
     * Demonstrates:
     * - Setting current user
     * - Accessing user from anywhere in call chain
     * - No need to pass user as parameter
     */
    private static void scenario1_BasicUserContext() {
        System.out.println("┌────────────────────────────────────────────────┐");
        System.out.println("│  Scenario 1: Basic User Context               │");
        System.out.println("└────────────────────────────────────────────────┘\n");

        // Create user
        User alice = new User(1L, "alice", "alice@example.com", "CUSTOMER");

        // Set user in ambient context
        UserContext.setCurrent(alice);

        // Now anywhere in the application can access current user
        System.out.println("\n--- Accessing User from Different Layers ---");
        accessUserFromDifferentLayers();

        // Always clear when done!
        UserContext.clear();
        System.out.println("\n✓ Context cleared");
    }

    private static void accessUserFromDifferentLayers() {
        // Layer 1: Service
        System.out.println("Service layer - Current user: " + UserContext.getCurrentUsername());

        // Layer 2: Repository
        System.out.println("Repository layer - Current user ID: " + UserContext.getCurrentUserId());

        // Layer 3: Utility
        System.out.println("Utility layer - User email: " +
                (UserContext.hasCurrentUser() ? UserContext.getCurrent().getEmail() : "none"));
    }

    /**
     * SCENARIO 2: Transaction Auto-Commit
     *
     * Demonstrates:
     * - TransactionScope with try-with-resources
     * - Auto-commit when complete() is called
     * - Using OrderService without passing transaction
     */
    private static void scenario2_TransactionAutoCommit() {
        System.out.println("┌────────────────────────────────────────────────┐");
        System.out.println("│  Scenario 2: Transaction Auto-Commit          │");
        System.out.println("└────────────────────────────────────────────────┘\n");

        // Setup contexts
        User bob = new User(2L, "bob", "bob@example.com", "CUSTOMER");
        UserContext.setCurrent(bob);
        LogContext.setCorrelationId("REQ-001");

        // Use service
        OrderService orderService = new OrderService();

        try {
            orderService.placeOrder(101L, 5);
        } finally {
            // Cleanup
            UserContext.clear();
            LogContext.clear();
        }
    }

    /**
     * SCENARIO 3: Transaction Rollback
     *
     * Demonstrates:
     * - Auto-rollback when complete() is NOT called
     * - Exception handling with transactions
     */
    private static void scenario3_TransactionRollback() {
        System.out.println("┌────────────────────────────────────────────────┐");
        System.out.println("│  Scenario 3: Transaction Rollback             │");
        System.out.println("└────────────────────────────────────────────────┘\n");

        User charlie = new User(3L, "charlie", "charlie@example.com", "CUSTOMER");
        UserContext.setCurrent(charlie);
        LogContext.setCorrelationId("REQ-002");

        System.out.println("Attempting transaction that will fail...\n");

        try (TransactionScope scope = TransactionContext.begin()) {
            LogContext.info("Step 1: Insert order");
            DatabaseConnection.insert("orders", "'data1'");

            LogContext.info("Step 2: Update stock");
            DatabaseConnection.update("products", "stock = stock - 5", "id = 101");

            // Simulate error - DON'T call scope.complete()
            LogContext.error("Simulated error occurred!");
            throw new RuntimeException("Simulated error");

            // scope.complete() NOT called - will auto-rollback!

        } catch (RuntimeException e) {
            System.out.println("\n❌ Error caught: " + e.getMessage());
            System.out.println("✓ Transaction automatically rolled back");
        } finally {
            UserContext.clear();
            LogContext.clear();
        }
    }

    /**
     * SCENARIO 4: Logging with Correlation ID
     *
     * Demonstrates:
     * - Correlation ID for request tracking
     * - Logs automatically include user and transaction info
     * - No need to pass logger around
     */
    private static void scenario4_LoggingWithCorrelationId() {
        System.out.println("┌────────────────────────────────────────────────┐");
        System.out.println("│  Scenario 4: Logging with Correlation ID      │");
        System.out.println("└────────────────────────────────────────────────┘\n");

        User david = new User(4L, "david", "david@example.com", "CUSTOMER");
        UserContext.setCurrent(david);

        // Generate correlation ID
        String correlationId = LogContext.generateCorrelationId();
        System.out.println("Processing request: " + correlationId + "\n");

        // All logs now include correlation ID, user, and transaction
        LogContext.info("Request received");
        LogContext.debug("Validating input");

        try (TransactionScope scope = TransactionContext.begin()) {
            LogContext.info("Processing in transaction");
            DatabaseConnection.executeQuery("SELECT * FROM orders");
            scope.complete();
        }

        LogContext.info("Request completed");

        // Show full context
        LogContext.printContext();

        // Cleanup
        UserContext.clear();
        LogContext.clear();
    }

    /**
     * SCENARIO 5: Security Check
     *
     * Demonstrates:
     * - Permission checking via UserContext
     * - Security exceptions
     * - Role-based access control
     */
    private static void scenario5_SecurityCheck() {
        System.out.println("┌────────────────────────────────────────────────┐");
        System.out.println("│  Scenario 5: Security Check                   │");
        System.out.println("└────────────────────────────────────────────────┘\n");

        // Regular user (not admin)
        User emma = new User(5L, "emma", "emma@example.com", "CUSTOMER");
        UserContext.setCurrent(emma);
        LogContext.setCorrelationId("REQ-003");

        OrderService orderService = new OrderService();

        System.out.println("User 'emma' (CUSTOMER) trying to cancel order...\n");

        try {
            orderService.cancelOrder(123L);
            System.out.println("❌ Should not reach here!");
        } catch (SecurityException e) {
            System.out.println("✓ Access denied (as expected): " + e.getMessage());
        }

        System.out.println("\n--- Now with ADMIN user ---\n");

        // Admin user
        User admin = new User(99L, "admin", "admin@example.com", "ADMIN");
        UserContext.setCurrent(admin);
        LogContext.setCorrelationId("REQ-004");

        System.out.println("User 'admin' (ADMIN) trying to cancel order...\n");

        try {
            orderService.cancelOrder(123L);
            System.out.println("\n✓ Admin successfully cancelled order");
        } catch (SecurityException e) {
            System.out.println("❌ Should not reach here: " + e.getMessage());
        } finally {
            UserContext.clear();
            LogContext.clear();
        }
    }

    /**
     * SCENARIO 6: Multi-threading (ThreadLocal behavior)
     *
     * Demonstrates:
     * - Each thread has its own context
     * - Contexts don't interfere with each other
     * - ThreadLocal isolation
     */
    private static void scenario6_MultiThreading() {
        System.out.println("┌────────────────────────────────────────────────┐");
        System.out.println("│  Scenario 6: Multi-threading (ThreadLocal)    │");
        System.out.println("└────────────────────────────────────────────────┘\n");

        System.out.println("Starting 3 threads with different users...\n");

        // Create 3 threads with different users
        Thread thread1 = new Thread(() -> simulateRequest("frank", "REQ-T1", 1));
        Thread thread2 = new Thread(() -> simulateRequest("grace", "REQ-T2", 2));
        Thread thread3 = new Thread(() -> simulateRequest("henry", "REQ-T3", 3));

        // Start all threads
        thread1.start();
        thread2.start();
        thread3.start();

        // Wait for all threads to complete
        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n✓ All threads completed");
        System.out.println("✓ Each thread had its own isolated context");
    }

    /**
     * Simulate a request in a separate thread
     */
    private static void simulateRequest(String username, String correlationId, int userId) {
        // Each thread sets its own context
        User user = new User((long) userId, username, username + "@example.com", "CUSTOMER");
        UserContext.setCurrent(user);
        LogContext.setCorrelationId(correlationId);

        LogContext.info("Thread started");
        LogContext.info("Processing order...");

        // Simulate work
        try {
            Thread.sleep(100 + (userId * 50));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LogContext.info("Thread completed");

        // Cleanup
        UserContext.clear();
        LogContext.clear();
    }
}
