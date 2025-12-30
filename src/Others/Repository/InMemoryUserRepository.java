package Others.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * In-Memory Repository Implementation
 *
 * Verileri bellekte (RAM) saklar. Veritabanı kullanmaz.
 *
 * Kullanım Alanları:
 * 1. Unit test'ler için (gerçek DB'ye ihtiyaç yok)
 * 2. Prototipleme aşamasında (hızlı geliştirme)
 * 3. Cache mekanizması olarak
 * 4. Development ortamında (kolay setup)
 *
 * Dikkat:
 * - Veriler uygulama kapanınca kaybolur (persistent değil)
 * - Thread-safe implementasyon için ConcurrentHashMap kullanılır
 */
public class InMemoryUserRepository implements IUserRepository {

    // Thread-safe map - birden fazla thread aynı anda erişebilir
    private final Map<Long, User> storage = new ConcurrentHashMap<>();

    // Auto-increment ID için atomic counter
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Constructor - İsteğe bağlı olarak başlangıç verileri eklenebilir
     */
    public InMemoryUserRepository() {
        // Boş başlatma
    }

    /**
     * Constructor - Başlangıç verileri ile oluşturma
     */
    public InMemoryUserRepository(List<User> initialData) {
        initialData.forEach(this::save);
    }

    @Override
    public Optional<User> findById(Long id) {
        log("Finding user by ID: " + id);
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        log("Finding user by username: " + username);
        return storage.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log("Finding user by email: " + email);
        return storage.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        log("Finding all users");
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<User> findAllActive() {
        log("Finding all active users");
        return storage.values().stream()
                .filter(User::isActive)
                .collect(Collectors.toList());
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            // Yeni kayıt - ID ata
            user.setId(idGenerator.getAndIncrement());
            log("Creating new user with ID: " + user.getId());
        } else {
            // Güncelleme
            log("Updating user with ID: " + user.getId());
        }

        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean deleteById(Long id) {
        log("Deleting user by ID: " + id);
        return storage.remove(id) != null;
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
        return storage.containsKey(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return storage.values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    @Override
    public long count() {
        return storage.size();
    }

    @Override
    public void deleteAll() {
        log("Deleting all users");
        storage.clear();
    }

    /**
     * Debug amaçlı loglama
     */
    private void log(String message) {
        System.out.println("[InMemoryRepository] " + message);
    }

    /**
     * Repository'nin mevcut durumunu gösterir
     */
    public void printStatus() {
        System.out.println("\n=== Repository Status ===");
        System.out.println("Total users: " + count());
        System.out.println("Active users: " + findAllActive().size());
        System.out.println("Next ID: " + idGenerator.get());
        System.out.println("========================\n");
    }
}
