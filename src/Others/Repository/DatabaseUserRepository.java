package Others.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Database Repository Implementation (Simulated)
 *
 * Gerçek bir veritabanı yerine, veritabanı işlemlerini simüle eder.
 * Gerçek uygulamada JDBC, JPA/Hibernate, MyBatis gibi teknolojiler kullanılır.
 *
 * Simüle Edilen Özellikler:
 * 1. Connection pooling (bağlantı havuzu)
 * 2. Transaction yönetimi
 * 3. Query execution time (sorgu çalıştırma süresi)
 * 4. Database logging
 *
 * Gerçek implementasyonda:
 * - JDBC ile SQL sorguları çalıştırılır
 * - JPA/Hibernate ile ORM kullanılır
 * - Connection pool için HikariCP kullanılır
 * - Transaction için Spring @Transactional kullanılır
 */
public class DatabaseUserRepository implements IUserRepository {

    // Simüle edilmiş veritabanı tablosu
    private final Map<Long, User> database = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    // Database konfigürasyonu
    private final String databaseName;
    private final boolean enableQueryLogging;
    private final int queryDelayMs;

    /**
     * Constructor - Database yapılandırması ile
     */
    public DatabaseUserRepository(String databaseName, boolean enableQueryLogging, int queryDelayMs) {
        this.databaseName = databaseName;
        this.enableQueryLogging = enableQueryLogging;
        this.queryDelayMs = queryDelayMs;
        log("Connected to database: " + databaseName);
    }

    /**
     * Default constructor
     */
    public DatabaseUserRepository() {
        this("ProductionDB", true, 50);
    }

    @Override
    public Optional<User> findById(Long id) {
        String query = "SELECT * FROM users WHERE id = " + id;
        executeQuery(query);

        return Optional.ofNullable(database.get(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = '" + username + "'";
        executeQuery(query);

        return database.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = '" + email + "'";
        executeQuery(query);

        return database.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        String query = "SELECT * FROM users";
        executeQuery(query);

        return new ArrayList<>(database.values());
    }

    @Override
    public List<User> findAllActive() {
        String query = "SELECT * FROM users WHERE active = true";
        executeQuery(query);

        return database.values().stream()
                .filter(User::isActive)
                .collect(Collectors.toList());
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            // INSERT operation
            user.setId(idGenerator.getAndIncrement());
            String query = "INSERT INTO users (id, username, email, full_name, active) VALUES (" +
                    user.getId() + ", '" + user.getUsername() + "', '" +
                    user.getEmail() + "', '" + user.getFullName() + "', " + user.isActive() + ")";
            executeQuery(query);
        } else {
            // UPDATE operation
            String query = "UPDATE users SET username = '" + user.getUsername() +
                    "', email = '" + user.getEmail() +
                    "', full_name = '" + user.getFullName() +
                    "', active = " + user.isActive() +
                    " WHERE id = " + user.getId();
            executeQuery(query);
        }

        database.put(user.getId(), user);
        commitTransaction();
        return user;
    }

    @Override
    public boolean deleteById(Long id) {
        String query = "DELETE FROM users WHERE id = " + id;
        executeQuery(query);

        boolean result = database.remove(id) != null;
        commitTransaction();
        return result;
    }

    @Override
    public boolean delete(User user) {
        if (user == null || user.getId() == null) {
            return false;
        }
        return deleteById(user.getId());
    }

    @Override
    public boolean existsById(Long id) {
        String query = "SELECT COUNT(*) FROM users WHERE id = " + id;
        executeQuery(query);

        return database.containsKey(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = '" + username + "'";
        executeQuery(query);

        return database.values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    @Override
    public long count() {
        String query = "SELECT COUNT(*) FROM users";
        executeQuery(query);

        return database.size();
    }

    @Override
    public void deleteAll() {
        String query = "TRUNCATE TABLE users";
        executeQuery(query);

        database.clear();
        commitTransaction();
    }

    /**
     * Query çalıştırma simülasyonu
     */
    private void executeQuery(String query) {
        if (enableQueryLogging) {
            log("Executing query: " + query);
        }

        // Veritabanı gecikmesini simüle et
        simulateQueryDelay();
    }

    /**
     * Transaction commit simülasyonu
     */
    private void commitTransaction() {
        log("Transaction committed successfully");
    }

    /**
     * Database query gecikmesini simüle et
     */
    private void simulateQueryDelay() {
        try {
            Thread.sleep(queryDelayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Database loglama
     */
    private void log(String message) {
        System.out.println("[DatabaseRepository:" + databaseName + "] " + message);
    }

    /**
     * Database istatistikleri
     */
    public void printDatabaseStats() {
        System.out.println("\n=== Database Statistics ===");
        System.out.println("Database: " + databaseName);
        System.out.println("Total records: " + count());
        System.out.println("Active users: " + findAllActive().size());
        System.out.println("Query logging: " + enableQueryLogging);
        System.out.println("Query delay: " + queryDelayMs + "ms");
        System.out.println("===========================\n");
    }

    /**
     * Bulk insert - Toplu veri ekleme (performans için)
     */
    public void bulkInsert(List<User> users) {
        log("Starting bulk insert of " + users.size() + " users");

        for (User user : users) {
            if (user.getId() == null) {
                user.setId(idGenerator.getAndIncrement());
            }
            database.put(user.getId(), user);
        }

        commitTransaction();
        log("Bulk insert completed");
    }
}
