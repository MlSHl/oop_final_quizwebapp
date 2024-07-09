import junit.framework.TestCase;
import org.example.quizwebapp.CustomExceptions.RequestAlreadyExists;
import org.example.quizwebapp.CustomExceptions.RequestDoesntExist;
import org.example.quizwebapp.CustomExceptions.UserAlreadyExistsException;
import org.example.quizwebapp.CustomExceptions.UserNotFoundException;
import org.example.quizwebapp.Model.User;
import org.example.quizwebapp.DAO.UserDAO;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class UserDAOTests extends TestCase {
    public void test1() throws SQLException, NoSuchAlgorithmException, UserAlreadyExistsException, ClassNotFoundException, UserNotFoundException {
        User user1 = new User("useri", "password");
        UserDAO userDAO = new UserDAO();

        userDAO.addUser(user1);
        assertEquals(0, userDAO.getPointAmount("useri"));
        assert(userDAO.getUserCompletedQuizzes(3, "useri").isEmpty());
        assert(userDAO.getRecentFriendActivities( "useri").isEmpty());
        assert(userDAO.getAchievements( "useri").isEmpty());
        assertEquals(user1, userDAO.getUser(user1));
        assertEquals(user1, userDAO.getUserByUsername("useri"));
    }

    public void test2() throws SQLException, ClassNotFoundException, RequestAlreadyExists {
        User user1 = new User("user1", "password");
        User user2 = new User("user2", "paroli");
        UserDAO userDAO = new UserDAO();
        userDAO.requestFriendship(user1, user2);
        assertTrue(userDAO.checkIfFriendRequestExists(user1, user2));
    }

    public void test3() throws SQLException, ClassNotFoundException, RequestDoesntExist, RequestAlreadyExists {
        User user3 = new User("user3", "password");
        User user4 = new User("user4", "paroli");
        UserDAO userDAO = new UserDAO();

        userDAO.requestFriendship(user3, user4);
        userDAO.acceptFriendship(user4, user3);
        assertFalse(userDAO.checkIfFriendRequestExists(user4, user3));
        assert(!userDAO.getFriendList("user4").isEmpty());
        assertEquals(user3, userDAO.getFriendList("user4").get(0));
    }
}
