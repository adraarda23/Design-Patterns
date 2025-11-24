package Structural.Bridge;

/**
 * ADIM 2a: Concrete Implementation - TV
 *
 * Device interface'inin somut bir implementasyonu.
 * TV'nin kendine özgü davranışlarını içerir.
 *
 * Bu sınıf RemoteControl hakkında hiçbir şey bilmez.
 * Sadece kendi işlevselliğine odaklanır.
 */
public class Tv implements Device {

    private boolean on = false;
    private int volume = 30;
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
        if (volume > 100) {
            this.volume = 100;
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
        System.out.println("| TV");
        System.out.println("| Durum: " + (on ? "Acik" : "Kapali"));
        System.out.println("| Ses seviyesi: " + volume + "%");
        System.out.println("| Kanal: " + channel);
        System.out.println("------------------------------------\n");
    }
}
