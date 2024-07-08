package org.example.quizwebapp.DAO;

import org.example.quizwebapp.Model.Quiz;
import org.example.quizwebapp.Model.User;
import java.util.List;

public class QuizDAO {

    public void createQuiz(User creator, Quiz quiz) {
        // Implementation
        // Adds a quiz created by the user to the database
    }

    public List<Quiz> getPopularQuizzes(int numQUizzes){
        return null;
        //returns top numQUizzes quizzes by number of users
    }

    public List<Quiz> getRecentQuizzes(int numQUizzes){
        return null;
        //return most recent numQUizzes quizzes
    }

}