import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        List<String> users = DBManager.getInstance().getAllUsers();
        for (String username : users ) {
            System.out.println(username);
        }
    }

}
