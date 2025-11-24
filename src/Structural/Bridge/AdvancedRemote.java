package Structural.Bridge;

/**
 * ADIM 4b: Refined Abstraction - AdvancedRemote
 *
 * RemoteControl'u genisleten gelismis bir kumanda.
 * Temel islevlere ek olarak "mute" (sessiz) ozelligi ekler.
 *
 * BRIDGE PATTERN'IN GUCU BURADA:
 * - AdvancedRemote, TV veya Radio hakkinda hicbir sey bilmiyor
 * - Sadece Device interface'ini biliyor
 * - Yarin yeni bir cihaz (SmartSpeaker) eklense bile bu sinif degismez!
 */
public class AdvancedRemote extends RemoteControl {

    public AdvancedRemote(Device device) {
        super(device);
    }

    /**
     * Gelismis ozellik: Sesi tamamen kapatir.
     * Bu ozellik sadece AdvancedRemote'da var.
     */
    public void mute() {
        System.out.println("Remote: sessiz moda aliniyor");
        device.setVolume(0);
    }

    /**
     * Cihazin durumunu ekrana yazdirir.
     */
    public void printDeviceStatus() {
        device.printStatus();
    }
}
