package BehavioralPatterns.Command;

/**
 * COMMAND INTERFACE
 *
 * Tüm komutların implement etmesi gereken arayüz.
 * Her komut execute() ve undo() metotlarına sahip olmalı.
 */
public interface Command {
    void execute();
    void undo();
}
