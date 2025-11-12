package AbstractFactory;

public interface IHotelFactory {
    IPayment createPayment();
    IReservation createReservation();
}
