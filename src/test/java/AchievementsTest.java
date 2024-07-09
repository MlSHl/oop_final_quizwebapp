import junit.framework.TestCase;
import org.example.quizwebapp.CustomExceptions.UserAlreadyExistsException;
import org.example.quizwebapp.CustomExceptions.UserNotFoundException;
import org.example.quizwebapp.Model.Achievement;
import org.example.quizwebapp.Model.Profile;
import org.example.quizwebapp.Model.Quiz;
import org.example.quizwebapp.Model.User;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AchievementsTest extends TestCase {

    public void test1() throws SQLException, NoSuchAlgorithmException, UserAlreadyExistsException, ClassNotFoundException, UserNotFoundException {
       Achievement ach = new Achievement("name", "description", "img");
       ach.setName("name");
       assert (ach.getName().equals("name"));
       ach.setDescription("newDesc");
       assert (ach.getDescription().equals("newDesc"));
       ach.setImg("hyperLink");
       assert (ach.getImg().equals("hyperLink"));
    }
    public void test2() throws SQLException, NoSuchAlgorithmException, UserAlreadyExistsException, ClassNotFoundException, UserNotFoundException {
        Achievement ach = new Achievement("name", "description", "img");
        ach.setName("");
        ach.setDescription("");
        ach.setImg("");
        ach.setDescription("");
        assert (ach.getName().equals(""));
        assert (ach.getDescription().equals(""));
        assert (ach.getImg().equals(""));
        assert (ach.getName().equals(""));
    }

}
