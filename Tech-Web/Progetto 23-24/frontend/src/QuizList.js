import React, { useEffect, useState } from 'react';
import axios from 'axios';

function QuizList() {
    const [quizzes, setQuizzes] = useState([]);

    useEffect(() => {
        const fetchQuizzes = async () => {
            try {
                const response = await axios.get('/api/quizzes');
                setQuizzes(response.data);
            } catch (error) {
                console.error('Errore nel recupero dei quiz', error);
            }
        };

        fetchQuizzes();
    }, []);

    return (
        <div>
            <h2>Lista Quiz</h2>
            <ul>
                {quizzes.map(quiz => (
                    <li key={quiz.id}>{quiz.title}</li>
                ))}
            </ul>
        </div>
    );
}

export default QuizList;
