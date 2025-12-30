# Ambient Context Pattern

## Ne İşe Yarar?

**Ambient Context Pattern**, context bilgisini (kullanıcı, transaction, güvenlik, loglama vb.) uygulama boyunca **implicit** (gizli/otomatik) olarak taşımanın bir yoludur. Her metoda parametre olarak geçirmek yerine, context'e her yerden **statik** olarak erişilebilir.

**Basit Analoji**: **Hava gibi** 🌬️
- Havayı her yere götürmek zorunda değilsin
- Zaten her yerde var
- İstediğin zaman nefes alırsın

Context de öyle - set et bir kere, her yerden kullan!

## Temel Problem

### ❌ Context'i Her Yere Parametre Olarak Geçmek

```java
public class OrderService {
    // Her metoda 4-5 parametre!
    public void placeOrder(Order order, User user, Transaction tx, Logger log, SecurityContext sec) {
        validateOrder(order, user, tx, log, sec);
        saveOrder(order, user, tx, log, sec);
        sendEmail(order, user, tx, log, sec);
    }

    private void validateOrder(Order order, User user, Transaction tx, Logger log, SecurityContext sec) {
        // ...
    }

    private void saveOrder(Order order, User user, Transaction tx, Logger log, SecurityContext sec) {
        // ...
    }
}
```

**Problemler:**
- Parametre kirliliği (parameter pollution)
- Her metod signature çok uzun
- Yeni context eklemek için tüm metodları değiştir
- Okunması zor, bakımı zor

### ✅ Ambient Context ile Çözüm

```java
public class OrderService {
    // Sadece iş mantığı parametreleri!
    public void placeOrder(Order order) {
        // Context'e ihtiyacın olduğunda al
        User user = UserContext.getCurrent();
        Transaction tx = TransactionContext.getCurrent();

        validateOrder(order);
        saveOrder(order);
        sendEmail(order);
    }

    private void validateOrder(Order order) {
        // Buradan da erişebilirsin
        User user = UserContext.getCurrent();
    }
}
```

**Avantajlar:**
- Temiz method signature
- Context her yerden erişilebilir
- Yeni context eklemek kolay
- Okunması kolay

## Pattern'in Yapısı

```
┌─────────────────────────────────────────┐
│     Application Entry Point             │
│  (HTTP Request, Main, etc.)             │
│                                         │
│  UserContext.setCurrent(user)           │
│  LogContext.setCorrelationId(id)        │
└────────────────┬────────────────────────┘
                 │
                 ▼
         ┌───────────────┐
         │  ThreadLocal  │  ← Per-thread storage
         │   Storage     │
         └───────┬───────┘
                 │
                 │ (Ambient - available everywhere)
                 │
      ┌──────────┼──────────┐
      │          │          │
      ▼          ▼          ▼
  Service 1  Service 2  Repository
      │          │          │
      └──────────┴──────────┘
               │
               ▼
    Context.getCurrent()  ← Access from anywhere
```

## ThreadLocal - Pattern'in Kalbi

Ambient Context genellikle **ThreadLocal** kullanır:

```java
public class UserContext {
    // Her thread'in kendi User'ı var
    private static ThreadLocal<User> current = new ThreadLocal<>();

    public static void setCurrent(User user) {
        current.set(user);  // Bu thread için set et
    }

    public static User getCurrent() {
        return current.get();  // Bu thread'den al
    }

    public static void clear() {
        current.remove();  // Bu thread'i temizle
    }
}
```

**ThreadLocal Nasıl Çalışır:**
- Her thread kendi değerini tutar
- Thread 1'in User'ı Thread 2'yi etkilemez
- Web server'da her request farklı thread → izole context

## Ne Zaman Kullanılır?

### ✅ KULLAN: Cross-Cutting Concerns

#### 1. **User/Security Context** 👤
```java
// Login sonrası set et
UserContext.setCurrent(user);

// Her yerden erişilebilir
void saveOrder() {
    User user = UserContext.getCurrent();
    log("Order saved by: " + user.getUsername());
}
```

**Kullanım Alanları:**
- Web uygulamaları (kim login?)
- Authorization (yetki kontrolü)
- Audit logging (kim ne yaptı?)

#### 2. **Transaction Management** 🔄
```java
try (TransactionScope scope = TransactionContext.begin()) {
    // Transaction her yerden erişilebilir
    repository.save(entity);
    scope.complete();  // Auto-commit
}
```

**Kullanım Alanları:**
- Database transaction'ları
- Unit of Work pattern
- Distributed transactions

