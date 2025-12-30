# Options Pattern

## Tanım
Options Pattern, bir nesnenin yapılandırılmasını esnek, okunabilir ve tip-güvenli hale getiren bir tasarım desenidir. Genellikle Builder Pattern ile birlikte kullanılarak fluent API oluşturur.

## Ne Zaman Kullanılır?
- Bir sınıfın çok sayıda yapılandırma parametresi olduğunda
- Parametrelerin çoğu opsiyonel olduğunda
- Farklı ortamlar için farklı yapılandırmalar gerektiğinde (dev, test, production)
- Yapılandırma değerlerinin validation'a ihtiyacı olduğunda
- Immutable yapılandırma nesneleri istediğinizde

## Avantajları
✅ **Okunabilirlik**: Fluent API sayesinde kod çok okunabilir
✅ **Tip Güvenliği**: Compile-time'da hata yakalama
✅ **Immutability**: Thread-safe yapılandırma nesneleri
✅ **Varsayılan Değerler**: Akıllı varsayılan değerler sağlama
✅ **Validation**: Builder'da yapılandırma doğrulama
✅ **Esneklik**: Kolayca yeni parametreler ekleme

## Dezavantajları
❌ **Verbose**: Daha fazla kod yazımı gerektirir
❌ **Complexity**: Basit durumlar için fazla karmaşık olabilir
❌ **Memory**: Her yapılandırma için yeni nesne oluşturulur

## Implementasyon

### Temel Yapı
```java
public class Options {
    private final String requiredField;
    private final int optionalField;

    private Options(Builder builder) {
        this.requiredField = builder.requiredField;
        this.optionalField = builder.optionalField;
    }

    public static class Builder {
        private final String requiredField;  // Zorunlu
        private int optionalField = 10;      // Opsiyonel (varsayılan değer)

        public Builder(String requiredField) {
            this.requiredField = requiredField;
        }

        public Builder optionalField(int value) {
            this.optionalField = value;
            return this;
        }

        public Options build() {
            return new Options(this);
        }
    }
}
```

### Kullanım
```java
// Minimal
Options options1 = new Options.Builder("required").build();

// Full
Options options2 = new Options.Builder("required")
    .optionalField(20)
    .build();
```

## Örnekler

Bu klasörde aşağıdaki örnekler bulunur:

### 1. HttpClientOptions.java
HTTP Client yapılandırması için kapsamlı bir Options Pattern implementasyonu:
- Timeout yapılandırması
- Header yönetimi
- Proxy ayarları
- Retry mekanizması
- Logging kontrol

### 2. HttpClient.java
Options kullanarak yapılandırılan HTTP Client implementasyonu

### 3. OptionsPatternDemo.java
Farklı senaryolar için kullanım örnekleri:
- **Minimal Configuration**: Sadece zorunlu alanlar
- **Full Configuration**: Tüm özellikler
- **Production Configuration**: Production ortamı ayarları
- **Development Configuration**: Development ortamı ayarları

## Çalıştırma
```bash
javac Others/*.java
java Others.OptionsPatternDemo
```

## Gerçek Dünya Kullanımları

### ASP.NET Core
```csharp
services.Configure<MyOptions>(options => {
    options.Property1 = value1;
    options.Property2 = value2;
});
```

### OkHttp (Java)
```java
OkHttpClient client = new OkHttpClient.Builder()
    .connectTimeout(10, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .addInterceptor(loggingInterceptor)
    .build();
```

### Retrofit (Java)
```java
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("https://api.example.com")
    .addConverterFactory(GsonConverterFactory.create())
    .client(okHttpClient)
    .build();
```

### Elasticsearch Client (Java)
```java
RestClient client = RestClient.builder(
    new HttpHost("localhost", 9200, "http"))
    .setRequestConfigCallback(builder ->
        builder.setConnectTimeout(5000)
               .setSocketTimeout(60000))
    .build();
```

## Builder Pattern ile İlişkisi

Options Pattern, Builder Pattern'in özelleşmiş bir kullanımıdır:

| Aspect | Builder Pattern | Options Pattern |
|--------|----------------|-----------------|
| Amaç | Karmaşık nesne oluşturma | Yapılandırma yönetimi |
| Immutability | Opsiyonel | Genellikle zorunlu |
| Validation | Build sırasında | Build sırasında + runtime |
| Kullanım | Nesne oluşturma | Dependency injection ile |

## Best Practices

1. **Immutability**: Options nesnelerini immutable yapın
2. **Validation**: Builder'da validation yapın
3. **Defaults**: Akıllı varsayılan değerler kullanın
4. **Documentation**: Her parametreyi dokümante edin
5. **Defensive Copying**: Collection'ları kopyalayın
6. **Null Safety**: Null kontrollerini builder'da yapın
7. **Fluent API**: Method chaining kullanın

## Alternatif Yaklaşımlar

### 1. Constructor Overloading
```java
// Kötü: Çok fazla constructor
public HttpClient(String url) { }
public HttpClient(String url, Duration timeout) { }
public HttpClient(String url, Duration timeout, int retries) { }
// ... ve daha fazlası
```

### 2. Setters
```java
// Kötü: Mutable, thread-unsafe
HttpClient client = new HttpClient();
client.setUrl("...");
client.setTimeout(...);
```

### 3. Configuration Class
```java
// İyi ama validation eksik
public class Config {
    public String url;
    public Duration timeout;
}
```

## Sonuç

Options Pattern, özellikle karmaşık yapılandırma gerektiren durumlarda çok kullanışlıdır. Builder Pattern ile birlikte kullanıldığında temiz, okunabilir ve güvenli kod yazmanızı sağlar.
