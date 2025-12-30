package Others.UnitOfWork;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Order Service - Business Logic Layer
 *
 * This service demonstrates the Unit of Work pattern in action.
 * It coordinates multiple repository operations in a single transaction.
 *
 * Key Points:
 * - Uses UnitOfWork to coordinate multiple changes
 * - All changes are atomic (all succeed or all fail)
 * - Business logic doesn't directly interact with database
 */
public class OrderService {

    private UnitOfWork unitOfWork;
    private ProductRepository productRepository;
    private AtomicLong orderIdGenerator;

    /**
     * Constructor - Dependency Injection
     */
    public OrderService(UnitOfWork unitOfWork, ProductRepository productRepository) {
        this.unitOfWork = unitOfWork;
        this.productRepository = productRepository;
        this.orderIdGenerator = new AtomicLong(1);
    }

    /**
     * Place an order - Main business operation
     *
     * This method demonstrates the power of Unit of Work:
     * 1. Update product stock
     * 2. Create order
     * 3. Both committed together in single transaction
     *
     * @param productId Product to order
     * @param quantity Quantity to order
     * @return Created order
     */
    public Order placeOrder(Long productId, int quantity) {
        log("\n╔════════════════════════════════════════════════╗");
        log("║          PLACING ORDER                         ║");
        log("╚════════════════════════════════════════════════╝");
        log("Product ID: " + productId);
        log("Quantity:   " + quantity);
        log("─".repeat(50));

        // 1. Validate and get product
        Product product = productRepository.findById(productId);
        if (product == null) {
            throw new RuntimeException("Product not found: " + productId);
        }
        log("✓ Product found: " + product.getName());

        // 2. Check stock availability
        if (product.getStock() < quantity) {
            throw new RuntimeException(
                    "Insufficient stock! Available: " + product.getStock() +
                            ", Requested: " + quantity
            );
        }
        log("✓ Stock available: " + product.getStock() + " units");

        // 3. Calculate total
        double totalAmount = product.getPrice() * quantity;
        log("✓ Total amount: $" + totalAmount);

        // 4. Update product stock (register as dirty with UoW)
        int originalStock = product.getStock();
        product.setStock(originalStock - quantity);
        productRepository.update(product);
        log("✓ Stock updated: " + originalStock + " → " + product.getStock());

        // 5. Create order (register as new with UoW)
        Order order = new Order(
                orderIdGenerator.getAndIncrement(),
                productId,
                quantity,
                totalAmount
        );
        unitOfWork.registerNew(order);
        log("✓ Order created: " + order);

        log("─".repeat(50));
        log("⏳ Ready to commit (stock update + order creation)");

        // 6. Commit all changes in single transaction
        // If this fails, both stock update AND order creation will rollback!
        unitOfWork.commit();

        log("✓ Order placed successfully!\n");
        return order;
    }

    /**
     * Cancel an order
     *
     * Demonstrates rollback scenario:
     * 1. Restore product stock
     * 2. Delete order
     * 3. Both committed together
     */
    public void cancelOrder(Order order) {
        log("\n╔════════════════════════════════════════════════╗");
        log("║          CANCELING ORDER                       ║");
        log("╚════════════════════════════════════════════════╝");
        log("Order ID: " + order.getId());
        log("─".repeat(50));

        // 1. Get product
        Product product = productRepository.findById(order.getProductId());
        if (product == null) {
            throw new RuntimeException("Product not found: " + order.getProductId());
        }

        // 2. Restore stock
        int originalStock = product.getStock();
        product.setStock(originalStock + order.getQuantity());
        productRepository.update(product);
        log("✓ Stock restored: " + originalStock + " → " + product.getStock());

        // 3. Delete order
        unitOfWork.registerDeleted(order);
        log("✓ Order marked for deletion");

        log("─".repeat(50));
        log("⏳ Committing cancellation...");

        // 4. Commit both changes
        unitOfWork.commit();

        log("✓ Order cancelled successfully!\n");
    }

    /**
     * Update product price
     *
     * Simple operation - demonstrates single entity update
     */
    public void updateProductPrice(Long productId, double newPrice) {
        log("\n--- Updating Product Price ---");

        Product product = productRepository.findById(productId);
        if (product == null) {
            throw new RuntimeException("Product not found: " + productId);
        }

        double oldPrice = product.getPrice();
        product.setPrice(newPrice);
        productRepository.update(product);

        log("Price updated: $" + oldPrice + " → $" + newPrice);

        unitOfWork.commit();
        log("✓ Price change committed\n");
    }

    /**
     * Bulk order placement
     *
     * Demonstrates multiple operations in one transaction:
     * - Update stock for multiple products
     * - Create multiple orders
     * - All atomic!
     */
    public void placeBulkOrders(Long[] productIds, int[] quantities) {
        if (productIds.length != quantities.length) {
            throw new IllegalArgumentException("Product IDs and quantities must have same length");
        }

        log("\n╔════════════════════════════════════════════════╗");
        log("║          BULK ORDER PLACEMENT                  ║");
        log("╚════════════════════════════════════════════════╝");
        log("Total orders: " + productIds.length);
        log("─".repeat(50));

        double grandTotal = 0;

        for (int i = 0; i < productIds.length; i++) {
            Long productId = productIds[i];
            int quantity = quantities[i];

            // Get product
            Product product = productRepository.findById(productId);
            if (product == null) {
                throw new RuntimeException("Product not found: " + productId);
            }

            // Check stock
            if (product.getStock() < quantity) {
                throw new RuntimeException(
                        "Insufficient stock for " + product.getName() +
                                "! Available: " + product.getStock() +
                                ", Requested: " + quantity
                );
            }

            // Update stock
            product.setStock(product.getStock() - quantity);
            productRepository.update(product);

            // Create order
            double totalAmount = product.getPrice() * quantity;
            grandTotal += totalAmount;

            Order order = new Order(
                    orderIdGenerator.getAndIncrement(),
                    productId,
                    quantity,
                    totalAmount
            );
            unitOfWork.registerNew(order);

            log("  [" + (i + 1) + "] " + product.getName() +
                    " x" + quantity + " = $" + totalAmount);
        }

        log("─".repeat(50));
        log("Grand Total: $" + grandTotal);
        log("⏳ Committing all " + productIds.length + " orders...");

        // Commit ALL changes at once!
        // If ANY operation fails, ALL will rollback
        unitOfWork.commit();

        log("✓ All orders placed successfully!\n");
    }

    /**
     * Log message
     */
    private void log(String message) {
        System.out.println("[OrderService] " + message);
    }
}
