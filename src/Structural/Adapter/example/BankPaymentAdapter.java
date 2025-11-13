package Structural.Adapter.example;

/**
 * ADAPTER ⭐
 *
 * BankAPI'yi (eski sistem) PaymentService interface'ine (yeni sistem) adapte eder.
 *
 * GÖREV:
 * 1. PaymentService interface'ini implement et (Client'ın beklediği)
 * 2. BankAPI referansını tut (Adaptee - gerçek işi yapan)
 * 3. pay() çağrısını transferMoney() çağrısına çevir
 */
public class BankPaymentAdapter implements PaymentService {

    // Adaptee referansı - Composition kullanıyoruz
    private BankAPI bankAPI;
    private String accountNumber;

    /**
     * Constructor
     * @param bankAPI Banka API'si
     * @param accountNumber Ödeme yapılacak hesap numarası
     */
    public BankPaymentAdapter(BankAPI bankAPI, String accountNumber) {
        this.bankAPI = bankAPI;
        this.accountNumber = accountNumber;
    }

    /**
     * Target interface'in metodu
     * Bu metodda DÖNÜŞÜM gerçekleşir!
     *
     * pay(amount) → transferMoney(amount, accountNumber)
     */
    @Override
    public void pay(double amount) {
        // Adaptee'nin metodunu çağır
        // Eksik parametreyi (accountNumber) adapter ekliyor
        bankAPI.transferMoney(amount, accountNumber);
    }
}
