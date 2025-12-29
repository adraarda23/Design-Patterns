# Mediator Design Pattern

## 📚 Genel Bakış

**Mediator Pattern**, nesneler arasındaki karmaşık iletişimi basitleştirmek için araya bir **aracı (mediator)** koyan behavioral tasarım desenidir.

**Basit Kural:** Nesneler birbirleriyle direkt konuşmaz, araya bir hakem koyar.

---

## 🤔 Problem

### Mediator Olmadan:

```
A ←→ B
↕  ×  ↕
C ←→ D

4 nesne = 6 bağlantı
10 nesne = 45 bağlantı
n nesne = n(n-1)/2 bağlantı
```

**Sorunlar:**
- Her nesne diğerlerini bilmeli (tight coupling)
- Bir değişiklik hepsini etkiler
- Test etmek zor
- Yeni nesne eklemek karmaşık

### Mediator İle:

```
A ──┐
B ──┤
C ──┼── Mediator
D ──┘

4 nesne = 4 bağlantı
10 nesne = 10 bağlantı
n nesne = n bağlantı
```

**Faydalar:**
- Herkes sadece mediator'ı bilir (loose coupling)
- Koordinasyon tek yerde
- Kolay test
- Yeni nesne eklemek basit

---

## 🏗️ Yapı

### 1. Mediator (Interface)
İletişim kurallarını tanımlar

### 2. Concrete Mediator
Gerçek koordinasyon mantığı, tüm component'leri bilir

### 3. Component (Colleague)
Sadece mediator'ı bilir, diğerlerini bilmez

---

## 📂 Örnekler

Bu implementasyonda **3 farklı örnek** bulunmaktadır:

```
Mediator/
├── Classic/
│   └── ChatRoomDemo.java          # Chat odası
├── UI/
│   └── FormMediatorDemo.java      # UI Form koordinasyonu
├── AirTraffic/
│   └── AirTrafficControlDemo.java # Havalimanı kontrol kulesi
└── README.md
```

---

## 1️⃣ Classic - Chat Room

### Senaryo:
Chat odası, kullanıcılar arasında mesaj iletişimini koordine eder.

### Nasıl Çalışır:
```java
// Kullanıcı mesaj gönderir
alice.send("Merhaba");

// ChatRoom (mediator) mesajı dağıtır
chatRoom.sendMessage(message, alice);

// Diğer kullanıcılar alır
bob.receive("Merhaba", "Alice");
charlie.receive("Merhaba", "Alice");
```

### Anahtar Nokta:
Alice, Bob ve Charlie'yi **bilmiyor**! Sadece ChatRoom'u biliyor.

### Çalıştırma:
```bash
cd "/Users/ardaaydinkilinc/IdeaProjects/Design Patterns"
java -cp src BehavioralPatterns.Mediator.Classic.ChatRoomDemo
```

### Çıktı Örneği:
```
✅ Alice 'Tech Talk' odasına katıldı
✅ Bob 'Tech Talk' odasına katıldı

📤 [Tech Talk] Alice: Merhaba!
  📥 Bob aldı: Merhaba!
  📥 Charlie aldı: Merhaba!
```

---

## 2️⃣ UI - Form Mediator

### Senaryo:
E-ticaret kayıt formu - checkbox, textbox, button koordinasyonu

### Kurallar:
1. Premium seçilirse → Fiyat 99₺, kredi kartı aktif
2. Premium seçilmezse → Fiyat 0₺, kredi kartı pasif
3. Email ve (premium ise kart) dolu → Buton aktif

### Nasıl Çalışır:
```java
// Checkbox değişti
premiumCheckBox.toggle();
  ↓
mediator.notify(checkbox, "check");
  ↓
// Mediator diğerlerini günceller
priceLabel.setText("99₺");
creditCardTextBox.setEnabled(true);
submitButton.setEnabled(false);
```

### Mediator Olmadan:
```java
class CheckBox {
    private TextBox cardBox;      // ❌ Tight coupling!
    private Label priceLabel;     // ❌ CheckBox her şeyi biliyor
    private Button submitBtn;     // ❌ Değişiklik zor

    public void toggle() {
        cardBox.enable();         // ❌ Spaghetti code
        priceLabel.setPrice(99);
        submitBtn.validate();
    }
}
```

### Mediator İle:
```java
class CheckBox {
    private Mediator mediator;    // ✅ Sadece mediator

    public void toggle() {
        mediator.notify(this, "check");  // ✅ Temiz!
    }
}
```

### Çalıştırma:
```bash
java -cp src BehavioralPatterns.Mediator.UI.FormMediatorDemo
```

---

## 3️⃣ AirTraffic - Hava Trafik Kontrol

