package Behaviourals.StrategyPattern;

/**
 * CONCRETE STRATEGY #3 - Kripto Para Ödemesi
 *
 * Bu sınıf PaymentStrategy interface'ini implement eder ve
 * kripto para (Bitcoin, Ethereum vb.) ile ödeme yapmanın mantığını içerir.
 *
 * Üç farklı strateji var ama hepsi aynı interface'i kullanıyor!
 * Bu sayede birbirinin yerine kullanılabiliyorlar.
 */
public class CryptoPayment implements PaymentStrategy {

    // Kripto para cüzdan bilgileri
    private String walletAddress;
    private String cryptoType; // Bitcoin, Ethereum, vb.

    /**
     * Constructor - Kripto cüzdan bilgilerini alır
     */
    public CryptoPayment(String walletAddress, String cryptoType) {
        this.walletAddress = walletAddress;
        this.cryptoType = cryptoType;
    }

    /**
     * Kripto para ile ödeme işlemi
     * Gerçek hayatta blockchain üzerinden transaction oluşturur
     */
    @Override
    public boolean pay(double amount) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   KRİPTO PARA İLE ÖDEME İŞLEMİ        ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("₿ Kripto Tipi: " + cryptoType);
        System.out.println("🔐 Cüzdan Adresi: " + walletAddress.substring(0, 10) + "...");
        System.out.println("💰 Tutar: " + amount + " TL (≈ " + String.format("%.6f", amount / 50000) + " BTC)");
        System.out.println("✅ Blockchain ağına bağlanılıyor...");
        System.out.println("✅ Transaction oluşturuluyor...");
        System.out.println("✅ Madenciler tarafından onaylanıyor...");
        System.out.println("✅ Blockchain'e kaydedildi! Transaction ID: 0x7a8f...");

        return true; // Ödeme başarılı
    }

    @Override
    public String getPaymentMethodName() {
        return "Kripto Para (" + cryptoType + ")";
    }
}
