package ba.unsa.etf.rpr;

import java.util.ArrayList;

public abstract class Subject {
    private ArrayList<ba.unsa.etf.rpr.Student> enrolledStudents;
    private String subjectName;
    private String responsibleTeacher;
    private int numberOfECTSPoints;
    public Subject(String subjectName, String reposnsibleTeacher, int points) throws IllegalArgumentException{
        if(subjectName == null || reposnsibleTeacher == null || points < 5) throw new IllegalArgumentException();
        this.enrolledStudents = new ArrayList<>();
        this.subjectName = subjectName;
        this.responsibleTeacher = reposnsibleTeacher;
        this.numberOfECTSPoints = points;
    }
    public ArrayList<ba.unsa.etf.rpr.Student> getEnrolledStudents() {
        return enrolledStudents;
    }
    public String getSubjectName() {
        return subjectName;
    }
    public String getResponsibleTeacher() {
        return responsibleTeacher;
    }
    public int getNumberOfECTSPoints() {
        return numberOfECTSPoints;
    }

    public void enrollStudent(ba.unsa.etf.rpr.Student student){
        for(ba.unsa.etf.rpr.Student s : enrolledStudents)
            if(s.getIndex().equals(student.getIndex())) return;
        enrolledStudents.add(student);
        student.increasePoints(getNumberOfECTSPoints());
    }
    public void deleteStudent(int index){
        for(int i = 0; i < enrolledStudents.size(); i++)
            if(enrolledStudents.get(i).getIndex().equals(index)){
                enrolledStudents.get(i).decreasePoints(getNumberOfECTSPoints());
                enrolledStudents.remove(i);
                return;
            }

    }
    public String printStudents(){
        String result = new String();
        for(int i = 0; i < enrolledStudents.size(); i++)
            result = result + Integer.toString(i + 1) + ". " + enrolledStudents.get(i).toString() + "\n";
        return result;
    }
}
