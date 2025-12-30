# Repository Pattern

## Ne İşe Yarar?

Repository Pattern, veri erişim katmanını (data access layer) uygulamanın iş mantığından (business logic) ayırmak için kullanılan bir tasarım desenidir. Veri kaynağı (veritabanı, API, dosya sistemi vb.) ile uygulama arasında bir soyutlama katmanı oluşturur.

## Temel Amaçlar

1. **Separation of Concerns (Sorumlulukların Ayrılması)**
   - İş mantığı kodunu veri erişim kodundan ayırır
   - Her katmanın kendi sorumluluğu olur

2. **Bağımlılığı Azaltma (Loose Coupling)**
   - Uygulama, veri kaynağının detaylarını bilmez
   - Veritabanı değiştiğinde sadece repository implementasyonu değişir

3. **Test Edilebilirlik**
   - Mock repository kullanarak birim testler yazılabilir
   - Gerçek veritabanına ihtiyaç duymadan test edilebilir

4. **Merkezi Veri Erişimi**
   - Tüm veri işlemleri tek bir yerden yönetilir
   - Kod tekrarı önlenir (DRY - Don't Repeat Yourself)

5. **Değiştirilebilirlik**
   - Veri kaynağı kolayca değiştirilebilir (SQL -> NoSQL, In-Memory -> Database)
   - Implementasyon değişse bile interface aynı kalır

## Repository Pattern'in Yapısı

```
┌─────────────────────────────────────────────────────────┐
│                    Business Logic                       │
│              (Service Layer / Use Cases)                │
└────────────────────┬────────────────────────────────────┘
                     │
                     │ uses
                     ▼
         ┌───────────────────────┐
         │  Repository Interface │ ◄─── Abstraction
         │  (IUserRepository)    │
         └───────────┬───────────┘
                     │
                     │ implements
         ┌───────────┴───────────┐
         │                       │
         ▼                       ▼
┌────────────────────┐  ┌──────────────────────┐
│ InMemoryRepository │  │ DatabaseRepository   │
│   (for testing)    │  │  (for production)    │
└────────────────────┘  └──────────────────────┘
         │                       │
         │                       │
         ▼                       ▼
┌────────────────────┐  ┌──────────────────────┐
│    Memory/Cache    │  │     Database         │
└────────────────────┘  └──────────────────────┘
```

## Ne Zaman Kullanılır?

✅ **Kullan:**
- Veritabanı işlemleri yapan uygulamalarda
- Birden fazla veri kaynağı kullanılabilecek durumlarda
- Test edilebilir kod yazmak istediğinde
- Veri erişim mantığını merkezileştirmek istediğinde
- Veri kaynağının detaylarını gizlemek istediğinde

❌ **Kullanma:**
- Çok basit CRUD işlemleri için (over-engineering olabilir)
- Veri erişimi tek bir yerde ve değişmeyecekse
- Ekstra soyutlama katmanına gerek yoksa

## Örnek Senaryolar

### Senaryo 1: E-ticaret Uygulaması
```
UserService -> IUserRepository -> DatabaseUserRepository -> MySQL
                                -> CacheUserRepository -> Redis
```
- Production'da MySQL kullanılır
- Önbellekleme için Redis kullanılır
- Test sırasında InMemoryRepository kullanılır

### Senaryo 2: Çoklu Veri Kaynağı
```
ProductService -> IProductRepository -> PrimaryDBRepository (master)
                                     -> ReplicaDBRepository (read-only)
                                     -> SearchRepository (Elasticsearch)
```
- Yazma işlemleri primary DB'ye gider
- Okuma işlemleri replica'dan yapılır
- Arama işlemleri Elasticsearch'ten yapılır

## Temel Repository Metodları

Genellikle bir Repository şu metodları içerir:

```java
- findById(id)        // ID ile tek kayıt getir
- findAll()           // Tüm kayıtları getir
- save(entity)        // Yeni kayıt ekle veya güncelle
- delete(id)          // Kayıt sil
- existsById(id)      // Kayıt var mı kontrol et
- count()             // Toplam kayıt sayısı
```

## Avantajları

1. **Esneklik**: Veri kaynağı değiştirilebilir
2. **Test Edilebilirlik**: Mock repository ile test edilebilir
3. **Bakım Kolaylığı**: Veri erişim kodu tek yerde
4. **Yeniden Kullanılabilirlik**: Repository farklı servislerde kullanılabilir
5. **Dependency Injection**: Interface sayesinde DI uygulanabilir

## Dezavantajları

1. **Ekstra Katman**: Basit uygulamalarda gereksiz karmaşıklık
2. **Öğrenme Eğrisi**: Yeni geliştiriciler için pattern anlaşılmalı
3. **Kod Miktarı**: Daha fazla dosya ve kod yazılması gerekir

## Bu Örnekte Neler Var?

1. **User.java** - Domain model (Entity)
2. **IUserRepository.java** - Repository interface
3. **InMemoryUserRepository.java** - Bellekte çalışan implementasyon (test için)
4. **DatabaseUserRepository.java** - Veritabanı simülasyonu (production için)
5. **UserService.java** - Repository kullanan business logic
6. **RepositoryPatternDemo.java** - Tüm kullanımı gösteren demo

## Kod Yazma Aşamaları

### Aşama 1: Domain Model Oluştur
Önce veri modelini (entity) oluştur. Örnek: User, Product, Order vb.

### Aşama 2: Repository Interface Tanımla
Hangi metodların olması gerektiğini belirle (findById, save, delete vb.)

### Aşama 3: Concrete Implementation Yaz
Interface'i implement eden sınıfları yaz (InMemory, Database vb.)

### Aşama 4: Service Layer'da Kullan
Business logic'te repository interface'ini kullan (dependency injection ile)

### Aşama 5: Test Yaz
Mock repository veya InMemory repository ile test yaz

## İlgili Pattern'ler

- **DAO Pattern**: Repository'nin daha veritabanı odaklı hali
- **Unit of Work**: Birden fazla repository'yi transaction içinde yönetir
- **Specification Pattern**: Karmaşık sorguları kapsüllemek için kullanılır
- **Factory Pattern**: Repository instance'ları oluşturmak için kullanılabilir

## Gerçek Dünya Örnekleri

- **Spring Data JPA**: Repository pattern'i built-in olarak kullanır
- **Entity Framework**: .NET'te repository pattern implementasyonu
- **Hibernate**: Java'da ORM framework'ü, repository pattern ile kullanılır
