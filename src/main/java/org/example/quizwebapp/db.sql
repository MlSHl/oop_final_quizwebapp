
DROP TABLE IF EXISTS user_quiz_scores;
DROP TABLE IF EXISTS answers;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS quizzes;
drop table if exists quizes;
drop table if exists user_achievements;
DROP TABLE IF EXISTS achievement_desc;
DROP TABLE IF EXISTS friends;
Drop table if exists friend_requests;
DROP TABLE IF EXISTS users;

CREATE TABLE users(
	user_name 	varchar(25) NOT NULL,
	password	varchar(255) NOT NULL,
	-- profile_picture varchar(255) DEFAULT "pictures/default.png",
	unique (user_name)
);


CREATE TABLE quizzes(
	quiz_id		int AUTO_INCREMENT PRIMARY KEY,
    quiz_name   varchar(255),
    quiz_desc   varchar(255),
	creator_name 	varchar(25),
	-- Constraints
	FOREIGN KEY (creator_name) REFERENCES users(user_name)
);

CREATE TABLE questions(
	question_id	int AUTO_INCREMENT PRIMARY KEY,
	quiz_id		int,
	question_text	varchar(255) NOT NULL,
	question_image	varchar(255), -- if empty then display no image 
	points		int NOT NULL, -- how many points you get for getting the question right
	-- Constraints
	FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id)
);

CREATE TABLE answers(
	answer_id 	int AUTO_INCREMENT PRIMARY KEY,
	question_id 	int, 			-- add foreign key constraint
	answer_text	varchar(255), 		-- option
	-- answer_image	varchar(255),		-- display no image if empty
	answer_type	varchar(1),
	-- Constraints
	check (answers.answer_type in ('C','I')), 	-- Either 'C' or 'I' (Correct / Incorrect)
	FOREIGN KEY (question_id) REFERENCES questions(question_id)
);

CREATE TABLE user_quiz_scores(
	user_name		varchar(25),
	quiz_id		int,
	best_score	int,
	last_score	int,
	PRIMARY KEY (user_name, quiz_id),
    	FOREIGN KEY (user_name) REFERENCES users(user_name),
    	FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id)
);


CREATE TABLE achievement_desc(
	achievement_id		int AUTO_INCREMENT PRIMARY KEY,
	achievement_name	varchar(255),
	achievement_desc	varchar(255),
	achievement_img		varchar(255)
);

-- Achievements
CREATE TABLE user_achievements(
	user_name 	varchar(25) , -- foreign key
	achievement_id 		int,
	FOREIGN KEY (user_name) REFERENCES users(user_name),
    	FOREIGN KEY (achievement_id) REFERENCES achievement_desc(achievement_id)
);

-- TODO: ADD FRIENDS

CREATE TABLE friend_requests(
	sender_name		varchar(25),
	reciever_name	varchar(25)
);

CREATE TABLE friends(
	user_name			varchar(25),
	friend_name		varchar(25)
)
