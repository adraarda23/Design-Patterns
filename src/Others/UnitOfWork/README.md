# Unit of Work Pattern

## Ne İşe Yarar?

**Unit of Work Pattern**, bir iş birimi (business transaction) sırasında veritabanını etkileyen tüm değişiklikleri takip eder ve bunları **tek bir transaction** içinde commit eder.

**Basit Analoji**: Bir alışveriş sepeti gibi 🛒
- Sepete ürünler eklersin ➕
- Bazılarını çıkarırsın ➖
- Miktar değiştirirsin ✏️
- Sonunda tek bir "Satın Al" butonuna basarsın 💳
- **O anda** tüm değişiklikler birden işleme alınır

## Temel Problem

### ❌ Repository Pattern Tek Başına Yeterli Değil

```java
// Her repository ayrı ayrı commit eder
userRepository.save(user);           // DB'ye yazıldı ✓
orderRepository.save(order);         // DB'ye yazıldı ✓
paymentRepository.save(payment);     // ❌ HATA! Yarıda kaldı!

// SONUÇ: Kullanıcı ve sipariş var ama ödeme yok!
// Tutarsız veri durumu! 💥
```

### ✅ Unit of Work ile Çözüm

```java
// Tüm değişiklikleri topla
unitOfWork.registerNew(user);
unitOfWork.registerNew(order);
unitOfWork.registerNew(payment);

// Tek seferde commit et
unitOfWork.commit();  // Ya hepsi başarılı ✓ ya da hepsi rollback ↩️
```

## Pattern'in Yapısı

```
┌──────────────────────────────────────────────┐
│         Business Logic (Service)             │
│  "Ürün stoğunu azalt, sipariş oluştur"       │
└────────────────┬─────────────────────────────┘
                 │
                 ▼
         ┌───────────────┐
         │ Unit of Work  │  ← Change Tracker
         │  (Registry)   │
         └───────┬───────┘
                 │
      ┌──────────┼──────────┐
      │          │          │
      ▼          ▼          ▼
  [New]      [Dirty]    [Deleted]
  Product    Product     Order
  Order      Stock

   commit() → Tek transaction'da yaz!
```

## Temel Bileşenler

### 1. Unit of Work
Değişiklikleri takip eden merkezi yapı:
- **newObjects**: Eklenecek nesneler (INSERT)
- **dirtyObjects**: Güncellenecek nesneler (UPDATE)
- **deletedObjects**: Silinecek nesneler (DELETE)

### 2. Repository
UnitOfWork ile çalışır, hemen kaydetmez:
```java
void add(Product p) {
    unitOfWork.registerNew(p);  // Kaydet DEMİYOR
                                // Sadece kaydet EDECEĞİM diyor
}
```

### 3. Service Layer
Business logic'i koordine eder:
```java
void placeOrder() {
    product.setStock(stock - qty);    // 1. Stok azalt
    repo.update(product);             // UoW'a bildir

    Order order = new Order(...);     // 2. Sipariş oluştur
    uow.registerNew(order);           // UoW'a bildir

    uow.commit();  // 3. İkisini birden kaydet!
}
```

## Repository Pattern vs Unit of Work

| **Özellik** | **Repository** | **Unit of Work** |
|-------------|----------------|------------------|
| **Amaç** | Veri erişimini soyutlar | Transaction'ları yönetir |
| **Sorumluluk** | CRUD işlemleri | Değişiklikleri takip et |
| **Odak** | Tek entity | Çoklu entity koordinasyonu |
| **Metodlar** | `save()`, `delete()`, `find()` | `commit()`, `rollback()`, `register*()` |
| **Kullanım** | Her zaman gerekli | Karmaşık transaction'larda |

## İkisi Birlikte Nasıl Çalışır?

```java
// Repository, UnitOfWork'ü kullanır
class ProductRepository {
    private UnitOfWork uow;

    void add(Product p) {
        uow.registerNew(p);      // Hemen kaydetme!
    }

    void update(Product p) {
        uow.registerDirty(p);    // Hemen güncelleme!
    }
}

// Service, her ikisini kullanır
class OrderService {
    void placeOrder() {
        productRepo.update(product);    // Tracked
        orderRepo.add(order);           // Tracked
        paymentRepo.add(payment);       // Tracked

        unitOfWork.commit();  // Şimdi hepsini kaydet!
    }
}
```

## Trade-offs (Avantajlar vs Dezavantajlar)

### ✅ AVANTAJLAR

#### 1. **Atomicity (Atomiklik)** ⚛️
**Tüm işlemler başarılı ya da hiçbiri!**

```java
// ✓ İyi: Her iki işlem de başarılı
product.setStock(5);
order.create();
uow.commit();  // İkisi de kaydedildi ✓

// ✓ İyi: Hata olunca ikisi de rollback
product.setStock(5);
order.create();
// Hata oldu!
uow.rollback();  // İkisi de geri alındı ✓
```

**Avantaj**: Veri tutarlılığı garantisi
**Önemli**: Bankacılık, e-ticaret gibi kritik uygulamalar için vazgeçilmez

