package Others.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Service Layer (Business Logic Layer)
 *
 * Bu katman, iş mantığını (business logic) içerir.
 * Repository'yi kullanarak veri işlemleri yapar, ancak veri kaynağının
 * detaylarını bilmez (Database mi, InMemory mi, API mi önemli değil).
 *
 * Dependency Injection Prensibi:
 * - Constructor'da IUserRepository interface'i alır (implementasyon değil!)
 * - Bu sayede farklı repository implementasyonları kullanılabilir
 * - Test sırasında mock repository inject edilebilir
 *
 * Single Responsibility Principle:
 * - Sadece iş mantığından sorumlu
 * - Veri erişiminden sorumlu değil (repository'nin işi)
 */
public class UserService {

    // Interface'e bağımlılık (implementasyona değil!)
    private final IUserRepository userRepository;

    /**
     * Constructor - Dependency Injection
     *
     * @param userRepository Repository implementasyonu (InMemory, Database vb.)
     */
    public UserService(IUserRepository userRepository) {
        if (userRepository == null) {
            throw new IllegalArgumentException("UserRepository cannot be null");
        }
        this.userRepository = userRepository;
        log("UserService initialized with repository: " + userRepository.getClass().getSimpleName());
    }

    /**
     * Yeni kullanıcı kaydı (Business Logic)
     *
     * Sadece repository.save() çağırmaktan fazlası:
     * - Validation (doğrulama)
     * - Business rules (iş kuralları)
     * - Logging
     */
    public User registerUser(String username, String email, String fullName) {
        log("Attempting to register user: " + username);

        // Validation
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Business Rule: Kullanıcı adı benzersiz olmalı
        if (userRepository.existsByUsername(username)) {
            throw new IllegalStateException("Username already exists: " + username);
        }

        // Business Rule: Email benzersiz olmalı
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new IllegalStateException("Email already registered: " + email);
        }

        // Yeni kullanıcı oluştur ve kaydet
        User newUser = new User(null, username, email, fullName);
        User savedUser = userRepository.save(newUser);

        log("User registered successfully with ID: " + savedUser.getId());
        return savedUser;
    }

    /**
     * Kullanıcı bilgilerini güncelle
     */
    public User updateUser(Long userId, String newEmail, String newFullName) {
        log("Updating user: " + userId);

        // Kullanıcı var mı kontrol et
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // Email değişiyorsa, başka kullanıcıda kullanılmıyor mu kontrol et
        if (newEmail != null && !newEmail.equals(user.getEmail())) {
            Optional<User> existingUser = userRepository.findByEmail(newEmail);
            if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
                throw new IllegalStateException("Email already in use: " + newEmail);
            }
            user.setEmail(newEmail);
        }

        // Full name güncelle
        if (newFullName != null && !newFullName.trim().isEmpty()) {
            user.setFullName(newFullName);
        }

        User updatedUser = userRepository.save(user);
        log("User updated successfully: " + userId);
        return updatedUser;
    }

    /**
     * Kullanıcıyı deaktif et (soft delete)
     */
    public void deactivateUser(Long userId) {
        log("Deactivating user: " + userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        user.deactivate();
        userRepository.save(user);

        log("User deactivated: " + userId);
    }

    /**
     * Kullanıcıyı aktif et
     */
    public void activateUser(Long userId) {
        log("Activating user: " + userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        user.activate();
        userRepository.save(user);

        log("User activated: " + userId);
    }

    /**
     * Kullanıcıyı tamamen sil (hard delete)
     */
    public boolean deleteUser(Long userId) {
        log("Deleting user: " + userId);

        // Business Rule: Sadece deaktif kullanıcılar silinebilir
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (user.isActive()) {
            throw new IllegalStateException("Cannot delete active user. Deactivate first.");
        }

        boolean deleted = userRepository.deleteById(userId);
        if (deleted) {
            log("User deleted successfully: " + userId);
        }
        return deleted;
    }

    /**
     * Kullanıcıyı ID ile getir
     */
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * Kullanıcıyı username ile getir
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Tüm aktif kullanıcıları getir
     */
    public List<User> getAllActiveUsers() {
        log("Fetching all active users");
        return userRepository.findAllActive();
    }

    /**
     * Tüm kullanıcıları getir
     */
    public List<User> getAllUsers() {
        log("Fetching all users");
        return userRepository.findAll();
    }

    /**
     * Kullanıcı sayısını getir
     */
    public long getUserCount() {
        return userRepository.count();
    }

    /**
     * Kullanıcının email'ini doğrula
     */
    public boolean validateUserEmail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        boolean isValid = user.hasValidEmail();
        log("Email validation for user " + userId + ": " + isValid);
        return isValid;
    }

    /**
     * Service loglama
     */
    private void log(String message) {
        System.out.println("[UserService] " + message);
    }

    /**
     * İstatistikleri yazdır
     */
    public void printStatistics() {
        System.out.println("\n=== User Service Statistics ===");
        System.out.println("Total users: " + userRepository.count());
        System.out.println("Active users: " + userRepository.findAllActive().size());
        System.out.println("Inactive users: " + (userRepository.count() - userRepository.findAllActive().size()));
        System.out.println("================================\n");
    }
}
