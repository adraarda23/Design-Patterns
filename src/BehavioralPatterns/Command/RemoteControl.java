package BehavioralPatterns.Command;

import java.util.Stack;

/**
 * INVOKER (Çağırıcı)
 *
 * Komutları çalıştıran sınıf. Uzaktan kumanda gibi düşünebilirsiniz.
 * Hangi komutun nasıl çalıştığını bilmez, sadece execute() metodunu çağırır.
 *
 * Undo özelliği için komut geçmişini (history) tutar.
 */
public class RemoteControl {
    private Command[] onCommands;
    private Command[] offCommands;
    private Stack<Command> undoStack;

    public RemoteControl() {
        // 7 slotlu uzaktan kumanda
        onCommands = new Command[7];
        offCommands = new Command[7];
        undoStack = new Stack<>();

        // Boş komut (Null Object Pattern)
        Command noCommand = new Command() {
            public void execute() { }
            public void undo() { }
        };

        for (int i = 0; i < 7; i++) {
            onCommands[i] = noCommand;
            offCommands[i] = noCommand;
        }
    }

    /**
     * Belirli bir slota komut atar
     */
    public void setCommand(int slot, Command onCommand, Command offCommand) {
        onCommands[slot] = onCommand;
        offCommands[slot] = offCommand;
    }

    /**
     * ON butonuna basıldığında çağrılır
     */
    public void onButtonPressed(int slot) {
        onCommands[slot].execute();
        undoStack.push(onCommands[slot]);  // Undo için kaydet
    }

    /**
     * OFF butonuna basıldığında çağrılır
     */
    public void offButtonPressed(int slot) {
        offCommands[slot].execute();
        undoStack.push(offCommands[slot]);  // Undo için kaydet
    }

    /**
     * UNDO butonuna basıldığında çağrılır
     */
    public void undoButtonPressed() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
        } else {
            System.out.println("❌ Geri alınacak komut yok!");
        }
    }

    /**
     * Uzaktan kumandanın mevcut durumunu gösterir
     */
    public void printStatus() {
        System.out.println("\n📱 ===== UZAKTAN KUMANDA =====");
        for (int i = 0; i < onCommands.length; i++) {
            System.out.println("[Slot " + i + "] " +
                onCommands[i].getClass().getSimpleName() + "  |  " +
                offCommands[i].getClass().getSimpleName());
        }
        System.out.println("==============================\n");
    }
}
