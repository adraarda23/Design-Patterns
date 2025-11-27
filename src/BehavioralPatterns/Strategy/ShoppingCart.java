package BehavioralPatterns.Strategy;

import java.util.ArrayList;
import java.util.List;

/**
 * CONTEXT SINIFI - Alışveriş Sepeti
 *
 * Bu sınıf, stratejileri kullanan ve yöneten sınıftır.
 * Strategy Pattern'in kalbidir!
 *
 * ÖNEMLİ NOKTALAR:
 * 1. İçinde bir PaymentStrategy referansı tutar (composition)
 * 2. Runtime'da bu stratejiyi değiştirebilir (setPaymentStrategy)
 * 3. Hangi stratejinin kullanıldığını bilmez/umursamaz
 * 4. Sadece interface'deki metotları çağırır
 *
 * Bu sayede yeni ödeme yöntemleri eklenebilir, bu sınıfa dokunmadan!
 */
public class ShoppingCart {

    // Sepetteki ürünler
    private List<String> items;
    private double totalAmount;

    // ÖNEMLİ: PaymentStrategy interface'ini tutuyoruz
    // Concrete sınıfı değil, interface'i!
    // Bu sayede herhangi bir PaymentStrategy implementasyonunu kullanabiliriz
    private PaymentStrategy paymentStrategy;

    /**
     * Constructor - Boş sepet oluşturur
     */
    public ShoppingCart() {
        this.items = new ArrayList<>();
        this.totalAmount = 0.0;
    }

    /**
     * Sepete ürün ekler
     *
     * @param item Ürün adı
     * @param price Ürün fiyatı
     */
    public void addItem(String item, double price) {
        items.add(item);
        totalAmount += price;
        System.out.println("🛒 Sepete eklendi: " + item + " - " + price + " TL");
    }

    /**
     * STRATEJİYİ DEĞİŞTİRME METODu
     *
     * Bu metot ile runtime'da ödeme stratejisini değiştirebiliriz!
     * Müşteri istediği zaman farklı bir ödeme yöntemi seçebilir.
     *
     * @param paymentStrategy Yeni ödeme stratejisi
     */
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
        System.out.println("💡 Ödeme yöntemi değiştirildi: " + paymentStrategy.getPaymentMethodName());
    }

    /**
     * Sepeti görüntüler
     */
    public void showCart() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         ALIŞVERİŞ SEPETİNİZ            ║");
        System.out.println("╚════════════════════════════════════════╝");

        if (items.isEmpty()) {
            System.out.println("🛒 Sepetiniz boş");
        } else {
            System.out.println("📦 Ürünler:");
            for (int i = 0; i < items.size(); i++) {
                System.out.println("   " + (i + 1) + ". " + items.get(i));
            }
            System.out.println("\n💰 Toplam Tutar: " + totalAmount + " TL");
        }
    }

    /**
     * ÖDEME İŞLEMİNİ GERÇEKLEŞTİRİR
     *
     * Bu metot Strategy Pattern'in gücünü gösterir!
     *
     * Dikkat et:
     * - Hangi ödeme yönteminin kullanıldığını bilmiyor
     * - if-else veya switch-case yok!
     * - Sadece paymentStrategy.pay() metodunu çağırıyor
     * - Runtime'da hangi strateji set edildiyse o çalışır
     *
     * Bu sayede:
     * - Yeni ödeme yöntemi eklenince bu kod değişmez (Open/Closed Principle)
     * - Test etmesi kolay (mock strategy verebilirsin)
     * - Bakımı kolay (her strateji kendi dosyasında)
     *
     * @return Ödeme başarılı mı?
     */
    public boolean checkout() {
        if (paymentStrategy == null) {
            System.out.println("❌ HATA: Lütfen önce bir ödeme yöntemi seçin!");
            return false;
        }

        if (items.isEmpty()) {
            System.out.println("❌ HATA: Sepetiniz boş!");
            return false;
        }

        System.out.println("\n" + "=".repeat(50));
        System.out.println("🔄 ÖDEME İŞLEMİ BAŞLIYOR...");
        System.out.println("=".repeat(50));

        // İşte Strategy Pattern'in büyüsü!
        // Runtime'da set edilen strateji çalışır
        boolean paymentSuccess = paymentStrategy.pay(totalAmount);

        if (paymentSuccess) {
            System.out.println("\n✅ Sipariş başarıyla tamamlandı!");
            System.out.println("📧 Onay e-postası gönderildi.");
            items.clear();
            totalAmount = 0.0;
        } else {
            System.out.println("\n❌ Ödeme başarısız oldu!");
        }

        return paymentSuccess;
    }

    /**
     * Sepetteki toplam tutarı döndürür
     */
    public double getTotalAmount() {
        return totalAmount;
    }
}
