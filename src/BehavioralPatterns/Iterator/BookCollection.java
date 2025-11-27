package BehavioralPatterns.Iterator;

import java.util.ArrayList;
import java.util.List;

/**
 * BookCollection - Kitapları saklayan ve iterator döndüren koleksiyon
 */
public class BookCollection implements Collection<Book> {
    private List<Book> books;

    public BookCollection() {
        this.books = new ArrayList<>();
    }

    /**
     * Koleksiyona yeni kitap ekler
     */
    public void addBook(Book book) {
        books.add(book);
    }

    /**
     * Koleksiyondan kitap çıkarır
     */
    public void removeBook(Book book) {
        books.remove(book);
    }

    /**
     * Koleksiyondaki kitap sayısını döndürür
     */
    public int getSize() {
        return books.size();
    }

    /**
     * Belirli bir indeksteki kitabı döndürür
     */
    public Book getBookAt(int index) {
        if (index >= 0 && index < books.size()) {
            return books.get(index);
        }
        return null;
    }

    /**
     * Bu koleksiyon için standart bir iterator oluşturur
     */
    @Override
    public Iterator<Book> createIterator() {
        return new BookIterator(this);
    }

    /**
     * Ters sırada dolaşan bir iterator oluşturur
     */
    public Iterator<Book> createReverseIterator() {
        return new ReverseBookIterator(this);
    }
}
