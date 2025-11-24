package Structural.Bridge;

/**
 * ADIM 1: Implementation Interface
 *
 * Bu interface, köprünün "implementasyon" tarafını temsil eder.
 * Tüm somut cihazlar (TV, Radio, vb.) bu interface'i implement eder.
 *
 * Abstraction (RemoteControl) bu interface üzerinden cihazlarla iletişim kurar.
 * Bu sayede RemoteControl, hangi cihazı kontrol ettiğini bilmek zorunda kalmaz.
 */
public interface Device {

    boolean isEnabled();

    void enable();

    void disable();

    int getVolume();

    void setVolume(int volume);

    int getChannel();

    void setChannel(int channel);

    void printStatus();
}
