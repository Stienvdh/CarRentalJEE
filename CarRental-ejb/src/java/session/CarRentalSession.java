package session;

import java.util.*;
import javax.ejb.Stateful;
import rental.*;
import rental.RentalStore;

@Stateful
public class CarRentalSession implements CarRentalSessionRemote {
    
    private List<Quote> quotes = new ArrayList<Quote>();

    @Override
    public Set<String> getAllRentalCompanies() {
        return new HashSet<String>(RentalStore.getRentals().keySet());
    }
    
    public List<Quote> getCurrentQuotes() {
        return this.quotes;
    }

    public Quote createQuote(ReservationConstraints constraints, String client) throws ReservationException {
        for (String company: getAllRentalCompanies()) {
            try {
                CarRentalCompany rental = RentalStore.getRental(company);
                Quote quote = rental.createQuote(constraints, client);
                this.getCurrentQuotes().add(quote);
                return quote;
            }
            catch (ReservationException ex) {
                
            }
        }
        throw new ReservationException("No car for given contraints available.");
    }
    
    public List<Reservation> confirmQuotes() throws ReservationException {
        List<Reservation> confirmedReservations = new ArrayList<Reservation>();
        for (Quote quote: getCurrentQuotes()) {
            CarRentalCompany company = RentalStore.getRental(quote.getRentalCompany());
            try {
                Reservation reservation = company.confirmQuote(quote);
                confirmedReservations.add(reservation);
            }
            catch (ReservationException ex) {
                for (Reservation res: confirmedReservations) {
                    CarRentalCompany comp = RentalStore.getRental(res.getRentalCompany());
                    comp.cancelReservation(res);
                }
                throw new ReservationException("Cannot confirm all reservations.");
            }
        }
        return confirmedReservations;
    }
    
    public Set<CarType> getAvailableCarTypes(Date start, Date end) {
        Set<CarType> available = new HashSet<CarType>();
        Map<String, CarRentalCompany> rentals = RentalStore.getRentals();
        for (String company : rentals.keySet()) {
            for (Car car : RentalStore.getRental(company).getCars()) {
                if (car.isAvailable(start, end)) {
                    available.add(car.getType());
                }
            }
        }
        return available;
    }
    
}
