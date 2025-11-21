package Structural.Decorator;

/**
 * Component Interface
 * Tüm kahve türlerinin uyması gereken temel arayüz.
 * Hem concrete component hem de decorator'lar bu arayüzü implement eder.
 */
public interface Coffee {

    /**
     * Kahvenin açıklamasını döndürür
     */
    String getDescription();

    /**
     * Kahvenin fiyatını döndürür
     */
    double getCost();
}
