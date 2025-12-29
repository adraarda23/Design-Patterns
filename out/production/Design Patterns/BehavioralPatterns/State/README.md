# State Design Pattern

## 📚 Genel Bakış

**State Pattern**, bir nesnenin **iç durumuna göre davranışını değiştirmesini** sağlar. Nesne sanki sınıfını değiştirmiş gibi görünür.

**Temel Fikir:** "Duruma göre farklı davran"

---

## 📂 Örnekler

```
State/
├── Document/                      # Döküman durumları
│   └── DocumentStateDemo.java
├── OrderProcess/                  # Sipariş süreci
│   └── OrderProcessDemo.java
├── TrafficLight/                  # Trafik ışığı
│   └── TrafficLightDemo.java
└── README.md
```

---

## 1️⃣ Document - Döküman Durumları

**Durumlar:** Draft → Review → Published

**Çalıştırma:**
```bash
java -cp src BehavioralPatterns.State.Document.DocumentStateDemo
```

---

## 2️⃣ OrderProcess - Sipariş Süreci

**Durumlar:** New → Paid → Shipped → Delivered

**Çalıştırma:**
```bash
java -cp src BehavioralPatterns.State.OrderProcess.OrderProcessDemo
```

---

## 3️⃣ TrafficLight - Trafik Işığı

**Durumlar:** Red → Yellow → Green → Yellow → Red (döngü)

**Çalıştırma:**
```bash
java -cp src BehavioralPatterns.State.TrafficLight.TrafficLightDemo
```

---

## 🔑 Avantajlar

- ✅ **Single Responsibility** - Her state ayrı sınıf
- ✅ **Open/Closed** - Yeni state eklemek kolay
- ✅ **No if-else** - Durum kontrolü yok
- ✅ **Clear transitions** - Geçişler açık

---

## 💡 State vs Strategy

| State | Strategy |
|-------|----------|
| Durum değiştirme | Algoritma değiştirme |
| Kendini değiştirir | Dışarıdan set edilir |
| İlişkili durumlar | İlişkisiz stratejiler |

---

**Son Güncelleme:** 23 Aralık 2025
