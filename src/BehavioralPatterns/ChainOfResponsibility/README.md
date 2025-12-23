# Chain of Responsibility Design Pattern

## 📚 Genel Bakış

**Chain of Responsibility**, bir isteği işlemek için bir **zincir** oluşturur. İstek zincirdeki handler'lardan geçer, uygun biri işleyene kadar devam eder.

**Temel Fikir:** "Ben işleyemezsem bir sonrakine gönder"

**Gerçek Hayat Örnekleri:**
- **Müşteri Destek:** Seviye 1 → Seviye 2 → Seviye 3 → Yönetici
- **Onay Süreci:** Takım Lideri → Müdür → Genel Müdür
- **Logging:** Debug → Info → Warning → Error

---

## 🤔 Problem

### Chain Olmadan:

```java
void handleRequest(Request req) {
    if (req.getPriority() == LOW) {
        level1.handle(req);
    } else if (req.getPriority() == MEDIUM) {
        level2.handle(req);
    } else if (req.getPriority() == HIGH) {
        level3.handle(req);
    } else {
        manager.handle(req);
    }
    // Sıkı bağlantı, değiştirmek zor
}
```

**Sorunlar:**
- Tüm if-else mantığı bir yerde
- Yeni seviye eklemek zor
- Handler'lar sabit kodlanmış
- Runtime'da değiştirilemez

### Chain İle:

```java
chain.handleRequest(req);
// İstek zincirden geçer
// Uygun handler işler
// Esnek ve dinamik
```

---

## 🏗️ Yapı

### Base Handler:
```java
abstract class Handler {
    protected Handler next;

    void setNext(Handler next) {
        this.next = next;
    }

    void handleRequest(Request req) {
        if (canHandle(req)) {
            process(req);
        } else if (next != null) {
            next.handleRequest(req);
        }
    }
}
```

### Concrete Handler:
```java
class Level1Handler extends Handler {
    void process(Request req) {
        // İşle
    }
}
```

---

## 📂 Örnekler

Bu implementasyonda **3 farklı chain tipi** bulunmaktadır:

```
ChainOfResponsibility/
├── SupportTicket/
│   └── SupportTicketDemo.java       # Klasik Chain
├── Authentication/
│   └── AuthenticationChainDemo.java # Filtering Chain
├── Logging/
│   └── LoggingChainDemo.java        # Responsibility Sharing
└── README.md
```

---

## 1️⃣ SupportTicket - Klasik Chain

### Senaryo:
Müşteri destek sistemi - farklı öncelik seviyeleri

### Chain Tipi: **Klasik (Tek handler işler)**

### Handler'lar:
- Level1Support → Basit sorunlar
- Level2Support → Teknik sorunlar
- Level3Support → Karmaşık sorunlar
- Manager → Kritik sorunlar

### Nasıl Çalışır:
```java
SupportTicket ticket = new SupportTicket("Şifremi unuttum", Priority.LOW);
chain.handleRequest(ticket);

// Level1 → İşleyebilirim! ✓
// (Zincir durur)
```

```java
SupportTicket ticket = new SupportTicket("Sistem çöktü!", Priority.CRITICAL);
chain.handleRequest(ticket);

// Level1 → İşleyemem, ilet
// Level2 → İşleyemem, ilet
// Level3 → İşleyemem, ilet
// Manager → İşledim! ✓
```

### Çalıştırma:
```bash
java -cp src BehavioralPatterns.ChainOfResponsibility.SupportTicket.SupportTicketDemo
```

### Öğrettiği:
- Chain of Responsibility'nin temel prensibi
- "İşleyemezsem ilet" mantığı
- En basit chain tipi

---

## 2️⃣ Authentication - Filtering Chain

### Senaryo:
Kimlik doğrulama - tüm kontroller geçilmeli

### Chain Tipi: **Filtering (Herkes kontrol eder)**

