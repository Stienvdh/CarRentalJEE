package session;

import java.util.*;
import javax.ejb.Stateless;
import rental.*;

@Stateless
public class ManagerSession implements ManagerSessionRemote {

    public List<CarType> getCarTypes(String rentalCompany) {
        CarRentalCompany company = RentalStore.getRental(rentalCompany);
        return new ArrayList<CarType>(company.getCarTypes());
    }
    
    public Map<CarType, Integer> getNumberReservationCarType(String rentalCompany) {
        Map<CarType, Integer> result = new HashMap<CarType, Integer>();
        for (CarType type: getCarTypes(rentalCompany)) {
            result.put(type, 0);
        }
        CarRentalCompany company = RentalStore.getRental(rentalCompany);
        for (Car car: company.getCars()) {
            for (Reservation res: car.getAllReservations()) {
                int value = result.get(car.getType());
                result.put(car.getType(), value+1);
            }
        }
        return result;
    }
    
    public String getBestRenter(String rentalCompany) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        CarRentalCompany company = RentalStore.getRental(rentalCompany);
        for (Car car: company.getCars()) {
            for (Reservation res: car.getAllReservations()) {
                result.put(res.getCarRenter(),company.getReservationsBy(res.getCarRenter()).size());
            }
        }
        Integer max = Collections.max(result.values());
        for (String renter: result.keySet()) {
            if (result.get(renter).equals(max)) {
                return renter;
            }
        }
        return null;
    }
    
    // in main van de client hebben we carType als string meegegeven dus misschien
    // is het beter om het in de map ook als string te zetten
    public Map<String,Integer> getNbReservationCarType(String carRentalCompany) {
        Map<String, Integer> result = new HashMap<String,Integer>();
        CarRentalCompany company = RentalStore.getRental(carRentalCompany);
        for (CarType carType : getCarTypes(carRentalCompany)) {
            result.put(carType.toString(), 0);
        }
        for (Car car : company.getCars()) {
            for (Reservation res : car.getAllReservations()){
                
            }
        }
        return result;
    }
    
    // misschien niet de meest elegante manier om dit te implementeren met al de 
    // geneste lussen maar ik denk dat het hier wel ongeveer op neer komt
    public int getNumberReservationsBy(String clientName) {
        int counter = 0;
        Map<String, CarRentalCompany> rentals = RentalStore.getRentals();
        for (String company : rentals.keySet()) {
            for (Car car : RentalStore.getRental(company).getCars()) {
                for (Reservation reservation : car.getAllReservations()) {
                    if (reservation.getCarRenter().equals(clientName)) {
                        counter += 1;
                    }
                }
            }
        }
        return counter;
    }

}
