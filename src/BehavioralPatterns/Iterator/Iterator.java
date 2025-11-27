package BehavioralPatterns.Iterator;

/**
 * Iterator interface - Koleksiyonda dolaşmak için temel metodları tanımlar
 */
public interface Iterator<T> {

    /**
     * Bir sonraki eleman var mı kontrol eder
     * @return true ise bir sonraki eleman var, false ise koleksiyon bitti
     */
    boolean hasNext();

    /**
     * Bir sonraki elemanı döndürür ve pozisyonu ilerletir
     * @return Koleksiyonun bir sonraki elemanı
     */
    T next();

    /**
     * Iterator'ı başa döndürür
     */
    void reset();
}
