package BehavioralPatterns.Strategy;

/**
 * CONCRETE STRATEGY #1 - Kredi Kartı Ödemesi
 *
 * Bu sınıf PaymentStrategy interface'ini implement eder ve
 * kredi kartı ile ödeme yapmanın spesifik mantığını içerir.
 *
 * Her Concrete Strategy, interface'deki metotları kendi iş mantığına göre doldurur.
 */
public class CreditCardPayment implements PaymentStrategy {

    // Kredi kartı bilgileri
    private String cardNumber;
    private String cardHolderName;
    private String cvv;
    private String expiryDate;

    /**
     * Constructor - Kredi kartı bilgilerini alır
     */
    public CreditCardPayment(String cardNumber, String cardHolderName, String cvv, String expiryDate) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
    }

    /**
     * Kredi kartı ile ödeme işlemi
     * Gerçek hayatta bu metot banka API'sine bağlanır, burada simüle ediyoruz
     */
    @Override
    public boolean pay(double amount) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   KREDİ KARTI İLE ÖDEME İŞLEMİ        ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("💳 Kart Sahibi: " + cardHolderName);
        System.out.println("💳 Kart No: **** **** **** " + cardNumber.substring(cardNumber.length() - 4));
        System.out.println("💰 Tutar: " + amount + " TL");
        System.out.println("✅ Banka ile bağlantı kuruluyor...");
        System.out.println("✅ CVV doğrulanıyor...");
        System.out.println("✅ Bakiye kontrol ediliyor...");
        System.out.println("✅ Ödeme başarıyla gerçekleştirildi!");

        return true; // Ödeme başarılı
    }

    @Override
    public String getPaymentMethodName() {
        return "Kredi Kartı";
    }
}