### Handler'lar:
- UsernamePasswordHandler → Şifre kontrolü
- TwoFactorHandler → 2FA kontrolü
- IPWhitelistHandler → IP kontrolü
- RateLimitHandler → Hız limiti

### Nasıl Çalışır:
```java
// Başarılı senaryo:
chain.handle(validRequest);
// ✓ Şifre doğru
// ✓ 2FA doğru
// ✓ IP izin listesinde
// ✓ Hız limiti OK
// → GİRİŞ BAŞARILI

// Başarısız senaryo:
chain.handle(wrongPassword);
// ✗ Şifre yanlış
// (Zincir durur, diğerleri kontrol edilmez)
// → GİRİŞ BAŞARISIZ
```

### Fark - Klasik vs Filtering:

**Klasik Chain:**
- Sadece BİR handler işler
- İşleyemeyen iletir

**Filtering Chain:**
- TÜM handler'lar kontrol eder
- Biri başarısız olursa zincir DURUR

### Çalıştırma:
```bash
java -cp src BehavioralPatterns.ChainOfResponsibility.Authentication.AuthenticationChainDemo
```

### Öğrettiği:
- Filtering chain prensibi
- Tüm handler'ların çalışması
- Başarısız olunca durmak

---

## 3️⃣ Logging - Responsibility Sharing

### Senaryo:
Loglama sistemi - birden fazla çıktı

### Chain Tipi: **Responsibility Sharing (Birden fazla handler işler)**

### Handler'lar:
- ConsoleLogger → Tüm seviyeleri konsola yaz
- FileLogger → INFO+ dosyaya yaz
- DatabaseLogger → WARNING+ DB'ye yaz
- EmailLogger → ERROR email gönder

### Log Seviye Kuralları:
```
DEBUG   → Console
INFO    → Console + File
WARNING → Console + File + Database
ERROR   → Console + File + Database + Email
```

### Nasıl Çalışır:
```java
logger.log(ERROR, "Sistem çöktü!");

// Console → Yaz ✓
// File → Yaz ✓
// Database → Yaz ✓
// Email → Gönder ✓
// (Zincir durmaz, herkes işler)
```

### Üç Chain Tipi Karşılaştırması:

| Tip | Handler Davranışı | Zincir |
|-----|------------------|--------|
| **Klasik** | Sadece biri işler | Durar |
| **Filtering** | Herkes kontrol eder | Başarısız olunca durur |
| **Sharing** | Birden fazla işler | Asla durmaz |

### Çalıştırma:
```bash
java -cp src BehavioralPatterns.ChainOfResponsibility.Logging.LoggingChainDemo
```

### Öğrettiği:
- Responsibility sharing prensibi
- Birden fazla handler'ın işlemesi
- Zincirin asla durmamas

ı

---

## 🔑 Ana Kavramlar

### 1. Handler Base Class

```java
abstract class Handler {
    protected Handler next;  // Bir sonraki handler

    public void setNext(Handler next) {
        this.next = next;
    }

    public abstract void handle(Request req);
}
```

### 2. Zincir Oluşturma

```java
Handler h1 = new Handler1();
Handler h2 = new Handler2();
Handler h3 = new Handler3();

h1.setNext(h2);
h2.setNext(h3);
// h1 → h2 → h3
```

### 3. İsteği Gönderme

```java
h1.handleRequest(request);
// İstek h1'den başlar, zincirde ilerler
```

---

## 📊 Ne Zaman Kullanılır?

### ✅ Kullan:

**1. Birden fazla nesne isteği işleyebilir**
```
Destek talebi → Level1, Level2, Level3, Manager
```

**2. İşleyici runtime'da belirlenecek**
```
Hangi seviye işleyecek önceden belli değil
```

**3. İşleyici sırası değişken**
```
Zinciri runtime'da yeniden yapılandırabilirsin
```

**4. İsteği gönderen, alıcıyı bilmemeli**
```
Caller → Chain (kimin işlediğini bilmez)
```

