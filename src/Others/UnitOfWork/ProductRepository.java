package Others.UnitOfWork;

import java.util.*;

/**
 * Product Repository
 *
 * Repository that works with Unit of Work pattern.
 * Instead of directly persisting changes, it registers them with the UoW.
 *
 * Key Difference from regular Repository:
 * - Regular Repository: save() → immediately writes to DB
 * - UoW Repository: save() → registers change, commits later
 */
public class ProductRepository {

    private UnitOfWork unitOfWork;
    private Map<Long, Product> inMemoryStorage;

    /**
     * Constructor - Dependency Injection of UnitOfWork
     */
    public ProductRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
        this.inMemoryStorage = new HashMap<>();
    }

    /**
     * Add a new product
     *
     * Registers the product with UoW instead of immediately saving
     */
    public void add(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (inMemoryStorage.containsKey(product.getId())) {
            throw new IllegalStateException("Product already exists: " + product.getId());
        }

        // Register with UnitOfWork - NOT saved to DB yet!
        unitOfWork.registerNew(product);
        inMemoryStorage.put(product.getId(), product);

        log("Added product to repository: " + product.getName());
    }

    /**
     * Update an existing product
     *
     * Registers the product as dirty with UoW
     */
    public void update(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (!inMemoryStorage.containsKey(product.getId())) {
            throw new IllegalStateException("Product not found: " + product.getId());
        }

        // Register with UnitOfWork as modified - NOT updated in DB yet!
        unitOfWork.registerDirty(product);

        log("Updated product in repository: " + product.getName());
    }

    /**
     * Delete a product
     *
     * Registers the product for deletion with UoW
     */
    public void delete(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        // Register with UnitOfWork for deletion - NOT deleted from DB yet!
        unitOfWork.registerDeleted(product);
        inMemoryStorage.remove(product.getId());

        log("Deleted product from repository: " + product.getName());
    }

    /**
     * Delete by ID
     */
    public void deleteById(Long id) {
        Product product = findById(id);
        if (product != null) {
            delete(product);
        }
    }

    /**
     * Find product by ID
     */
    public Product findById(Long id) {
        return inMemoryStorage.get(id);
    }

    /**
     * Find all products
     */
    public List<Product> findAll() {
        return new ArrayList<>(inMemoryStorage.values());
    }

    /**
     * Check if product exists
     */
    public boolean exists(Long id) {
        return inMemoryStorage.containsKey(id);
    }

    /**
     * Get count of products
     */
    public int count() {
        return inMemoryStorage.size();
    }

    /**
     * Find products by name (case-insensitive)
     */
    public List<Product> findByName(String name) {
        List<Product> results = new ArrayList<>();
        for (Product product : inMemoryStorage.values()) {
            if (product.getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(product);
            }
        }
        return results;
    }

    /**
     * Find products with low stock
     */
    public List<Product> findLowStock(int threshold) {
        List<Product> results = new ArrayList<>();
        for (Product product : inMemoryStorage.values()) {
            if (product.getStock() < threshold) {
                results.add(product);
            }
        }
        return results;
    }

    /**
     * Log message
     */
    private void log(String message) {
        System.out.println("[ProductRepository] " + message);
    }

    /**
     * Print repository statistics
     */
    public void printStatistics() {
        System.out.println("\n=== Product Repository Stats ===");
        System.out.println("Total products: " + count());
        System.out.println("Low stock items: " + findLowStock(10).size());
        System.out.println("================================\n");
    }
}
