# 📚 Design Patterns - Tasarım Desenleri Ansiklopedisi

Bu proje, Gang of Four (GoF) tasarım desenlerinin Java ile uygulamalarını içeren kapsamlı bir koleksiyondur. **10 farklı tasarım deseni**, gerçek dünya senaryoları ile birlikte detaylı bir şekilde gösterilmektedir.

## 📖 İçindekiler

- [Genel Bakış](#genel-bakış)
- [Proje Yapısı](#proje-yapısı)
- [Creational (Yaratımsal) Desenler](#creational-yaratımsal-desenler)
  - [1. Singleton Pattern](#1-singleton-pattern)
  - [2. Factory Method Pattern](#2-factory-method-pattern)
  - [3. Abstract Factory Pattern](#3-abstract-factory-pattern)
  - [4. Builder Pattern](#4-builder-pattern)
  - [5. Prototype Pattern](#5-prototype-pattern)
- [Structural (Yapısal) Desenler](#structural-yapısal-desenler)
  - [6. Adapter Pattern](#6-adapter-pattern)
  - [7. Composite Pattern](#7-composite-pattern)
  - [8. Facade Pattern](#8-facade-pattern)
  - [9. Proxy Pattern](#9-proxy-pattern)
  - [10. Flyweight Pattern](#10-flyweight-pattern)
- [Nasıl Çalıştırılır](#nasıl-çalıştırılır)
- [Tasarım Desenleri Özet Tablosu](#tasarım-desenleri-özet-tablosu)

---

## 🎯 Genel Bakış

Bu proje **56 Java dosyası** içerir ve **2 ana kategori** altında **10 tasarım deseni** uygular:

- **Creational Patterns (Yaratımsal)**: 5 desen - Nesne yaratma mekanizmalarıyla ilgilenir
- **Structural Patterns (Yapısal)**: 5 desen - Nesnelerin kompozisyonu ve ilişkileriyle ilgilenir

Her desen, çalıştırılabilir örnek kodlar ve gerçek hayat senaryoları ile gösterilmiştir.

---

## 📁 Proje Yapısı

```
Design Patterns/src/
├── Creational/
│   ├── AbstractFactory/        # 9 dosya - Otel rezervasyon sistemi
│   ├── Builder/                # 5 dosya - Ev inşaat sistemi
│   ├── FactoryMethod/          # 6 dosya - İnsan yaratma fabrikası
│   ├── Prototype/              # 3 dosya - Araba ve Bilgisayar klonlama
│   └── Singleton/              # 2 dosya - Tekil nesne örnekleri
│
└── Structural/
    ├── Adapter/                # 5 dosya - Banka API adaptörü
    ├── Composite/              # 5 dosya - Şirket organizasyon hiyerarşisi
    ├── Facade/                 # 7 dosya - Ev sinema sistemi
    ├── Flyweight/              # 6 dosya - Kahve dükkanı sipariş sistemi
    └── Proxy/                  # 4 dosya - Resim yükleme sistemi
```

---

## 🏗️ Creational (Yaratımsal) Desenler

Yaratımsal desenler, nesne oluşturma mekanizmalarıyla ilgilenir ve nesneleri duruma uygun şekilde oluşturmak için kullanılır.

### 1. Singleton Pattern

**📂 Konum**: [`src/Creational/Singleton/`](src/Creational/Singleton/)

**🎯 Amaç**: Bir sınıfın yalnızca bir örneğinin (instance) olmasını garanti eder ve bu örneğe global bir erişim noktası sağlar.

**📝 Dosyalar**:
- [`NaiveSingleton.java`](src/Creational/Singleton/NaiveSingleton.java) - Basit singleton uygulaması
- [`EnumSingleton.java`](src/Creational/Singleton/EnumSingleton.java) - Thread-safe enum tabanlı singleton
- [`SingletonMain.java`](src/Creational/SingletonMain.java) - Demo uygulaması

**💡 Kullanım Senaryoları**:
- Database bağlantı havuzları
- Logging servisleri
- Konfigürasyon yöneticileri
- Uygulama ayarları

**🔍 Örnek Kullanım**:
```java
// Basit singleton
NaiveSingleton ns = NaiveSingleton.getInstance();

// Enum singleton (tercih edilen)
EnumSingleton singleton = EnumSingleton.INSTANCE;
singleton.doSomething();
```

**⚙️ Çalıştırma**: `SingletonMain.java`

---

### 2. Factory Method Pattern

**📂 Konum**: [`src/Creational/FactoryMethod/`](src/Creational/FactoryMethod/)

**🎯 Amaç**: Nesnelerin tam sınıflarını belirtmeden nesne yaratma arayüzü tanımlar. Alt sınıfların hangi sınıfın örnekleneceğine karar vermesini sağlar.

**📝 Dosyalar**:
- [`IHuman.java`](src/Creational/FactoryMethod/IHuman.java) - Ürün arayüzü
- [`Male.java`](src/Creational/FactoryMethod/Male.java) - Somut ürün (Erkek)
- [`Female.java`](src/Creational/FactoryMethod/Female.java) - Somut ürün (Kadın)
- [`IHumanFactory.java`](src/Creational/FactoryMethod/IHumanFactory.java) - Fabrika arayüzü
- [`MaleFactory.java`](src/Creational/FactoryMethod/MaleFactory.java) - Erkek fabrikası
- [`FemaleFactory.java`](src/Creational/FactoryMethod/FemaleFactory.java) - Kadın fabrikası
- [`FactoryMethodMain.java`](src/Creational/FactoryMethodMain.java) - Demo uygulaması

**💡 Kullanım Senaryoları**:
- Farklı türde nesneler yaratmak
- Nesne yaratma mantığını soyutlamak
- Kod esnekliği ve genişletilebilirlik

**🔍 Örnek Kullanım**:
```java
IHumanFactory maleFactory = new MaleFactory();
IHuman male = maleFactory.createHuman();
male.saySomething(); // "Ben bir erkeğim"

IHumanFactory femaleFactory = new FemaleFactory();
IHuman female = femaleFactory.createHuman();
female.saySomething(); // "Ben bir kadınım"
```

**⚙️ Çalıştırma**: `FactoryMethodMain.java`

---

### 3. Abstract Factory Pattern

**📂 Konum**: [`src/Creational/AbstractFactory/`](src/Creational/AbstractFactory/)

**🎯 Amaç**: Birbiriyle ilişkili veya bağımlı nesne ailelerini somut sınıflarını belirtmeden yaratmak için bir arayüz sağlar.

**📝 Dosyalar**:
- [`IHotelFactory.java`](src/Creational/AbstractFactory/IHotelFactory.java) - Soyut fabrika arayüzü
- [`LuksHotelFactory.java`](src/Creational/AbstractFactory/LuksHotelFactory.java) - Lüks otel fabrikası
- [`EkonomikHotelFactory.java`](src/Creational/AbstractFactory/EkonomikHotelFactory.java) - Ekonomik otel fabrikası
- [`IPayment.java`](src/Creational/AbstractFactory/IPayment.java) - Ödeme arayüzü
- [`IReservation.java`](src/Creational/AbstractFactory/IReservation.java) - Rezervasyon arayüzü
- [`LuksPayment.java`](src/Creational/AbstractFactory/LuksPayment.java) - Lüks ödeme
- [`EkonomikPayment.java`](src/Creational/AbstractFactory/EkonomikPayment.java) - Ekonomik ödeme
- [`LuksReservation.java`](src/Creational/AbstractFactory/LuksReservation.java) - Lüks rezervasyon
- [`EkonomikReservation.java`](src/Creational/AbstractFactory/EkonomikReservation.java) - Ekonomik rezervasyon
- [`AbstractFactoryMain.java`](src/Creational/AbstractFactoryMain.java) - Demo uygulaması

**💡 Gerçek Dünya Senaryosu**: Otel rezervasyon sistemi - Lüks ve ekonomik oteller, kendi ödeme ve rezervasyon sistemlerini yaratır.

**🔍 Örnek Kullanım**:
```java
// Lüks otel sistemi
IHotelFactory luksHotel = new LuksHotelFactory();
luksHotel.createPayment().payment();         // "Lüks otel için ödeme yapıldı"
luksHotel.createReservation().reservation();  // "Lüks otel için rezervasyon yapıldı"

// Ekonomik otel sistemi
IHotelFactory ekonomikHotel = new EkonomikHotelFactory();
ekonomikHotel.createPayment().payment();         // "Ekonomik otel için ödeme yapıldı"
ekonomikHotel.createReservation().reservation(); // "Ekonomik otel için rezervasyon yapıldı"
```

**⚙️ Çalıştırma**: `AbstractFactoryMain.java`

---

### 4. Builder Pattern

**📂 Konum**: [`src/Creational/Builder/`](src/Creational/Builder/)

**🎯 Amaç**: Karmaşık bir nesnenin yapımını temsilinden ayırır. Aynı yapım sürecinin farklı temsiller oluşturmasını sağlar.

**📝 Dosyalar**:
- [`House.java`](src/Creational/Builder/House.java) - Karmaşık nesne (Ev)
- [`IHouseBuilder.java`](src/Creational/Builder/IHouseBuilder.java) - Builder arayüzü
- [`LuxuryHouseBuilder.java`](src/Creational/Builder/LuxuryHouseBuilder.java) - Lüks ev builder
- [`SimpleHouseBuilder.java`](src/Creational/Builder/SimpleHouseBuilder.java) - Basit ev builder
- [`HouseDirector.java`](src/Creational/Builder/HouseDirector.java) - Yapım sürecini yöneten director
- [`BuilderMain.java`](src/Creational/BuilderMain.java) - Demo uygulaması

**💡 Özellikler**:
- **Zorunlu**: Temel, yapı, çatı, iç dekorasyon
- **Opsiyonel**: Oda sayısı, bahçe, garaj, yüzme havuzu

**🔍 Örnek Kullanım**:
```java
// Director ile tam donanımlı lüks ev
IHouseBuilder luxuryBuilder = new LuxuryHouseBuilder();
HouseDirector director = new HouseDirector(luxuryBuilder);
House luxuryHouse = director.constructFullHouse();
luxuryHouse.displayInfo();

// Manuel olarak basit ev
IHouseBuilder simpleBuilder = new SimpleHouseBuilder();
House simpleHouse = simpleBuilder
    .buildFoundation()
    .buildStructure()
    .buildRoof()
    .buildInterior()
    .setRooms(2)
    .getResult();
```

**⚙️ Çalıştırma**: `BuilderMain.java`

---

### 5. Prototype Pattern

**📂 Konum**: [`src/Creational/Prototype/`](src/Creational/Prototype/)

**🎯 Amaç**: Mevcut bir nesneyi klonlayarak yeni nesneler yaratır. Sıfırdan yaratmak yerine prototip nesneyi kopyalar.

**📝 Dosyalar**:
- [`IPrototype.java`](src/Creational/Prototype/IPrototype.java) - Clone metodunu tanımlayan arayüz
- [`Car.java`](src/Creational/Prototype/Car.java) - Araba sınıfı (klonlanabilir)
- [`Computer.java`](src/Creational/Prototype/Computer.java) - Bilgisayar sınıfı (klonlanabilir)
- [`PrototypeMain.java`](src/Creational/PrototypeMain.java) - Demo uygulaması

**💡 Kullanım Senaryoları**:
- Nesne yaratma maliyeti yüksek olduğunda
- Benzer nesneler yaratmak gerektiğinde
- Deep copy gerektiğinde

**🔍 Örnek Kullanım**:
```java
// Orijinal araba
Car originalCar = new Car("BMW", "M5", "Siyah", 2023);

// Klon oluştur ve rengini değiştir
Car clonedCar = (Car) originalCar.clone();
clonedCar.setColor("Kırmızı");

System.out.println(originalCar);  // BMW M5 Siyah 2023
System.out.println(clonedCar);    // BMW M5 Kırmızı 2023

// Bilgisayar klonlama
Computer originalPC = new Computer("Intel i9", 32, 1024, "Windows 11");
Computer clonedPC = (Computer) originalPC.clone();
```

**⚙️ Çalıştırma**: `PrototypeMain.java`

---

## 🏛️ Structural (Yapısal) Desenler

Yapısal desenler, nesnelerin kompozisyonu ve sınıflar/nesneler arası ilişkilerle ilgilenir.

### 6. Adapter Pattern

**📂 Konum**: [`src/Structural/Adapter/example/`](src/Structural/Adapter/example/)

**🎯 Amaç**: Uyumsuz arayüzleri birlikte çalışabilir hale getirir. Eski arayüzleri yeni sistemlere adapte eder.

**📝 Dosyalar**:
- [`PaymentService.java`](src/Structural/Adapter/example/PaymentService.java) - Hedef arayüz (yeni sistem)
- [`BankAPI.java`](src/Structural/Adapter/example/BankAPI.java) - Adaptee (eski üçüncü parti sistem)
- [`BankPaymentAdapter.java`](src/Structural/Adapter/example/BankPaymentAdapter.java) - Adaptör sınıfı
- [`ShoppingCart.java`](src/Structural/Adapter/example/ShoppingCart.java) - İstemci sınıf
- [`AdapterDemo.java`](src/Structural/Adapter/example/AdapterDemo.java) - Demo uygulaması

**💡 Gerçek Dünya Senaryosu**: Eski BankAPI'yi modern PaymentService arayüzüne entegre etme.

**🔍 Nasıl Çalışır**:
```
Client (ShoppingCart)
    ↓ calls pay()
PaymentService Interface
    ↓ implements
BankPaymentAdapter
    ↓ converts to transferMoney()
BankAPI (Legacy System)
```

**🔍 Örnek Kullanım**:
```java
ShoppingCart cart = new ShoppingCart(250.50);

// Eski banka API'si
BankAPI ziraatBankAPI = new BankAPI("Ziraat");

// Adaptör ile yeni sisteme entegrasyon
PaymentService paymentService = new BankPaymentAdapter(ziraatBankAPI, "TR123456789");

// İstemci yeni arayüzü kullanır
cart.checkout(paymentService);  // Arka planda BankAPI.transferMoney() çağrılır
```

**⚙️ Çalıştırma**: `AdapterDemo.java`

---

### 7. Composite Pattern

**📂 Konum**: [`src/Structural/Composite/`](src/Structural/Composite/)

**🎯 Amaç**: Nesneleri ağaç yapılarına yerleştirir ve parça-bütün hiyerarşilerini temsil eder. Tek nesneler ve nesne gruplarını aynı şekilde ele alır.

**📝 Dosyalar**:
- [`Employee.java`](src/Structural/Composite/Employee.java) - Component arayüzü
- [`Developer.java`](src/Structural/Composite/Developer.java) - Leaf (bireysel geliştirici)
- [`Designer.java`](src/Structural/Composite/Designer.java) - Leaf (bireysel tasarımcı)
- [`Manager.java`](src/Structural/Composite/Manager.java) - Composite (diğer çalışanları içerebilir)
- [`CompositeMain.java`](src/Structural/Composite/CompositeMain.java) - Demo uygulaması

**💡 Gerçek Dünya Senaryosu**: Şirket organizasyon yapısı - geliştiriciler, tasarımcılar ve yöneticiler.

**🔍 Organizasyon Yapısı**:
```
CTO (Manager)
├── Tech Lead (Manager)
│   ├── Ali Yılmaz (Developer - Java)
│   ├── Mehmet Demir (Developer - Python)
│   └── Ayşe Kaya (Developer - JavaScript)
└── Design Lead (Manager)
    ├── Zeynep Ak (Designer - UI)
    └── Can Öztürk (Designer - UX)
```

**🔍 Örnek Kullanım**:
```java
// Yönetici ve takım oluşturma
Manager cto = new Manager("Burak Yıldız", 12000, "Technology");
Manager techLead = new Manager("Ahmet Şahin", 8000, "Development");
Developer dev1 = new Developer("Ali Yılmaz", 5000, "Java");

// Hiyerarşi kurma
techLead.addEmployee(dev1);
cto.addEmployee(techLead);

// Tüm hiyerarşiyi göster
cto.showDetails("");  // Recursive olarak tüm yapıyı gösterir

// Toplam maliyet hesaplama (recursive)
System.out.println("Toplam Maliyet: $" + cto.getSalary());
```

**⚙️ Çalıştırma**: `CompositeMain.java`

---

### 8. Facade Pattern

**📂 Konum**: [`src/Structural/Facade/`](src/Structural/Facade/)

**🎯 Amaç**: Bir alt sistemdeki bir dizi arayüze birleşik bir arayüz sağlar. Karmaşık sistemleri basitleştirir.

**📝 Dosyalar**:
- [`HomeTheaterFacade.java`](src/Structural/Facade/HomeTheaterFacade.java) - Facade sınıfı
- [`Light.java`](src/Structural/Facade/Light.java) - Işık sistemi
- [`SoundSystem.java`](src/Structural/Facade/SoundSystem.java) - Ses sistemi
- [`Projector.java`](src/Structural/Facade/Projector.java) - Projektör
- [`DVDPlayer.java`](src/Structural/Facade/DVDPlayer.java) - DVD oynatıcı
- [`Amplifier.java`](src/Structural/Facade/Amplifier.java) - Amplifikatör
- [`Main.java`](src/Structural/Facade/Main.java) - Demo uygulaması

**💡 Gerçek Dünya Senaryosu**: Ev sinema sistemi - ışık, ses, projektör, DVD gibi birden fazla bileşeni kontrol etme.

**🔍 Facade Olmadan (Karmaşık)**:
```java
light.dim();
soundSystem.on();
soundSystem.setVolume(5);
projector.on();
projector.wideScreenMode();
dvdPlayer.on();
dvdPlayer.play("Inception");
// ... 7 adım!
```

**🔍 Facade İle (Basit)**:
```java
homeTheater.watchMovie("Inception");  // Tek satır!
homeTheater.endMovie();               // Film bittiğinde tek satır!
```

**⚙️ Çalıştırma**: `Main.java` (Facade paketi içinde)

---

### 9. Proxy Pattern

**📂 Konum**: [`src/Structural/Proxy/`](src/Structural/Proxy/)

**🎯 Amaç**: Başka bir nesneye erişimi kontrol etmek için bir vekil (proxy) sağlar. Lazy loading, caching ve erişim kontrolü için kullanılır.

**📝 Dosyalar**:
- [`Image.java`](src/Structural/Proxy/Image.java) - Subject arayüzü
- [`RealImage.java`](src/Structural/Proxy/RealImage.java) - Gerçek nesne (pahalı işlemler)
- [`ProxyImage.java`](src/Structural/Proxy/ProxyImage.java) - Proxy nesnesi
- [`Main.java`](src/Structural/Proxy/Main.java) - Demo uygulaması

**💡 Özellikler**:
- **Lazy Loading**: Resimler sadece gerektiğinde yüklenir
- **Caching**: Yüklenen resim önbellekte tutulur
- **Erişim Kontrolü**: Proxy tüm erişimi kontrol eder

**🔍 Nasıl Çalışır**:
```
1. ProxyImage oluştur (hafif, gerçek yükleme YOK)
2. İlk display() çağrısı → RealImage yükle
3. Sonraki çağrılar → Cache'den kullan (hızlı!)
```

**🔍 Örnek Kullanım**:
```java
Image image1 = new ProxyImage("tatil_foto.jpg");
System.out.println("--- Resim oluşturuldu ama henüz yüklenmedi ---");

image1.display();  // İlk çağrı: disk'ten yüklenir (YAVAŞ)
// Çıktı: [PROXY] İlk kez gösterilecek, gerçek resim yükleniyor...
//        [REAL IMAGE] Yükleniyor: tatil_foto.jpg

image1.display();  // İkinci çağrı: cache'den kullanılır (HIZLI)
// Çıktı: [PROXY] Resim zaten yüklü, cache'den kullanılıyor...
//        [REAL IMAGE] Gösteriliyor: tatil_foto.jpg
```

**⚙️ Çalıştırma**: `Main.java` (Proxy paketi içinde)

---

### 10. Flyweight Pattern

**📂 Konum**: [`src/Structural/Flyweight/`](src/Structural/Flyweight/)

**🎯 Amaç**: Çok sayıda benzer nesneyi paylaşımlı kullanarak bellek kullanımını azaltır. Nesne havuzu (object pool) oluşturur.

**📝 Dosyalar**:
- [`ICoffee.java`](src/Structural/Flyweight/ICoffee.java) - Flyweight arayüzü
- [`Espresso.java`](src/Structural/Flyweight/Espresso.java) - Somut flyweight
- [`Latte.java`](src/Structural/Flyweight/Latte.java) - Somut flyweight
- [`Cappuccino.java`](src/Structural/Flyweight/Cappuccino.java) - Somut flyweight
- [`CoffeeFactory.java`](src/Structural/Flyweight/CoffeeFactory.java) - Flyweight factory (cache yönetimi)
- [`FlyweightMain.java`](src/Structural/Flyweight/FlyweightMain.java) - Demo (kahve dükkanı)

**💡 Gerçek Dünya Senaryosu**: Kahve dükkanı - 100 sipariş var ama sadece 3 kahve türü objesi yaratılır.

**🔍 Anahtar Kavramlar**:
- **Intrinsic State (İçsel Durum)**: Paylaşılan - kahve tipi özellikleri
- **Extrinsic State (Dışsal Durum)**: Parametre olarak geçilen - masa numarası, sipariş ID

**🔍 Bellek Verimliliği**:
```
10 sipariş alındı
✅ Sadece 3 benzersiz kahve türü
✅ Sadece 3 nesne yaratıldı (Espresso, Latte, Cappuccino)
✅ Nesneler her sipariş için tekrar kullanıldı
```

**🔍 Örnek Kullanım**:
```java
// Sipariş 1: Espresso
ICoffee order1 = CoffeeFactory.getCoffee("Espresso");  // YENİ nesne yaratır
order1.serveCoffee(1, 101);  // Masa 1, Sipariş 101

// Sipariş 2: Latte
ICoffee order2 = CoffeeFactory.getCoffee("Latte");     // YENİ nesne yaratır
order2.serveCoffee(2, 102);  // Masa 2, Sipariş 102

// Sipariş 3: Espresso (AYNI TÜR!)
ICoffee order3 = CoffeeFactory.getCoffee("Espresso");  // Mevcut nesneyi TEKRAR KULLANIR
order3.serveCoffee(3, 103);  // Farklı masa ve sipariş numarası

System.out.println("Toplam kahve türü yaratıldı: 3");  // 10 sipariş için sadece 3 nesne!
System.out.println("Toplam sipariş: 10");
```

**⚙️ Çalıştırma**: `FlyweightMain.java`

---

## 🚀 Nasıl Çalıştırılır

### Tüm Örnekleri Derleme
```bash
# Projeyi derle
javac -d out $(find src -name "*.java")

# Veya klasör klasör derle
javac -d out src/Creational/**/*.java
javac -d out src/Structural/**/*.java
```

### Bireysel Desenleri Çalıştırma

**Creational Patterns:**
```bash
# Singleton
java -cp out Creational.SingletonMain

# Factory Method
java -cp out Creational.FactoryMethodMain

# Abstract Factory
java -cp out Creational.AbstractFactoryMain

# Builder
java -cp out Creational.BuilderMain

# Prototype
java -cp out Creational.PrototypeMain
```

**Structural Patterns:**
```bash
# Adapter
java -cp out Structural.Adapter.example.AdapterDemo

# Composite
java -cp out Structural.Composite.CompositeMain

# Facade
java -cp out Structural.Facade.Main

# Proxy
java -cp out Structural.Proxy.Main

# Flyweight
java -cp out Structural.Flyweight.FlyweightMain
```

---

## 📊 Tasarım Desenleri Özet Tablosu

| # | Desen | Kategori | Amaç | Dosya Sayısı | Demo Dosyası |
|---|-------|----------|------|--------------|--------------|
| 1 | **Singleton** | Creational | Tek bir global instance | 2 | [`SingletonMain.java`](src/Creational/SingletonMain.java) |
| 2 | **Factory Method** | Creational | Sınıf belirtmeden nesne yaratma | 6 | [`FactoryMethodMain.java`](src/Creational/FactoryMethodMain.java) |
| 3 | **Abstract Factory** | Creational | İlişkili nesne aileleri yaratma | 9 | [`AbstractFactoryMain.java`](src/Creational/AbstractFactoryMain.java) |
| 4 | **Builder** | Creational | Karmaşık nesneleri adım adım inşa etme | 5 | [`BuilderMain.java`](src/Creational/BuilderMain.java) |
| 5 | **Prototype** | Creational | Mevcut nesneleri klonlama | 3 | [`PrototypeMain.java`](src/Creational/PrototypeMain.java) |
| 6 | **Adapter** | Structural | Uyumsuz arayüzleri uyumlu hale getirme | 5 | [`AdapterDemo.java`](src/Structural/Adapter/example/AdapterDemo.java) |
| 7 | **Composite** | Structural | Ağaç yapıları, parça-bütün hiyerarşisi | 5 | [`CompositeMain.java`](src/Structural/Composite/CompositeMain.java) |
| 8 | **Facade** | Structural | Karmaşık sistemleri basitleştirme | 7 | [`Main.java`](src/Structural/Facade/Main.java) |
| 9 | **Proxy** | Structural | Erişim kontrolü, lazy loading, caching | 4 | [`Main.java`](src/Structural/Proxy/Main.java) |
| 10 | **Flyweight** | Structural | Bellek verimliliği için nesne paylaşımı | 6 | [`FlyweightMain.java`](src/Structural/Flyweight/FlyweightMain.java) |

**Toplam**: **56 Java dosyası** | **10 tasarım deseni** | **2 kategori**

---

## 🎓 Tasarım Desenleri Kategorileri

### 🏗️ Creational (Yaratımsal) - 5 Desen
Nesne yaratma mekanizmalarını kontrol eder ve optimize eder.
- Singleton, Factory Method, Abstract Factory, Builder, Prototype

### 🏛️ Structural (Yapısal) - 5 Desen
Sınıfların ve nesnelerin nasıl birleştirileceğini tanımlar.
- Adapter, Composite, Facade, Proxy, Flyweight

### 🎭 Behavioral (Davranışsal) - 0 Desen
*(Bu projede henüz uygulanmamış)*

---

## 📖 Öğrenme Sırası Önerisi

**Başlangıç Seviyesi:**
1. [Singleton](#1-singleton-pattern) - En basit desen
2. [Facade](#8-facade-pattern) - Basit ve pratik
3. [Prototype](#5-prototype-pattern) - Klonlama kavramı

**Orta Seviye:**
4. [Factory Method](#2-factory-method-pattern) - Nesne yaratma soyutlaması
5. [Adapter](#6-adapter-pattern) - Arayüz uyumlama
6. [Composite](#7-composite-pattern) - Ağaç yapıları

**İleri Seviye:**
7. [Abstract Factory](#3-abstract-factory-pattern) - Nesne aileleri
8. [Builder](#4-builder-pattern) - Karmaşık nesne inşası
9. [Proxy](#9-proxy-pattern) - Erişim kontrolü
10. [Flyweight](#10-flyweight-pattern) - Bellek optimizasyonu

---

## 🔑 Anahtar Kavramlar

- **Interface**: Sözleşme tanımlar, implementasyon içermez
- **Abstract Class**: Kısmi implementasyon içerebilir
- **Composition**: "Has-a" ilişkisi (bir nesne başka nesneleri içerir)
- **Inheritance**: "Is-a" ilişkisi (bir sınıf başka sınıftan türer)
- **Polymorphism**: Aynı arayüz, farklı davranışlar
- **Encapsulation**: Veri gizleme ve soyutlama
- **Dependency Injection**: Bağımlılıkları dışarıdan verme
- **SOLID Principles**: Nesne yönelimli tasarım prensipleri

---

## 💻 Teknik Detaylar

- **Dil**: Java
- **Toplam Dosya**: 56 Java dosyası
- **Paket Yapısı**: `Creational.*` ve `Structural.*`
- **Derleme**: `javac` ile standart derleme
- **Çalıştırma**: Her desenin kendi Main dosyası var

---

## 📚 Kaynaklar

- **Gang of Four (GoF)**: "Design Patterns: Elements of Reusable Object-Oriented Software"
- **Refactoring Guru**: https://refactoring.guru/design-patterns
- **Head First Design Patterns**: O'Reilly yayınları

---

## 👨‍💻 Geliştirici Notları

Bu proje, öğrenme ve referans amaçlıdır. Her desen:
- ✅ Çalışan kod örnekleri içerir
- ✅ Türkçe yorumlar ile açıklanmıştır
- ✅ Gerçek dünya senaryoları kullanır
- ✅ Gang of Four (GoF) standartlarına uygundur
- ✅ SOLID prensiplerine sadık kalır

---

## 🤝 Katkıda Bulunma

Yeni desenler eklemek veya mevcut desenleri iyileştirmek için:
1. Fork yapın
2. Yeni bir branch oluşturun
3. Değişikliklerinizi commit edin
4. Pull request gönderin

---

## 📄 Lisans

Bu proje eğitim amaçlıdır ve özgürce kullanılabilir.

---

**Son Güncelleme**: 2025-11-17
**Proje Durumu**: Aktif Geliştirme 🚀