#### 2. **Performance (Performans)** 🚀
**Tek transaction = Daha az veritabanı erişimi**

```java
// ❌ Kötü: Her işlem ayrı transaction
for (int i = 0; i < 1000; i++) {
    repo.save(product);  // 1000 DB call!
}

// ✓ İyi: Tek transaction
for (int i = 0; i < 1000; i++) {
    uow.registerNew(product);
}
uow.commit();  // 1 DB call!
```

**Avantaj**:
- Network round-trip azalır
- Database lock süresi kısalır
- Batch işlemler optimize edilir

**Dikkat**: Çok fazla nesne bellekte tutulur

#### 3. **Consistency (Tutarlılık)** 🎯
**Referential integrity korunur**

```java
// Order ve Payment birlikte oluşturulmalı
Order order = new Order(100);
Payment payment = new Payment(order.getId(), 100);

uow.registerNew(order);
uow.registerNew(payment);
uow.commit();

// Ya ikisi de var ya da hiçbiri!
// Ödeme olmayan sipariş ASLA olmaz!
```

**Avantaj**: Foreign key ihlalleri engellenir

#### 4. **Transaction Yönetimi** 🔄
**Merkezi transaction kontrolü**

```java
try {
    // Birden fazla işlem
    uow.registerNew(user);
    uow.registerNew(order);
    uow.registerDirty(product);

    uow.commit();
} catch (Exception e) {
    uow.rollback();  // Tek noktadan rollback
}
```

**Avantaj**: Transaction logic'i tek yerde

#### 5. **Change Tracking (Değişiklik Takibi)** 📊
**Hangi nesneler değişti otomatik takip edilir**

```java
product.setPrice(100);  // Değişiklik
product.setStock(50);   // Değişiklik

uow.registerDirty(product);  // Sadece bir kez kaydet
uow.commit();  // Tüm değişiklikler tek UPDATE ile
```

**Avantaj**: Gereksiz UPDATE'ler engellenir

---

### ❌ DEZAVANTAJLAR

#### 1. **Karmaşıklık (Complexity)** 🌀
**Ekstra katman ve öğrenme eğrisi**

```java
// Basit: Repository ile
productRepo.save(product);  // Anlaşılır ✓

// Karmaşık: UnitOfWork ile
uow.registerNew(product);
uow.commit();  // Neden iki adım? 🤔
```

**Dezavantaj**:
- Yeni geliştiriciler için kafa karıştırıcı
- Basit uygulamalar için overkill
- Daha fazla kod yazmak gerekir

**Ne zaman sorun?**
- Küçük projeler (CRUD app)
- Tek kişilik projeler
- Prototipleme aşaması

#### 2. **Memory Kullanımı** 💾
**Tüm değişiklikler bellekte tutulur**

```java
// ⚠️ DİKKAT: 10,000 nesne bellekte!
for (int i = 0; i < 10000; i++) {
    Product p = new Product(...);
    uow.registerNew(p);  // Memory'de bekliyor
}
uow.commit();  // Şimdi tümü flush edildi
```

**Dezavantaj**:
- Büyük batch işlemlerde OutOfMemoryError
- Sunucu belleği tükenir
- GC pressure artar

**Çözüm**:
```java
// Batch'lere böl
for (int i = 0; i < 100000; i++) {
    uow.registerNew(product);

    if (i % 1000 == 0) {
        uow.commit();  // Her 1000'de flush
        uow = new UnitOfWork();  // Yeni UoW
    }
}
```

#### 3. **Long-lived Transactions** ⏱️
**Transaction'lar uzun süre açık kalabilir**

```java
UnitOfWork uow = new UnitOfWork();

// Kullanıcı düşünüyor... ⏳ (30 saniye)
product.setPrice(100);
uow.registerDirty(product);

// Kullanıcı hala düşünüyor... ⏳ (30 saniye)
order.create();
uow.registerNew(order);

// Sonunda commit!
uow.commit();  // 1 dakikalık transaction! 😱
```

**Dezavantaj**:
- Database lock'ları uzun sürer
- Deadlock riski artar
- Diğer kullanıcılar bloke olur

**Çözüm**:
```java
// Transaction'ı mümkün olduğunca geç başlat
product.setPrice(100);  // Transaction dışı

uow.registerDirty(product);
uow.commit();  // Hemen commit
```

#### 4. **Detached Objects** 🔌
**UoW scope'u dışındaki nesneler sorun olur**

```java
// UoW 1
UnitOfWork uow1 = new UnitOfWork();
Product p = new Product(...);
uow1.registerNew(p);
uow1.commit();

// UoW 2 - Farklı scope
UnitOfWork uow2 = new UnitOfWork();
p.setPrice(100);  // Değişiklik
uow2.registerDirty(p);  // ⚠️ UoW2 bu nesneyi tanımıyor!
```

**Dezavantaj**: Object lifecycle yönetimi karmaşık

**Çözüm**: Her UoW için nesneleri yeniden fetch et

#### 5. **Testing Zorluğu** 🧪
**Mock'lamak daha karmaşık**

