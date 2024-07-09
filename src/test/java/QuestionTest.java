import junit.framework.TestCase;
import org.example.quizwebapp.CustomExceptions.UserAlreadyExistsException;
import org.example.quizwebapp.CustomExceptions.UserNotFoundException;
import org.example.quizwebapp.Model.*;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionTest extends TestCase {
    public void test1() throws SQLException, NoSuchAlgorithmException, UserAlreadyExistsException, ClassNotFoundException, UserNotFoundException {
        Question question = new Question("question");
        List <String> answer = new ArrayList<>();
        question.addAnswer("answer", true);
        question.setQuestionId(0);
        assertEquals (question.getQuestionId(),0);

    }
    public void test2() throws SQLException, NoSuchAlgorithmException, UserAlreadyExistsException, ClassNotFoundException, UserNotFoundException {
        Question question = new Question("question");
        List <String> answer = new ArrayList<>();
        question.addAnswer("answer", true);
        question.setQuestionId(0);
        assertEquals (question.getQuestionId(),0);
        question.setQuestionId(10);
        assertEquals (question.getQuestionId(),10);
    }

    public void test3() throws SQLException, NoSuchAlgorithmException, UserAlreadyExistsException, ClassNotFoundException, UserNotFoundException {
        Question question = new Question("question");
        List <String> answer = new ArrayList<>();
        question.addAnswer("", false);
        question.setQuestionId(0);
        assertEquals (question.getQuestionId(),0);
        question.setQuestionId(10);
        assertEquals (question.getQuestionId(),10);
    }

}
