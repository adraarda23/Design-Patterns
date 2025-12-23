package BehavioralPatterns.Mediator.UI;

// ============================================
// MEDIATOR INTERFACE
// ============================================
/**
 * DialogMediator: UI elementleri arası koordinasyon arayüzü
 */
interface DialogMediator {
    void notify(Component sender, String event);
}

// ============================================
// BASE COMPONENT
// ============================================
/**
 * Component: Tüm UI elementlerinin base sınıfı
 * Her component sadece mediator'ı bilir
 */
abstract class Component {
    protected DialogMediator mediator;

    public void setMediator(DialogMediator mediator) {
        this.mediator = mediator;
    }
}

// ============================================
// CONCRETE COMPONENTS
// ============================================

/**
 * CheckBox: Checkbox UI elementi
 */
class CheckBox extends Component {
    private boolean checked = false;
    private final String label;

    public CheckBox(String label) {
        this.label = label;
    }

    public void toggle() {
        checked = !checked;
        System.out.println("☑️  " + label + ": " + (checked ? "✓ İşaretlendi" : "☐ İşaretsiz"));
        mediator.notify(this, "check");
    }

    public boolean isChecked() {
        return checked;
    }

    public String getLabel() {
        return label;
    }
}

/**
 * TextBox: Metin girişi
 */
class TextBox extends Component {
    private String text = "";
    private final String label;
    private boolean enabled = true;

    public TextBox(String label) {
        this.label = label;
    }

    public void setText(String text) {
        if (!enabled) {
            System.out.println("❌ " + label + " devre dışı!");
            return;
        }
        this.text = text;
        System.out.println("✏️  " + label + ": \"" + text + "\"");
        mediator.notify(this, "textChanged");
    }

    public String getText() {
        return text;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        System.out.println("   " + label + " " + (enabled ? "✅ aktif" : "🔒 pasif"));
    }

    public boolean isEmpty() {
        return text.isEmpty();
    }
}

/**
 * Button: Tıklanabilir buton
 */
class Button extends Component {
    private final String label;
    private boolean enabled = false;

    public Button(String label) {
        this.label = label;
    }

    public void click() {
        if (!enabled) {
            System.out.println("❌ " + label + " butonu devre dışı!");
            return;
        }
        System.out.println("🔘 " + label + " tıklandı!");
        mediator.notify(this, "click");
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        System.out.println("   " + label + " butonu " + (enabled ? "✅ aktif" : "🔒 pasif"));
    }
}

/**
 * Label: Bilgi gösterme
 */
class Label extends Component {
    private String text;
    private final String name;

    public Label(String name, String initialText) {
        this.name = name;
        this.text = initialText;
    }

    public void setText(String text) {
        this.text = text;
        System.out.println("📄 " + name + ": " + text);
    }

    public String getText() {
        return text;
    }
}

// ============================================
// CONCRETE MEDIATOR
// ============================================
/**
 * RegistrationFormMediator: Kayıt formu koordinatörü
 *
 * SENARYO: E-ticaret sitesi kayıt formu
 * - Premium üyelik checkbox'ı
 * - Kredi kartı girişi
 * - Email girişi
 * - Fiyat label'ı
 * - Kayıt butonu
 *
 * KURALLAR:
 * 1. Premium seçilirse → Fiyat 99₺, kredi kartı aktif
 * 2. Premium seçilmezse → Fiyat 0₺, kredi kartı pasif
 * 3. Email ve (premium ise kart) dolu olmalı → Buton aktif
 */
class RegistrationFormMediator implements DialogMediator {
    private CheckBox premiumCheckBox;
    private TextBox emailTextBox;
    private TextBox creditCardTextBox;
    private Button submitButton;
    private Label priceLabel;

    public RegistrationFormMediator(
            CheckBox premium,
            TextBox email,
            TextBox creditCard,
            Button submit,
            Label price
    ) {
        this.premiumCheckBox = premium;
        this.emailTextBox = email;
        this.creditCardTextBox = creditCard;
        this.submitButton = submit;
        this.priceLabel = price;

        // Tüm componentlere mediator'ı bildir
        premium.setMediator(this);
        email.setMediator(this);
        creditCard.setMediator(this);
        submit.setMediator(this);
        price.setMediator(this);
    }

