package BehavioralPatterns.Command;

/**
 * RECEIVER (Alıcı)
 *
 * Klimanın açılması/kapatılması ve sıcaklık ayarlarını yöneten sınıf.
 */
public class AirConditioner {
    private String location;
    private boolean isOn = false;
    private int temperature = 24;

    public AirConditioner(String location) {
        this.location = location;
    }

    public void on() {
        isOn = true;
        System.out.println(location + " kliması AÇILDI ❄️ (Sıcaklık: " + temperature + "°C)");
    }

    public void off() {
        isOn = false;
        System.out.println(location + " kliması KAPANDI");
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
        if (isOn) {
            System.out.println(location + " klima sıcaklığı " + temperature + "°C olarak ayarlandı 🌡️");
        }
    }

    public int getTemperature() {
        return temperature;
    }
}
