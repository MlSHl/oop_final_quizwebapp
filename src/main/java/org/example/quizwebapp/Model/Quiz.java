package org.example.quizwebapp.Model;
import org.example.quizwebapp.Model.Question;

import java.util.List;

public class Quiz {

    private Integer id;
    private String title;
    private String desc;
    private List<Question> questions;

    public Quiz() {

    }

    public Quiz(String title, String desc, List<Question> questions) {
       this.title = title;
       this.desc = desc;
       this.questions = questions;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
