package client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import rental.CarType;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import session.CarRentalSessionRemote;
import session.ManagerSessionRemote;

public class Main extends AbstractTestAgency<CarRentalSessionRemote,ManagerSessionRemote> {
    
    @EJB
    static CarRentalSessionRemote session;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("found rental companies: "+session.getAllRentalCompanies());
    }

    @Override
    protected Object getNewReservationSession(String name) throws Exception {
        InitialContext context = new InitialContext();
        session = (CarRentalSessionRemote) context.lookup(CarRentalSessionRemote.class.getName());
        return session;
    }

    @Override
    protected Object getNewManagerSession(String name, String carRentalName) throws Exception {
        InitialContext context = new InitialContext();
        return (ManagerSessionRemote) context.lookup(ManagerSessionRemote.class.getName());
    }

    @Override
    public void checkForAvailableCarTypes(CarRentalSessionRemote session, Date start, Date end) throws Exception{
        // nieuwe methode aangemaakt in CarRentalSession
        List<CarType> result = session.getAvailableCarTypes(start, end);
        for (CarType type : result) {
            System.out.println(type.toString());
        }
    }
    
    @Override
    public void addQuoteToSession(CarRentalSessionRemote session, String name, Date start, Date end, String carType, String region) throws Exception {
        ReservationConstraints constraints = new ReservationConstraints(start, end, carType, region);
        session.createQuote(constraints, name);
    }
    
    @Override
    public List<Reservation> confirmQuotes(CarRentalSessionRemote session, String name) throws Exception {
        // ook mogelijk om een lijst met confirmed reservations bij te houden in CarRentalSession
        // of om confirmQuotes een List<Reservation> terug te laten geven
        session.confirmQuotes();
        List<Quote> quotes = session.getCurrentQuotes();
        List<Reservation> reservations = new ArrayList<Reservation>();
        for(Quote quote : quotes) {
            reservations.add((Reservation)quote);
        }
        return reservations;
    }
    
    @Override
    public int getNumberOfReservationsBy(ManagerSessionRemote ms, String clientName) throws Exception {
        // nieuwe methode gemaakt in ManagerSession 
        return ms.getNumberReservationsBy(clientName);
    }
    
    @Override
    public int getNumberOfReservationsForCarType(ManagerSessionRemote ms, String carRentalName, String carType) throws Exception {
        // nieuwe methode met andere map gemaakt in ManagerSession
        Map<String,Integer> reservations = ms.getNbReservationCarType(carRentalName);
        return reservations.get(carType);
    }
}
