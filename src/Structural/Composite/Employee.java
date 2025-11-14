package Structural.Composite;

/**
 * Component: Hem bireysel çalışanlar hem de yöneticiler için ortak interface
 */
public interface Employee {
    /**
     * Çalışan bilgilerini gösterir
     * @param indent Girinti seviyesi (hiyerarşiyi göstermek için)
     */
    void showDetails(String indent);

    /**
     * Çalışanın maaşını döndürür
     * @return Maaş tutarı
     */
    double getSalary();

    /**
     * Çalışanın adını döndürür
     * @return İsim
     */
    String getName();
}
