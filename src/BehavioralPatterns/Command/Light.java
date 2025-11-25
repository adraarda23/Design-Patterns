package BehavioralPatterns.Command;

/**
 * RECEIVER (Alıcı)
 *
 * Gerçek işi yapan sınıf. Işığın açılması/kapatılması işlemlerini bilir.
 * Command pattern'de komutlar bu sınıfın metotlarını çağırır.
 */
public class Light {
    private String location;
    private boolean isOn = false;

    public Light(String location) {
        this.location = location;
    }

    public void on() {
        isOn = true;
        System.out.println(location + " ışığı AÇILDI 💡");
    }

    public void off() {
        isOn = false;
        System.out.println(location + " ışığı KAPANDI 🌑");
    }

    public boolean isOn() {
        return isOn;
    }
}
