package learn.DontWreckMyHouse.data;

import learn.DontWreckMyHouse.models.Guest;
import learn.DontWreckMyHouse.models.Host;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostFileRepository implements HostRepository {

    public HostFileRepository(String filePath) {
        this.filePath = filePath;
    }

    private final String filePath;

    public List<Host> findAll() {
        ArrayList<Host> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // read header
            for (String line = reader.readLine(); line != null; line = reader.readLine()) { // read each line
                String[] fields = line.split(",", -1);
                if (fields.length == 10) {
                    result.add(deserialize(fields));
                }
            }
        }catch (IOException ex){
            // do nothing for now.
        }
        return result;
    }

    @Override
    public List<Host> findGuestById() {
        return null;
    }

    @Override
    public List<Host> findGuestByEmail() {
        return null;
    }

    // id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate
    private Host deserialize(String[] fields) {
        Host result = new Host();
        result.setHostId(fields[0]);
        result.setLastName(fields[1]);
        result.setEmail(fields[2]);
        result.setPhone(fields[3]);
        result.setAddress(fields[4]);
        result.setCity(fields[5]);
        result.setState(fields[6]);
        result.setPostalCode(fields[7]);
        result.setStandardRate(BigDecimal.valueOf(Double.parseDouble(fields[8])));
        result.setWeekEndRate(BigDecimal.valueOf(Double.parseDouble(fields[9])));
        return result;
    }

}