### Senaryo:
Havalimanı kontrol kulesi, uçaklar arasında koordinasyonu sağlar.

### Kurallar:
1. Uçaklar birbirleriyle **direkt konuşamaz**
2. Her şey kule üzerinden olur
3. Pist durumu kontrol edilir
4. İniş/kalkış izinleri yönetilir

### Nasıl Çalışır:
```java
// Uçak iniş izni istiyor
turkishAir.requestLanding();
  ↓
tower.requestLanding(turkishAir);
  ↓
// Kule pisti kontrol eder
if (runwayAvailable) {
    // İzin ver
    turkishAir.land();
    // Diğerlerini uyar
    lufthansa.receiveMessage("Pist dolu");
}
```

### Gerçek Hayat Karşılığı:
Bu, gerçek havalimanlarının **tam** çalışma prensibidir!
- Pilotlar birbirleriyle konuşmaz
- Herkes kule ile konuşur
- Kule koordine eder

### Çalıştırma:
```bash
java -cp src BehavioralPatterns.Mediator.AirTraffic.AirTrafficControlDemo
```

### Çıktı Örneği:
```
✈️  TK123: İniş izni istiyorum
🗼 IST Kulesi: TK123 iniş izni verildi
   🛬 TK123 pistte
   📻 LH456 alıyor: Pist dolu, beklemede kalın
```

---

## 📊 Ne Zaman Kullanılır?

### ✅ Kullan:
- Çok sayıda nesne birbirine bağımlı
- Bağımlılıkları azaltmak istiyorsun
- n² bağlantı → n bağlantıya indirmek istiyorsun
- Koordinasyon mantığı karmaşık

### ❌ Kullanma:
- Sadece 2-3 nesne var
- İletişim çok basit
- Mediator "God Object" olacak

---

## 🔑 Avantajlar vs Dezavantajlar

### Avantajlar:
- ✅ **Loose Coupling** - Nesneler birbirini bilmiyor
- ✅ **Single Responsibility** - Koordinasyon tek yerde
- ✅ **Open/Closed** - Yeni component eklemek kolay
- ✅ **Reusability** - Component'ler bağımsız

### Dezavantajlar:
- ❌ Mediator karmaşık olabilir
- ❌ "God Object" riski
- ❌ Mediator'a yüksek bağımlılık

---

## 🎯 Diğer Pattern'larla İlişkisi

### **Observer Pattern ile:**
- Observer: 1-to-many broadcast
- Mediator: many-to-many coordination

**Birlikte kullanılabilir!** Mediator içinde Observer kullanabilirsin.

### **Facade Pattern ile:**
Benzer ama farklı:
- **Facade:** Basitleştirme (tek yönlü)
- **Mediator:** Koordinasyon (çift yönlü)

### **Command Pattern ile:**
Mediator içinde Command kullanılabilir (undo/redo için)

---

## 💡 Best Practices

### 1. Mediator'ı Şişirme
```java
// ❌ Kötü - God Object
class Mediator {
    void handleEverything() {
        // 1000+ satır kod
    }
}

// ✅ İyi - Delegate et
class Mediator {
    private ValidationService validator;
    private NotificationService notifier;

    void handle() {
        validator.validate();
        notifier.notify();
    }
}
```

### 2. Interface Segregation
```java
// ❌ Fat Interface
interface Mediator {
    void method1();
    void method2();
    // ... 20+ method
}

// ✅ Segregated
interface MessageMediator { void sendMessage(); }
interface FileMediator { void sendFile(); }
```

### 3. Event-Based Communication
```java
// Modern yaklaşım
mediator.notify(new ButtonClickedEvent(this));

// Klasik yaklaşım
mediator.notify(this, "clicked");
```

---

## 🌍 Gerçek Dünya Kullanımları

1. **Chat Applications** - Slack, Discord, WhatsApp
2. **UI Frameworks** - Form validations, MVC controllers
3. **Air Traffic Control** - Gerçek havalimanları
4. **Game Development** - Player/NPC koordinasyonu
5. **Smart Home** - IoT cihaz koordinasyonu
6. **Workflow Engines** - İş akışı yönetimi

---

## 🚀 Özet

**Mediator Pattern:**
- Nesneler arası karmaşık iletişimi basitleştirir
- n² → n bağlantıya indirir
- Loose coupling sağlar
- Koordinasyonu merkezileştirir

**Anahtar Kural:**
> "Birbirinizle konuşmayın, benimle konuşun!" - Mediator

---

## 📚 Kaynaklar

- Gang of Four - Design Patterns Book
- [Refactoring Guru - Mediator Pattern](https://refactoring.guru/design-patterns/mediator)
- Head First Design Patterns

---

**Son Güncelleme:** 23 Aralık 2025
**Yazar:** Design Patterns Learning Project
