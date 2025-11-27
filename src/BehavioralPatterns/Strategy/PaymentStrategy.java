package BehavioralPatterns.Strategy;

/**
 * STRATEGY INTERFACE (Strateji Arayüzü)
 *
 * Bu interface, tüm ödeme stratejilerinin uyması gereken kuralları tanımlar.
 * Her ödeme yöntemi bu interface'i implement ederek kendi ödeme mantığını yazmak zorundadır.
 *
 * Strategy Pattern'in temelidir - tüm stratejiler aynı "kontrat"ı takip eder.
 */
public interface PaymentStrategy {

    /**
     * Ödeme işlemini gerçekleştirir
     *
     * @param amount Ödenecek tutar
     * @return Ödeme başarılı mı? (true/false)
     */
    boolean pay(double amount);

    /**
     * Ödeme yönteminin ismini döndürür
     *
     * @return Ödeme yöntemi adı
     */
    String getPaymentMethodName();
}
