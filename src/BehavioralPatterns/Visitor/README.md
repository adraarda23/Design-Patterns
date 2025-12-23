# Visitor Design Pattern

## 📚 Genel Bakış

**Visitor Pattern**, nesne yapısındaki elementlere **yeni işlemler eklemek** için kullanılan bir behavioral tasarım desenidir. Element sınıflarını değiştirmeden yeni operasyonlar ekleyebilirsiniz.

**Temel Fikir:** "İşlem" ile "Veri Yapısı"nı ayır.

---

## 🤔 Problem

### Visitor Olmadan:

```java
class Dog {
    void feed() { }
    void examine() { }
    void train() { }
    void wash() { }
    // Her yeni işlem için yeni metod!
    // Dog sınıfı şişiyor
}

class Cat {
    void feed() { }
    void examine() { }
    void train() { }
    void wash() { }
    // Aynı sorun burada da!
}
```

**Sorunlar:**
- Her yeni işlem için **TÜM** element sınıflarını değiştirmek gerekiyor
- Element sınıfları şişiyor (çok fazla sorumluluk)
- İş mantığı yanlış yerde (besleme mantığı köpek sınıfında olmamalı)

### Visitor İle:

```java
class Dog {
    void accept(Visitor v) {
        v.visitDog(this);
    }
    // Sadece bu! Başka bir şey yok.
}

// Yeni işlem? Yeni Visitor!
class Feeder implements Visitor {
    void visitDog(Dog dog) { /* besleme mantığı */ }
    void visitCat(Cat cat) { /* besleme mantığı */ }
}
```

**Fayda:** Dog/Cat sınıflarına **dokunmadan** yeni işlem ekliyorsun!

---

## 🏗️ Yapı

### 1. Element (Interface)
Visitor'ı kabul eden arayüz
```java
interface Element {
    void accept(Visitor visitor);
}
```

### 2. Concrete Element
Gerçek element sınıfları
```java
class Dog implements Element {
    void accept(Visitor v) {
        v.visitDog(this);
    }
}
```

### 3. Visitor (Interface)
Her element tipi için visit metodu
```java
interface Visitor {
    void visitDog(Dog dog);
    void visitCat(Cat cat);
}
```

### 4. Concrete Visitor
Gerçek işlem mantığı
```java
class Feeder implements Visitor {
    void visitDog(Dog dog) { /* asıl iş */ }
    void visitCat(Cat cat) { /* asıl iş */ }
}
```

---

## 📂 Örnekler

Bu implementasyonda **3 farklı örnek** bulunmaktadır:

```
Visitor/
├── Animals/
│   └── AnimalsVisitorDemo.java
├── ShoppingCart/
│   └── ShoppingCartVisitorDemo.java
├── DocumentExport/
│   └── DocumentExportVisitorDemo.java
└── README.md
```

---

## 1️⃣ Animals - Basit Başlangıç

### Senaryo:
Hayvanat bahçesi - farklı işlemler (besleme, muayene, eğitim)

### Element'ler:
- Dog (Köpek)
- Cat (Kedi)

### Visitor'lar:
- Feeder → Her hayvana uygun yiyecek
- Veterinarian → Her hayvana uygun muayene
- Trainer → Her hayvana uygun eğitim

### Nasıl Çalışır:
```java
Dog karabas = new Dog("Karabaş");
Feeder besleyici = new Feeder();

karabas.accept(besleyici);
// → besleyici.visitDog(karabas)
// → "Karabaş (köpek) → Kemik veriliyor"
```

### Çalıştırma:
```bash
java -cp src BehavioralPatterns.Visitor.Animals.AnimalsVisitorDemo
```

### Çıktı Örneği:
```
=== BESLEME ===
🦴 Karabaş (köpek) → Kemik veriliyor
🥛 Tekir (kedi) → Süt veriliyor

=== MUAYENE ===
🦷 Karabaş (köpek) → Diş kontrolü yapılıyor
✨ Tekir (kedi) → Tüy kontrolü yapılıyor
```

### Öğrettiği:
- Visitor Pattern'ın temel prensibi
- Double Dispatch nasıl çalışır
- Basit ve anlaşılır örnek

---

## 2️⃣ ShoppingCart - E-Ticaret

### Senaryo:
Alışveriş sepeti - farklı ürünlere farklı hesaplamalar

### Element'ler:
- Book (Kitap)
- Electronics (Elektronik)
- Food (Gıda)

