package Structural.Bridge;

/**
 * ADIM 2b: Concrete Implementation - Radio
 *
 * Device interface'inin baska bir somut implementasyonu.
 * Radio'nun kendine ozgu davranislarini icerir.
 *
 * TV ile ayni interface'i paylasir ama farkli davranislar gosterebilir.
 * Ornegin: Radio'nun maksimum ses seviyesi farkli olabilir.
 */
public class Radio implements Device {

    private boolean on = false;
    private int volume = 20;
    private int channel = 1;

    @Override
    public boolean isEnabled() {
        return on;
    }

    @Override
    public void enable() {
        on = true;
    }

    @Override
    public void disable() {
        on = false;
    }

    @Override
    public int getVolume() {
        return volume;
    }

    @Override
    public void setVolume(int volume) {
        // Radio'nun maksimum sesi 50
        if (volume > 50) {
            this.volume = 50;
        } else if (volume < 0) {
            this.volume = 0;
        } else {
            this.volume = volume;
        }
    }

    @Override
    public int getChannel() {
        return channel;
    }

    @Override
    public void setChannel(int channel) {
        this.channel = channel;
    }

    @Override
    public void printStatus() {
        System.out.println("------------------------------------");
        System.out.println("| Radio");
        System.out.println("| Durum: " + (on ? "Acik" : "Kapali"));
        System.out.println("| Ses seviyesi: " + volume + "%");
        System.out.println("| Kanal: " + channel);
        System.out.println("------------------------------------\n");
    }
}
