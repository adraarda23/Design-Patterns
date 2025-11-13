package Structural.Adapter.example;

/**
 * TARGET INTERFACE
 *
 * Senin modern e-ticaret uygulamanın kullandığı ödeme interface'i.
 * Tüm ödeme sağlayıcılar bu interface'i implement etmeli.
 */
public interface PaymentService {
    /**
     * Ödeme yapar
     * @param amount Ödenecek tutar
     */
    void pay(double amount);
}
