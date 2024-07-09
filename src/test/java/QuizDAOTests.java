import junit.framework.TestCase;
import org.example.quizwebapp.CustomExceptions.RequestAlreadyExists;
import org.example.quizwebapp.CustomExceptions.RequestDoesntExist;
import org.example.quizwebapp.CustomExceptions.UserAlreadyExistsException;
import org.example.quizwebapp.CustomExceptions.UserNotFoundException;
import org.example.quizwebapp.DAO.QuizDAO;
import org.example.quizwebapp.Model.Question;
import org.example.quizwebapp.Model.Quiz;
import org.example.quizwebapp.Model.User;
import org.example.quizwebapp.DAO.UserDAO;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class QuizDAOTests extends TestCase {

    public void test1() throws SQLException, NoSuchAlgorithmException, UserAlreadyExistsException, ClassNotFoundException, UserNotFoundException {
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < 10; i ++) {
            Question ques = new Question("gvishvelet");
            questions.add(ques);
        }

        Quiz quiz = new Quiz("title", "desc", questions);
        QuizDAO quizDAO = new QuizDAO();

        User user1 = new User("gurama", "123") ;

        //quizDAO.createQuiz(user1, quiz);

        quiz.setId(0);
        assert(quiz.getId().equals(0));
        assert (quiz.getTitle().equals("title"));
        quiz.setTitle("newTitle");
        assert (quiz.getTitle().equals("newTitle"));
        assert(quiz.getDesc().equals("desc"));

        quiz.setDesc("newdesc");
        assert(quiz.getDesc().equals("newdesc"));
        assert(quiz.getQuestions().equals(questions));
        }

    public void test2() throws SQLException, ClassNotFoundException, RequestAlreadyExists {
        List<Question> questions = new ArrayList<>();
        int numQues=15;
        for (int i = 0; i < numQues; i ++) {
            Question ques = new Question("agar gvishvelot");
            questions.add(ques);
        }

        Quiz quiz = new Quiz("title", "desc", questions);
        QuizDAO quizDAO = new QuizDAO();

        User user1 = new User("gurama", "123") ;


//        int id = quiz.getId();
        quiz.setId(10);
        assert(quiz.getId().equals(10));
        assert (quiz.getTitle().equals("title"));
        quiz.setTitle("");
        quiz.setDesc("");
        assert (quiz.getTitle().equals(""));
        assert(quiz.getDesc().equals(""));


        assert(quiz.getQuestions().equals(questions));

    }

}