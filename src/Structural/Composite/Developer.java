package Structural.Composite;

/**
 * Leaf: Alt elemanı olmayan bireysel çalışan (Geliştirici)
 */
public class Developer implements Employee {
    private String name;
    private double salary;
    private String programmingLanguage;

    public Developer(String name, double salary, String programmingLanguage) {
        this.name = name;
        this.salary = salary;
        this.programmingLanguage = programmingLanguage;
    }

    @Override
    public void showDetails(String indent) {
        System.out.println(indent + "Developer: " + name +
                " | Salary: $" + salary +
                " | Language: " + programmingLanguage);
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
