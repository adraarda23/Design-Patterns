package BehavioralPatterns.Visitor.ShoppingCart;

// ============================================
// ELEMENT INTERFACE
// ============================================
/**
 * Product: Ürün arayüzü
 * Tüm ürünler visitor'ı kabul etmelidir
 */
interface Product {
    void accept(ShoppingVisitor visitor);
}

// ============================================
// CONCRETE ELEMENTS (ÜRÜNLER)
// ============================================

/**
 * Book: Kitap ürünü
 */
class Book implements Product {
    private String title;
    private double price;

    public Book(String title, double price) {
        this.title = title;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public void accept(ShoppingVisitor visitor) {
        visitor.visitBook(this);
    }
}

/**
 * Electronics: Elektronik ürün
 */
class Electronics implements Product {
    private String name;
    private double price;

    public Electronics(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public void accept(ShoppingVisitor visitor) {
        visitor.visitElectronics(this);
    }
}

/**
 * Food: Gıda ürünü
 */
class Food implements Product {
    private String name;
    private double price;

    public Food(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public void accept(ShoppingVisitor visitor) {
        visitor.visitFood(this);
    }
}

// ============================================
// VISITOR INTERFACE
// ============================================
/**
 * ShoppingVisitor: Alışveriş işlemleri için visitor arayüzü
 * Her ürün tipi için visit metodu
 */
interface ShoppingVisitor {
    void visitBook(Book book);
    void visitElectronics(Electronics electronics);
    void visitFood(Food food);
}

// ============================================
// CONCRETE VISITORS
// ============================================

/**
 * TaxCalculator: Vergi hesaplama
 * Her ürün tipine farklı vergi oranı uygular
 *
 * Vergi Oranları:
 * - Kitap: %1 (düşük vergi)
 * - Elektronik: %18 (standart KDV)
 * - Gıda: %8 (indirimli oran)
 */
class TaxCalculator implements ShoppingVisitor {
    private double totalTax = 0;

    @Override
    public void visitBook(Book book) {
        double tax = book.getPrice() * 0.01;  // %1
        totalTax += tax;
        System.out.println("📚 " + book.getTitle() + " - Fiyat: " + book.getPrice() +
                         "₺ → Vergi (%1): " + String.format("%.2f", tax) + "₺");
    }

    @Override
    public void visitElectronics(Electronics electronics) {
        double tax = electronics.getPrice() * 0.18;  // %18
        totalTax += tax;
        System.out.println("📱 " + electronics.getName() + " - Fiyat: " + electronics.getPrice() +
                         "₺ → Vergi (%18): " + String.format("%.2f", tax) + "₺");
    }

    @Override
    public void visitFood(Food food) {
        double tax = food.getPrice() * 0.08;  // %8
        totalTax += tax;
        System.out.println("🍎 " + food.getName() + " - Fiyat: " + food.getPrice() +
                         "₺ → Vergi (%8): " + String.format("%.2f", tax) + "₺");
    }

    public double getTotalTax() {
        return totalTax;
    }
}

/**
 * DiscountCalculator: İndirim hesaplama
 * Her ürün tipine farklı indirim oranı uygular
 *
 * İndirim Oranları:
 * - Kitap: %10 (kampanya)
 * - Elektronik: %5 (standart)
 * - Gıda: %0 (indirimsiz)
 */
class DiscountCalculator implements ShoppingVisitor {
    private double totalDiscount = 0;

    @Override
    public void visitBook(Book book) {
        double discount = book.getPrice() * 0.10;  // %10
        totalDiscount += discount;
        System.out.println("📚 " + book.getTitle() + " - Fiyat: " + book.getPrice() +
                         "₺ → İndirim (%10): " + String.format("%.2f", discount) + "₺");
    }

    @Override
    public void visitElectronics(Electronics electronics) {
        double discount = electronics.getPrice() * 0.05;  // %5
        totalDiscount += discount;
        System.out.println("📱 " + electronics.getName() + " - Fiyat: " + electronics.getPrice() +
                         "₺ → İndirim (%5): " + String.format("%.2f", discount) + "₺");
    }

    @Override
    public void visitFood(Food food) {
        double discount = 0;  // %0
        totalDiscount += discount;
        System.out.println("🍎 " + food.getName() + " - Fiyat: " + food.getPrice() +
                         "₺ → İndirim (%0): " + String.format("%.2f", discount) + "₺");
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }
}

/**
 * ShippingCostCalculator: Kargo ücreti hesaplama
 * Her ürün tipine farklı kargo ücreti
 *
 * Kargo Ücretleri:
 * - Kitap: 5₺ (hafif)
 * - Elektronik: 15₺ (paketleme gerekir)
 * - Gıda: 10₺ (özel taşıma)
 */
class ShippingCostCalculator implements ShoppingVisitor {
    private double totalShipping = 0;

