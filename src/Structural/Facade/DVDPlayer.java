package Structural.Facade;

// Alt sistem sınıfı - DVD Player
public class DVDPlayer {
    public void on() {
        System.out.println("DVD Player açıldı");
    }

    public void play(String movie) {
        System.out.println("'" + movie + "' filmi oynatılıyor");
    }

    public void stop() {
        System.out.println("DVD Player durduruldu");
    }

    public void off() {
        System.out.println("DVD Player kapatıldı");
    }
}
