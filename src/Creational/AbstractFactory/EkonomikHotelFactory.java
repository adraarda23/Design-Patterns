package Creational.AbstractFactory;

public class EkonomikHotelFactory implements IHotelFactory {
    @Override
    public IPayment createPayment() {
        return new EkonomikPayment();
    }

    @Override
    public IReservation createReservation() {
        return new EkonomikReservation();
    }
}
