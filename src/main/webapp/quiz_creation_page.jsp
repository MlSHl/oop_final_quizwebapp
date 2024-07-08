<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Quiz</title>
    <script>
        let questionCount = 0;

        function addQuestion() {
            questionCount++;
            const questionDiv = document.createElement("div");
            questionDiv.className = "question";
            questionDiv.dataset.questionId = questionCount;

            const questionLabel = document.createElement("label");
            questionLabel.innerHTML = "Question: ";
            questionDiv.appendChild(questionLabel);

            const questionInput = document.createElement("input");
            questionInput.type = "text";
            questionInput.name = `questions_${questionCount}`;
            questionDiv.appendChild(questionInput);

            const addAnswerButton = document.createElement("button");
            addAnswerButton.type = "button";
            addAnswerButton.innerHTML = "Add Answer";
            addAnswerButton.onclick = function() { addAnswer(questionDiv); };
            questionDiv.appendChild(addAnswerButton);

            const answersDiv = document.createElement("div");
            answersDiv.className = "answers";
            questionDiv.appendChild(answersDiv);

            document.getElementById("quizForm").appendChild(questionDiv);
        }

        function addAnswer(questionDiv) {
            const questionId = questionDiv.dataset.questionId;
            const answersDiv = questionDiv.querySelector(".answers");

            const answerDiv = document.createElement("div");
            answerDiv.className = "answer";

            const answerLabel = document.createElement("label");
            answerLabel.innerHTML = "Answer: ";
            answerDiv.appendChild(answerLabel);

            const answerInput = document.createElement("input");
            answerInput.type = "text";
            answerInput.name = `answers_${questionId}`;
            answerDiv.appendChild(answerInput);

            const correctCheckbox = document.createElement("input");
            correctCheckbox.type = "checkbox";
            correctCheckbox.name = `correctAnswers_${questionId}`;
            answerDiv.appendChild(correctCheckbox);

            const correctLabel = document.createElement("label");
            correctLabel.innerHTML = " Correct";
            answerDiv.appendChild(correctLabel);

            answersDiv.appendChild(answerDiv);
        }
    </script>
</head>
<body>
<h1>Create Quiz</h1>
<form id="quizForm" action="CreateQuizServlet" method="post">
    <button type="button" onclick="addQuestion()">Add Question</button>
    <button type="submit">Submit Quiz</button>
</form>
</body>
</html>
