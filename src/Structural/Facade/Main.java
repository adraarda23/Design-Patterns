package Structural.Facade;

public class Main {
    public static void main(String[] args) {
        // Alt sistem bileşenlerini oluştur
        Light light = new Light();
        SoundSystem soundSystem = new SoundSystem();
        Projector projector = new Projector();
        DVDPlayer dvdPlayer = new DVDPlayer();

        // Facade'i oluştur
        HomeTheaterFacade homeTheater = new HomeTheaterFacade(
            light, soundSystem, projector, dvdPlayer
        );

        // FACADE OLMADAN: Her bir alt sistemi ayrı ayrı kontrol etmemiz gerekirdi
        // light.dim();
        // soundSystem.on();
        // soundSystem.setVolume(5);
        // projector.on();
        // projector.wideScreenMode();
        // dvdPlayer.on();
        // dvdPlayer.play("Inception");

        // FACADE İLE: Tek bir basit metod çağrısı yeterli!
        homeTheater.watchMovie("Inception");

        // Biraz bekle...
        System.out.println("... Film izleniyor ...\n");

        // Film bittiğinde tek bir metod ile kapat
        homeTheater.endMovie();
    }
}
