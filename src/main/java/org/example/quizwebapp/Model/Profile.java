package org.example.quizwebapp.Model;

import java.util.List;

public class Profile {
    private String username;
    private List<Achievement> achievements;
    private List<Quiz> createdQuizzes;
    private int quizzesTaken;
    private List<User> friendsList;
    private boolean ownProfile = false;

    public Profile(String username, List<Achievement> achievements, List<Quiz> createdQuizzes, int quizzesTaken, List<User> friendsList) {
        this.username = username;
        this.achievements = achievements;
        this.createdQuizzes = createdQuizzes;
        this.quizzesTaken = quizzesTaken;
        this.friendsList = friendsList;
    }

    public boolean getOwnProfile(){
        return ownProfile;
    }

    public void setOwnProfile(boolean ownProfile){
        this.ownProfile = ownProfile;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }

    public List<Quiz> getCreatedQuizzes() {
        return createdQuizzes;
    }

    public void setCreatedQuizzes(List<Quiz> createdQuizzes) {
        this.createdQuizzes = createdQuizzes;
    }

    public int getQuizzesTaken() {
        return quizzesTaken;
    }

    public void setQuizzesTaken(int quizzesTaken) {
        this.quizzesTaken = quizzesTaken;
    }

    public List<User> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(List<User> friendsList) {
        this.friendsList = friendsList;
    }
}
