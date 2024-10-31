import React, { useState } from 'react';
import axios from 'axios';

function CreateQuiz() {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.post('/api/quizzes', { title, description, creator_id: 1 }); // Adjust creator_id as needed
            alert('Quiz creato con successo!');
        } catch (error) {
            alert('Errore nella creazione del quiz.');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input type="text" placeholder="Titolo" value={title} onChange={(e) => setTitle(e.target.value)} required />
            <textarea placeholder="Descrizione" value={description} onChange={(e) => setDescription(e.target.value)}></textarea>
            <button type="submit">Crea Quiz</button>
        </form>
    );
}

export default CreateQuiz;
