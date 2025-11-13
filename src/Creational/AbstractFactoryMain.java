package Creational;

import Creational.AbstractFactory.EkonomikHotelFactory;
import Creational.AbstractFactory.IHotelFactory;
import Creational.AbstractFactory.LuksHotelFactory;


public class AbstractFactoryMain {
    public static void main(String[] args) {

        String HOTEL_NAME = "A";


        if(HOTEL_NAME.equals("A")){
            IHotelFactory LuksHotel = new LuksHotelFactory();
            LuksHotel.createPayment().payment();
            LuksHotel.createReservation().reservation();
        }
        else{
            IHotelFactory EkonomikHotel = new EkonomikHotelFactory();
            EkonomikHotel.createPayment().payment();
            EkonomikHotel.createReservation().reservation();
        }


    }
}
