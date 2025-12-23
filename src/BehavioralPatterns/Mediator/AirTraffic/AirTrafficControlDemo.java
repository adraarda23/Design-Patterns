package BehavioralPatterns.Mediator.AirTraffic;

import java.util.ArrayList;
import java.util.List;

// ============================================
// MEDIATOR INTERFACE
// ============================================
/**
 * ControlTower: Hava trafik kontrol kulesi arayüzü
 */
interface ControlTower {
    void registerAircraft(Aircraft aircraft);
    void requestLanding(Aircraft aircraft);
    void requestTakeoff(Aircraft aircraft);
    void sendWarning(String message, Aircraft sender);
}

// ============================================
// BASE COMPONENT
// ============================================
/**
 * Aircraft: Uçak base sınıfı
 * Her uçak sadece kontrol kulesini bilir
 */
abstract class Aircraft {
    protected String callSign;      // Uçak çağrı işareti (TK123, LH456)
    protected ControlTower tower;   // Kontrol kulesi
    protected String status;        // Durum

    public Aircraft(String callSign) {
        this.callSign = callSign;
        this.status = "Havada";
    }

    public void setTower(ControlTower tower) {
        this.tower = tower;
    }

    public String getCallSign() {
        return callSign;
    }

    public String getStatus() {
        return status;
    }

    // Alt sınıflar implement edecek
    public abstract void receiveMessage(String message);
}

// ============================================
// CONCRETE COMPONENTS
// ============================================

/**
 * PassengerPlane: Yolcu uçağı
 */
class PassengerPlane extends Aircraft {

    public PassengerPlane(String callSign) {
        super(callSign);
    }

    public void requestLanding() {
        System.out.println("✈️  " + callSign + ": İniş izni istiyorum");
        tower.requestLanding(this);
    }

    public void requestTakeoff() {
        System.out.println("✈️  " + callSign + ": Kalkış izni istiyorum");
        tower.requestTakeoff(this);
    }

    public void land() {
        this.status = "Piste indi";
        System.out.println("   🛬 " + callSign + " pistte");
    }

    public void takeoff() {
        this.status = "Havada";
        System.out.println("   🛫 " + callSign + " havada");
    }

    @Override
    public void receiveMessage(String message) {
        System.out.println("   📻 " + callSign + " alıyor: " + message);
    }
}

/**
 * CargoPlane: Kargo uçağı
 */
class CargoPlane extends Aircraft {

    public CargoPlane(String callSign) {
        super(callSign);
    }

    public void requestLanding() {
        System.out.println("📦 " + callSign + ": İniş izni istiyorum");
        tower.requestLanding(this);
    }

    public void requestTakeoff() {
        System.out.println("📦 " + callSign + ": Kalkış izni istiyorum");
        tower.requestTakeoff(this);
    }

    public void land() {
        this.status = "Piste indi";
        System.out.println("   🛬 " + callSign + " pistte (KARGO)");
    }

    public void takeoff() {
        this.status = "Havada";
        System.out.println("   🛫 " + callSign + " havada (KARGO)");
    }

    @Override
    public void receiveMessage(String message) {
        System.out.println("   📻 " + callSign + " alıyor: " + message);
    }
}

// ============================================
// CONCRETE MEDIATOR
// ============================================
/**
 * AirportControlTower: Gerçek kontrol kulesi
 *
 * SORUMLULUKLAR:
 * - Tüm uçakları koordine eder
 * - İniş/kalkış izinlerini yönetir
 * - Pist durumunu kontrol eder
 * - Güvenlik uyarıları gönderir
 *
 * ÖNEMLİ:
 * Uçaklar birbirleriyle DİREKT konuşmaz!
 * Herşey kule üzerinden koordine edilir.
 */
class AirportControlTower implements ControlTower {
    private final List<Aircraft> aircrafts = new ArrayList<>();
    private boolean runwayAvailable = true;
    private final String airportCode;

    public AirportControlTower(String airportCode) {
        this.airportCode = airportCode;
    }

    @Override
    public void registerAircraft(Aircraft aircraft) {
        aircrafts.add(aircraft);
        aircraft.setTower(this);
        System.out.println("🗼 " + airportCode + " Kulesi: " + aircraft.getCallSign() +
                         " kayıt edildi");
    }

