package Structural.Facade;

// FACADE - Karmaşık alt sistemleri basit bir arayüzle sunar
public class HomeTheaterFacade {
    private Light light;
    private SoundSystem soundSystem;
    private Projector projector;
    private DVDPlayer dvdPlayer;

    public HomeTheaterFacade(Light light, SoundSystem soundSystem,
                            Projector projector, DVDPlayer dvdPlayer) {
        this.light = light;
        this.soundSystem = soundSystem;
        this.projector = projector;
        this.dvdPlayer = dvdPlayer;
    }

    // Tek bir metod ile tüm sistemi film izlemeye hazırlar
    public void watchMovie(String movie) {
        System.out.println("\n========== Film izlemeye hazırlanıyor... ==========");
        light.dim();
        soundSystem.on();
        soundSystem.setVolume(5);
        projector.on();
        projector.wideScreenMode();
        dvdPlayer.on();
        dvdPlayer.play(movie);
        System.out.println("========== Keyifli seyirler! ==========\n");
    }

    // Tek bir metod ile tüm sistemi kapatır
    public void endMovie() {
        System.out.println("\n========== Film bitti, sistem kapatılıyor... ==========");
        dvdPlayer.stop();
        dvdPlayer.off();
        projector.off();
        soundSystem.off();
        light.on();
        System.out.println("========== Sistem kapatıldı ==========\n");
    }
}
