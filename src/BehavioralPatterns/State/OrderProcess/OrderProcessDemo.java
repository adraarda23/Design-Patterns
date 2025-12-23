package BehavioralPatterns.State.OrderProcess;

// STATE INTERFACE
interface OrderState {
    void pay(Order order);
    void ship(Order order);
    void deliver(Order order);
    void cancel(Order order);
    String getStateName();
}

// CONTEXT
class Order {
    private OrderState state;
    private String orderId;

    public Order(String orderId) {
        this.orderId = orderId;
        this.state = new NewOrderState();
        System.out.println("🛒 Sipariş oluşturuldu: " + orderId);
    }

    public void setState(OrderState state) {
        this.state = state;
        System.out.println("   ➡️  Durum: " + state.getStateName());
    }

    public String getOrderId() { return orderId; }

    public void pay() {
        System.out.println("\n💳 Ödeme işlemi...");
        state.pay(this);
    }

    public void ship() {
        System.out.println("\n📦 Kargolama işlemi...");
        state.ship(this);
    }

    public void deliver() {
        System.out.println("\n✅ Teslim işlemi...");
        state.deliver(this);
    }

    public void cancel() {
        System.out.println("\n🚫 İptal işlemi...");
        state.cancel(this);
    }
}

// CONCRETE STATES
class NewOrderState implements OrderState {
    public void pay(Order order) {
        System.out.println("   ✅ Ödeme alındı");
        order.setState(new PaidOrderState());
    }
    public void ship(Order order) {
        System.out.println("   ❌ Önce ödeme yapılmalı!");
    }
    public void deliver(Order order) {
        System.out.println("   ❌ Önce ödeme yapılmalı!");
    }
    public void cancel(Order order) {
        System.out.println("   ✅ Sipariş iptal edildi");
        order.setState(new CancelledOrderState());
    }
    public String getStateName() { return "YENİ SİPARİŞ"; }
}

class PaidOrderState implements OrderState {
    public void pay(Order order) {
        System.out.println("   ⚠️  Ödeme zaten yapıldı");
    }
    public void ship(Order order) {
        System.out.println("   ✅ Kargoya verildi");
        order.setState(new ShippedOrderState());
    }
    public void deliver(Order order) {
        System.out.println("   ❌ Önce kargoya verilmeli!");
    }
    public void cancel(Order order) {
        System.out.println("   ✅ Sipariş iptal edildi, iade işlemi başlatıldı");
        order.setState(new CancelledOrderState());
    }
    public String getStateName() { return "ÖDENDİ"; }
}

class ShippedOrderState implements OrderState {
    public void pay(Order order) {
        System.out.println("   ⚠️  Ödeme zaten yapıldı");
    }
    public void ship(Order order) {
        System.out.println("   ⚠️  Zaten kargoda");
    }
    public void deliver(Order order) {
        System.out.println("   ✅ Teslim edildi");
        order.setState(new DeliveredOrderState());
    }
    public void cancel(Order order) {
        System.out.println("   ⚠️  Kargodaki sipariş iptal edilemez!");
    }
    public String getStateName() { return "KARGODA"; }
}

class DeliveredOrderState implements OrderState {
    public void pay(Order order) {
        System.out.println("   ⚠️  Sipariş tamamlandı");
    }
    public void ship(Order order) {
        System.out.println("   ⚠️  Sipariş teslim edildi");
    }
    public void deliver(Order order) {
        System.out.println("   ⚠️  Zaten teslim edildi");
    }
    public void cancel(Order order) {
        System.out.println("   ❌ Teslim edilen sipariş iptal edilemez!");
    }
    public String getStateName() { return "TESLİM EDİLDİ"; }
}

class CancelledOrderState implements OrderState {
    public void pay(Order order) {
        System.out.println("   ❌ İptal edilen sipariş için ödeme yapılamaz");
    }
    public void ship(Order order) {
        System.out.println("   ❌ İptal edilen sipariş kargolanamaz");
    }
    public void deliver(Order order) {
        System.out.println("   ❌ İptal edilen sipariş teslim edilemez");
    }
    public void cancel(Order order) {
        System.out.println("   ⚠️  Zaten iptal edilmiş");
    }
    public String getStateName() { return "İPTAL EDİLDİ"; }
}

// DEMO
public class OrderProcessDemo {
    public static void main(String[] args) {
        System.out.println("🎯 STATE PATTERN - SİPARİŞ SÜRECİ\n");

        System.out.println("=".repeat(50));
        System.out.println("📋 SENARYO 1: Normal Sipariş Akışı");
        System.out.println("=".repeat(50));
        Order order1 = new Order("ORD-001");
        order1.pay();
        order1.ship();
        order1.deliver();

        System.out.println("\n" + "=".repeat(50));
        System.out.println("📋 SENARYO 2: Erken İptal");
        System.out.println("=".repeat(50));
        Order order2 = new Order("ORD-002");
        order2.cancel();

        System.out.println("\n" + "=".repeat(50));
        System.out.println("📋 SENARYO 3: Geç İptal (Kargoda)");
        System.out.println("=".repeat(50));
        Order order3 = new Order("ORD-003");
        order3.pay();
        order3.ship();
        order3.cancel();

        System.out.println("\n" + "=".repeat(50));
        System.out.println("💡 E-TİCARET STATE YÖNETİMİ");
        System.out.println("=".repeat(50));
    }
}