```java
// Basit: Repository mock
@Mock ProductRepository repo;

// Karmaşık: UoW + Repository mock
@Mock UnitOfWork uow;
@Mock ProductRepository repo;

// Her iki etkileşimi de verify et
verify(uow).registerNew(any());
verify(uow).commit();
```

**Dezavantaj**: Test kodu karmaşıklaşır

#### 6. **Debugging** 🐛
**Değişiklikler hemen DB'ye yansımaz**

```java
product.setStock(5);
repo.update(product);

// DB'ye bakıyorsun → Hala 10! 🤔
// Çünkü henüz commit edilmedi

uow.commit();
// Şimdi DB'de 5 ✓
```

**Dezavantaj**:
- Hata ayıklama zorlaşır
- Debugger'da "kaydetmiş gibi" görünür ama DB'de yok
- Log'lar yanıltıcı olabilir

---

## Ne Zaman Kullanmalı?

### ✅ KULLAN

#### 1. Birden Fazla Entity İlişkili
```java
// Order + Payment + Inventory güncellemesi
orderService.placeOrder() {
    updateInventory();   // Product stock
    createOrder();       // Order
    processPayment();    // Payment
    uow.commit();  // Hepsi birden!
}
```

#### 2. Data Consistency Kritik
```java
// Bankacılık: Para transferi
transferMoney(from, to, amount) {
    from.deduct(amount);
    to.add(amount);
    uow.commit();  // İkisi de ya da hiçbiri!
}
```

#### 3. Karmaşık Business Transaction
```java
// E-ticaret: Sipariş işleme
processOrder() {
    - Validate payment
    - Reserve inventory
    - Create order
    - Send notification
    - Update analytics
    uow.commit();
}
```

#### 4. ORM Kullanıyorsun
- Entity Framework → DbContext = UoW
- Hibernate → Session = UoW
- Spring Data JPA → @Transactional = UoW

### ❌ KULLANMA

#### 1. Basit CRUD İşlemleri
```java
// Tek entity, basit işlem
productRepo.save(product);  // UoW gereksiz
```

#### 2. Tek Entity Üzerinde Çalışma
```java
// Sadece product güncelleniyor
product.setPrice(100);
productRepo.update(product);  // UoW overkill
```

#### 3. Microservice Mimarisi
```java
// Her service kendi DB'si
OrderService → OrderDB
PaymentService → PaymentDB
InventoryService → InventoryDB

// Transaction DB'ler arası çalışmaz!
// Saga pattern kullan
```

#### 4. Read-Only İşlemler
```java
// Sadece okuma
List<Product> products = repo.findAll();
// UoW gereksiz
```

## Gerçek Dünya Örnekleri

| **Framework/Tool** | **UoW Kullanımı** |
|-------------------|-------------------|
| **Entity Framework (.NET)** | `DbContext` = UnitOfWork |
| **Hibernate (Java)** | `Session` = UnitOfWork |
| **Spring Data JPA** | `@Transactional` = UnitOfWork |
| **SQLAlchemy (Python)** | `Session` = UnitOfWork |
| **Doctrine (PHP)** | `EntityManager` = UnitOfWork |

## Örnek Kodlar

### Dosya Yapısı
```
src/Others/UnitOfWork/
├── Product.java              # Entity
├── Order.java                # Entity
├── UnitOfWork.java           # Ana pattern
├── ProductRepository.java    # UoW kullanan repository
├── OrderService.java         # Business logic
├── UnitOfWorkDemo.java       # Demo senaryoları
└── README.md                 # Bu dosya
```

### Demo Senaryoları

1. **Başarılı Transaction**: Ürün + Sipariş birlikte kaydedilir
2. **Rollback**: Stok yetersiz → Tüm işlem iptal
3. **Çoklu İşlemler**: Birden fazla ürün aynı transaction'da
4. **Bulk Orders**: Toplu sipariş işleme

## Özet

**Unit of Work = Değişiklikleri topla, tek seferde commit et**

### Ana Fikir
```java
// Sepete ekle
uow.registerNew(...)
uow.registerDirty(...)
uow.registerDeleted(...)

// Satın al (tek seferde!)
uow.commit()
```

### Temel Trade-off
- **Avantaj**: Atomicity, consistency, performance
- **Dezavantaj**: Karmaşıklık, memory, long transactions

### Altın Kural
> "Birden fazla entity değişiyorsa ve tutarlılık kritikse → Unit of Work kullan"
> "Tek entity basit CRUD → Repository yeterli"

## İlgili Pattern'ler

- **Repository Pattern**: Veri erişimini soyutlar (UoW ile birlikte kullanılır)
- **Transaction Script**: Business logic organizasyonu
- **Domain Model**: Entity'lerin tanımlanması
- **Lazy Loading**: Veri yükleme stratejisi (UoW ile optimize edilir)
- **Identity Map**: Aynı nesnenin birden fazla instance'ını engeller

## Kaynaklar

- Martin Fowler - "Patterns of Enterprise Application Architecture"
- Microsoft Docs - Entity Framework Core
- Hibernate Documentation
