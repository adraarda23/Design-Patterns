package Structural.Decorator;

/**
 * Decorator Pattern Demo
 *
 * Bu örnek, bir kahve siparişi senaryosu üzerinden
 * Decorator Pattern'in nasıl çalıştığını gösterir.
 *
 * AVANTAJLARI:
 * - Inheritance yerine composition kullanır (daha esnek)
 * - Çalışma zamanında özellik ekleyebilirsin
 * - Open/Closed Principle: Mevcut kodu değiştirmeden genişletebilirsin
 * - Single Responsibility: Her decorator tek bir özellik ekler
 *
 * DEZAVANTAJLARI:
 * - Çok fazla küçük sınıf oluşabilir
 * - Decorator sırası önemli olabilir bazı durumlarda
 * - Sarmalanan nesneyi doğrudan erişmek zordur
 */
public class Main {

    public static void main(String[] args) {

        // ==================== ESKİ YÖNTEM (Çirkin) ====================
        System.out.println("========== ESKİ YÖNTEM (İç içe new) ==========\n");

        // Çirkin ve okunması zor
        Coffee uglyWay = new WhippedCreamDecorator(
                            new SugarDecorator(
                                new MilkDecorator(
                                    new SimpleCoffee())));
        System.out.println("Açıklama: " + uglyWay.getDescription());
        System.out.println("Fiyat: " + uglyWay.getCost() + " TL\n");


        // ==================== YENİ YÖNTEM (Builder ile) ====================
        System.out.println("========== YENİ YÖNTEM (Builder ile) ==========\n");

        // 1. Sade kahve
        Coffee sade = new CoffeeBuilder().build();
        System.out.println("Sade: " + sade.getDescription() + " -> " + sade.getCost() + " TL");

        // 2. Sütlü kahve
        Coffee sutlu = new CoffeeBuilder()
                            .withMilk()
                            .build();
        System.out.println("Sütlü: " + sutlu.getDescription() + " -> " + sutlu.getCost() + " TL");

        // 3. Tam donanımlı - ÇOK DAHA TEMİZ!
        Coffee tamDonanım = new CoffeeBuilder()
                                .withMilk()
                                .withSugar()
                                .withWhippedCream()
                                .build();
        System.out.println("Full: " + tamDonanım.getDescription() + " -> " + tamDonanım.getCost() + " TL");

        // 4. Çift sütlü
        Coffee ciftSutlu = new CoffeeBuilder()
                                .withMilk()
                                .withMilk()
                                .build();
        System.out.println("Çift Sütlü: " + ciftSutlu.getDescription() + " -> " + ciftSutlu.getCost() + " TL");

        System.out.println("\n==============================================");
        System.out.println("Builder sayesinde:");
        System.out.println("- Okunabilir kod");
        System.out.println("- Method chaining");
        System.out.println("- İç içe new yok!");
    }

    private static void printCoffeeDetails(Coffee coffee) {
        System.out.println("Açıklama: " + coffee.getDescription());
        System.out.println("Fiyat: " + coffee.getCost() + " TL");
        System.out.println();
    }
}
