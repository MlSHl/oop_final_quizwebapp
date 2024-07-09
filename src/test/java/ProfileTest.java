import junit.framework.TestCase;
import org.example.quizwebapp.CustomExceptions.UserAlreadyExistsException;
import org.example.quizwebapp.CustomExceptions.UserNotFoundException;
import org.example.quizwebapp.Model.*;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;

public class ProfileTest extends TestCase
{

    public void test1() throws SQLException, NoSuchAlgorithmException, UserAlreadyExistsException, ClassNotFoundException, UserNotFoundException {
        List<Achievement> achievements = new ArrayList<>();
        List<Quiz> createdQuizzes = new ArrayList<>();
        List<User> friendsList = new ArrayList<>();
        Profile profile = new Profile("username",achievements,createdQuizzes, 4,friendsList );

        assert(profile.getUsername().equals("username"));
        profile.setUsername("newUserName");
        assert(profile.getUsername().equals("newUserName"));
    }
    public void test2() throws SQLException, NoSuchAlgorithmException, UserAlreadyExistsException, ClassNotFoundException, UserNotFoundException {
        List<Achievement> achievements = new ArrayList<>();
        List<Quiz> createdQuizzes = new ArrayList<>();
        List<User> friendsList = new ArrayList<>();
        Profile profile = new Profile("username",achievements,createdQuizzes, 4,friendsList );

        assert(profile.getUsername().equals("username"));
        profile.setUsername("newUserName");
        assert(profile.getUsername().equals("newUserName"));

        assert (profile.getAchievements().equals(achievements));
        assert (profile.getCreatedQuizzes().equals(createdQuizzes));
        assert (profile.getFriendsList().equals(friendsList));
    }

    public void test3() throws SQLException, NoSuchAlgorithmException, UserAlreadyExistsException, ClassNotFoundException, UserNotFoundException {
        List<Achievement> achievements = new ArrayList<>();
        List<Quiz> createdQuizzes = new ArrayList<>();
        List<User> friendsList = new ArrayList<>();
        Profile profile = new Profile("",achievements,createdQuizzes, 4,friendsList );

        assert(profile.getUsername().equals(""));
        profile.setUsername("newUserName");
        assert(profile.getUsername().equals("newUserName"));

        assert (profile.getAchievements().equals(achievements));
        assert (profile.getCreatedQuizzes().equals(createdQuizzes));
        assert (profile.getFriendsList().equals(friendsList));
    }

    public void test4() throws SQLException, NoSuchAlgorithmException, UserAlreadyExistsException, ClassNotFoundException, UserNotFoundException {
        List<Achievement> achievements = new ArrayList<>();
        List<Quiz> createdQuizzes = new ArrayList<>();
        List<User> friendsList = new ArrayList<>();
        Profile profile = new Profile("",achievements,createdQuizzes, 4,friendsList );

        assert(profile.getUsername().equals(""));
        profile.setUsername("");
        assert(profile.getUsername().equals(""));

        assert (profile.getAchievements().equals(achievements));
        assert (profile.getCreatedQuizzes().equals(createdQuizzes));
        assert (profile.getFriendsList().equals(friendsList));
    }
}