#### 3. **Logging Context** 📝
```java
// Request başlangıcında
LogContext.setCorrelationId("REQ-12345");

// Tüm loglar correlation ID içerir
LogContext.info("Processing order");
// Output: [2024-01-15 10:30:45] [REQ-12345] [john] INFO: Processing order
```

**Kullanım Alanları:**
- Request tracking
- Distributed tracing
- MDC (Mapped Diagnostic Context)

#### 4. **Localization** 🌍
```java
LocaleContext.setCurrent(Locale.FRENCH);

// Her yerden locale'e eriş
String message = MessageSource.get("welcome");  // "Bienvenue"
```

**Kullanım Alanları:**
- i18n (internationalization)
- Multi-language apps
- Regional settings

#### 5. **HTTP Request Context** 🌐
```java
// ASP.NET Core
HttpContext current = HttpContext.Current;

// Spring
RequestContextHolder.getRequestAttributes();
```

**Kullanım Alanları:**
- Web frameworks
- Request-scoped data
- Session management

### ❌ KULLANMA

#### 1. **Business Logic** 💼
```java
// ❌ Kötü: Business data ambient context'te
ProductContext.setCurrent(product);

// ✓ İyi: Business data parametre olarak
void processProduct(Product product) { }
```

#### 2. **Dependencies** 🔌
```java
// ❌ Kötü: Service'i ambient context'ten al
UserService service = ServiceContext.getCurrent();

// ✓ İyi: Dependency Injection kullan
class OrderService {
    private final UserService userService;

    OrderService(UserService userService) {
        this.userService = userService;
    }
}
```

#### 3. **Configuration** ⚙️
```java
// ❌ Kötü: Config ambient context'te
String dbUrl = ConfigContext.get("database.url");

// ✓ İyi: Configuration injection
@Value("${database.url}")
private String dbUrl;
```

#### 4. **Test Data** 🧪
```java
// ❌ Kötü: Test data ambient context'te
TestContext.setTestUser(mockUser);

// ✓ İyi: Test fixture ile
@BeforeEach
void setup() {
    mockUser = new User(...);
}
```

## Trade-offs (Avantajlar vs Dezavantajlar)

### ✅ AVANTAJLAR

#### 1. **Temiz Kod - Clean Method Signatures** 🧹

**Öncesi:**
```java
void processOrder(Order order, User user, Logger logger,
                  Transaction tx, SecurityContext security,
                  AuditTrail audit, Configuration config) {
    validateOrder(order, user, logger, tx, security, audit, config);
    saveOrder(order, user, logger, tx, security, audit, config);
}
```

**Sonrası:**
```java
void processOrder(Order order) {
    validateOrder(order);
    saveOrder(order);
}
```

**Avantaj**: Kod daha okunabilir ve bakımı kolay

#### 2. **DRY Principle** 📋

```java
// Context logic tek yerde
public class UserContext {
    public static User getCurrentRequired() {
        User user = getCurrent();
        if (user == null) {
            throw new SecurityException("Not authenticated");
        }
        return user;
    }
}

// Her yerden kullan
void method1() { User u = UserContext.getCurrentRequired(); }
void method2() { User u = UserContext.getCurrentRequired(); }
```

**Avantaj**: Tekrar eden kod yok

#### 3. **Cross-Cutting Concerns** ✂️

```java
// Logging, security, transaction tek noktadan
LogContext.info("Action");  // Auto user + tx + correlation
```

**Avantaj**: Aspect'ler kolay eklenir

#### 4. **Convenience** 🎯

```java
// Derinlemesine call chain
layer1() → layer2() → layer3() → layer4()

// Her katmanda context'e eriş (geçmeye gerek yok)
void layer4() {
    User user = UserContext.getCurrent();
}
```

**Avantaj**: Derin call stack'te kullanışlı

#### 5. **Framework Integration** 🔧

```java
// ASP.NET Core
var user = HttpContext.Current.User;

// Spring Security
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
```

**Avantaj**: Modern framework'ler zaten kullanıyor

---

### ❌ DEZAVANTAJLAR

#### 1. **Hidden Dependencies** 🙈

**En büyük problem!**

```java
// Metod signature'a bakınca ne gerektiği belli değil
public void placeOrder(Order order) {
    // Gizli dependency: UserContext
    User user = UserContext.getCurrent();  // ⚠️ Görünmüyor!

    // Gizli dependency: TransactionContext
    Transaction tx = TransactionContext.getCurrent();  // ⚠️ Görünmüyor!
}
```

