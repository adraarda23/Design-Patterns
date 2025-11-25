package Behaviourals.StrategyPattern;

/**
 * STRATEGY PATTERN DEMO
 *
 * Bu sınıf, Strategy Pattern'in nasıl kullanıldığını gösterir.
 *
 * SENARYO:
 * 1. Müşteri alışveriş yapıyor (sepete ürün ekliyor)
 * 2. Farklı ödeme yöntemleri deniyor
 * 3. Runtime'da ödeme stratejisi değiştiriliyor
 *
 * STRATEGY PATTERN'İN AVANTAJLARI:
 * ✅ Açık/Kapalı Prensibi (Open/Closed): Yeni strateji eklerken mevcut kodu değiştirmiyoruz
 * ✅ Tek Sorumluluk (Single Responsibility): Her strateji kendi işini yapıyor
 * ✅ Bağımlılığın Tersine Çevrilmesi (DIP): Context, interface'e bağımlı, concrete sınıfa değil
 * ✅ Runtime'da değiştirilebilir: Stratejiyi kod çalışırken değiştirebiliyoruz
 * ✅ Test edilebilir: Her stratejiyi ayrı ayrı test edebiliriz
 * ✅ Okunabilir kod: Her algoritma kendi dosyasında, if-else kalabalığı yok
 */
public class StrategyPatternDemo {

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║                                                  ║");
        System.out.println("║         STRATEGY PATTERN DEMONSTRATION           ║");
        System.out.println("║         (Ödeme Sistemi Örneği)                   ║");
        System.out.println("║                                                  ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        // ADIM 1: Alışveriş Sepeti Oluştur (Context)
        ShoppingCart cart = new ShoppingCart();

        // ADIM 2: Sepete Ürün Ekle
        System.out.println("\n📌 ADIM 1: Sepete ürünler ekleniyor...\n");
        cart.addItem("Laptop - Asus ROG", 35000.00);
        cart.addItem("Kablosuz Mouse", 450.00);
        cart.addItem("Mekanik Klavye", 1200.00);

        // Sepeti göster
        cart.showCart();

        // ═══════════════════════════════════════════════════════════
        // SENARYO 1: KREDİ KARTI İLE ÖDEME
        // ═══════════════════════════════════════════════════════════
        System.out.println("\n\n" + "═".repeat(60));
        System.out.println("📌 SENARYO 1: Müşteri kredi kartı ile ödeme yapmak istiyor");
        System.out.println("═".repeat(60));

        // Kredi kartı stratejisini oluştur
        PaymentStrategy creditCardStrategy = new CreditCardPayment(
                "1234567890123456",
                "Ahmet Yılmaz",
                "123",
                "12/25"
        );

        // Stratejyi sepete ata
        cart.setPaymentStrategy(creditCardStrategy);

        // Ödeme yap
        cart.checkout();

        // ═══════════════════════════════════════════════════════════
        // SENARYO 2: Yeni alışveriş - PAYPAL İLE ÖDEME
        // ═══════════════════════════════════════════════════════════
        System.out.println("\n\n" + "═".repeat(60));
        System.out.println("📌 SENARYO 2: Yeni müşteri PayPal ile ödeme yapmak istiyor");
        System.out.println("═".repeat(60));

        // Yeni sepet oluştur
        ShoppingCart cart2 = new ShoppingCart();
        cart2.addItem("Kulaklık - Sony WH-1000XM4", 5000.00);
        cart2.addItem("USB-C Kablo", 150.00);
        cart2.showCart();

        // PayPal stratejisini oluştur ve ata
        PaymentStrategy paypalStrategy = new PayPalPayment(
                "mehmet@example.com",
                "gizliSifre123"
        );
        cart2.setPaymentStrategy(paypalStrategy);

        // Ödeme yap
        cart2.checkout();

        // ═══════════════════════════════════════════════════════════
        // SENARYO 3: KRİPTO PARA İLE ÖDEME
        // ═══════════════════════════════════════════════════════════
        System.out.println("\n\n" + "═".repeat(60));
        System.out.println("📌 SENARYO 3: Müşteri kripto para ile ödeme yapmak istiyor");
        System.out.println("═".repeat(60));

        ShoppingCart cart3 = new ShoppingCart();
        cart3.addItem("Gaming Chair", 7500.00);
        cart3.addItem("Monitör - 27\" 144Hz", 4500.00);
        cart3.showCart();

        // Kripto para stratejisini oluştur ve ata
        PaymentStrategy cryptoStrategy = new CryptoPayment(
                "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa",
                "Bitcoin"
        );
        cart3.setPaymentStrategy(cryptoStrategy);

        // Ödeme yap
        cart3.checkout();

        // ═══════════════════════════════════════════════════════════
        // SENARYO 4: RUNTIME'DA STRATEJİ DEĞİŞTİRME (EN ÖNEMLİ ÖZELLIK!)
        // ═══════════════════════════════════════════════════════════
        System.out.println("\n\n" + "═".repeat(60));
        System.out.println("📌 SENARYO 4: Müşteri ödeme yöntemini değiştirmek istiyor!");
        System.out.println("═".repeat(60));

        ShoppingCart cart4 = new ShoppingCart();
        cart4.addItem("iPhone 15 Pro", 45000.00);
        cart4.showCart();

        // Önce kredi kartı seçiyor
        System.out.println("\n🤔 Müşteri önce kredi kartı seçti...");
        cart4.setPaymentStrategy(creditCardStrategy);

        // Ama sonra fikir değiştirip PayPal'a geçiyor
        System.out.println("🤔 Ama sonra PayPal ile ödemeye karar verdi!");
        cart4.setPaymentStrategy(paypalStrategy);

        // PayPal ile ödeme yapılıyor
        cart4.checkout();

        // ═══════════════════════════════════════════════════════════
        // ÖNEMLİ NOTLAR
        // ═══════════════════════════════════════════════════════════
        System.out.println("\n\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                                                          ║");
        System.out.println("║              STRATEGY PATTERN'İN FAYDALARI               ║");
        System.out.println("║                                                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");

        System.out.println("\n✨ GÖRDÜĞÜMÜZ ÖNEMLİ NOKTALAR:");
        System.out.println("\n1️⃣ ESNEKLIK:");
        System.out.println("   • Runtime'da ödeme yöntemini değiştirebildik");
        System.out.println("   • Her müşteri farklı ödeme yöntemi kullanabildi");

        System.out.println("\n2️⃣ BAKIM KOLAYLığI:");
        System.out.println("   • Her ödeme yöntemi kendi dosyasında");
        System.out.println("   • Birini değiştirmek diğerini etkilemiyor");

        System.out.println("\n3️⃣ GENİŞLETİLEBİLİRLİK:");
        System.out.println("   • Yeni ödeme yöntemi eklemek için:");
        System.out.println("     → PaymentStrategy interface'ini implement et");
        System.out.println("     → ShoppingCart sınıfına DOKUNMA!");
        System.out.println("   • Örnek: ApplePayPayment, GooglePayPayment eklenebilir");

        System.out.println("\n4️⃣ TEMİZ KOD:");
        System.out.println("   • ShoppingCart'ta if-else yok!");
        System.out.println("   • Her sınıf tek bir iş yapıyor (Single Responsibility)");

        System.out.println("\n5️⃣ TEST EDİLEBİLİRLİK:");
        System.out.println("   • Her ödeme stratejisini ayrı test edebiliriz");
        System.out.println("   • Mock strategy kullanarak birim testler yazabiliriz");

        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║  Strategy Pattern başarıyla gösterildi! 🎉             ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");
    }
}
