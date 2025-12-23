package BehavioralPatterns.Visitor.DocumentExport;

// ============================================
// ELEMENT INTERFACE
// ============================================
/**
 * DocumentElement: Döküman elementi arayüzü
 * Tüm döküman elementleri visitor'ı kabul etmelidir
 */
interface DocumentElement {
    void accept(ExportVisitor visitor);
}

// ============================================
// CONCRETE ELEMENTS
// ============================================

/**
 * Paragraph: Paragraf elementi
 */
class Paragraph implements DocumentElement {
    private String text;

    public Paragraph(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public void accept(ExportVisitor visitor) {
        visitor.visitParagraph(this);
    }
}

/**
 * Image: Resim elementi
 */
class Image implements DocumentElement {
    private String url;
    private String altText;

    public Image(String url, String altText) {
        this.url = url;
        this.altText = altText;
    }

    public String getUrl() {
        return url;
    }

    public String getAltText() {
        return altText;
    }

    @Override
    public void accept(ExportVisitor visitor) {
        visitor.visitImage(this);
    }
}

/**
 * Table: Tablo elementi
 */
class Table implements DocumentElement {
    private String[][] data;

    public Table(String[][] data) {
        this.data = data;
    }

    public String[][] getData() {
        return data;
    }

    @Override
    public void accept(ExportVisitor visitor) {
        visitor.visitTable(this);
    }
}

// ============================================
// VISITOR INTERFACE
// ============================================
/**
 * ExportVisitor: Export işlemleri için visitor arayüzü
 * Her element tipi için visit metodu
 */
interface ExportVisitor {
    void visitParagraph(Paragraph paragraph);
    void visitImage(Image image);
    void visitTable(Table table);
}

// ============================================
// CONCRETE VISITORS (EXPORT FORMATLARI)
// ============================================

/**
 * HTMLExporter: HTML formatına export
 * Her elementi HTML etiketlerine çevirir
 */
class HTMLExporter implements ExportVisitor {
    private StringBuilder output = new StringBuilder();

    @Override
    public void visitParagraph(Paragraph paragraph) {
        String html = "<p>" + paragraph.getText() + "</p>";
        output.append(html).append("\n");
        System.out.println(html);
    }

    @Override
    public void visitImage(Image image) {
        String html = "<img src=\"" + image.getUrl() + "\" alt=\"" + image.getAltText() + "\" />";
        output.append(html).append("\n");
        System.out.println(html);
    }

    @Override
    public void visitTable(Table table) {
        StringBuilder tableHtml = new StringBuilder("<table>\n");

        for (String[] row : table.getData()) {
            tableHtml.append("  <tr>\n");
            for (String cell : row) {
                tableHtml.append("    <td>").append(cell).append("</td>\n");
            }
            tableHtml.append("  </tr>\n");
        }
        tableHtml.append("</table>");

        output.append(tableHtml).append("\n");
        System.out.println(tableHtml);
    }

    public String getOutput() {
        return output.toString();
    }
}

/**
 * PDFExporter: PDF formatına export (simülasyon)
 * Her elementi PDF formatına uygun şekilde işler
 */
class PDFExporter implements ExportVisitor {
    private StringBuilder output = new StringBuilder();

    @Override
    public void visitParagraph(Paragraph paragraph) {
        String pdf = "PDF_TEXT: " + paragraph.getText();
        output.append(pdf).append("\n");
        System.out.println(pdf);
    }

    @Override
    public void visitImage(Image image) {
        String pdf = "PDF_IMAGE: [Resim: " + image.getAltText() + " - " + image.getUrl() + "]";
        output.append(pdf).append("\n");
        System.out.println(pdf);
    }

    @Override
    public void visitTable(Table table) {
        StringBuilder tablePdf = new StringBuilder("PDF_TABLE:\n");

        for (int i = 0; i < table.getData().length; i++) {
            tablePdf.append("  Satır ").append(i + 1).append(": ");
            for (String cell : table.getData()[i]) {
                tablePdf.append("[").append(cell).append("] ");
            }
            tablePdf.append("\n");
        }

        output.append(tablePdf).append("\n");
        System.out.println(tablePdf.toString().trim());
    }

