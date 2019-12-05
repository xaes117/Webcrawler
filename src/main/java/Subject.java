import java.util.ArrayList;

/**
 * Created by nathanliu on 16/07/2016.
 */
public class Subject {

    private String SubjectLink;
    private String SubjectString;
    private ArrayList<Teacher> TeacherList;

    public Subject(String subjectLink, String subjectString) {
        SubjectLink = subjectLink;
        SubjectString = subjectString;
        TeacherList = new ArrayList<Teacher>();
    }

    public String getSubjectString() {
        return this.SubjectString;
    }

    public ArrayList<Teacher> getTeacherList() {
        return TeacherList;
    }

    public void addTeacher(Teacher teacher) {
        this.TeacherList.add(teacher);
    }

}
