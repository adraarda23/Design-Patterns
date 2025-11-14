package Structural.Composite;

/**
 * Leaf: Alt elemanı olmayan bireysel çalışan (Tasarımcı)
 */
public class Designer implements Employee {
    private String name;
    private double salary;
    private String toolExpertise;

    public Designer(String name, double salary, String toolExpertise) {
        this.name = name;
        this.salary = salary;
        this.toolExpertise = toolExpertise;
    }

    @Override
    public void showDetails(String indent) {
        System.out.println(indent + "Designer: " + name +
                " | Salary: $" + salary +
                " | Tool: " + toolExpertise);
    }

    @Override
    public double getSalary() {
        return salary;
    }

    @Override
    public String getName() {
        return name;
    }
}
