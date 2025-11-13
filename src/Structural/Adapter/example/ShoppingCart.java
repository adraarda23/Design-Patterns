package Structural.Adapter.example;

/**
 * CLIENT
 *
 * Alışveriş sepeti. Ödeme yapmak için PaymentService interface'ini kullanır.
 * Hangi ödeme sağlayıcının kullanıldığından habersizdir (encapsulation).
 */
public class ShoppingCart {

    private double totalAmount;

    public ShoppingCart(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * Ödemeyi tamamla
     * @param paymentService Herhangi bir PaymentService implementasyonu
     */
    public void checkout(PaymentService paymentService) {
        System.out.println("Sepet Toplamı: " + totalAmount + " TL");
        System.out.println("Ödeme işleniyor...");

        // Polymorphism: PaymentService interface'i üzerinden çağır
        // Adapter mı, başka bir implementasyon mu? Client umursamaz!
        paymentService.pay(totalAmount);

        System.out.println("Ödeme tamamlandı! ✅\n");
    }
}
