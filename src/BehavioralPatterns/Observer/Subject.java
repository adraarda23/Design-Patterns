package BehavioralPatterns.Observer;

/**
 * Subject Interface
 * Observer'ları yönetmek için gerekli metodları tanımlar
 */
public interface Subject {
    /**
     * Yeni bir observer ekler
     * @param observer Eklenecek observer
     */
    void registerObserver(Observer observer);

    /**
     * Bir observer'ı çıkarır
     * @param observer Çıkarılacak observer
     */
    void removeObserver(Observer observer);

    /**
     * Tüm kayıtlı observer'ları bilgilendirir
     */
    void notifyObservers();
}
