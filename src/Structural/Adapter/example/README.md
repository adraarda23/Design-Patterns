# 🔌 Adapter Pattern - Temiz Ödeme Örneği

## 🎯 Senaryo

Yeni bir e-ticaret uygulaması yapıyorsun. Modern ödeme sistemi kullanıyorsun:

```java
PaymentService.pay(amount)
```

Ama banka **eski bir API** sağlamış:

```java
BankAPI.transferMoney(amount, accountNumber)
```

**Problem:**
- Metod isimleri farklı: `pay()` vs `transferMoney()`
- Parametreler farklı: `transferMoney()` ekstra `accountNumber` bekliyor
- BankAPI'yi değiştiremezsin (3. parti sistem)

**Çözüm:** Adapter Pattern! 🔌

---

## 📁 Dosyalar

```
1. PaymentService.java      (Target Interface - Senin sistemin)
2. BankAPI.java              (Adaptee - Bankanın eski API'si)
3. BankPaymentAdapter.java   (ADAPTER ⭐ - Köprü)
4. ShoppingCart.java         (Client - Kullanan sınıf)
5. AdapterDemo.java          (Demo - Test)
```

---

## 🔧 Kod Akışı

```
1. ShoppingCart ödeme yapmak istiyor
   ↓
2. PaymentService interface bekliyor
   cart.checkout(paymentService)
   ↓
3. paymentService.pay(250.50) çağrılır
   ↓
4. BankPaymentAdapter devreye girer (DÖNÜŞÜM!)
   pay(250.50) → transferMoney(250.50, "TR123456789")
   ↓
5. BankAPI.transferMoney() çalışır
   ↓
6. ✅ Ödeme tamamlandı!
```

---

## 🎓 Adapter Nasıl Yazıldı?

### ADIM 1: Target Interface'i Implement Et
```java
public class BankPaymentAdapter implements PaymentService {
    // PaymentService = Client'ın bildiği interface
}
```

### ADIM 2: Adaptee'yi Tut (Composition)
```java
private BankAPI bankAPI;
private String accountNumber;  // Eksik parametreyi adapter ekleyecek
```

### ADIM 3: Constructor'da Al
```java
public BankPaymentAdapter(BankAPI bankAPI, String accountNumber) {
    this.bankAPI = bankAPI;
    this.accountNumber = accountNumber;
}
```

### ADIM 4: Dönüşümü Yap
```java
@Override
public void pay(double amount) {
    // pay() → transferMoney() çevirisi
    bankAPI.transferMoney(amount, accountNumber);
}
```

---

## ✅ Neden Bu Tasarım Temiz?

| Özellik | Açıklama |
|---------|----------|
| ✅ **SOLID** | Interface Segregation ihlali yok |
| ✅ **Gerçekçi** | 3. parti API senaryosu |
| ✅ **Basit** | Gereksiz komplekslik yok |
| ✅ **Single Responsibility** | Her sınıf tek iş yapıyor |
| ✅ **Composition** | Inheritance değil, composition kullanıldı |

---

## 🚀 Çalıştırma

Rider'da:
```
AdapterDemo.java → Sağ Tık → Run
```

**Çıktı:**
```
=== ADAPTER PATTERN ÖRNEĞİ ===

Sepet Toplamı: 250.5 TL
Ödeme işleniyor...
[Ziraat Bank API] 250.5 TL transfer edildi (Hesap: TR123456789)
Ödeme tamamlandı! ✅
```

---

## 💡 Anahtar Noktalar

1. **BankAPI değişmedi** - Olduğu gibi kullanıldı ✅
2. **ShoppingCart habersiz** - Sadece PaymentService biliyor ✅
3. **Adapter köprü** - İki uyumsuz sistemi bağladı ✅
4. **Eksik parametre** - Adapter `accountNumber`'ı ekledi ✅
5. **Temiz kod** - Boş metod yok, ISP ihlali yok ✅

---

## 🎯 Özet

**Adapter Pattern:**
```
Client bilir:     pay(amount)
Banka sağlar:     transferMoney(amount, account)
Adapter çevirir:  pay() → transferMoney()
```

**3 Satırda:**
1. Target interface'i implement et
2. Adaptee'yi içinde tut
3. Metod çağrısını dönüştür

**Hepsi bu! 🎉**
