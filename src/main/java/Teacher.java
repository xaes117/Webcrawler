/**
 * Created by nathanliu on 16/07/2016.
 */
public class Teacher {

    private String TeacherLink;
    private String Name;
    private String Email;
    private String Description;

    public Teacher(String name, String teacherLink) {
        TeacherLink = teacherLink;
        Name = name;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
