import exceptions.TrainAttachmentException;
import models.*;

import java.util.Locale;

public class TrainsMain {

    public static void main(String[] args) throws TrainAttachmentException {
        Locale.setDefault(Locale.ENGLISH);
        System.out.println("\n");
//        System.out.println("Welcome to the HvA trains configurator");
//
//        Locomotive rembrandt = new Locomotive(24531, 7);
//        Train amsterdamParis = new Train(rembrandt, "Amsterdam", "Paris");
//
//        amsterdamParis.attachToRear(new PassengerWagon(8001,32));
//        amsterdamParis.attachToRear(new PassengerWagon(8002,32));
//        amsterdamParis.attachToRear(new PassengerWagon(8003,18));
//        amsterdamParis.attachToRear(new PassengerWagon(8004,18));
//
//
////        System.out.println(amsterdamParis.getFirstWagon().getPreviousWagon().getId());
//        System.out.println("n/o wagons : " + amsterdamParis.getNumberOfWagons());
//        System.out.println(amsterdamParis);

        Wagon passengerWagon1 , passengerWagon2, passengerWagon3, passengerWagon4, passengerWagon5, passengerWagon6, passengerWagon7,passengerWagon8;

        passengerWagon1 = new PassengerWagon(8001, 36);
        passengerWagon2 = new PassengerWagon(8002, 18);
        passengerWagon3 = new PassengerWagon(8003, 18);
        passengerWagon4 = new PassengerWagon(8004, 18);
        passengerWagon5 = new PassengerWagon(8005, 18);
        passengerWagon6 = new PassengerWagon(8006, 18);
        passengerWagon7 = new PassengerWagon(8007, 18);
        passengerWagon8 = new PassengerWagon(8008, 18);

        passengerWagon1.attachTail(passengerWagon2);
        passengerWagon2.attachTail(passengerWagon3);
        passengerWagon3.attachTail(passengerWagon4);
//
//
        Wagon rev = passengerWagon1.reverseSequence();
        System.out.println(rev);
//        passengerWagon6.attachTail(passengerWagon7);
//        passengerWagon7.attachTail(passengerWagon8);

//
//        Locomotive rembrandt = new Locomotive(24531, 7);
//        Locomotive locomotive = new Locomotive(54333, 7);
//        Train amsterdamParis = new Train(rembrandt, "Amsterdam", "Paris");
//
//        Train berlinParis = new Train(locomotive, "Berlin", "Paris");
//
//        amsterdamParis.attachToRear(passengerWagon1);
//        amsterdamParis.attachToRear(passengerWagon5);
//        amsterdamParis.attachToRear(passengerWagon6);
//        amsterdamParis.attachToRear(passengerWagon7);
//
//
////        System.out.println(amsterdamParis + "\n");
////        System.out.println(berlinParis+ "\n");
////
//////        amsterdamParis.splitAtPosition(0, berlinParis);
////
////        System.out.println("SPLIT \n" );
////
////        System.out.println(amsterdamParis + "\n");
////        System.out.println(berlinParis+ "\n");
//
//        System.out.println( amsterdamParis);
//        amsterdamParis.reverse();
//
//        System.out.println(amsterdamParis);




//        System.out.println(passengerWagon1.reverseSequence());

//        amsterdamParis.attachToRear(passengerWagon1);

//        System.out.println(amsterdamParis);

//        System.out.println(amsterdamParis);
//
////        passengerWagon1.removeFromSequence();
//
//        System.out.println(amsterdamParis);

//        System.out.println(passengerWagon1);
//        System.out.println(passengerWagon2);
//        System.out.println("\nConfigurator result:");

//        amsterdamParis.attachToRear(passengerWagon2);
//
//        System.out.println(amsterdamParis);
//
//        System.out.println(passengerWagon2);
//
//        Locomotive vanGogh = new Locomotive(63427, 6);
//        Train amsterdamLondon = new Train(vanGogh, "Amsterdam", "London");
//        amsterdamParis.splitAtPosition(4, amsterdamLondon);
//        amsterdamLondon.reverse();
//        amsterdamLondon.insertAtFront((FreightWagon)(Object)new FreightWagon(9001, 50000));
//        amsterdamParis.reverse();
//        amsterdamParis.splitAtPosition(1, amsterdamLondon);
//        amsterdamParis.attachToRear(amsterdamLondon.getLastWagonAttached());
//        amsterdamLondon.moveOneWagon(8003, amsterdamParis);
//
//        System.out.println(amsterdamParis);
//        System.out.println("Total number of seats: " + amsterdamParis.getTotalNumberOfSeats());
//        System.out.println(amsterdamLondon);
//        System.out.println("Total number of seats: " + amsterdamLondon.getTotalNumberOfSeats());
    }
}
