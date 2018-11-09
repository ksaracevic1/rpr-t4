package ba.unsa.etf.rpr;

import java.util.ArrayList;

public class Semester {
    public ArrayList<ba.unsa.etf.rpr.Student> getStudents() {
        return students;
    }

    public ArrayList<ba.unsa.etf.rpr.Subject> getSubjects() {
        return subjects;
    }

    private ArrayList<ba.unsa.etf.rpr.Student> students =  new ArrayList<>();
    private ArrayList<ba.unsa.etf.rpr.Subject> subjects = new  ArrayList<>();
    Integer indexCounter = 15000;
    public Semester(ArrayList<ba.unsa.etf.rpr.Tuple<String, String, Integer, Class>> subjects)throws IllegalArgumentException, ba.unsa.etf.rpr.NotEnoughPoints {
        if(subjects == null) throw new IllegalArgumentException();
        int sum = 0;
        ArrayList<ba.unsa.etf.rpr.Subject> newSubjects = new ArrayList<>();
        for(ba.unsa.etf.rpr.Tuple<String, String, Integer, Class> t : subjects) {
            sum += t.getItem3();
            if(t.getItem4() == ba.unsa.etf.rpr.Obligatory.class)
                newSubjects.add(new ba.unsa.etf.rpr.Obligatory(t.getItem1(), t.getItem2(), t.getItem3()));
            else newSubjects.add(new ba.unsa.etf.rpr.Electoral(t.getItem1(), t.getItem2(), t.getItem3()));
        }
        if(sum < 30) throw new ba.unsa.etf.rpr.NotEnoughPoints();
        this.subjects = newSubjects;
    }

    public String getElectoralSubjects(){
        String result = new String();
        int i = 1;
        for(Subject s : this.subjects){
            if(s instanceof Electoral){
                result += Integer.toString(i) + ". Subject name: " + s.getSubjectName() + ", Responsible teacher: "
                        + s.getResponsibleTeacher() + ", ECTS: " + Integer.toString(s.getNumberOfECTSPoints()) + "\n";
                i++;
            }
        }
        return result;
    }
    public String getObligatorySubjects(){
        String result = new String();
        int i = 1;
        for(Subject s : this.subjects){
            if(s instanceof Obligatory){
                result += Integer.toString(i) + ". Subject name: " + s.getSubjectName() + ", Responsible teacher: "
                        + s.getResponsibleTeacher() + ", ECTS: " + Integer.toString(s.getNumberOfECTSPoints()) + "\n";
                i++;
            }
        }
        return result;
    }
    public void enrollStudent(ba.unsa.etf.rpr.Student student, ArrayList<String> electoralSubjects) throws ba.unsa.etf.rpr.NotEnoughPoints, IllegalArgumentException{
        if(student == null || electoralSubjects == null) throw new IllegalArgumentException();
        boolean ima = false; // check if electorals are correct
        for(String electoral : electoralSubjects) {
            ima = false;
            for (ba.unsa.etf.rpr.Subject s : this.subjects) {
                if (electoral.equalsIgnoreCase(s.getSubjectName())){
                    ima = true;
                    break;
                }
            }
            if(!ima) throw new IllegalArgumentException();
        }
        for(ba.unsa.etf.rpr.Subject s : this.subjects){ // enroll student in subjects
            if(s instanceof Obligatory) s.enrollStudent(student);
            else {
                for(String electoral : electoralSubjects)
                    if(electoral.equalsIgnoreCase(s.getSubjectName())){
                        s.enrollStudent(student);
                        break;
                    }
            }
        }
        this.students.add(student); // add student to the semestar
        student.setIndex(this.indexCounter++);
        if(student.getNumberOfECTSPoints() < 30){ // if not enugh ECTS delete student
            student.setIndex(0);
            this.indexCounter--;
            deleteStudent(student.getIndex());

            throw new ba.unsa.etf.rpr.NotEnoughPoints();
        }
    }
    public void deleteStudent(Integer index){
        for(ba.unsa.etf.rpr.Subject subject : this.subjects)
            subject.deleteStudent(index);
        for(int i = 0; i < this.students.size(); i++){
            if(this.students.get(i).getIndex().equals(index)){
                this.students.remove(i);
                return;
            }
        }
    }
    public void addSubject(ba.unsa.etf.rpr.Subject subject){
        for(ba.unsa.etf.rpr.Subject s : this.subjects)
            if(s.getSubjectName().equalsIgnoreCase(subject.getSubjectName()))
                throw new IllegalArgumentException();
        this.subjects.add(subject);
    }
    public void deleteSubject(String subjectName){
        ba.unsa.etf.rpr.Subject subject = null;
        for(ba.unsa.etf.rpr.Subject s : this.subjects)
            if(s.getSubjectName().equalsIgnoreCase(subjectName)){
                subject = s;
                break;
            }
        if(subject == null) throw new IllegalArgumentException();
        this.subjects.remove(subject);
        for(int i = 0; i < students.size(); i++){
            subject.deleteStudent(students.get(i).getIndex());
            if(students.get(i).getNumberOfECTSPoints() < 30){
                deleteStudent(students.get(i).getIndex());
                i--;
            }
        }
    }
}
