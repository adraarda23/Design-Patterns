package Structural.Proxy;

// Gerçek nesne - Pahalı işlemleri yapan asıl sınıf
public class RealImage implements Image {
    private String fileName;
    private int width;
    private int height;

    public RealImage(String fileName) {
        this.fileName = fileName;
        this.width = 1920;
        this.height = 1080;
        loadFromDisk();
    }

    // Pahalı işlem - Diskten yükleme
    private void loadFromDisk() {
        System.out.println(">>> DISKTEN YÜKLENIYOR: " + fileName);
        System.out.println(">>> Bu işlem zaman alıyor (Büyük dosya yükleniyor)...");
        // Simüle edilmiş yükleme gecikmesi
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(">>> YÜKLEME TAMAMLANDI!\n");
    }

    @Override
    public void display() {
        System.out.println("Gösteriliyor: " + fileName + " (" + width + "x" + height + ")");
    }

    @Override
    public void rotate() {
        int temp = width;
        width = height;
        height = temp;
        System.out.println(fileName + " döndürüldü. Yeni boyut: " + width + "x" + height);
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        System.out.println(fileName + " yeniden boyutlandırıldı: " + width + "x" + height);
    }
}
