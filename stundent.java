import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

class Student implements Comparable<Student> {
    private String firstName;
    private String lastName;
    private int studentId;

    public Student(String firstName, String lastName, int studentId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getStudentId() {
        return studentId;
    }

    @Override
    public int compareTo(Student other) {
        return Integer.compare(this.studentId, other.studentId);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (ID: " + studentId + ")";
    }
}

class StudyGroup implements Iterable<Student> {
    private List<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudentByFullName(String firstName, String lastName) {
        students.removeIf(student -> student.getFirstName().equals(firstName) && student.getLastName().equals(lastName));
    }

    @Override
    public Iterator<Student> iterator() {
        return new StudyGroupIterator(students);
    }
}

class StudyGroupIterator implements Iterator<Student> {
    private List<Student> students;
    private int index = 0;

    public StudyGroupIterator(List<Student> students) {
        this.students = students;
    }

    @Override
    public boolean hasNext() {
        return index < students.size();
    }

    @Override
    public Student next() {
        return students.get(index++);
    }

    @Override
    public void remove() {
        if (index <= 0) throw new IllegalStateException("You cannot remove an element until you've done at least one next()");
        students.remove(--index);
    }
}

class StudyGroupService {
    public void removeStudentByFullName(StudyGroup group, String firstName, String lastName) {
        group.removeStudentByFullName(firstName, lastName);
    }

    public void sortStudentsById(StudyGroup group) {
        group.students.sort(Comparator.naturalOrder());
    }

    public void sortStudentsByFullName(StudyGroup group) {
        group.students.sort(new StudentComparator());
    }
}

class StudentComparator implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        int lastNameComparison = s1.getLastName().compareTo(s2.getLastName());
        if (lastNameComparison == 0) {
            return s1.getFirstName().compareTo(s2.getFirstName());
        } else {
            return lastNameComparison;
        }
    }
}

class Controller {
    private StudyGroupService service;

    public Controller(StudyGroupService service) {
        this.service = service;
    }

    public void removeStudentByFullName(StudyGroup group, String firstName, String lastName) {
        service.removeStudentByFullName(group, firstName, lastName);
    }

    public void sortStudentsById(StudyGroup group) {
        service.sortStudentsById(group);
    }

    public void sortStudentsByFullName(StudyGroup group) {
        service.sortStudentsByFullName(group);
    }
}

class Stream implements Iterable<StudyGroup> {
    private List<StudyGroup> groups = new ArrayList<>();

    public void addGroup(StudyGroup group) {
        groups.add(group);
    }

    @Override
    public Iterator<StudyGroup> iterator() {
        return groups.iterator();
    }

    public int getGroupCount() {
        return groups.size();
    }
}

class StreamComparator implements Comparator<Stream> {
    @Override
    public int compare(Stream s1, Stream s2) {
        return Integer.compare(s1.getGroupCount(), s2.getGroupCount());
    }
}

class StreamService {
    public void sortStreams(List<Stream> streams) {
        streams.sort(new StreamComparator());
    }
}

public class Main {
    public static void main(String[] args) {
        Student s1 = new Student("John", "Doe", 1);
        Student s2 = new Student("Jane", "Doe", 2);
        Student s3 = new Student("Jim", "Beam", 3);

        StudyGroup group = new StudyGroup();
        group.addStudent(s1);
        group.addStudent(s2);
        group.addStudent(s3);

        StudyGroupService service = new StudyGroupService();
        Controller controller = new Controller(service);

        System.out.println("Before sorting:");
        for (Student s : group) {
            System.out.println(s);
        }

        controller.sortStudentsById(group);

        System.out.println("After sorting by ID:");
        for (Student s : group) {
            System.out.println(s);
        }

        controller.sortStudentsByFullName(group);

        System.out.println("After sorting by Full Name:");
        for (Student s : group) {
            System.out.println(s);
        }
    }
}