### Visitor'lar:
- TaxCalculator → Her ürüne farklı vergi oranı
  - Kitap: %1
  - Elektronik: %18
  - Gıda: %8
- DiscountCalculator → Farklı indirim oranları
- ShippingCostCalculator → Farklı kargo ücretleri

### Nasıl Çalışır:
```java
Product book = new Book("Design Patterns", 100.0);
TaxCalculator taxCalc = new TaxCalculator();

book.accept(taxCalc);
// → taxCalc.visitBook(book)
// → Vergi hesapla: 100 * 0.01 = 1₺
```

### Çalıştırma:
```bash
java -cp src BehavioralPatterns.Visitor.ShoppingCart.ShoppingCartVisitorDemo
```

### Çıktı Örneği:
```
=== VERGİ HESAPLAMA ===
📚 Design Patterns - Fiyat: 100.0₺ → Vergi (%1): 1.00₺
📱 iPhone - Fiyat: 15000.0₺ → Vergi (%18): 2700.00₺
🍎 Çikolata - Fiyat: 20.0₺ → Vergi (%8): 1.60₺
```

### Öğrettiği:
- Farklı tiplere farklı işlemler
- State accumulation (toplam hesaplama)
- Pratik e-ticaret örneği

---

## 3️⃣ DocumentExport - Döküman İşleme

### Senaryo:
Döküman editörü - farklı formatlara export

### Element'ler:
- Paragraph (Paragraf)
- Image (Resim)
- Table (Tablo)

### Visitor'lar:
- HTMLExporter → HTML formatına çevir
- PDFExporter → PDF formatına çevir
- PlainTextExporter → Düz metne çevir

### Nasıl Çalışır:
```java
Paragraph p = new Paragraph("Merhaba Dünya");
HTMLExporter html = new HTMLExporter();

p.accept(html);
// → html.visitParagraph(p)
// → "<p>Merhaba Dünya</p>"
```

### Çalıştırma:
```bash
java -cp src BehavioralPatterns.Visitor.DocumentExport.DocumentExportVisitorDemo
```

### Çıktı Örneği:
```
=== HTML ===
<p>Visitor Pattern, nesneler arasındaki işlemleri ayırmanızı sağlar.</p>
<img src="visitor-diagram.png" alt="Visitor Pattern Diyagramı" />

=== PDF ===
PDF_TEXT: Visitor Pattern, nesneler arasındaki işlemleri ayırmanızı sağlar.
PDF_IMAGE: [Resim: Visitor Pattern Diyagramı - visitor-diagram.png]
```

### Öğrettiği:
- Gerçek dünya kullanımı
- Export işlemleri
- Farklı format dönüşümleri

---

## 🔑 Double Dispatch Prensibi

**Ne demek "Double Dispatch"?**

Normal metod çağrısı (Single Dispatch):
```java
dog.feed();  // Sadece Dog tipine göre karar
```

Visitor Pattern (Double Dispatch):
```java
dog.accept(visitor);
// 1. Dispatch: Dog tipi belirlendi
// 2. Dispatch: Visitor tipi belirlendi
// → visitor.visitDog(dog) çağrıldı
```

**İki seviye seçim:**
1. Hangi element? (Dog, Cat)
2. Hangi işlem? (Feeder, Vet)

**Bu sayede:**
- Element tipi runtime'da belli oluyor
- Visitor tipi de runtime'da belli oluyor
- Doğru metod çağrılıyor

---

## 📊 Ne Zaman Kullanılır?

### ✅ Kullan:

**1. Nesne yapısı sabit, işlemler değişken**
```
Element'ler: Dog, Cat (değişmiyor)
İşlemler: Feed, Examine, Train, Wash... (sık ekleniyor)
→ Visitor ideal!
```

**2. Farklı tiplere farklı işlemler**
```
Book → %1 vergi
Electronics → %18 vergi
Food → %8 vergi
→ Her tip için farklı mantık
```

**3. İşlemi element'ten ayırmak istiyorsun**
```
Besleme mantığı Dog sınıfında olmamalı
→ Feeder visitor'da olmalı
```

### ❌ Kullanma:

**1. Nesne yapısı sık değişiyor**
```
Her gün yeni hayvan ekliyorsan
→ Her yeni hayvan için TÜM visitor'ları değiştirmen gerekir
→ Visitor uygun değil
```