**Problem:**
- Dependency görünmez (testability ⬇️)
- Yeni geliştirici kafası karışır
- Mock'lamak zor
- Refactoring zorlaşır

**Çözüm:**
```java
// Alternatif: Dependency görünür yap (ama parametre değil)
public class OrderService {
    // Constructor'da dependency belirt (dökümantasyon için)
    // Requires: UserContext, TransactionContext

    public void placeOrder(Order order) {
        // ...
    }
}
```

#### 2. **Threading Issues** 🧵

**ThreadLocal dikkat gerektirir!**

```java
// ❌ Problem: Thread pool'da context leak
ExecutorService executor = Executors.newFixedThreadPool(10);

UserContext.setCurrent(user);
executor.submit(() -> {
    // ⚠️ User yok! Farklı thread!
    User u = UserContext.getCurrent();  // null veya eski user!
});
```

**Problem:**
- ThreadLocal thread'e bağlı
- Thread pool'da context kaybolur
- Async/await'te dikkat gerekir

**Çözüm:**
```java
// Context'i yeni thread'e taşı
User currentUser = UserContext.getCurrent();
executor.submit(() -> {
    UserContext.setCurrent(currentUser);  // Yeni thread'e set et
    try {
        // İş yap
    } finally {
        UserContext.clear();  // Temizle
    }
});
```

#### 3. **Global State (Anti-Pattern'e Benzer)** 🌍

```java
// Singleton gibi - global erişim
UserContext.setCurrent(user);  // Anywhere
User u = UserContext.getCurrent();  // Anywhere
```

**Problem:**
- Global state genelde kötüdür
- Side effect'ler oluşturabilir
- Test izolasyonu zorlaşır

**Çözüm:**
- Sadece cross-cutting concern'lerde kullan
- Business logic için kullanma

#### 4. **Tight Coupling** 🔗

```java
public class OrderService {
    public void placeOrder(Order order) {
        // OrderService artık UserContext'e sıkı sıkıya bağlı
        User user = UserContext.getCurrent();
    }
}
```

**Problem:**
- Class'lar context'lere bağımlı
- Context olmadan çalışmaz
- Yeniden kullanılabilirlik ⬇️

**Çözüm:**
```java
// Graceful fallback
User user = UserContext.hasCurrentUser()
    ? UserContext.getCurrent()
    : getDefaultUser();
```

#### 5. **Lifecycle Management - Memory Leak!** 💾

**En tehlikeli problem!**

```java
// ❌ Unutulan cleanup
UserContext.setCurrent(user);
// İş yap
// Context clear edilmedi! → MEMORY LEAK!

// Thread pool'da bu thread tekrar kullanılırsa:
// Bir sonraki request eski user'ı görür! 🐛
```

**Problem:**
- `clear()` çağrılmazsa memory leak
- Thread pool'da context leak
- Security riski (başka user'ın session'ı)

**Çözüm:**
```java
// ✓ Always use try-finally
UserContext.setCurrent(user);
try {
    // İş yap
} finally {
    UserContext.clear();  // ✓ Mutlaka temizle
}

// ✓ Veya wrapper method
UserContext.executeAs(user, () -> {
    // İş yap
    // Auto-cleanup
});
```

#### 6. **Testing Challenges** 🧪

```java
// Test edilmesi zor
@Test
void testPlaceOrder() {
    // Setup context (her test için)
    UserContext.setCurrent(testUser);
    TransactionContext.begin();

    try {
        orderService.placeOrder(order);
    } finally {
        // Cleanup (her test için)
        UserContext.clear();
        TransactionContext.clear();
    }
}
```

**Problem:**
- Test setup karmaşık
- Context mock'lamak zor
- Test isolation gerekir

**Çözüm:**
```java
// Test base class
class ContextTest {
    @BeforeEach
    void setupContext() {
        UserContext.setCurrent(defaultUser);
    }

    @AfterEach
    void clearContext() {
        UserContext.clear();
    }
}
```

#### 7. **Debugging Zorluğu** 🐛

```java
void placeOrder(Order order) {
    // User nereden geliyor? 🤔
    User user = UserContext.getCurrent();

    // Transaction kim başlattı? 🤔
    Transaction tx = TransactionContext.getCurrent();
}
```

**Problem:**
- Dataflow görünmez
- Stack trace yardımcı olmaz
- Debugging zor

**Çözüm:**
- Logging ekle
- Context set/get noktalarını logla

