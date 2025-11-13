package Structural.Adapter.example;

/**
 * DEMO - Test Sınıfı
 *
 * Adapter Pattern'in nasıl çalıştığını gösterir.
 */
public class AdapterDemo {

    public static void main(String[] args) {
        System.out.println("=== ADAPTER PATTERN ÖRNEĞİ ===\n");

        // 1. Alışveriş sepeti oluştur
        ShoppingCart cart = new ShoppingCart(250.50);

        // 2. Banka API'sini oluştur (3. parti sistem)
        BankAPI ziraatBankAPI = new BankAPI("Ziraat");

        // 3. Adapter ile sarmala
        // Banka API'sini PaymentService interface'ine uyumlu hale getir
        PaymentService paymentService = new BankPaymentAdapter(
            ziraatBankAPI,
            "TR123456789"  // Hesap numarası
        );

        // 4. Ödeme yap
        // Client (ShoppingCart) sadece PaymentService interface'ini biliyor
        // Adapter sayesinde BankAPI kullanılıyor ama client habersiz!
        cart.checkout(paymentService);

        System.out.println("=== AKIŞ ANALİZİ ===");
        System.out.println("1. cart.checkout(paymentService)");
        System.out.println("2. paymentService.pay(250.50)  ← PaymentService interface");
        System.out.println("3. BankPaymentAdapter.pay(250.50)");
        System.out.println("4. bankAPI.transferMoney(250.50, \"TR123456789\")  ← BankAPI metodu");
        System.out.println("5. ✅ Ödeme tamamlandı!");

        System.out.println("\n💡 Adapter olmadan:");
        System.out.println("❌ cart.checkout(ziraatBankAPI)  → HATA!");
        System.out.println("Çünkü BankAPI, PaymentService interface'ini implement etmiyor!");
    }
}
