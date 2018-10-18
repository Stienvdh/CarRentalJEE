package session;

import java.util.*;
import javax.ejb.Remote;
import rental.CarType;
import rental.Quote;
import rental.ReservationConstraints;
import rental.ReservationException;

@Remote
public interface CarRentalSessionRemote {

    Set<String> getAllRentalCompanies();
    Quote createQuote(ReservationConstraints constraints, String client) throws ReservationException;
    List<Quote> getCurrentQuotes();
    void confirmQuotes() throws ReservationException;
    public List<CarType> getAvailableCarTypes(Date start, Date end);
}
