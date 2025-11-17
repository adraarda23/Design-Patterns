package Structural.Proxy;

// PROXY - Gerçek nesneye erişimi kontrol eder
public class ProxyImage implements Image {
    private RealImage realImage;
    private String fileName;

    public ProxyImage(String fileName) {
        this.fileName = fileName;
        System.out.println("ProxyImage oluşturuldu: " + fileName + " (Henüz yüklenmedi)");
    }

    @Override
    public void display() {
        // Lazy Loading - Gerçek nesne sadece ihtiyaç duyulduğunda oluşturulur
        if (realImage == null) {
            System.out.println("\n[PROXY] İlk kez gösterilecek, gerçek resim yükleniyor...");
            realImage = new RealImage(fileName);
        } else {
            System.out.println("\n[PROXY] Resim zaten yüklü, cache'den kullanılıyor...");
        }
        realImage.display();
    }

    @Override
    public void rotate() {
        // Gerçek nesne yoksa önce oluştur
        if (realImage == null) {
            System.out.println("\n[PROXY] Döndürme işlemi için resim yükleniyor...");
            realImage = new RealImage(fileName);
        }
        realImage.rotate();
    }

    @Override
    public void resize(int width, int height) {
        // Gerçek nesne yoksa önce oluştur
        if (realImage == null) {
            System.out.println("\n[PROXY] Boyutlandırma işlemi için resim yükleniyor...");
            realImage = new RealImage(fileName);
        }
        realImage.resize(width, height);
    }
}
