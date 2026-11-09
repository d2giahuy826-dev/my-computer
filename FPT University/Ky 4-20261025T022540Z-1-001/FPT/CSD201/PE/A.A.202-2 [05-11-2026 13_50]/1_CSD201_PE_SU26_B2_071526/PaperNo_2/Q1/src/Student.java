// =========== DO NOT EDIT THE GIVEN CONTENT OF THIS FILE ============

class Student {

    String name;
    int score, grade;

    Student() {
    }

    Student(String name, int score, int grade) {
        this.name = name;
        this.score = score;
        this.grade = grade;
    }

    public String toString() {
        return ("(" + name + "," + score + "," + grade + ")");
    }
}
