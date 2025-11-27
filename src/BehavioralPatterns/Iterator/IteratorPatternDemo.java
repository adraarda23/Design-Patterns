package BehavioralPatterns.Iterator;

/**
 * IteratorPatternDemo - Iterator Pattern'in kullanımını gösteren demo sınıfı
 */
public class IteratorPatternDemo {

    public static void main(String[] args) {
        // Kitap koleksiyonu oluştur
        BookCollection library = new BookCollection();

        // Koleksiyona kitaplar ekle
        library.addBook(new Book("1984", "George Orwell", 1949));
        library.addBook(new Book("Brave New World", "Aldous Huxley", 1932));
        library.addBook(new Book("Fahrenheit 451", "Ray Bradbury", 1953));
        library.addBook(new Book("The Handmaid's Tale", "Margaret Atwood", 1985));
        library.addBook(new Book("Animal Farm", "George Orwell", 1945));

        System.out.println("=== NORMAL SIRAYLA DOLAŞMA ===");
        Iterator<Book> normalIterator = library.createIterator();
        printBooks(normalIterator);

        System.out.println("\n=== TERS SIRAYLA DOLAŞMA ===");
        Iterator<Book> reverseIterator = library.createReverseIterator();
        printBooks(reverseIterator);

        // Iterator'ı reset edip tekrar kullanma
        System.out.println("\n=== ITERATOR'I RESET EDIP TEKRAR KULLANMA ===");
        normalIterator.reset();
        System.out.println("İlk üç kitap:");
        int count = 0;
        while (normalIterator.hasNext() && count < 3) {
            System.out.println((count + 1) + ". " + normalIterator.next());
            count++;
        }

        // Iterator Pattern'in avantajlarını göster
        demonstrateAdvantages(library);
    }

    /**
     * Iterator kullanarak kitapları yazdırır
     * Bu metod, koleksiyonun iç yapısını bilmez!
     */
    private static void printBooks(Iterator<Book> iterator) {
        int counter = 1;
        while (iterator.hasNext()) {
            Book book = iterator.next();
            System.out.println(counter + ". " + book);
            counter++;
        }
    }

    /**
     * Iterator Pattern'in avantajlarını gösterir
     */
    private static void demonstrateAdvantages(BookCollection library) {
        System.out.println("\n=== ITERATOR PATTERN AVANTAJLARI ===");
        System.out.println("1. Koleksiyonun iç yapısını bilmeden dolaşabiliyoruz");
        System.out.println("2. Aynı koleksiyonda farklı dolaşma stratejileri kullanabiliyoruz");
        System.out.println("3. Tek Sorumluluk Prensibi: Dolaşma mantığı ayrı bir sınıfta");
        System.out.println("4. Açık/Kapalı Prensibi: Yeni iterator türleri ekleyebiliriz");

        System.out.println("\n=== ÖRNEK: Sadece George Orwell kitaplarını filtrele ===");
        Iterator<Book> iterator = library.createIterator();
        while (iterator.hasNext()) {
            Book book = iterator.next();
            if (book.getAuthor().equals("George Orwell")) {
                System.out.println("- " + book);
            }
        }
    }
}