### ❌ Kullanma:

**1. Sadece bir işleyici var**
```
Tek handler → Chain gereksiz
```

**2. Her istek mutlaka işlenmeli**
```
Chain sonu → İstek işlenmeyebilir
```

**3. Performans kritik**
```
Her handler'dan geçmek maliyetli olabilir
```

---

## 🎯 Avantajlar vs Dezavantajlar

### Avantajlar:

- ✅ **Loose Coupling** - Gönderen alıcıyı bilmiyor
- ✅ **Flexible** - Zinciri runtime'da değiştir
- ✅ **Single Responsibility** - Her handler bir şey yapar
- ✅ **Open/Closed** - Yeni handler ekle
- ✅ **Dynamic** - İşleyici runtime'da belirlenir

### Dezavantajlar:

- ❌ **Garanti yok** - İstek işlenmeyebilir
- ❌ **Debug zor** - Hangi handler işledi?
- ❌ **Performance** - Tüm zincirden geçmeli
- ❌ **Runtime error** - Zincir yanlış kurulursa

---

## 💡 Best Practices

### 1. Her Zaman Son Handler Koy

```java
class DefaultHandler extends Handler {
    void handle(Request req) {
        System.out.println("Kimse işleyemedi!");
        // Varsayılan davranış
    }
}

// Zincir sonuna ekle
chain → h1 → h2 → h3 → defaultHandler
```

### 2. Chain Builder Kullan

```java
class ChainBuilder {
    public static Handler build() {
        Handler h1 = new Level1();
        Handler h2 = new Level2();
        Handler h3 = new Level3();

        h1.setNext(h2);
        h2.setNext(h3);

        return h1;
    }
}

// Kullanım
Handler chain = ChainBuilder.build();
```

### 3. Logging Ekle

```java
void handle(Request req) {
    System.out.println(this.getClass().getName() + " işliyor...");

    if (canHandle(req)) {
        process(req);
    } else {
        System.out.println(this.getClass().getName() + " iletiyor...");
        next.handle(req);
    }
}
```

---

## 🔄 Diğer Pattern'larla İlişkisi

### **Decorator Pattern ile:**
Benzer yapı ama farklı amaç:
- **Chain:** İsteği ilet (sadece biri işler)
- **Decorator:** İşlevsellik ekle (hepsi işler)

### **Command Pattern ile:**
Beraber kullanılabilir:
```java
class CommandHandler extends Handler {
    void handle(Command cmd) {
        cmd.execute();
        if (next != null) next.handle(cmd);
    }
}
```

### **Composite Pattern ile:**
Chain, Composite yapılar üzerinde işlem yapabilir

---

## 🌍 Gerçek Dünya Kullanımları

1. **Java Servlet Filters** - Request/Response filtering
2. **Logging Frameworks** - Log4j, SLF4J
3. **Event Bubbling** - JavaScript DOM events
4. **Middleware** - Express.js, ASP.NET
5. **Exception Handling** - Try-catch chain
6. **Authentication** - Spring Security filter chain

---

## 🚀 Özet

**Chain of Responsibility:**
- İstekleri zincir boyunca iletir
- Uygun handler işler
- Loose coupling sağlar
- Runtime'da esnek

**Üç Ana Tip:**
1. **Klasik** - Sadece biri işler
2. **Filtering** - Herkes kontrol eder, başarısız olunca durur
3. **Sharing** - Birden fazla işler, asla durmaz

**Anahtar Kural:**
> Hangi handler'ın işleyeceği önceden belli değilse → Chain kullan!

---

## 📚 Kaynaklar

- Gang of Four - Design Patterns Book
- [Refactoring Guru - Chain of Responsibility](https://refactoring.guru/design-patterns/chain-of-responsibility)
- Head First Design Patterns

---

**Son Güncelleme:** 23 Aralık 2025
**Yazar:** Design Patterns Learning Project
