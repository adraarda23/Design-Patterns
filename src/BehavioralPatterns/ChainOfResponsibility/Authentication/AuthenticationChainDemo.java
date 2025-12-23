package BehavioralPatterns.ChainOfResponsibility.Authentication;

// ============================================
// REQUEST (GİRİŞ TALEBİ)
// ============================================
/**
 * AuthRequest: Kimlik doğrulama talebi
 * Kullanıcı bilgilerini içerir
 */
class AuthRequest {
    private String username;
    private String password;
    private String twoFactorCode;
    private String ipAddress;

    public AuthRequest(String username, String password, String twoFactorCode, String ipAddress) {
        this.username = username;
        this.password = password;
        this.twoFactorCode = twoFactorCode;
        this.ipAddress = ipAddress;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getTwoFactorCode() {
        return twoFactorCode;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}

// ============================================
// HANDLER (BASE CLASS)
// ============================================
/**
 * AuthHandler: Kimlik doğrulama zincirinin base sınıfı
 *
 * BU FARKLI BİR CHAIN TİPİ: Filtering Chain
 * - Her handler kontrol yapar
 * - Başarısız olursa zincir DURUR
 * - Tümü başarılıysa giriş yapılır
 */
abstract class AuthHandler {
    protected AuthHandler nextHandler;

    public void setNext(AuthHandler next) {
        this.nextHandler = next;
    }

    // İsteği kontrol et
    public boolean handle(AuthRequest request) {
        // Bu handler kontrolünü yap
        if (check(request)) {
            // Başarılı, bir sonrakine geç
            if (nextHandler != null) {
                return nextHandler.handle(request);
            }
            // Zincir sonu, tüm kontroller geçildi
            return true;
        }
        // Kontrol başarısız, zincir durur
        return false;
    }

    // Alt sınıflar bu metodu implement edecek
    protected abstract boolean check(AuthRequest request);
}

// ============================================
// CONCRETE HANDLERS
// ============================================

/**
 * UsernamePasswordHandler: Kullanıcı adı ve şifre kontrolü
 */
class UsernamePasswordHandler extends AuthHandler {
    // Basit database simülasyonu
    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "12345";

    @Override
    protected boolean check(AuthRequest request) {
        System.out.println("🔐 Kullanıcı adı ve şifre kontrolü...");

        if (VALID_USERNAME.equals(request.getUsername()) &&
            VALID_PASSWORD.equals(request.getPassword())) {
            System.out.println("   ✅ Kullanıcı adı ve şifre doğru");
            return true;
        } else {
            System.out.println("   ❌ Kullanıcı adı veya şifre yanlış!");
            return false;
        }
    }
}

/**
 * TwoFactorHandler: İki faktörlü kimlik doğrulama
 */
class TwoFactorHandler extends AuthHandler {
    private static final String VALID_CODE = "123456";

    @Override
    protected boolean check(AuthRequest request) {
        System.out.println("📱 İki faktörlü kimlik doğrulama...");

        if (VALID_CODE.equals(request.getTwoFactorCode())) {
            System.out.println("   ✅ 2FA kodu doğru");
            return true;
        } else {
            System.out.println("   ❌ 2FA kodu yanlış!");
            return false;
        }
    }
}

/**
 * IPWhitelistHandler: IP adresi beyaz liste kontrolü
 */
class IPWhitelistHandler extends AuthHandler {
    private static final String[] ALLOWED_IPS = {"192.168.1.1", "10.0.0.1"};

    @Override
    protected boolean check(AuthRequest request) {
        System.out.println("🌐 IP adresi kontrolü...");

        for (String allowedIp : ALLOWED_IPS) {
            if (allowedIp.equals(request.getIpAddress())) {
                System.out.println("   ✅ IP adresi izin listesinde");
                return true;
            }
        }

        System.out.println("   ❌ IP adresi izin listesinde değil!");
        return false;
    }
}

/**
 * RateLimitHandler: Hız limiti kontrolü (basit simülasyon)
 */
class RateLimitHandler extends AuthHandler {

    @Override
    protected boolean check(AuthRequest request) {
        System.out.println("⏱️  Hız limiti kontrolü...");

        // Basit simülasyon - her zaman geçer
        System.out.println("   ✅ Hız limiti aşılmadı");
        return true;
    }
}

// ============================================
// DEMO - AUTHENTICATION CHAIN (FILTERING)
// ============================================
/**
 * AMAÇ: Filtering Chain - Her handler bir kontrol yapar
 *
 * SENARYO: Kimlik Doğrulama Sistemi
 * - Kullanıcı adı/şifre kontrolü
 * - İki faktörlü kimlik doğrulama
 * - IP beyaz liste kontrolü
 * - Hız limiti kontrolü
 *
 * FARK: Klasik Chain vs Filtering Chain
 *
 * KLASİK CHAIN (Support Ticket):
 * - Sadece BİR handler işler
 * - İşleyemeyen bir sonrakine iletir
 *
 * FILTERING CHAIN (Authentication):
 * - TÜM handler'lar kontrol yapar
 * - Biri başarısız olursa zincir DURUR
 * - Tümü başarılıysa işlem tamamlanır
 *
 * FAYDA:
 * - Her kontrol ayrı sınıfta (Single Responsibility)
 * - Kontrol sırası değiştirilebilir
 * - Yeni kontrol eklemek kolay
 * - Test etmek kolay (her handler bağımsız)
 */
public class AuthenticationChainDemo {
    public static void main(String[] args) {
        System.out.println("🎯 CHAIN OF RESPONSIBILITY - KİMLİK DOĞRULAMA ÖRNEĞİ\n");
        System.out.println("💡 Bu bir 'Filtering Chain' örneğidir");
        System.out.println("   Tüm handler'lar kontrol yapar, biri başarısız olursa durur\n");

        // Zinciri oluştur
        AuthHandler usernamePassword = new UsernamePasswordHandler();
        AuthHandler twoFactor = new TwoFactorHandler();
        AuthHandler ipWhitelist = new IPWhitelistHandler();
        AuthHandler rateLimit = new RateLimitHandler();

        usernamePassword.setNext(twoFactor);
        twoFactor.setNext(ipWhitelist);
        ipWhitelist.setNext(rateLimit);

        // Test 1: Başarılı giriş
        System.out.println("=".repeat(60));
        System.out.println("📋 TEST 1: Başarılı Giriş");
        System.out.println("=".repeat(60));
        AuthRequest validRequest = new AuthRequest(
            "admin",
            "12345",
            "123456",
            "192.168.1.1"
        );

        boolean result1 = usernamePassword.handle(validRequest);
        if (result1) {
            System.out.println("\n✅ GİRİŞ BAŞARILI!");
        } else {
            System.out.println("\n❌ GİRİŞ BAŞARISIZ!");
        }

        // Test 2: Yanlış şifre
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 TEST 2: Yanlış Şifre");
        System.out.println("=".repeat(60));
        AuthRequest wrongPassword = new AuthRequest(
            "admin",
            "wrong",
            "123456",
            "192.168.1.1"
        );

        boolean result2 = usernamePassword.handle(wrongPassword);
        if (result2) {
            System.out.println("\n✅ GİRİŞ BAŞARILI!");
        } else {
            System.out.println("\n❌ GİRİŞ BAŞARISIZ!");
        }

        // Test 3: Yanlış 2FA kodu
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 TEST 3: Yanlış 2FA Kodu");
        System.out.println("=".repeat(60));
        AuthRequest wrong2FA = new AuthRequest(
            "admin",
            "12345",
            "wrong",
            "192.168.1.1"
        );

        boolean result3 = usernamePassword.handle(wrong2FA);
        if (result3) {
            System.out.println("\n✅ GİRİŞ BAŞARILI!");
        } else {
            System.out.println("\n❌ GİRİŞ BAŞARISIZ!");
        }

        // Test 4: Yetkisiz IP
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 TEST 4: Yetkisiz IP Adresi");
        System.out.println("=".repeat(60));
        AuthRequest wrongIP = new AuthRequest(
            "admin",
            "12345",
            "123456",
            "99.99.99.99"
        );

        boolean result4 = usernamePassword.handle(wrongIP);
        if (result4) {
            System.out.println("\n✅ GİRİŞ BAŞARILI!");
        } else {
            System.out.println("\n❌ GİRİŞ BAŞARISIZ!");
        }

        // Açıklama
        System.out.println("\n" + "=".repeat(60));
        System.out.println("💡 FILTERING CHAIN'İN ÖZELLİKLERİ:");
        System.out.println("=".repeat(60));
        System.out.println("✅ Her handler bir kontrol yapar");
        System.out.println("✅ Tüm kontroller sırayla geçilir");
        System.out.println("✅ Biri başarısız olursa zincir durur");
        System.out.println("✅ Kontrol sırası önemli (önce şifre, sonra 2FA)");
        System.out.println("✅ Yeni kontrol eklemek kolay");
        System.out.println("=".repeat(60));
    }
}