    @Override
    public void visitBook(Book book) {
        double shipping = 5.0;  // 5₺
        totalShipping += shipping;
        System.out.println("📚 " + book.getTitle() + " → Kargo: " + shipping + "₺");
    }

    @Override
    public void visitElectronics(Electronics electronics) {
        double shipping = 15.0;  // 15₺
        totalShipping += shipping;
        System.out.println("📱 " + electronics.getName() + " → Kargo: " + shipping + "₺");
    }

    @Override
    public void visitFood(Food food) {
        double shipping = 10.0;  // 10₺
        totalShipping += shipping;
        System.out.println("🍎 " + food.getName() + " → Kargo: " + shipping + "₺");
    }

    public double getTotalShipping() {
        return totalShipping;
    }
}

// ============================================
// DEMO - SHOPPING CART VISITOR
// ============================================
/**
 * AMAÇ: Farklı ürün tiplerine farklı işlemler uygulama
 *
 * SENARYO: E-ticaret sepeti
 * - Farklı ürün tipleri (Book, Electronics, Food)
 * - Her ürün tipine farklı vergi, indirim, kargo
 *
 * VISITOR OLMADAN:
 * class Book {
 *     double calculateTax() { return price * 0.01; }
 *     double calculateDiscount() { return price * 0.10; }
 *     double calculateShipping() { return 5.0; }
 * }
 * // Aynı mantık Electronics ve Food için de tekrarlanır
 * // Kod tekrarı, her sınıfa aynı metodları eklemek gerekir
 *
 * VISITOR İLE:
 * class Book {
 *     void accept(Visitor v) { v.visitBook(this); }
 * }
 * // İşlemler ayrı visitor'larda (Tax, Discount, Shipping)
 *
 * FAYDA:
 * - Her işlem ayrı sınıfta (Single Responsibility)
 * - Yeni işlem eklemek kolay (yeni Visitor)
 * - Ürün sınıfları basit kalıyor
 */
public class ShoppingCartVisitorDemo {
    public static void main(String[] args) {
        System.out.println("🎯 VISITOR PATTERN - E-TİCARET SEPETİ ÖRNEĞİ\n");

        // Sepete ürünler ekle
        Product[] cart = {
            new Book("Design Patterns", 100.0),
            new Book("Clean Code", 120.0),
            new Electronics("iPhone", 15000.0),
            new Electronics("AirPods", 2500.0),
            new Food("Çikolata", 20.0),
            new Food("Kahve", 50.0)
        };

        // İşlem 1: Vergi Hesaplama
        System.out.println("=".repeat(60));
        System.out.println("📍 İŞLEM 1: VERGİ HESAPLAMA");
        System.out.println("=".repeat(60));
        TaxCalculator taxCalc = new TaxCalculator();
        for (Product product : cart) {
            product.accept(taxCalc);
        }
        System.out.println("\n💰 Toplam Vergi: " + String.format("%.2f", taxCalc.getTotalTax()) + "₺");

        // İşlem 2: İndirim Hesaplama
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📍 İŞLEM 2: İNDİRİM HESAPLAMA");
        System.out.println("=".repeat(60));
        DiscountCalculator discountCalc = new DiscountCalculator();
        for (Product product : cart) {
            product.accept(discountCalc);
        }
        System.out.println("\n🎁 Toplam İndirim: " + String.format("%.2f", discountCalc.getTotalDiscount()) + "₺");

        // İşlem 3: Kargo Hesaplama
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📍 İŞLEM 3: KARGO ÜCRETİ HESAPLAMA");
        System.out.println("=".repeat(60));
        ShippingCostCalculator shippingCalc = new ShippingCostCalculator();
        for (Product product : cart) {
            product.accept(shippingCalc);
        }
        System.out.println("\n📦 Toplam Kargo: " + String.format("%.2f", shippingCalc.getTotalShipping()) + "₺");

        // Özet
        System.out.println("\n" + "=".repeat(60));
        System.out.println("💡 VISITOR PATTERN'IN FAYDASI:");
        System.out.println("=".repeat(60));
        System.out.println("✅ Her ürün tipi farklı vergi/indirim/kargo oranı alıyor");
        System.out.println("✅ Ürün sınıfları basit (sadece accept metodu)");
        System.out.println("✅ İşlemler ayrı visitor sınıflarında");
        System.out.println("✅ Yeni işlem? → Yeni Visitor (ürünlere dokunma)");
        System.out.println("=".repeat(60));
    }
}
