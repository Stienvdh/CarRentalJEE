/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.util.*;
import javax.ejb.Remote;
import rental.CarType;

/**
 *
 * @author Stien
 */
@Remote
public interface ManagerSessionRemote {

    List<CarType> getCarTypes(String rentalCompany);
    Map<CarType, Integer> getNumberReservationCarType(String rentalCompany);
    String getBestRenter(String rentalCompany);
    public int getNumberReservationsBy(String clientName);
}
