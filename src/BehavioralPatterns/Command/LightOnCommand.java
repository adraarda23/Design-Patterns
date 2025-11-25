package BehavioralPatterns.Command;

/**
 * CONCRETE COMMAND (Somut Komut)
 *
 * Işığı açma komutu. Command interface'ini implement eder.
 * Receiver (Light) nesnesini tutar ve execute() çağrıldığında
 * receiver'ın on() metodunu çalıştırır.
 */
public class LightOnCommand implements Command {
    private Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.on();
    }

    @Override
    public void undo() {
        light.off();  // Undo işlemi için ters işlemi yapar
    }
}
