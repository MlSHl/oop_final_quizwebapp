<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link href="CSS/qcp1.css" rel="stylesheet">
    <title>Create Quiz</title>
    <script class = "gasacentria">
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
<nav>
    <ul class="nav-ul">
        <li class="nav-li"><a href="#"><img class="logo" src="image/logo.jpg"></a></li>
        <li class="nav-li"><a href="#"><i class="fas fa-user nav-icon"></i>profile</a></li>
        <li class="nav-li"><a href="#"><i class="fas fa-pencil-alt nav-icon"></i>create quiz</a></li>
    </ul>

    <section class="search">
        <input type="text" placeholder="quizzes, users ..." class="search-field">
        <button type="button" class="search-button"><i class="fas fa-search"></i></button>
    </section>
</nav>
<h1>Create Quiz</h1>
<form id="quizForm" action="CreateQuizServlet" method="post">
    <div class = "grid">
        <section>
            <fieldset class = "q-desc">
                <legend class = "q-legend">About Your Quiz</legend>
                <section>
                    <label for="quizName" class = "q-name-label">Quiz Name</label>
                    <input class = "q-name" type="text" id="quizName" name="quizName">

                    <label for="quizDescription" class = "q-desc-label">Quiz Description</label>
                    <textarea placeholder="Max 300 Words" id="quizDescription" name="quizDescription"></textarea>
                </section>
            </fieldset>
        </section>
        <section class = "q-b">
            <button class = "q-buttons" type="button" onclick="addQuestion()">Add Question</button>
            <button class = "q-buttons" type="submit">Submit Quiz</button>
        </section>
    </div>
</form>
</body>
</html>
