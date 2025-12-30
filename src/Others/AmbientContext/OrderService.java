package Others.AmbientContext;

/**
 * Order Service
 *
 * Business logic that uses Ambient Context pattern.
 *
 * Notice how methods don't need parameters for:
 * - Current user (from UserContext)
 * - Transaction (from TransactionContext)
 * - Logging context (from LogContext)
 *
 * All context is available "ambientally" through static contexts.
 */
public class OrderService {

    /**
     * Place an order
     *
     * Notice: No user, transaction, or logger parameters!
     * Everything comes from ambient context.
     *
     * @param productId Product to order
     * @param quantity Quantity to order
     */
    public void placeOrder(long productId, int quantity) {
        // Get current user from ambient context
        User currentUser = UserContext.getCurrentRequired();

        LogContext.info("─".repeat(60));
        LogContext.info("PLACE ORDER - Product: " + productId + ", Qty: " + quantity);
        LogContext.info("─".repeat(60));

        // Validate user permissions
        if (!currentUser.hasRole("CUSTOMER") && !currentUser.hasRole("ADMIN")) {
            LogContext.error("User doesn't have permission to place orders");
            throw new SecurityException("Access denied");
        }

        // Use transaction from ambient context
        try (TransactionScope scope = TransactionContext.begin()) {
            LogContext.info("Starting order placement...");

            // Simulate database operations
            // Notice: No need to pass user, transaction, or logger!
            validateStock(productId, quantity);
            reserveStock(productId, quantity);
            createOrder(productId, quantity);
            sendConfirmationEmail();

            // Mark transaction as complete (commit on scope close)
            scope.complete();
            LogContext.info("✓ Order placed successfully!");
        }
    }

    /**
     * Cancel an order
     *
     * Requires admin role (checked via UserContext)
     *
     * @param orderId Order to cancel
     */
    public void cancelOrder(long orderId) {
        LogContext.info("─".repeat(60));
        LogContext.info("CANCEL ORDER - Order ID: " + orderId);
        LogContext.info("─".repeat(60));

        // Check admin permission using ambient context
        try {
            UserContext.requireAdmin();
        } catch (SecurityException e) {
            LogContext.error("Permission denied", e);
            throw e;
        }

        try (TransactionScope scope = TransactionContext.begin()) {
            LogContext.info("Starting order cancellation...");

            restoreStock(orderId);
            markOrderCancelled(orderId);
            sendCancellationEmail();

            scope.complete();
            LogContext.info("✓ Order cancelled successfully!");
        }
    }

    /**
     * Get order status
     *
     * Read-only operation, no transaction needed
     *
     * @param orderId Order ID
     * @return Order status
     */
    public String getOrderStatus(long orderId) {
        LogContext.debug("Getting order status: " + orderId);

        // Simulate database read
        String result = DatabaseConnection.executeQuery(
                "SELECT status FROM orders WHERE id = " + orderId
        );

        LogContext.debug("Order status retrieved");
        return result;
    }

    /**
     * Process bulk orders
     *
     * Demonstrates transaction spanning multiple operations
     *
     * @param orders Array of order data
     */
    public void processBulkOrders(String[] orders) {
        LogContext.info("─".repeat(60));
        LogContext.info("BULK ORDER PROCESSING - " + orders.length + " orders");
        LogContext.info("─".repeat(60));

        try (TransactionScope scope = TransactionContext.begin()) {
            for (int i = 0; i < orders.length; i++) {
                LogContext.info("Processing order " + (i + 1) + "/" + orders.length);
                DatabaseConnection.insert("orders", orders[i]);
            }

            scope.complete();
            LogContext.info("✓ All " + orders.length + " orders processed!");
        }
    }

    // Private helper methods - all use ambient context internally

    private void validateStock(long productId, int quantity) {
        LogContext.debug("Validating stock for product: " + productId);
        // Simulate validation
        DatabaseConnection.executeQuery(
                "SELECT stock FROM products WHERE id = " + productId
        );
        LogContext.debug("Stock validation passed");
    }

    private void reserveStock(long productId, int quantity) {
        LogContext.info("Reserving stock: " + quantity + " units");
        DatabaseConnection.update(
                "products",
                "stock = stock - " + quantity,
                "id = " + productId
        );
    }

    private void createOrder(long productId, int quantity) {
        LogContext.info("Creating order in database");

        // Current user is retrieved from ambient context
        User user = UserContext.getCurrent();

        DatabaseConnection.insert(
                "orders",
                "user_id=" + user.getId() + ", product_id=" + productId + ", qty=" + quantity
        );
    }

    private void sendConfirmationEmail() {
        LogContext.info("Sending confirmation email");

        // Get user email from ambient context
        User user = UserContext.getCurrent();
        LogContext.debug("Email sent to: " + user.getEmail());
    }

    private void restoreStock(long orderId) {
        LogContext.info("Restoring stock for cancelled order");
        DatabaseConnection.executeUpdate(
                "UPDATE products SET stock = stock + (SELECT quantity FROM orders WHERE id = " +
                orderId + ")"
        );
    }

    private void markOrderCancelled(long orderId) {
        LogContext.info("Marking order as cancelled");
        DatabaseConnection.update(
                "orders",
                "status = 'CANCELLED'",
                "id = " + orderId
        );
    }

    private void sendCancellationEmail() {
        LogContext.info("Sending cancellation email");
        User user = UserContext.getCurrent();
        LogContext.debug("Email sent to: " + user.getEmail());
    }
}
