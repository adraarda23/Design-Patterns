package BehavioralPatterns.State.TrafficLight;

// STATE INTERFACE
interface TrafficLightState {
    void change(TrafficLight light);
    String getColor();
    int getDuration();  // saniye
}

// CONTEXT
class TrafficLight {
    private TrafficLightState state;

    public TrafficLight() {
        this.state = new RedState();
        System.out.println("🚦 Trafik ışığı başlatıldı");
    }

    public void setState(TrafficLightState state) {
        this.state = state;
    }

    public void change() {
        state.change(this);
    }

    public void display() {
        System.out.println("🚦 " + state.getColor() + " (" + state.getDuration() + "sn)");
    }
}

// CONCRETE STATES
class RedState implements TrafficLightState {
    public void change(TrafficLight light) {
        System.out.println("   🔴 Kırmızı → 🟡 Sarı");
        light.setState(new YellowAfterRedState());
    }
    public String getColor() { return "🔴 KIRMIZI - DUR!"; }
    public int getDuration() { return 30; }
}

class YellowAfterRedState implements TrafficLightState {
    public void change(TrafficLight light) {
        System.out.println("   🟡 Sarı → 🟢 Yeşil");
        light.setState(new GreenState());
    }
    public String getColor() { return "🟡 SARI - Hazırlan"; }
    public int getDuration() { return 3; }
}

class GreenState implements TrafficLightState {
    public void change(TrafficLight light) {
        System.out.println("   🟢 Yeşil → 🟡 Sarı");
        light.setState(new YellowAfterGreenState());
    }
    public String getColor() { return "🟢 YEŞİL - Geç!"; }
    public int getDuration() { return 30; }
}

class YellowAfterGreenState implements TrafficLightState {
    public void change(TrafficLight light) {
        System.out.println("   🟡 Sarı → 🔴 Kırmızı");
        light.setState(new RedState());
    }
    public String getColor() { return "🟡 SARI - Yavaşla"; }
    public int getDuration() { return 3; }
}

// DEMO
public class TrafficLightDemo {
    public static void main(String[] args) {
        System.out.println("🎯 STATE PATTERN - TRAFİK IŞIĞI SİMÜLASYONU\n");

        TrafficLight light = new TrafficLight();

        System.out.println("\n📍 10 Döngü Simülasyonu:\n");

        for (int i = 1; i <= 10; i++) {
            System.out.println("Döngü " + i + ":");
            light.display();
            light.change();
            System.out.println();
        }

        System.out.println("=".repeat(50));
        System.out.println("💡 OTOMATIK STATE GEÇİŞLERİ");
        System.out.println("=".repeat(50));
        System.out.println("🔴 Kırmızı → 🟡 Sarı → 🟢 Yeşil → 🟡 Sarı → 🔴 (döngü)");
    }
}
