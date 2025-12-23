package BehavioralPatterns.Mediator.Classic;

import java.util.ArrayList;
import java.util.List;

// ============================================
// MEDIATOR INTERFACE
// ============================================
/**
 * ChatMediator: Chat odası arayüzü
 * Kullanıcılar arası mesaj iletişimini koordine eder
 */
interface ChatMediator {
    void sendMessage(String message, User sender);
    void addUser(User user);
}

// ============================================
// CONCRETE MEDIATOR
// ============================================
/**
 * ChatRoom: Gerçek chat odası implementasyonu
 * - Tüm kullanıcıları tutar
 * - Mesajları dağıtır
 * - Kullanıcılar birbirini BİLMEZ!
 */
class ChatRoom implements ChatMediator {
    private final List<User> users = new ArrayList<>();
    private final String roomName;

    public ChatRoom(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public void addUser(User user) {
        users.add(user);
        System.out.println("✅ " + user.getName() + " '" + roomName + "' odasına katıldı");
    }

    @Override
    public void sendMessage(String message, User sender) {
        System.out.println("\n📤 [" + roomName + "] " + sender.getName() + ": " + message);

        // Gönderici hariç herkese mesajı ilet
        for (User user : users) {
            if (user != sender) {
                user.receive(message, sender.getName());
            }
        }
    }
}

// ============================================
// COMPONENT (COLLEAGUE)
// ============================================
/**
 * User: Chat kullanıcısı
 *
 * ÖNEMLİ:
 * - Sadece mediator'ı bilir
 * - Diğer kullanıcıları BİLMEZ
 * - Mesajı mediator'a gönderir, mediator dağıtır
 */
class User {
    private final String name;
    private final ChatMediator mediator;

    public User(String name, ChatMediator mediator) {
        this.name = name;
        this.mediator = mediator;
    }

    public String getName() {
        return name;
    }

    // Mesaj gönder (mediator üzerinden)
    public void send(String message) {
        mediator.sendMessage(message, this);
    }

    // Mesaj al (mediator'dan)
    public void receive(String message, String senderName) {
        System.out.println("  📥 " + name + " aldı: " + message);
    }
}

// ============================================
// DEMO - KLASİK MEDİATOR PATTERN
// ============================================
/**
 * AMAÇ: Nesneler arası karmaşık iletişimi basitleştirmek
 *
 * SENARYO: Chat Room
 * - 3 kullanıcı var
 * - Birbirleriyle mesajlaşıyorlar
 * - Ama birbirlerini BİLMİYORLAR!
 *
 * FAYDASI:
 * - Alice, Bob ve Charlie'yi bilmiyor
 * - Herkes sadece ChatRoom'u biliyor
 * - Yeni kullanıcı eklemek kolay
 * - n² bağlantı yerine n bağlantı
 */
public class ChatRoomDemo {
    public static void main(String[] args) {
        System.out.println("🎯 MEDİATOR PATTERN - CHAT ROOM ÖRNEĞİ\n");

        // 1. Chat room oluştur (Mediator)
        ChatMediator techRoom = new ChatRoom("Tech Talk");

        // 2. Kullanıcılar oluştur (Components)
        User alice = new User("Alice", techRoom);
        User bob = new User("Bob", techRoom);
        User charlie = new User("Charlie", techRoom);

        // 3. Kullanıcıları odaya ekle
        techRoom.addUser(alice);
        techRoom.addUser(bob);
        techRoom.addUser(charlie);

        // 4. Mesajlaşma başlasın
        System.out.println("\n--- Sohbet Başlıyor ---");

        alice.send("Merhaba herkese!");

        System.out.println();
        bob.send("Selam Alice! Nasılsın?");

        System.out.println();
        charlie.send("Hey! Ben de buradayım!");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("💡 DİKKAT:");
        System.out.println("   - Alice, Bob'u bilmiyor");
        System.out.println("   - Bob, Charlie'yi bilmiyor");
        System.out.println("   - Herkes sadece ChatRoom'u biliyor!");
        System.out.println("   - Bu sayede loose coupling sağlanıyor");
        System.out.println("=".repeat(60));
    }
}