    @Override
    public void requestLanding(Aircraft aircraft) {
        if (runwayAvailable) {
            runwayAvailable = false;
            System.out.println("🗼 " + airportCode + " Kulesi: " + aircraft.getCallSign() +
                             " iniş izni verildi");

            if (aircraft instanceof PassengerPlane) {
                ((PassengerPlane) aircraft).land();
            } else if (aircraft instanceof CargoPlane) {
                ((CargoPlane) aircraft).land();
            }

            // Diğer uçakları uyar
            for (Aircraft other : aircrafts) {
                if (other != aircraft && other.getStatus().equals("Havada")) {
                    other.receiveMessage("Pist dolu, beklemede kalın");
                }
            }

            // Simülasyon: Pist 2 saniye sonra boşalacak
            runwayAvailable = true;

        } else {
            aircraft.receiveMessage("Pist dolu, beklemeye alındınız");
        }
    }

    @Override
    public void requestTakeoff(Aircraft aircraft) {
        if (runwayAvailable) {
            runwayAvailable = false;
            System.out.println("🗼 " + airportCode + " Kulesi: " + aircraft.getCallSign() +
                             " kalkış izni verildi");

            if (aircraft instanceof PassengerPlane) {
                ((PassengerPlane) aircraft).takeoff();
            } else if (aircraft instanceof CargoPlane) {
                ((CargoPlane) aircraft).takeoff();
            }

            // Pist boşaldı
            runwayAvailable = true;

        } else {
            aircraft.receiveMessage("Pist dolu, beklemeye alındınız");
        }
    }

    @Override
    public void sendWarning(String message, Aircraft sender) {
        System.out.println("🗼 " + airportCode + " Kulesi: ⚠️  UYARI - " + message);
        for (Aircraft aircraft : aircrafts) {
            if (aircraft != sender) {
                aircraft.receiveMessage("UYARI: " + message);
            }
        }
    }
}

// ============================================
// DEMO - AIR TRAFFIC CONTROL
// ============================================
/**
 * AMAÇ: Gerçek dünya Mediator Pattern örneği
 *
 * SENARYO: Havalimanı kontrol kulesi
 *
 * MEDIATOR OLMADAN:
 * - Her uçak diğer uçakları bilmeli
 * - Herkes herkesle konuşmalı
 * - Kaos! Çarpışma riski!
 *
 * MEDIATOR İLE:
 * - Uçaklar sadece kule ile konuşur
 * - Kule koordinasyonu sağlar
 * - Güvenli ve organize
 *
 * GERÇEK HAYAT KARŞILIĞI:
 * Bu pattern, gerçek hayattaki ATC (Air Traffic Control)
 * sisteminin tam bir yansımasıdır!
 */
public class AirTrafficControlDemo {
    public static void main(String[] args) {
        System.out.println("🎯 MEDİATOR PATTERN - HAVA TRAFİK KONTROL ÖRNEĞİ\n");

        // 1. Kontrol kulesini oluştur (Mediator)
        AirportControlTower istanbulTower = new AirportControlTower("IST");
        System.out.println("🗼 İstanbul Havalimanı Kontrol Kulesi aktif\n");

        // 2. Uçakları oluştur (Components)
        PassengerPlane turkishAir = new PassengerPlane("TK123");
        PassengerPlane lufthansa = new PassengerPlane("LH456");
        CargoPlane cargo = new CargoPlane("CG789");

        // 3. Uçakları kuleye kaydet
        istanbulTower.registerAircraft(turkishAir);
        istanbulTower.registerAircraft(lufthansa);
        istanbulTower.registerAircraft(cargo);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("--- Senaryo Başlıyor ---\n");

        // 4. İlk uçak iniş izni istiyor
        System.out.println("📍 Adım 1: TK123 İniş İstiyor");
        turkishAir.requestLanding();

        System.out.println("\n📍 Adım 2: LH456 de İniş İstiyor (pist dolu!)");
        lufthansa.requestLanding();

        System.out.println("\n📍 Adım 3: TK123 Kalkış İzni İstiyor");
        turkishAir.requestTakeoff();

        System.out.println("\n📍 Adım 4: Kargo Uçağı Kalkış İzni İstiyor");
        cargo.requestTakeoff();

        System.out.println("\n📍 Adım 5: LH456 Tekrar İniş İstiyor");
        lufthansa.requestLanding();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("💡 MEDIATOR PATTERN'IN GÜCÜ:");
        System.out.println("   ✅ TK123, LH456'yı bilmiyor");
        System.out.println("   ✅ LH456, CG789'u bilmiyor");
        System.out.println("   ✅ Herkes sadece kuleyi biliyor");
        System.out.println("   ✅ Koordinasyon merkezileşmiş");
        System.out.println("   ✅ Güvenli ve yönetilebilir!");
        System.out.println("=".repeat(60));

        System.out.println("\n🌍 GERÇEK HAYAT:");
        System.out.println("   Gerçek havalimanlarında da aynen böyle çalışır!");
        System.out.println("   Pilotlar birbirleriyle değil, kule ile konuşur.");
    }
}