---

## Özet: Ambient Context vs Dependency Injection

| **Özellik** | **Ambient Context** | **Dependency Injection** |
|-------------|---------------------|--------------------------|
| **Erişim** | Static (`Context.getCurrent()`) | Constructor/method parameter |
| **Visibility** | Hidden (görünmez) | Explicit (açık) |
| **Scope** | Thread-local | Instance-based |
| **Kullanım** | Cross-cutting concerns | Business dependencies |
| **Testability** | Orta (context mock gerekir) | Yüksek (kolay mock) |
| **Coupling** | Tight (sıkı bağımlılık) | Loose (gevşek) |
| **Lifecycle** | Manual (clear gerekir) | Automatic (DI container) |

**Altın Kural:**
> **Cross-cutting concerns** → Ambient Context
> **Business dependencies** → Dependency Injection

## Gerçek Dünya Örnekleri

| **Framework/Library** | **Ambient Context Kullanımı** |
|-----------------------|-------------------------------|
| **ASP.NET Core** | `HttpContext.Current` |
| **Spring Security** | `SecurityContextHolder.getContext()` |
| **Log4j/SLF4J** | `MDC` (Mapped Diagnostic Context) |
| **Entity Framework** | `DbContext.Database.CurrentTransaction` |
| **Java EE** | `TransactionSynchronizationRegistry` |
| **.NET** | `Thread.CurrentPrincipal` |
| **Hibernate** | `Session.getCurrentSession()` |

## Örnek Kodlar

### Dosya Yapısı
```
src/Others/AmbientContext/
├── User.java                    # Entity
├── Transaction.java             # Transaction entity
├── TransactionScope.java        # Auto-commit/rollback
├── UserContext.java             # User ambient context
├── TransactionContext.java      # Transaction ambient context
├── LogContext.java              # Logging ambient context
├── DatabaseConnection.java      # DB simulation
├── OrderService.java            # Service using contexts
├── AmbientContextDemo.java      # Demo scenarios
└── README.md                    # This file
```

### Demo Senaryoları

1. **Basic User Context**: Set user, access from anywhere
2. **Transaction Auto-Commit**: TransactionScope with try-with-resources
3. **Transaction Rollback**: Auto-rollback without complete()
4. **Logging with Correlation ID**: Request tracking
5. **Security Check**: Permission denied/granted
6. **Multi-threading**: ThreadLocal isolation

## Best Practices

### ✅ DO

1. **Always Clear Context**
```java
try {
    UserContext.setCurrent(user);
    // Work
} finally {
    UserContext.clear();  // ✓ Always
}
```

2. **Use for Cross-Cutting Only**
```java
// ✓ Good
UserContext, TransactionContext, LogContext

// ❌ Bad
ProductContext, OrderContext
```

3. **Provide Helper Methods**
```java
public static void executeAs(User user, Runnable action) {
    try {
        setCurrent(user);
        action.run();
    } finally {
        clear();
    }
}
```

4. **Document Dependencies**
```java
/**
 * Place an order
 *
 * Requires:
 * - UserContext.getCurrent() must be set
 * - TransactionContext must be active
 */
public void placeOrder(Order order) { }
```

### ❌ DON'T

1. **Don't Forget to Clear**
```java
// ❌ Memory leak!
UserContext.setCurrent(user);
// No clear()
```

2. **Don't Use for Business Data**
```java
// ❌ Bad
ProductContext.setCurrent(product);

// ✓ Good
void processProduct(Product product) { }
```

3. **Don't Assume Context Exists**
```java
// ❌ Bad
User user = UserContext.getCurrent();  // Might be null!

// ✓ Good
User user = UserContext.getCurrentRequired();  // Throws if null
```

## İlgili Pattern'ler

- **Dependency Injection**: Alternative for dependencies
- **Service Locator**: Similar but more explicit
- **Thread-Local Storage**: Implementation mechanism
- **MDC (Mapped Diagnostic Context)**: Logging-specific ambient context

## Sonuç

**Ambient Context = Hava gibi her yerde mevcut context**

### Ne Zaman Kullan?
- ✅ User/security context
- ✅ Transaction management
- ✅ Logging/correlation
- ✅ Localization
- ❌ Business logic
- ❌ Dependencies
- ❌ Configuration

### Temel Trade-off
**Convenience vs Explicitness**
- 👍 Kod temiz, parametre yok
- 👎 Dependency gizli, test zor

**Kullan ama dikkatli kullan!** ⚠️
