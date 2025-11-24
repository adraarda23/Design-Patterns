package Structural.Bridge;

/**
 * ADIM 3: Abstraction
 *
 * Bu sinif, Bridge pattern'in "abstraction" tarafini temsil eder.
 * Bir Device referansi tutar - bu KOPRU'dur!
 *
 * RemoteControl, Device interface'i uzerinden cihazlarla iletisim kurar.
 * Hangi cihazi kontrol ettigini bilmez (TV mi, Radio mu?).
 * Bu sayede yeni cihaz turleri eklendiginde RemoteControl degismez.
 *
 * KOPRU NEREDE?
 * "protected Device device" -> Bu alan kopruyu olusturur.
 * Abstraction (RemoteControl) ve Implementation (Device) arasindaki baglanti.
 */
public class RemoteControl {

    // KOPRU: Implementation'a referans
    protected Device device;

    /**
     * Constructor ile Device enjekte edilir (Dependency Injection).
     * Bu sayede runtime'da farkli cihazlar baglanabilir.
     */
    public RemoteControl(Device device) {
        this.device = device;
    }

    public void togglePower() {
        System.out.println("Remote: guc dugmesine basildi");
        if (device.isEnabled()) {
            device.disable();
        } else {
            device.enable();
        }
    }

    public void volumeDown() {
        System.out.println("Remote: ses kisiliyor");
        device.setVolume(device.getVolume() - 10);
    }

    public void volumeUp() {
        System.out.println("Remote: ses aciliyor");
        device.setVolume(device.getVolume() + 10);
    }

    public void channelDown() {
        System.out.println("Remote: kanal degistiriliyor (geri)");
        device.setChannel(device.getChannel() - 1);
    }

    public void channelUp() {
        System.out.println("Remote: kanal degistiriliyor (ileri)");
        device.setChannel(device.getChannel() + 1);
    }
}
