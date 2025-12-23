# Memento Design Pattern

## 📚 Genel Bakış

**Memento Pattern**, bir nesnenin iç durumunu **encapsulation'ı bozmadan** kaydetmenizi ve daha sonra o duruma geri dönmenizi sağlayan bir **behavioral (davranışsal)** tasarım desenidir.

**Gerçek Hayat Örneği:** Video oyunundaki "Save Point" veya metin editöründeki "Undo/Redo" işlevi.

---

## 🏗️ Yapı

Memento pattern **3 ana bileşenden** oluşur:

1. **Originator** - İç durumu olan ve kaydedilmesi gereken asıl nesne
2. **Memento** - Originator'ın durumunu tutan snapshot (immutable)
3. **Caretaker** - Memento'ları saklayan ve yöneten, ama içeriğini bilmeyen sınıf

---

## 📂 Klasör Yapısı

Bu implementasyonda **3 farklı yaklaşım** bulunmaktadır:

```
Memento/
├── Classic/                    # Klasik Full Snapshot yaklaşımı
│   └── MementoPatternDemo.java
├── Incremental/                # Git-like Delta yaklaşımı
│   └── IncrementalMementoDemo.java
├── Realistic/                  # Gerçekçi karşılaştırma
│   └── RealisticMementoComparison.java
└── README.md                   # Bu dosya
```

---

## 1️⃣ Classic - Klasik Memento Pattern

### Nasıl Çalışır?
Her snapshot'ta **tüm state** kaydedilir.

### Avantajlar:
- ✅ Basit implementasyon
- ✅ Hızlı restore
- ✅ Anlaşılır ve bakımı kolay

### Dezavantajlar:
- ❌ Her save'de tüm state kopyalanır
- ❌ Büyük state'lerde yüksek bellek tüketimi

### Ne Zaman Kullanılmalı?
- Küçük state boyutları (< 1KB)
- Az sayıda snapshot (< 10)
- Hız öncelikli uygulamalar

### Çalıştırma:
```bash
cd "/Users/ardaaydinkilinc/IdeaProjects/Design Patterns"
javac src/BehavioralPatterns/Memento/Classic/MementoPatternDemo.java
java -cp src BehavioralPatterns.Memento.Classic.MementoPatternDemo
```

### Örnek Çıktı:
```
💾 Kaydediliyor: "Merhaba "
📚 Geçmişe eklendi (Toplam: 1)
💾 Kaydediliyor: "Merhaba Dünya"
📚 Geçmişe eklendi (Toplam: 2)
```

---

## 2️⃣ Incremental - Git-like Delta Yaklaşımı

### Nasıl Çalışır?
İlk snapshot **full** kaydedilir, sonrasında sadece **değişiklikler (delta)** saklanır.

**Git Analojisi:**
```
Base Snapshot (initial commit)
  ↓
+ Delta 1 (commit 1)
  ↓
+ Delta 2 (commit 2)
  ↓
+ Delta 3 (commit 3)
```