    @Override
    public void notify(Component sender, String event) {
        // Premium checkbox değişti
        if (sender == premiumCheckBox && event.equals("check")) {
            handlePremiumToggle();
        }

        // Email değişti
        if (sender == emailTextBox && event.equals("textChanged")) {
            validateForm();
        }

        // Kredi kartı değişti
        if (sender == creditCardTextBox && event.equals("textChanged")) {
            validateForm();
        }

        // Submit butonu tıklandı
        if (sender == submitButton && event.equals("click")) {
            handleSubmit();
        }
    }

    private void handlePremiumToggle() {
        if (premiumCheckBox.isChecked()) {
            // Premium seçildi
            priceLabel.setText("Fiyat: 99₺");
            creditCardTextBox.setEnabled(true);
        } else {
            // Premium iptal edildi
            priceLabel.setText("Fiyat: Ücretsiz");
            creditCardTextBox.setEnabled(false);
        }
        validateForm();
    }

    private void validateForm() {
        boolean isValid = !emailTextBox.isEmpty();

        // Premium ise kredi kartı da dolu olmalı
        if (premiumCheckBox.isChecked()) {
            isValid = isValid && !creditCardTextBox.isEmpty();
        }

        submitButton.setEnabled(isValid);
    }

    private void handleSubmit() {
        System.out.println("\n✅ KAYIT BAŞARILI!");
        System.out.println("   Email: " + emailTextBox.getText());
        if (premiumCheckBox.isChecked()) {
            System.out.println("   Premium üyelik aktif");
            System.out.println("   Kart: " + creditCardTextBox.getText());
        }
    }
}

// ============================================
// DEMO - UI FORM MEDİATOR
// ============================================
/**
 * AMAÇ: Form elementleri arası karmaşık bağımlılığı yönetmek
 *
 * MEDIATOR OLMADAN:
 * - CheckBox, TextBox'ı bilmeli
 * - TextBox, Button'ı bilmeli
 * - Button, Label'ı bilmeli
 * → Spaghetti code!
 *
 * MEDIATOR İLE:
 * - Herkes sadece Mediator'ı biliyor
 * - Koordinasyon tek yerde
 * - Kolay test, kolay değişiklik
 */
public class FormMediatorDemo {
    public static void main(String[] args) {
        System.out.println("🎯 MEDİATOR PATTERN - UI FORM ÖRNEĞİ\n");
        System.out.println("📝 E-Ticaret Kayıt Formu\n");

        // 1. UI componentlerini oluştur
        CheckBox premiumCheckBox = new CheckBox("Premium Üyelik");
        TextBox emailTextBox = new TextBox("Email");
        TextBox creditCardTextBox = new TextBox("Kredi Kartı");
        Button submitButton = new Button("Kayıt Ol");
        Label priceLabel = new Label("Fiyat", "Fiyat: Ücretsiz");

        // 2. Mediator'ı oluştur ve componentleri bağla
        RegistrationFormMediator mediator = new RegistrationFormMediator(
                premiumCheckBox,
                emailTextBox,
                creditCardTextBox,
                submitButton,
                priceLabel
        );

        System.out.println("--- İlk Durum ---");
        System.out.println("📄 Fiyat: Ücretsiz");
        System.out.println("   Email ✅ aktif");
        System.out.println("   Kredi Kartı 🔒 pasif");
        System.out.println("   Kayıt Ol butonu 🔒 pasif\n");

        // 3. Kullanıcı etkileşimleri
        System.out.println("--- Adım 1: Email girildi ---");
        emailTextBox.setText("alice@example.com");

        System.out.println("\n--- Adım 2: Premium üyelik seçildi ---");
        premiumCheckBox.toggle();

        System.out.println("\n--- Adım 3: Kredi kartı girildi ---");
        creditCardTextBox.setText("1234-5678-9012-3456");

        System.out.println("\n--- Adım 4: Kayıt butonu tıklandı ---");
        submitButton.click();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("💡 MEDIATOR'IN FAYDASI:");
        System.out.println("   - CheckBox, TextBox'ı bilmiyor");
        System.out.println("   - TextBox, Button'ı bilmiyor");
        System.out.println("   - Tüm koordinasyon Mediator'da!");
        System.out.println("=".repeat(60));
    }
}