**2. Sadece 1-2 işlem var**
```
Basit durumlar için overkill
```

**3. Element sayısı çok fazla**
```
100 farklı element
→ Visitor interface'de 100 metod!
```

---

## 🎯 Avantajlar vs Dezavantajlar

### Avantajlar:

- ✅ **Open/Closed Principle** - Yeni işlem eklemek kolay
- ✅ **Single Responsibility** - İşlemler ayrı sınıflarda
- ✅ **Separation of Concerns** - Veri vs İşlem ayrımı
- ✅ **Accumulation** - Visitor state tutabilir (toplam hesaplama)

### Dezavantajlar:

- ❌ **Element ekleme zor** - Yeni element → Tüm visitor'ları değiştir
- ❌ **Encapsulation ihlali** - Element internal state'i paylaşmalı
- ❌ **Karmaşıklık** - Basit durumlar için fazla kod
- ❌ **Circular dependency** - Element ve Visitor birbirini bilmeli

---

## 💡 Best Practices

### 1. Element Sınıfları Basit Tutun

```java
// ✅ İyi
class Dog implements Animal {
    private String name;
    void accept(Visitor v) { v.visitDog(this); }
}

// ❌ Kötü
class Dog implements Animal {
    void accept(Visitor v) { /* ... */ }
    void feed() { /* ... */ }
    void examine() { /* ... */ }
    // Element'e iş mantığı ekleme!
}
```

### 2. Visitor Interface'i Organize Et

```java
// Çok element varsa, gruplara ayır
interface AnimalVisitor {
    void visitDog(Dog dog);
    void visitCat(Cat cat);
}

interface BirdVisitor {
    void visitParrot(Parrot parrot);
    void visitEagle(Eagle eagle);
}
```

### 3. Default Implementation Kullan

```java
// Java 8+
interface Visitor {
    default void visitDog(Dog dog) {
        // Default davranış
    }
    default void visitCat(Cat cat) {
        // Default davranış
    }
}

// Sadece ihtiyacın olanı override et
class SpecialVisitor implements Visitor {
    @Override
    void visitDog(Dog dog) {
        // Özel davranış
    }
    // visitCat default kalır
}
```

---

## 🔄 Diğer Pattern'larla İlişkisi

### **Composite Pattern ile:**
Visitor, Composite yapılar üzerinde işlem yapmak için sıklıkla kullanılır.

```java
class Folder implements FileSystemElement {
    List<FileSystemElement> children;

    void accept(Visitor v) {
        v.visitFolder(this);
        for (child : children) {
            child.accept(v);  // Recursive
        }
    }
}
```

### **Strategy Pattern ile:**
Benzer ama farklı:
- **Strategy:** Algoritma değiştirme (tek nesne)
- **Visitor:** Farklı nesnelere farklı işlemler

### **Command Pattern ile:**
İkisi de işlemleri ayrı sınıflara koyar ama:
- **Command:** İstek nesnesi (execute)
- **Visitor:** Double dispatch (visit)

---

## 🌍 Gerçek Dünya Kullanımları

1. **Compiler Design** - AST (Abstract Syntax Tree) traversal
2. **Graphics Editors** - Farklı shape'lere farklı render
3. **Export Tools** - Farklı formatlara dönüştürme
4. **Tax Calculators** - Farklı ürün tiplerine vergi
5. **File Systems** - Dosya/klasör işlemleri
6. **Game Engines** - Farklı entity'lere farklı update

---

## 🚀 Özet

**Visitor Pattern:**
- İşlemleri element'lerden ayırır
- Yeni işlem eklemek kolay (yeni Visitor)
- Yeni element eklemek zor (tüm Visitor'ları değiştir)
- Double Dispatch kullanır

**Anahtar Kural:**
> Element yapısı sabit, işlemler değişkensse → Visitor kullan!

**Kod Yazma Sırası:**
1. Element Interface → `void accept(Visitor v)`
2. Visitor Interface → Her element için `visit()` metodu
3. Concrete Elements → `accept()` içinde `visitor.visit(this)`
4. Concrete Visitors → Her `visit()` metodunda asıl iş mantığı

---

## 📚 Kaynaklar

- Gang of Four - Design Patterns Book
- [Refactoring Guru - Visitor Pattern](https://refactoring.guru/design-patterns/visitor)
- Head First Design Patterns

---

**Son Güncelleme:** 23 Aralık 2025
**Yazar:** Design Patterns Learning Project