### Avantajlar:
- ✅ Büyük dosyalarda bellek tasarrufu (%90'a kadar!)
- ✅ Git gibi değişiklik geçmişi
- ✅ Sadece farkları saklar

### Dezavantajlar:
- ❌ Restore daha yavaş (tüm delta'lar uygulanmalı)
- ❌ Daha karmaşık implementasyon
- ❌ Küçük dosyalarda overhead yüksek olabilir

### Ne Zaman Kullanılmalı?
- Büyük state boyutları (> 10KB)
- Çok sayıda snapshot (> 50)
- Benzer içerikler (kod dosyaları, dökümanlar)
- Bellek kritik uygulamalar (mobile, embedded)

### Çalıştırma:
```bash
cd "/Users/ardaaydinkilinc/IdeaProjects/Design Patterns"
javac src/BehavioralPatterns/Memento/Incremental/IncrementalMementoDemo.java
java -cp src BehavioralPatterns.Memento.Incremental.IncrementalMementoDemo
```

### Örnek Çıktı:
```
📝 DEĞİŞİKLİK KAYDI (git log benzeri):
Commit 1: [INSERT] pos=7, old='', new=' Dünya'
Commit 2: [INSERT] pos=13, old='', new='!'
```

---

## 3️⃣ Realistic - Gerçekçi Karşılaştırma

### Ne Gösterir?
5000 satırlık büyük bir kod dosyasında **Full Snapshot** vs **Delta** yaklaşımını karşılaştırır.

### Sonuçlar:
- **Full Snapshot:** 5401 KB (11 snapshot × 491 KB)
- **Delta:** 491 KB (1 base + 10 küçük delta)
- **Tasarruf:** %90.9 (4910 KB!)

### Çalıştırma:
```bash
cd "/Users/ardaaydinkilinc/IdeaProjects/Design Patterns"
javac src/BehavioralPatterns/Memento/Realistic/RealisticMementoComparison.java
java -cp src BehavioralPatterns.Memento.Realistic.RealisticMementoComparison
```

---

## 🎯 Hangi Yaklaşımı Kullanmalıyım?

| Senaryo | Klasik | Incremental | Hibrit |
|---------|--------|-------------|--------|
| Küçük dosyalar (< 1KB) | ✅ | ❌ | - |
| Büyük dosyalar (> 10KB) | ❌ | ✅ | ✅ |
| Az snapshot (< 10) | ✅ | ❌ | - |
| Çok snapshot (> 50) | ❌ | ✅ | ✅ |
| Hız öncelikli | ✅ | ❌ | ✅ |
| Bellek öncelikli | ❌ | ✅ | ✅ |
| Production ortamı | - | - | ✅ |

---

## 🔑 Önemli Prensipler

### 1. Encapsulation Korunmalı
```java
// ❌ Kötü
class BadMemento {
    public String content;  // Herkes erişebilir!
}

// ✅ İyi
class GoodMemento {
    private final String content;  // Sadece Originator erişebilir
    private GoodMemento(String content) { ... }
}
```

### 2. Immutability (Değiştirilemezlik)
```java
class Memento {
    private final String content;  // final keyword
    // Setter YOK, sadece getter
}
```

### 3. Caretaker İçeriği Bilmez
```java
class Caretaker {
    private Stack<Memento> history;

    // Memento'nun içini asla incelemez
    public void save(Memento m) { history.push(m); }
    public Memento undo() { return history.pop(); }
}
```

---

## 🔥 Git ile Memento Pattern Karşılaştırması

### Benzerlikler:
| Memento Pattern | Git |
|-----------------|-----|
| Originator | Working Directory |
| Memento | Commit |
| Caretaker | .git repository |
| save() | git commit |
| restore() | git checkout |
| Delta | git diff / pack files |

### Farklar:
- **Memento:** In-memory (RAM), geçici, undo/redo için
- **Git:** Disk'te, kalıcı, version control için

---

## 📊 Memory Optimizasyon Stratejileri

### 1. Snapshot Interval (Git gc benzeri)
```java
// Her 10 değişiklikte bir full snapshot
if (changeCount % 10 == 0) {
    saveFullSnapshot();
} else {
    saveDelta();
}
```

### 2. Adaptive Storage
```java
int deltaSize = calculateDelta();
int fullSize = content.length();

if (deltaSize > fullSize * 0.3) {
    saveFullSnapshot();  // Delta çok büyükse full kaydet
} else {
    saveDelta();
}
```

### 3. Memory Limit
```java
private static final int MAX_HISTORY = 50;

if (history.size() >= MAX_HISTORY) {
    history.removeFirst();  // En eski kaydı sil
}
```

---

## 💡 Gerçek Dünya Kullanım Alanları

1. **Text Editors:** Undo/Redo (VS Code, Word)
2. **Graphics Editors:** Photoshop'ta geri alma
3. **Games:** Save/Load, Checkpoint sistemi
4. **Database:** Transaction rollback
5. **Version Control:** Git, SVN
6. **Mobile Apps:** Form state'i kaydetme

---

## 🚀 Sonraki Adımlar

1. Klasik örneği çalıştırın ve anlayın
2. Incremental örneği inceleyin
3. Realistic karşılaştırmayı çalıştırın
4. Kendi use case'iniz için uyarlayın

---

## 📚 Kaynaklar

- Gang of Four - Design Patterns Book
- [Git Internals - Git Objects](https://git-scm.com/book/en/v2/Git-Internals-Git-Objects)
- Refactoring Guru - Memento Pattern

---

**Son Güncelleme:** 23 Aralık 2025
**Yazar:** Design Patterns Learning Project
