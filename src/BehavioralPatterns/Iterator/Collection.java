package BehavioralPatterns.Iterator;

/**
 * Collection interface - Iterator döndürebilen koleksiyonlar için arayüz
 */
public interface Collection<T> {

    /**
     * Bu koleksiyon için bir iterator oluşturur
     * @return Koleksiyonu dolaşmak için iterator
     */
    Iterator<T> createIterator();
}
