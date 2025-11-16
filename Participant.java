package CollegeEventSystem;
import java.io.Serializable;

public class Participant implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String rollNo;
    private String department;

    public Participant(String name, String rollNo, String department) {
        this.name = name;
        this.rollNo = rollNo;
        this.department = department;
    }

    public String getName() { return name; }
    public String getRollNo() { return rollNo; }
    public String getDepartment() { return department; }

    @Override
    public String toString() {
        return String.format("%s (Roll: %s, Dept: %s)", name, rollNo, department);
    }
}

