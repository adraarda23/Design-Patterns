package Structural.Adapter.example;

/**
 * ADAPTEE (3. Parti Sistem)
 *
 * Bankanın eski API'si. Sen bu kodu yazmadın ve değiştiremezsin!
 * Farklı metod ismi kullanıyor: transferMoney()
 * Ek parametre bekliyor: accountNumber
 */
public class BankAPI {

    private String bankName;

    public BankAPI(String bankName) {
        this.bankName = bankName;
    }

    /**
     * Bankanın para transfer metodu (eski API)
     * @param amount Transfer edilecek tutar
     * @param accountNumber Hesap numarası
     */
    public void transferMoney(double amount, String accountNumber) {
        System.out.println("[" + bankName + " Bank API] " +
                           amount + " TL transfer edildi (Hesap: " + accountNumber + ")");
    }
}
