package Others.UnitOfWork;

/**
 * Unit of Work Pattern Demonstration
 *
 * This demo shows different scenarios of using Unit of Work pattern:
 * 1. Successful transaction with multiple changes
 * 2. Failed transaction with rollback
 * 3. Bulk operations
 * 4. Order cancellation
 */
public class UnitOfWorkDemo {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║     UNIT OF WORK PATTERN DEMONSTRATION         ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        // Scenario 1: Successful Order Placement
        scenario1_SuccessfulTransaction();

        System.out.println("\n" + "=".repeat(80) + "\n");

        // Scenario 2: Failed Transaction (Insufficient Stock)
        scenario2_FailedTransaction();

        System.out.println("\n" + "=".repeat(80) + "\n");

        // Scenario 3: Multiple Operations in One Transaction
        scenario3_MultipleOperations();

        System.out.println("\n" + "=".repeat(80) + "\n");

        // Scenario 4: Bulk Orders
        scenario4_BulkOrders();
    }

    /**
     * SCENARIO 1: Successful Transaction
     *
     * Demonstrates:
     * - Adding products to repository
     * - Placing an order
     * - Both product stock update and order creation committed together
     */
    private static void scenario1_SuccessfulTransaction() {
        System.out.println("┌────────────────────────────────────────────────┐");
        System.out.println("│  Scenario 1: Successful Transaction           │");
        System.out.println("└────────────────────────────────────────────────┘\n");

        // Setup
        UnitOfWork uow = new UnitOfWork();
        ProductRepository productRepo = new ProductRepository(uow);
        OrderService orderService = new OrderService(uow, productRepo);

        // Add products
        Product laptop = new Product(1L, "Laptop", 1500.0, 10);
        Product mouse = new Product(2L, "Mouse", 25.0, 100);

        productRepo.add(laptop);
        productRepo.add(mouse);

        // Initial commit to "save" products
        uow.commit();

        // Place order
        System.out.println("\n📦 Customer wants to buy 2 laptops");
        orderService.placeOrder(1L, 2);

        // Check result
        System.out.println("\n--- Final State ---");
        System.out.println("Laptop stock after order: " + laptop.getStock());
        System.out.println("Expected: 8 (10 - 2)");
        System.out.println("✓ Transaction successful!");
    }

    /**
     * SCENARIO 2: Failed Transaction with Rollback
     *
     * Demonstrates:
     * - Attempting to order more than available stock
     * - Transaction fails and rolls back
     * - No changes are persisted (atomicity)
     */
    private static void scenario2_FailedTransaction() {
        System.out.println("┌────────────────────────────────────────────────┐");
        System.out.println("│  Scenario 2: Failed Transaction (Rollback)    │");
        System.out.println("└────────────────────────────────────────────────┘\n");

        // Setup
        UnitOfWork uow = new UnitOfWork();
        ProductRepository productRepo = new ProductRepository(uow);
        OrderService orderService = new OrderService(uow, productRepo);

        // Add product with limited stock
        Product phone = new Product(3L, "Phone", 800.0, 5);
        productRepo.add(phone);
        uow.commit();

        System.out.println("📦 Customer wants to buy 10 phones (only 5 available)\n");

        int originalStock = phone.getStock();

        try {
            orderService.placeOrder(3L, 10);  // Will fail!
            System.out.println("❌ ERROR: This should not happen!");
        } catch (RuntimeException e) {
            System.out.println("\n╔════════════════════════════════════════════════╗");
            System.out.println("║  TRANSACTION FAILED                            ║");
            System.out.println("╚════════════════════════════════════════════════╝");
            System.out.println("Error: " + e.getMessage());
            System.out.println("\n--- Rollback Effect ---");
            System.out.println("Phone stock before attempt: " + originalStock);
            System.out.println("Phone stock after rollback: " + phone.getStock());
            System.out.println("✓ Stock unchanged - transaction rolled back!");
        }
    }

    /**
     * SCENARIO 3: Multiple Operations in One Transaction
     *
     * Demonstrates:
     * - Multiple products being modified
     * - Multiple orders being created
     * - All committed together (all-or-nothing)
     */
    private static void scenario3_MultipleOperations() {
        System.out.println("┌────────────────────────────────────────────────┐");
        System.out.println("│  Scenario 3: Multiple Operations              │");
        System.out.println("└────────────────────────────────────────────────┘\n");

        // Setup
        UnitOfWork uow = new UnitOfWork();
        ProductRepository productRepo = new ProductRepository(uow);

        // Add multiple products
        Product keyboard = new Product(4L, "Keyboard", 75.0, 50);
        Product monitor = new Product(5L, "Monitor", 300.0, 20);
        Product webcam = new Product(6L, "Webcam", 80.0, 30);

        System.out.println("--- Adding Multiple Products ---");
        productRepo.add(keyboard);
        productRepo.add(monitor);
        productRepo.add(webcam);

        System.out.println("\n--- Updating Prices ---");
        keyboard.setPrice(85.0);
        monitor.setPrice(280.0);
        productRepo.update(keyboard);
        productRepo.update(monitor);

        // Show pending changes before commit
        uow.printState();

        // Commit all at once!
        System.out.println("⏳ Committing all changes together...\n");
        uow.commit();

        System.out.println("✓ All 3 products added and 2 price updates committed!");
        productRepo.printStatistics();
    }

    /**
     * SCENARIO 4: Bulk Order Placement
     *
     * Demonstrates:
     * - Complex business transaction with multiple entities
     * - Multiple stock updates
     * - Multiple order creations
     * - All atomic - if any fails, all rollback
     */
    private static void scenario4_BulkOrders() {
        System.out.println("┌────────────────────────────────────────────────┐");
        System.out.println("│  Scenario 4: Bulk Order Placement             │");
        System.out.println("└────────────────────────────────────────────────┘\n");

        // Setup
        UnitOfWork uow = new UnitOfWork();
        ProductRepository productRepo = new ProductRepository(uow);
        OrderService orderService = new OrderService(uow, productRepo);

        // Add products
        Product tablet = new Product(7L, "Tablet", 500.0, 15);
        Product charger = new Product(8L, "Charger", 20.0, 50);
        Product headphones = new Product(9L, "Headphones", 150.0, 25);

        productRepo.add(tablet);
        productRepo.add(charger);
        productRepo.add(headphones);
        uow.commit();

        // Customer orders multiple items
        System.out.println("📦 Customer bulk order:");
        System.out.println("  - 3x Tablets");
        System.out.println("  - 5x Chargers");
        System.out.println("  - 2x Headphones\n");

        Long[] productIds = {7L, 8L, 9L};
        int[] quantities = {3, 5, 2};

        orderService.placeBulkOrders(productIds, quantities);

        // Show final state
        System.out.println("--- Final Stock Levels ---");
        System.out.println("Tablet:     " + tablet.getStock() + " (was 15, sold 3)");
        System.out.println("Charger:    " + charger.getStock() + " (was 50, sold 5)");
        System.out.println("Headphones: " + headphones.getStock() + " (was 25, sold 2)");
        System.out.println("\n✓ All 3 orders placed in single atomic transaction!");
    }
}
