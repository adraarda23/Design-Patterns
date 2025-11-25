package BehavioralPatterns.Command;

/**
 * CONCRETE COMMAND (Somut Komut)
 *
 * Işığı kapatma komutu.
 */
public class LightOffCommand implements Command {
    private Light light;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.off();
    }

    @Override
    public void undo() {
        light.on();  // Undo işlemi için ters işlemi yapar
    }
}
