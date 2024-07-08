<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Quiz</title>
    <script>
        var questionCount = 0;

        function addQuestion() {
            var questionDiv = document.createElement("div");
            questionDiv.className = "question";
            questionDiv.setAttribute("data-question-index", questionCount);

            var questionLabel = document.createElement("label");
            questionLabel.innerHTML = "Question: ";
            questionDiv.appendChild(questionLabel);

            var questionInput = document.createElement("input");
            questionInput.type = "text";
            questionInput.name = "questions";
            questionDiv.appendChild(questionInput);

            var addAnswerButton = document.createElement("button");
            addAnswerButton.type = "button";
            addAnswerButton.innerHTML = "Add Answer";
            addAnswerButton.onclick = function() { addAnswer(questionDiv); };
            questionDiv.appendChild(addAnswerButton);

            var answersDiv = document.createElement("div");
            answersDiv.className = "answers";
            questionDiv.appendChild(answersDiv);

            document.getElementById("quizForm").appendChild(questionDiv);

            questionCount++;
        }

        function addAnswer(questionDiv) {
            var questionIndex = questionDiv.getAttribute("data-question-index");
            var answersDiv = questionDiv.getElementsByClassName("answers")[0];
            var answerCount = answersDiv.getElementsByClassName("answer").length;

            var answerDiv = document.createElement("div");
            answerDiv.className = "answer";

            var answerLabel = document.createElement("label");
            answerLabel.innerHTML = "Answer: ";
            answerDiv.appendChild(answerLabel);

            var answerInput = document.createElement("input");
            answerInput.type = "text";
            answerInput.name = "answers[" + questionIndex + "]";
            answerDiv.appendChild(answerInput);

            var correctCheckbox = document.createElement("input");
            correctCheckbox.type = "checkbox";
            correctCheckbox.name = "correct[" + questionIndex + "][" + answerCount + "]";
            answerDiv.appendChild(correctCheckbox);

            var correctLabel = document.createElement("label");
            correctLabel.innerHTML = "Correct";
            answerDiv.appendChild(correctLabel);

            answersDiv.appendChild(answerDiv);
        }
    </script>
</head>
<body>
<h1>Create Quiz</h1>
<form id="quizForm" action="CreateQuizServlet" method="post">
    <div>
        <label for="quizName">Quiz Name: </label>
        <input type="text" id="quizName" name="quizName">
    </div>
    <div>
        <label for="quizDescription">Quiz Description: </label>
        <textarea id="quizDescription" name="quizDescription"></textarea>
    </div>
    <button type="button" onclick="addQuestion()">Add Question</button>
    <button type="submit">Submit Quiz</button>
</form>
</body>
</html>
