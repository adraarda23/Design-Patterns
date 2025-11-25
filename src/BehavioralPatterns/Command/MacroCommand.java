package BehavioralPatterns.Command;

/**
 * MACRO COMMAND (Makro Komut)
 *
 * Birden fazla komutu bir arada çalıştırmak için kullanılır.
 * Örneğin "Eve geliyorum" modu: tüm ışıkları aç, klimayı aç vb.
 */
public class MacroCommand implements Command {
    private Command[] commands;

    public MacroCommand(Command[] commands) {
        this.commands = commands;
    }

    @Override
    public void execute() {
        System.out.println("\n🎯 Makro komut çalıştırılıyor...");
        for (Command command : commands) {
            command.execute();
        }
        System.out.println("✅ Makro komut tamamlandı!\n");
    }

    @Override
    public void undo() {
        System.out.println("\n↩️ Makro komut geri alınıyor...");
        // Ters sırayla undo yap
        for (int i = commands.length - 1; i >= 0; i--) {
            commands[i].undo();
        }
        System.out.println("✅ Makro komut geri alındı!\n");
    }
}
