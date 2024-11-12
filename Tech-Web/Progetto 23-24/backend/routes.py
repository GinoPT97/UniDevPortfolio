from flask import request, jsonify
from app import app, db
from models import User, Quiz, Question, Choice, UserResponse
import bcrypt

@app.route('/register', methods=['POST'])
def register():
    data = request.json
    hashed_password = bcrypt.hashpw(data['password'].encode('utf-8'), bcrypt.gensalt())
    user = User(username=data['username'], email=data['email'], password_hash=hashed_password.decode('utf-8'))
    db.session.add(user)
    db.session.commit()
    return jsonify({"message": "User registered successfully"}), 201

@app.route('/login', methods=['POST'])
def login():
    data = request.json
    user = User.query.filter_by(email=data['email']).first()
    if user and bcrypt.checkpw(data['password'].encode('utf-8'), user.password_hash.encode('utf-8')):
        return jsonify({"message": "Login successful"}), 200
    return jsonify({"message": "Invalid credentials"}), 401

@app.route('/quizzes', methods=['POST'])
def create_quiz():
    data = request.json
    quiz = Quiz(title=data['title'], description=data['description'], creator_id=data['creator_id'])
    db.session.add(quiz)
    db.session.commit()
    return jsonify({"message": "Quiz created successfully"}), 201

@app.route('/quizzes', methods=['GET'])
def get_quizzes():
    quizzes = Quiz.query.all()
    return jsonify([{
        'id': quiz.id,
        'title': quiz.title,
        'description': quiz.description
    } for quiz in quizzes]), 200

@app.route('/quizzes/<int:quiz_id>/questions', methods=['POST'])
def add_question(quiz_id):
    data = request.json
    question = Question(quiz_id=quiz_id, text=data['text'], question_type=data['question_type'])
    db.session.add(question)
    db.session.commit()
    return jsonify({"message": "Question added successfully"}), 201

@app.route('/quizzes/<int:quiz_id>/questions/<int:question_id>/choices', methods=['POST'])
def add_choice(quiz_id, question_id):
    data = request.json
    choice = Choice(question_id=question_id, text=data['text'], is_correct=data['is_correct'])
    db.session.add(choice)
    db.session.commit()
    return jsonify({"message": "Choice added successfully"}), 201

@app.route('/quizzes/<int:quiz_id>/responses', methods=['POST'])
def submit_response(quiz_id):
    data = request.json
    user_response = UserResponse(quiz_id=quiz_id, user_id=data['user_id'], response_data=data['response_data'])
    db.session.add(user_response)
    db.session.commit()
    return jsonify({"message": "Response submitted successfully"}), 201
