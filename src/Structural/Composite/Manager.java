package Structural.Composite;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite: Alt elemanları (subordinates) olan çalışan (Yönetici)
 * Hem kendi bilgilerini tutar hem de altındaki çalışanları yönetir
 */
public class Manager implements Employee {
    private String name;
    private double salary;
    private String department;
    private List<Employee> subordinates; // Alt çalışanlar listesi

    public Manager(String name, double salary, String department) {
        this.name = name;
        this.salary = salary;
        this.department = department;
        this.subordinates = new ArrayList<>();
    }

    /**
     * Yöneticinin altına çalışan ekler
     * @param employee Eklenecek çalışan (Developer, Designer veya başka bir Manager olabilir)
     */
    public void addEmployee(Employee employee) {
        subordinates.add(employee);
    }

    /**
     * Yöneticinin altından çalışan çıkarır
     * @param employee Çıkarılacak çalışan
     */
    public void removeEmployee(Employee employee) {
        subordinates.remove(employee);
    }

    /**
     * Altındaki tüm çalışanları döndürür
     * @return Çalışanlar listesi
     */
    public List<Employee> getSubordinates() {
        return subordinates;
    }

    @Override
    public void showDetails(String indent) {
        System.out.println(indent + "Manager: " + name +
                " | Salary: $" + salary +
                " | Department: " + department);

        // Alt çalışanların detaylarını göster (recursive - özyinelemeli)
        for (Employee employee : subordinates) {
            employee.showDetails(indent + "  "); // Girinti arttırılır
        }
    }

    @Override
    public double getSalary() {
        // Yöneticinin toplam maliyeti: Kendi maaşı + altındaki tüm çalışanların maaşları
        double totalSalary = salary;
        for (Employee employee : subordinates) {
            totalSalary += employee.getSalary();
        }
        return totalSalary;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Departman bilgisini döndürür
     * @return Departman adı
     */
    public String getDepartment() {
        return department;
    }
}
