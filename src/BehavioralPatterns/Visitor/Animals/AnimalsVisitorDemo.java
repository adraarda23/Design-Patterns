package BehavioralPatterns.Visitor.Animals;

// ============================================
// ELEMENT INTERFACE
// ============================================
/**
 * Animal: Hayvan arayüzü
 * Visitor'ı kabul etmek için accept() metodu tanımlar
 */
interface Animal {
    void accept(Visitor visitor);
}

// ============================================
// CONCRETE ELEMENTS
// ============================================

/**
 * Dog: Köpek
 * Visitor'ı kabul eder ve kendini visitDog() metoduna gönderir
 */
class Dog implements Animal {
    private String name;

    public Dog(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitDog(this);  // Visitor'a kendini Dog olarak ver
    }
}

/**
 * Cat: Kedi
 * Visitor'ı kabul eder ve kendini visitCat() metoduna gönderir
 */
class Cat implements Animal {
    private String name;

    public Cat(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitCat(this);  // Visitor'a kendini Cat olarak ver
    }
}

// ============================================
// VISITOR INTERFACE
// ============================================
/**
 * Visitor: Ziyaretçi arayüzü
 * Her element tipi için bir visit metodu tanımlar
 */
interface Visitor {
    void visitDog(Dog dog);
    void visitCat(Cat cat);
}

// ============================================
// CONCRETE VISITORS
// ============================================

/**
 * Feeder: Besleme işlemi
 * Her hayvan tipine uygun yiyecek verir
 */
class Feeder implements Visitor {

    @Override
    public void visitDog(Dog dog) {
        System.out.println("🦴 " + dog.getName() + " (köpek) → Kemik veriliyor");
    }

    @Override
    public void visitCat(Cat cat) {
        System.out.println("🥛 " + cat.getName() + " (kedi) → Süt veriliyor");
    }
}

/**
 * Veterinarian: Veteriner muayenesi
 * Her hayvan tipine uygun muayene yapar
 */
class Veterinarian implements Visitor {

    @Override
    public void visitDog(Dog dog) {
        System.out.println("🦷 " + dog.getName() + " (köpek) → Diş kontrolü yapılıyor");
    }

    @Override
    public void visitCat(Cat cat) {
        System.out.println("✨ " + cat.getName() + " (kedi) → Tüy kontrolü yapılıyor");
    }
}

/**
 * Trainer: Eğitim işlemi
 * Her hayvan tipine uygun eğitim verir
 */
class Trainer implements Visitor {

    @Override
    public void visitDog(Dog dog) {
        System.out.println("🎾 " + dog.getName() + " (köpek) → Top getirme eğitimi");
    }

    @Override
    public void visitCat(Cat cat) {
        System.out.println("🧶 " + cat.getName() + " (kedi) → Oyun eğitimi");
    }
}

// ============================================
// DEMO - VISITOR PATTERN (BASİT ÖRNEK)
// ============================================
/**
 * AMAÇ: Visitor Pattern'ın temel prensibini göstermek
 *
 * SENARYO: Hayvanat Bahçesi
 * - Farklı hayvan tipleri var (Dog, Cat)
 * - Farklı işlemler yapılacak (Feed, Examine, Train)
 *
 * VISITOR OLMADAN:
 * class Dog {
 *     void feed() { }
 *     void examine() { }
 *     void train() { }
 *     // Her yeni işlem için Dog ve Cat'e metod eklemen gerekir!
 * }
 *
 * VISITOR İLE:
 * class Dog {
 *     void accept(Visitor v) { v.visitDog(this); }
 *     // Yeni işlem? Sadece yeni Visitor yaz!
 * }
 *
 * FAYDA:
 * - İşlemler ayrı sınıflarda (Feeder, Vet, Trainer)
 * - Yeni işlem eklemek kolay (yeni Visitor)
 * - Hayvan sınıfları temiz kalıyor
 */
public class AnimalsVisitorDemo {
    public static void main(String[] args) {
        System.out.println("🎯 VISITOR PATTERN - HAYVANAT BAHÇESİ ÖRNEĞİ\n");

        // Hayvanları oluştur
        Dog karabas = new Dog("Karabaş");
        Cat tekir = new Cat("Tekir");
        Dog pamuk = new Dog("Pamuk");
        Cat minnoş = new Cat("Minnoş");

        // İşlem 1: Besleme
        System.out.println("=".repeat(50));
        System.out.println("📍 İŞLEM 1: BESLEME");
        System.out.println("=".repeat(50));
        Feeder besleyici = new Feeder();
        karabas.accept(besleyici);
        tekir.accept(besleyici);
        pamuk.accept(besleyici);
        minnoş.accept(besleyici);

        // İşlem 2: Veteriner Muayenesi
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📍 İŞLEM 2: VETERİNER MUAYENESİ");
        System.out.println("=".repeat(50));
        Veterinarian veteriner = new Veterinarian();
        karabas.accept(veteriner);
        tekir.accept(veteriner);
        pamuk.accept(veteriner);
        minnoş.accept(veteriner);

        // İşlem 3: Eğitim
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📍 İŞLEM 3: EĞİTİM");
        System.out.println("=".repeat(50));
        Trainer egitmen = new Trainer();
        karabas.accept(egitmen);
        tekir.accept(egitmen);
        pamuk.accept(egitmen);
        minnoş.accept(egitmen);

        // Açıklama
        System.out.println("\n" + "=".repeat(50));
        System.out.println("💡 VISITOR PATTERN'IN FAYDASI:");
        System.out.println("=".repeat(50));
        System.out.println("✅ Dog ve Cat sınıfları sadece accept() içeriyor");
        System.out.println("✅ İşlemler ayrı Visitor sınıflarında");
        System.out.println("✅ Yeni işlem eklemek kolay:");
        System.out.println("   → Yeni Visitor yaz (Dog/Cat'e dokunma!)");
        System.out.println("✅ Separation of Concerns (İşleri ayırma)");
        System.out.println("=".repeat(50));
    }
}
