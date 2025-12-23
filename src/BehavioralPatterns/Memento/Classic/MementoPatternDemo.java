package BehavioralPatterns.Memento.Classic;

import java.util.*;

// ============================================
// ADIM 1: MEMENTO CLASS
// ============================================
/**
 * TextMemento: Editor'ün state'ini tutan snapshot
 * - Immutable (değiştirilemez)
 * - Sadece content saklar
 * - Dışarıdan değiştirilemez
 */
class TextMemento {
    private final String content;  // final = immutable
    private final long timestamp;   // Ne zaman kaydedildi?

    // Constructor: State'i al ve kaydet
    public TextMemento(String content) {
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    // Sadece getter - setter YOK!
    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

// ============================================
// ADIM 2: ORIGINATOR CLASS
// ============================================
/**
 * TextEditor: Asıl state'i tutan ve değiştiren sınıf
 * - İç durumu: content (metin)
 * - Memento oluşturabilir
 * - Memento'dan geri yükleyebilir
 */
class TextEditor {
    private String content;  // Bu bizim state'imiz

    public TextEditor() {
        this.content = "";
    }

    // İş mantığı: Metin yaz
    public void write(String text) {
        this.content += text;
    }

    // İş mantığı: Metni göster
    public String getContent() {
        return content;
    }

    // MEMENTO OLUŞTUR: Mevcut durumu kaydet
    public TextMemento save() {
        System.out.println("💾 Kaydediliyor: \"" + content + "\"");
        return new TextMemento(content);
    }

    // MEMENTO'DAN GERİ YÜKLE: Eski duruma dön
    public void restore(TextMemento memento) {
        this.content = memento.getContent();
        System.out.println("⏮️  Geri yüklendi: \"" + content + "\"");
    }
}

// ============================================
// ADIM 3: CARETAKER CLASS
// ============================================
/**
 * History: Memento'ları saklayan ve yöneten sınıf
 * - Stack yapısı kullanır (LIFO)
 * - Memento içeriğini bilmez/değiştirmez
 * - Sadece saklar ve geri verir
 */
class History {
    private final Stack<TextMemento> mementos = new Stack<>();

    // Memento'yu sakla
    public void push(TextMemento memento) {
        mementos.push(memento);
        System.out.println("📚 Geçmişe eklendi (Toplam: " + mementos.size() + ")");
    }

    // Son Memento'yu al ve çıkar
    public TextMemento pop() {
        if (mementos.isEmpty()) {
            System.out.println("❌ Geçmiş boş!");
            return null;
        }
        System.out.println("📖 Geçmişten alınıyor...");
        return mementos.pop();
    }

    // Geçmişte kayıt var mı?
    public boolean isEmpty() {
        return mementos.isEmpty();
    }
}

// ============================================
// KULLANIM ÖRNEĞİ - KLASİK MEMENTO PATTERN
// ============================================
/**
 * YAKLAŞIM: Full Snapshot (Klasik Memento)
 *
 * AVANTAJLAR:
 * - Basit implementasyon
 * - Hızlı restore (direkt eski state'e dön)
 * - Anlaşılır ve bakımı kolay
 *
 * DEZAVANTAJLAR:
 * - Her save'de tüm state kopyalanır
 * - Büyük state'lerde bellek tüketimi yüksek
 * - Çok sayıda snapshot belleği şişirir
 *
 * NE ZAMAN KULLANILMALI:
 * - Küçük state boyutları (< 1KB)
 * - Az sayıda snapshot (< 10)
 * - Hız öncelikli uygulamalar
 * - Basit undo/redo işlevleri
 */
public class MementoPatternDemo {
    public static void main(String[] args) {
        System.out.println("🎯 KLASİK MEMENTO PATTERN - FULL SNAPSHOT YAKLAŞIMI\n");

        TextEditor editor = new TextEditor();
        History history = new History();

        // 1. Adım: İlk metin
        editor.write("Merhaba ");
        history.push(editor.save());  // Kaydet

        // 2. Adım: Daha fazla metin
        editor.write("Dünya");
        history.push(editor.save());  // Kaydet

        // 3. Adım: Daha da fazla metin
        editor.write("!");
        System.out.println("\n📝 Şu anki metin: " + editor.getContent());

        // UNDO 1: Son kayda dön
        System.out.println("\n--- UNDO 1 ---");
        TextMemento memento = history.pop();
        if (memento != null) {
            editor.restore(memento);
        }
        System.out.println("📝 Şu anki metin: " + editor.getContent());

        // UNDO 2: Bir önceki kayda dön
        System.out.println("\n--- UNDO 2 ---");
        memento = history.pop();
        if (memento != null) {
            editor.restore(memento);
        }
        System.out.println("📝 Şu anki metin: " + editor.getContent());

        System.out.println("\n✅ Klasik Memento Pattern - Basit ve etkili!");
    }
}
