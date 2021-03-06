package learn.DontWreckMyHouse.data;
import learn.DontWreckMyHouse.models.Guest;
import learn.DontWreckMyHouse.models.Host;

import java.util.List;

public interface HostRepository {

    List<Host> findAll() throws DataException;

    Host findHostByEmail(String hostEmail) throws DataException;

    Host findHostById(String hostId) throws DataException;



}
