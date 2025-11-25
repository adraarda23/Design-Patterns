package Behaviourals.StrategyPattern;

/**
 * CONCRETE STRATEGY #2 - PayPal Ödemesi
 *
 * Bu sınıf PaymentStrategy interface'ini implement eder ve
 * PayPal ile ödeme yapmanın spesifik mantığını içerir.
 *
 * Dikkat: Aynı interface'i kullanıyor ama tamamen farklı bir algoritma!
 */
public class PayPalPayment implements PaymentStrategy {

    // PayPal hesap bilgileri
    private String email;
    private String password;

    /**
     * Constructor - PayPal hesap bilgilerini alır
     */
    public PayPalPayment(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * PayPal ile ödeme işlemi
     * Gerçek hayatta PayPal API'sine istek gönderir
     */
    @Override
    public boolean pay(double amount) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      PAYPAL İLE ÖDEME İŞLEMİ          ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("📧 Email: " + email);
        System.out.println("💰 Tutar: " + amount + " TL");
        System.out.println("✅ PayPal'a bağlanılıyor...");
        System.out.println("✅ Kullanıcı doğrulanıyor...");
        System.out.println("✅ PayPal bakiyesi kontrol ediliyor...");
        System.out.println("✅ Ödeme PayPal üzerinden tamamlandı!");

        return true; // Ödeme başarılı
    }

    @Override
    public String getPaymentMethodName() {
        return "PayPal";
    }
}
