package Others.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository Interface (Abstraction Layer)
 *
 * Bu interface, veri erişim işlemlerini tanımlar ancak implementasyon detaylarını içermez.
 * Böylece farklı veri kaynakları (InMemory, Database, API vb.) aynı interface'i implement edebilir.
 *
 * Avantajları:
 * 1. Business logic, veri kaynağından bağımsız çalışır
 * 2. Test sırasında mock repository kullanılabilir
 * 3. Veri kaynağı değiştirildiğinde business logic etkilenmez
 * 4. Dependency Injection ile esnek yapı sağlar
 */
public interface IUserRepository {

    /**
     * Kullanıcıyı ID ile bulur
     *
     * @param id Kullanıcı ID'si
     * @return Optional<User> - Kullanıcı bulunursa User, bulunamazsa empty Optional
     */
    Optional<User> findById(Long id);

    /**
     * Kullanıcıyı username ile bulur
     *
     * @param username Kullanıcı adı
     * @return Optional<User> - Kullanıcı bulunursa User, bulunamazsa empty Optional
     */
    Optional<User> findByUsername(String username);

    /**
     * Kullanıcıyı email ile bulur
     *
     * @param email Email adresi
     * @return Optional<User> - Kullanıcı bulunursa User, bulunamazsa empty Optional
     */
    Optional<User> findByEmail(String email);

    /**
     * Tüm kullanıcıları getirir
     *
     * @return List<User> - Tüm kullanıcıların listesi
     */
    List<User> findAll();

    /**
     * Aktif kullanıcıları getirir
     *
     * @return List<User> - Aktif kullanıcıların listesi
     */
    List<User> findAllActive();

    /**
     * Kullanıcı kaydeder (yeni ekleme veya güncelleme)
     *
     * @param user Kaydedilecek kullanıcı
     * @return User - Kaydedilen kullanıcı (genelde ID atanmış haliyle döner)
     */
    User save(User user);

    /**
     * Kullanıcıyı ID ile siler
     *
     * @param id Silinecek kullanıcının ID'si
     * @return boolean - Silme işlemi başarılıysa true, değilse false
     */
    boolean deleteById(Long id);

    /**
     * Kullanıcıyı siler
     *
     * @param user Silinecek kullanıcı
     * @return boolean - Silme işlemi başarılıysa true, değilse false
     */
    boolean delete(User user);

    /**
     * ID'ye sahip kullanıcının var olup olmadığını kontrol eder
     *
     * @param id Kontrol edilecek ID
     * @return boolean - Kullanıcı varsa true, yoksa false
     */
    boolean existsById(Long id);

    /**
     * Username'e sahip kullanıcının var olup olmadığını kontrol eder
     *
     * @param username Kontrol edilecek kullanıcı adı
     * @return boolean - Kullanıcı varsa true, yoksa false
     */
    boolean existsByUsername(String username);

    /**
     * Toplam kullanıcı sayısını döndürür
     *
     * @return long - Toplam kullanıcı sayısı
     */
    long count();

    /**
     * Tüm kullanıcıları siler
     * Dikkat: Bu metod genellikle test senaryolarında kullanılır
     */
    void deleteAll();
}
