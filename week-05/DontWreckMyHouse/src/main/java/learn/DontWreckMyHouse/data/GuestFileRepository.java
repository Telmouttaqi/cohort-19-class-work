package learn.DontWreckMyHouse.data;
import learn.DontWreckMyHouse.models.Guest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Repository
public class GuestFileRepository implements GuestRepository{

    private final String filePath;

    public GuestFileRepository(@Value("${guestsFile}")String filePath) { this.filePath = filePath; }

    public List<Guest> findAll() throws DataException {
        ArrayList<Guest> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // read header
            for (String line = reader.readLine(); line != null; line = reader.readLine()) { // read each line
                String[] fields = line.split(",", -1);
                if (fields.length == 6) {
                    result.add(deserialize(fields));
                }
            }
        }catch (FileNotFoundException ex){
            // do nothing for now.
        }catch (IOException ex){
            throw new DataException(ex.getMessage(), ex);
        }

        return result;
    }



    @Override
    public Guest findGuestByEmail(String guestEmail) throws DataException {
        return  findAll().stream()
                .filter(i -> i.getEmail().equalsIgnoreCase(guestEmail))
                .findFirst()
                .orElse(null);
    }


    @Override
    public Guest findGuestById(int guestId) throws DataException {
        return findAll().stream()
                .filter(i -> i.getGuestId() == guestId)
                .findFirst()
                .orElse(null);
    }




    // guest_id,first_name,last_name,email,phone,state
    private Guest deserialize(String[] fields) {
        Guest result = new Guest();
        result.setGuestId(Integer.parseInt(fields[0]));
        result.setFirstName(fields[1]);
        result.setLastName(fields[2]);
        result.setEmail(fields[3]);
        result.setPhone(fields[4]);
        result.setState(fields[5]);
        return result;
    }

}


