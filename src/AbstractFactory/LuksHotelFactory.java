package AbstractFactory;

public class LuksHotelFactory implements IHotelFactory {
    @Override
    public IPayment createPayment() {
        return new LuksPayment();
    }

    @Override
    public IReservation createReservation() {
        return new LuksReservation();
    }
}
