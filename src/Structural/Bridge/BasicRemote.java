package Structural.Bridge;

/**
 * ADIM 4a: Refined Abstraction - BasicRemote
 *
 * RemoteControl'u genisleten basit bir kumanda.
 * Temel islevlere ek olarak sadece printStatus ekler.
 *
 * Bu sinif da Device hakkinda detay bilmez.
 * Sadece parent sinifin sundugu Device referansini kullanir.
 */
public class BasicRemote extends RemoteControl {

    public BasicRemote(Device device) {
        super(device);
    }

    /**
     * Cihazin durumunu ekrana yazdirir.
     */
    public void printDeviceStatus() {
        device.printStatus();
    }
}
