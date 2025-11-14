package Structural.Composite;

/**
 * Composite Pattern Demo: Şirket Organizasyon Yapısı
 *
 * Bu örnekte:
 * - Hem bireysel çalışanlar (Developer, Designer) - LEAF
 * - Hem de yöneticiler (Manager) - COMPOSITE
 * aynı interface'i (Employee) kullanıyor
 *
 * Böylece client kod, tek bir çalışan mı yoksa bir ekip mi olduğunu bilmek zorunda kalmıyor
 */
public class CompositeMain {
    public static void main(String[] args) {
        System.out.println("=== COMPOSITE PATTERN DEMO ===\n");

        // Leaf nesneler oluştur (bireysel çalışanlar)
        Developer dev1 = new Developer("Ali Yılmaz", 5000, "Java");
        Developer dev2 = new Developer("Ayşe Kaya", 5500, "Python");
        Developer dev3 = new Developer("Mehmet Demir", 6000, "JavaScript");

        Designer designer1 = new Designer("Zeynep Aydın", 4500, "Figma");
        Designer designer2 = new Designer("Can Öztürk", 4800, "Adobe XD");

        // Composite nesneler oluştur (yöneticiler)
        Manager techLead = new Manager("Ahmet Şahin", 8000, "Development");
        Manager designLead = new Manager("Elif Arslan", 7500, "Design");
        Manager cto = new Manager("Burak Yıldız", 12000, "Technology");

        // Hiyerarşi oluştur
        // Tech Lead altına developer'ları ekle
        techLead.addEmployee(dev1);
        techLead.addEmployee(dev2);
        techLead.addEmployee(dev3);

        // Design Lead altına designer'ları ekle
        designLead.addEmployee(designer1);
        designLead.addEmployee(designer2);

        // CTO altına diğer yöneticileri ekle (Manager içinde Manager!)
        cto.addEmployee(techLead);
        cto.addEmployee(designLead);

        // Tüm organizasyonu göster
        System.out.println("📊 ORGANIZATION HIERARCHY:");
        System.out.println("─────────────────────────────");
        cto.showDetails("");

        // Toplam maliyet hesapla
        System.out.println("\n💰 COST ANALYSIS:");
        System.out.println("─────────────────────────────");
        System.out.println("Tech Lead Team Total: $" + techLead.getSalary());
        System.out.println("Design Lead Team Total: $" + designLead.getSalary());
        System.out.println("CTO Organization Total: $" + cto.getSalary());

        // Composite Pattern'in gücü: Tek bir çalışan ile ekibi aynı şekilde işle
        System.out.println("\n🎯 TREATING INDIVIDUAL AND GROUP UNIFORMLY:");
        System.out.println("─────────────────────────────");
        printEmployeeInfo(dev1);      // Tek bir developer
        printEmployeeInfo(techLead);  // Bir ekip yöneticisi
        printEmployeeInfo(cto);       // Tüm organizasyon

        // Dinamik değişiklik gösterisi
        System.out.println("\n🔄 DYNAMIC CHANGE: Removing a developer...");
        System.out.println("─────────────────────────────");
        techLead.removeEmployee(dev1);
        System.out.println("New Tech Lead Team Total: $" + techLead.getSalary());
    }

    /**
     * Bu metod, Employee'nin ne olduğunu bilmeden çalışır
     * Tek bir kişi mi, ekip mi umurunda değil - Composite Pattern'in gücü!
     */
    private static void printEmployeeInfo(Employee employee) {
        System.out.println("\nProcessing: " + employee.getName());
        System.out.println("Total Cost: $" + employee.getSalary());
    }
}