    public String getOutput() {
        return output.toString();
    }
}

/**
 * PlainTextExporter: Düz metin formatına export
 * Her elementi basit metin olarak işler
 */
class PlainTextExporter implements ExportVisitor {
    private StringBuilder output = new StringBuilder();

    @Override
    public void visitParagraph(Paragraph paragraph) {
        String text = paragraph.getText();
        output.append(text).append("\n\n");
        System.out.println(text + "\n");
    }

    @Override
    public void visitImage(Image image) {
        String text = "[Resim: " + image.getAltText() + "]";
        output.append(text).append("\n\n");
        System.out.println(text + "\n");
    }

    @Override
    public void visitTable(Table table) {
        StringBuilder tableText = new StringBuilder();

        for (String[] row : table.getData()) {
            for (String cell : row) {
                tableText.append(cell).append("\t");
            }
            tableText.append("\n");
        }

        output.append(tableText).append("\n");
        System.out.println(tableText);
    }

    public String getOutput() {
        return output.toString();
    }
}

// ============================================
// DEMO - DOCUMENT EXPORT VISITOR
// ============================================
/**
 * AMAÇ: Farklı formatlara export işlemi
 *
 * SENARYO: Döküman editörü
 * - Farklı element tipleri (Paragraph, Image, Table)
 * - Farklı export formatları (HTML, PDF, PlainText)
 *
 * VISITOR OLMADAN:
 * class Paragraph {
 *     String toHTML() { return "<p>" + text + "</p>"; }
 *     String toPDF() { return "PDF_TEXT: " + text; }
 *     String toPlainText() { return text; }
 * }
 * // Aynı metodlar Image ve Table için de
 * // Her yeni format için tüm elementlere metod eklemek gerekir!
 *
 * VISITOR İLE:
 * class Paragraph {
 *     void accept(ExportVisitor v) { v.visitParagraph(this); }
 * }
 * // Yeni format? → Yeni ExportVisitor (elementlere dokunma)
 *
 * FAYDA:
 * - Element sınıfları basit (sadece veri + accept)
 * - Export mantığı ayrı visitor'larda
 * - Yeni format eklemek kolay
 * - Her visitor kendi formatını bilir
 */
public class DocumentExportVisitorDemo {
    public static void main(String[] args) {
        System.out.println("🎯 VISITOR PATTERN - DÖKÜMAN EXPORT ÖRNEĞİ\n");

        // Döküman elementlerini oluştur
        DocumentElement[] document = {
            new Paragraph("Visitor Pattern, nesneler arasındaki işlemleri ayırmanızı sağlar."),
            new Image("visitor-diagram.png", "Visitor Pattern Diyagramı"),
            new Paragraph("Bu pattern, Double Dispatch prensibini kullanır."),
            new Table(new String[][] {
                {"Pattern", "Tip", "Amaç"},
                {"Visitor", "Behavioral", "İşlemleri ayırma"},
                {"Strategy", "Behavioral", "Algoritma değiştirme"}
            })
        };

        // Export 1: HTML
        System.out.println("=".repeat(60));
        System.out.println("📍 EXPORT 1: HTML");
        System.out.println("=".repeat(60));
        HTMLExporter htmlExporter = new HTMLExporter();
        for (DocumentElement element : document) {
            element.accept(htmlExporter);
        }

        // Export 2: PDF
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📍 EXPORT 2: PDF");
        System.out.println("=".repeat(60));
        PDFExporter pdfExporter = new PDFExporter();
        for (DocumentElement element : document) {
            element.accept(pdfExporter);
        }

        // Export 3: Plain Text
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📍 EXPORT 3: PLAIN TEXT");
        System.out.println("=".repeat(60));
        PlainTextExporter textExporter = new PlainTextExporter();
        for (DocumentElement element : document) {
            element.accept(textExporter);
        }

        // Açıklama
        System.out.println("=".repeat(60));
        System.out.println("💡 VISITOR PATTERN'IN FAYDASI:");
        System.out.println("=".repeat(60));
        System.out.println("✅ Element sınıfları basit (sadece veri + accept)");
        System.out.println("✅ Her export formatı ayrı visitor");
        System.out.println("✅ Yeni format eklemek kolay:");
        System.out.println("   → Yeni Visitor yaz (elementlere dokunma)");
        System.out.println("✅ Separation of Concerns (Veri vs İşlem)");
        System.out.println("=".repeat(60));
    }
}
