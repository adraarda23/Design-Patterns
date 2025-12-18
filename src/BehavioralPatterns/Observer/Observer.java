package BehavioralPatterns.Observer;

/**
 * Observer Interface
 * Subject'ten gelen güncellemeleri almak için tüm observer'ların implement etmesi gereken interface
 */
public interface Observer {
    /**
     * Subject'in durumu değiştiğinde çağrılır
     * @param temperature Sıcaklık değeri
     * @param humidity Nem değeri
     * @param pressure Basınç değeri
     */
    void update(float temperature, float humidity, float pressure);
}
