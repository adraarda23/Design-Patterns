package Structural.Proxy;

// Ortak arayüz - Hem gerçek nesne hem de proxy bu arayüzü uygular
public interface Image {
    void display();
    void rotate();
    void resize(int width, int height);
}
