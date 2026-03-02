🏋️ Fitness Tracker Application (Java Mini Project)
📌 Project Overview

The Fitness Tracker Application is a purely Java-based desktop application built using Swing GUI.
It allows users to track cardio and strength workouts, calculate calories burned, view workout history, and generate summary reports.

This project demonstrates core Object-Oriented Programming (OOP) concepts, exception handling, multithreading, collections, and GUI development in Java.

🎯 Features

Add Cardio workouts with average heart rate

Add Strength workouts with sets, reps, and weight

Automatic user profile management

Calories burned calculation

View workout history (user-wise or all)

Generate fitness summary report

Multithreading to simulate workout sessions

Input validation using custom exceptions

Interactive Swing-based GUI

🛠 Technologies Used

Java

Java Swing (GUI)

AWT

Multithreading

Collections Framework

Exception Handling

Git & GitHub

🧠 OOP Concepts Used
Concept	Implementation
Interface	Trackable
Abstraction	Workout abstract class
Inheritance	CardioWorkout, StrengthWorkout
Polymorphism	calculateCaloriesBurned() overridden
Encapsulation	Private fields + getters
Exception Handling	InvalidWorkoutDataException
Multithreading	UserSession class
Collections	ArrayList<UserProfile>, ArrayList<Workout>
🧩 Class Description

Trackable → Interface for session tracking

Workout → Abstract base class

CardioWorkout → Cardio exercise implementation

StrengthWorkout → Strength training implementation

UserProfile → Stores user workout data

UserSession → Thread simulation of workout

InvalidWorkoutDataException → Custom validation exception

FitnessTrackerApp → Main Swing GUI application

▶ How to Run the Project

Clone the repository:

git clone https://github.com/bharathakn49-dev/java-mini-project.git

Navigate to the project folder

Compile the program:

javac FitnessTrackerApp.java

Run the application:

java FitnessTrackerApp
🖥 Sample Output

Calories burned displayed after each workout

Workout history shown in a dialog box

Summary report showing:

Total sessions

Total duration

Total calories burned

📂 Project Structure
java-mini-project/
│
├── FitnessTrackerApp.java
├── .gitignore
└── README.md
🚀 Future Enhancements

Save data using files or database

Add BMI and diet tracking

Graphical progress charts

Login authentication

Mobile version

👨‍🎓 Author

Bharatha K N
3rd Semester – Computer Science & Engineering
GitHub: https://github.com/bharathakn49-dev

⭐ Conclusion

This project showcases practical implementation of Java OOP concepts along with GUI development and multithreading, making it a strong academic mini-project.
