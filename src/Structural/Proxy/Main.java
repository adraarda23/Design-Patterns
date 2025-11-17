package Structural.Proxy;

public class Main {
    public static void main(String[] args) {
        System.out.println("========== PROXY DESIGN PATTERN ÖRNEĞI ==========\n");

        // Proxy kullanarak resim nesneleri oluştur
        // NOT: Bu noktada gerçek resimler HENÜZ yüklenmedi!
        Image image1 = new ProxyImage("tatil_foto.jpg");
        Image image2 = new ProxyImage("profil_resmi.png");

        System.out.println("\n--- Resimler oluşturuldu ama henüz yüklenmedi ---");
        System.out.println("--- Bu sayede hızlı başlangıç yapıldı ---\n");

        // İlk gösterim - Resim yüklenecek (lazy loading)
        System.out.println("=== İlk gösterim çağrısı ===");
        image1.display();

        // İkinci gösterim - Resim cache'den gelecek (hızlı)
        System.out.println("\n=== İkinci gösterim çağrısı ===");
        image1.display();

        // Döndürme işlemi
        System.out.println("\n=== Döndürme işlemi ===");
        image1.rotate();

        // Üçüncü gösterim - Yine cache'den
        System.out.println("\n=== Üçüncü gösterim çağrısı ===");
        image1.display();

        System.out.println("\n" + "=".repeat(50));

        // İkinci resimle işlem
        System.out.println("\n=== İkinci resim ile çalışma ===");
        image2.resize(800, 600);
        image2.display();

        System.out.println("\n========== PROXY'NİN FAYDALARI ==========");
        System.out.println("1. Lazy Loading: Resimler sadece gerektiğinde yüklendi");
        System.out.println("2. Performans: İkinci gösterimde yükleme olmadı (cache)");
        System.out.println("3. Kontrol: Proxy her erişimi kontrol etti");
        System.out.println("4. Hızlı başlangıç: Program hemen başladı, yükleme ertelendi");
    }
}
