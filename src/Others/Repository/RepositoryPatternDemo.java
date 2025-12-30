package Others.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository Pattern Demonstration
 *
 * Bu demo, Repository Pattern'in farklı kullanım senaryolarını gösterir:
 * 1. InMemory Repository ile çalışma (test/development)
 * 2. Database Repository ile çalışma (production)
 * 3. Repository değişimi (InMemory -> Database)
 * 4. Service katmanının repository'den bağımsız olması
 */
public class RepositoryPatternDemo {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║     REPOSITORY PATTERN DEMONSTRATION          ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        // Scenario 1: InMemory Repository ile çalışma
        scenario1_InMemoryRepository();

        System.out.println("\n" + "=".repeat(80) + "\n");

        // Scenario 2: Database Repository ile çalışma
        scenario2_DatabaseRepository();

        System.out.println("\n" + "=".repeat(80) + "\n");

        // Scenario 3: Repository Değişimi (Flexibility)
        scenario3_SwitchingRepositories();

        System.out.println("\n" + "=".repeat(80) + "\n");

        // Scenario 4: Business Logic Test Edilebilirliği
        scenario4_TestableBusinessLogic();
    }

    /**
     * SENARYO 1: InMemory Repository Kullanımı
     *
     * InMemory repository genellikle şu durumlarda kullanılır:
     * - Unit test'ler
     * - Prototipleme
     * - Development ortamı
     * - Cache mekanizması
     */
    private static void scenario1_InMemoryRepository() {
        System.out.println("┌────────────────────────────────────────────────┐");
        System.out.println("│  Scenario 1: InMemory Repository              │");
        System.out.println("└────────────────────────────────────────────────┘\n");

        // Repository oluştur
        IUserRepository repository = new InMemoryUserRepository();

        // Service oluştur (repository injection)
        UserService userService = new UserService(repository);

        // Kullanıcı kaydet
        User user1 = userService.registerUser("alice", "alice@example.com", "Alice Smith");
        User user2 = userService.registerUser("bob", "bob@example.com", "Bob Johnson");
        User user3 = userService.registerUser("charlie", "charlie@example.com", "Charlie Brown");

        System.out.println("\n--- Registered Users ---");
        userService.getAllUsers().forEach(System.out::println);

        // Kullanıcı güncelle
        System.out.println("\n--- Updating User ---");
        userService.updateUser(user1.getId(), "alice.smith@example.com", "Alice M. Smith");

        // Kullanıcıyı deaktif et
        System.out.println("\n--- Deactivating User ---");
        userService.deactivateUser(user2.getId());

        // İstatistikler
        userService.printStatistics();
    }

    /**
     * SENARYO 2: Database Repository Kullanımı
     *
     * Database repository production ortamında kullanılır:
     * - Persistent data storage
     * - Transaction yönetimi
     * - ACID özellikleri
     */
    private static void scenario2_DatabaseRepository() {
        System.out.println("┌────────────────────────────────────────────────┐");
        System.out.println("│  Scenario 2: Database Repository              │");
        System.out.println("└────────────────────────────────────────────────┘\n");

        // Database repository oluştur
        DatabaseUserRepository dbRepository = new DatabaseUserRepository("ProductionDB", true, 10);

        // Service oluştur (aynı service, farklı repository!)
        UserService userService = new UserService(dbRepository);

        // Kullanıcı kaydet
        System.out.println("\n--- Creating Users in Database ---");
        User user1 = userService.registerUser("david", "david@company.com", "David Wilson");
        User user2 = userService.registerUser("emma", "emma@company.com", "Emma Davis");

        // Kullanıcı ara
        System.out.println("\n--- Finding User by Username ---");
        Optional<User> foundUser = userService.getUserByUsername("david");
        foundUser.ifPresent(user -> System.out.println("Found: " + user));

        // Bulk insert (database specific feature)
        System.out.println("\n--- Bulk Insert ---");
        List<User> bulkUsers = List.of(
                new User(null, "frank", "frank@company.com", "Frank Miller"),
                new User(null, "grace", "grace@company.com", "Grace Lee")
        );
        dbRepository.bulkInsert(bulkUsers);

        // Database stats
        dbRepository.printDatabaseStats();
    }

    /**
     * SENARYO 3: Repository Değişimi (Flexibility Demonstration)
     *
     * Bu senaryo, Repository Pattern'in en büyük avantajını gösterir:
     * - Aynı service kodu, farklı repository implementasyonları ile çalışır
     * - Service kodu değişmeden veri kaynağı değiştirilebilir
     * - Production'da Database, Test'te InMemory kullanılabilir
     */
    private static void scenario3_SwitchingRepositories() {
        System.out.println("┌────────────────────────────────────────────────┐");
        System.out.println("│  Scenario 3: Repository Switching             │");
        System.out.println("└────────────────────────────────────────────────┘\n");

        System.out.println("--- Phase 1: Development with InMemory ---");

        // Development aşaması: InMemory kullan (hızlı, kolay)
        IUserRepository devRepository = new InMemoryUserRepository();
        UserService devService = new UserService(devRepository);

        devService.registerUser("test_user", "test@dev.com", "Test User");
        System.out.println("Dev environment users: " + devService.getUserCount());

        System.out.println("\n--- Phase 2: Production with Database ---");

        // Production aşaması: Database kullan (persistent)
        IUserRepository prodRepository = new DatabaseUserRepository("ProductionDB", false, 5);
        UserService prodService = new UserService(prodRepository);

        // AYNI KOD, FARKLI REPOSITORY!
        prodService.registerUser("prod_user", "user@prod.com", "Production User");
        System.out.println("Production environment users: " + prodService.getUserCount());

        System.out.println("\n💡 NOTICE: Same UserService code, different repositories!");
        System.out.println("   This is the power of Repository Pattern!");
    }

    /**
     * SENARYO 4: Test Edilebilirlik
     *
     * Repository Pattern, unit test yazmayı kolaylaştırır:
     * - Gerçek veritabanına ihtiyaç yok
     * - Mock repository kullanılabilir
     * - Hızlı test execution
     */
    private static void scenario4_TestableBusinessLogic() {
        System.out.println("┌────────────────────────────────────────────────┐");
        System.out.println("│  Scenario 4: Testable Business Logic         │");
        System.out.println("└────────────────────────────────────────────────┘\n");

        System.out.println("--- Testing Business Rules ---");

        // Test için InMemory repository kullan (hızlı, izole)
        IUserRepository testRepository = new InMemoryUserRepository();
        UserService userService = new UserService(testRepository);

        // Test 1: Username uniqueness
        System.out.println("\n✓ Test 1: Username must be unique");
        userService.registerUser("john", "john@test.com", "John Doe");
        try {
            userService.registerUser("john", "another@test.com", "Another John");
            System.out.println("❌ FAILED: Should throw exception for duplicate username");
        } catch (IllegalStateException e) {
            System.out.println("✓ PASSED: " + e.getMessage());
        }

        // Test 2: Email validation
        System.out.println("\n✓ Test 2: Email must be valid");
        try {
            userService.registerUser("invalid", "notanemail", "Invalid User");
            System.out.println("❌ FAILED: Should throw exception for invalid email");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ PASSED: " + e.getMessage());
        }

        // Test 3: Cannot delete active user
        System.out.println("\n✓ Test 3: Cannot delete active user");
        User user = userService.registerUser("temp", "temp@test.com", "Temp User");
        try {
            userService.deleteUser(user.getId());
            System.out.println("❌ FAILED: Should not delete active user");
        } catch (IllegalStateException e) {
            System.out.println("✓ PASSED: " + e.getMessage());
        }

        // Test 4: Can delete inactive user
        System.out.println("\n✓ Test 4: Can delete inactive user");
        userService.deactivateUser(user.getId());
        boolean deleted = userService.deleteUser(user.getId());
        if (deleted) {
            System.out.println("✓ PASSED: Inactive user deleted successfully");
        } else {
            System.out.println("❌ FAILED: Should delete inactive user");
        }

        System.out.println("\n💡 All tests executed without real database!");
        System.out.println("   InMemory repository makes testing fast and easy!");
    }
}
