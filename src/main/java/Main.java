import Dao.DeveloperDAO;
import Model.Developer;

/**
 * Created by Андрей on 26.06.2017.
 */
public class Main {
    public static void main(String[] args) {
        DeveloperDAO developerDAO = new DeveloperDAO();
        developerDAO.create(new Developer(1100,"testname",null));
    }
}
