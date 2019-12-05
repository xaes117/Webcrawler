import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by nathanliu on 16/07/2016.
 */
public class University {

    private String UniversityName;
    private ArrayList<Subject> SubjectList;

    public University(String universityName, ArrayList<Subject> subjectList) {
        UniversityName = universityName;
        SubjectList = subjectList;
    }

    public ArrayList<Subject> getSubjectList() {
        return SubjectList;
    }

    public void setSubjectList(ArrayList<Subject> subjectList) {
        SubjectList = subjectList;
    }

    public void addSubject(Subject subject) {
        SubjectList.add(subject);
    }
}
