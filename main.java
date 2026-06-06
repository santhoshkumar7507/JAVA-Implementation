import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Color Constants for an impressive UI
class Colors {
    public static final String RESET = "\033[0m";
    public static final String BLACK = "\033[0;30m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\033[0;37m";
    public static final String BOLD = "\033[1m";
}

class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private int age;
    private String course;
    private String grade;

    public Student(String id, String name, int age, String course, String grade) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.course = course;
        this.grade = grade;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}

class StudentManager {
    private List<Student> students;
    private final String DATA_FILE = "students.dat";

    public StudentManager() {
        students = new ArrayList<>();
        loadStudents();
    }

    public void addStudent(Student student) {
        if(getStudentById(student.getId()) != null) {
             System.out.println(Colors.RED + "Error: Student with ID " + student.getId() + " already exists." + Colors.RESET);
             return;
        }
        students.add(student);
        saveStudents();
        System.out.println(Colors.GREEN + "Student added successfully!" + Colors.RESET);
    }

    public void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println(Colors.YELLOW + "No students found in the system." + Colors.RESET);
            return;
        }
        
        System.out.println(Colors.CYAN + "--------------------------------------------------------------------------------");
        System.out.printf("| %-10s | %-20s | %-5s | %-20s | %-5s |\n", "ID", "Name", "Age", "Course", "Grade");
        System.out.println("--------------------------------------------------------------------------------" + Colors.RESET);
        
        for (Student s : students) {
            System.out.printf(Colors.WHITE + "| %-10s | %-20s | %-5d | %-20s | %-5s |\n" + Colors.RESET,
                    s.getId(), s.getName(), s.getAge(), s.getCourse(), s.getGrade());
        }
        System.out.println(Colors.CYAN + "--------------------------------------------------------------------------------" + Colors.RESET);
    }

    public void updateStudent(String id, Scanner scanner) {
        Student s = getStudentById(id);
        if (s == null) {
            System.out.println(Colors.RED + "Error: Student not found." + Colors.RESET);
            return;
        }

        System.out.print("Enter New Name (leave blank to keep '" + s.getName() + "'): ");
        String name = scanner.nextLine();
        if (!name.trim().isEmpty()) s.setName(name);

        System.out.print("Enter New Age (leave blank to keep '" + s.getAge() + "'): ");
        String ageStr = scanner.nextLine();
        if (!ageStr.trim().isEmpty()) {
            try {
                int newAge = Integer.parseInt(ageStr);
                if (newAge >= 0) {
                    s.setAge(newAge);
                } else {
                     System.out.println(Colors.RED + "Invalid age. Keeping old age." + Colors.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(Colors.RED + "Invalid age format. Keeping old age." + Colors.RESET);
            }
        }

        System.out.print("Enter New Course (leave blank to keep '" + s.getCourse() + "'): ");
        String course = scanner.nextLine();
        if (!course.trim().isEmpty()) s.setCourse(course);

        System.out.print("Enter New Grade (leave blank to keep '" + s.getGrade() + "'): ");
        String grade = scanner.nextLine();
        if (!grade.trim().isEmpty()) s.setGrade(grade);

        saveStudents();
        System.out.println(Colors.GREEN + "Student updated successfully!" + Colors.RESET);
    }

    public void deleteStudent(String id) {
        Student s = getStudentById(id);
        if (s != null) {
            students.remove(s);
            saveStudents();
            System.out.println(Colors.GREEN + "Student deleted successfully!" + Colors.RESET);
        } else {
            System.out.println(Colors.RED + "Error: Student not found." + Colors.RESET);
        }
    }

    private Student getStudentById(String id) {
        for (Student s : students) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void loadStudents() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                students = (List<Student>) ois.readObject();
            } catch (Exception e) {
                System.out.println(Colors.RED + "Error loading student data." + Colors.RESET);
            }
        }
    }

    private void saveStudents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(students);
        } catch (IOException e) {
            System.out.println(Colors.RED + "Error saving student data." + Colors.RESET);
        }
    }
}

public class main {
    private static void printHeader() {
        System.out.println(Colors.BLUE + Colors.BOLD + "\n" +
                "  ____  _             _             _      __  __                                              _   \n" +
                " / ___|| |_ _   _  __| | ___ _ __ | |_   |  \\/  | __ _ _ __   __ _  __ _  ___ _ __ ___   ___| |_ \n" +
                " \\___ \\| __| | | |/ _` |/ _ \\ '_ \\| __|  | |\\/| |/ _` | '_ \\ / _` |/ _` |/ _ \\ '_ ` _ \\ / _ \\ __|\n" +
                "  ___) | |_| |_| | (_| |  __/ | | | |_   | |  | | (_| | | | | (_| | (_| |  __/ | | | | |  __/ |_ \n" +
                " |____/ \\__|\\__,_|\\__,_|\\___|_| |_|\\__|  |_|  |_|\\__,_|_| |_|\\__,_|\\__, |\\___|_| |_| |_|\\___|\\__|\n" +
                "                                                                   |___/                         \n" +
                Colors.RESET);
        System.out.println(Colors.CYAN + "=================================================================================================" + Colors.RESET);
        System.out.println(Colors.PURPLE + "                         Welcome to the Student Management System                                " + Colors.RESET);
        System.out.println(Colors.CYAN + "=================================================================================================" + Colors.RESET);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentManager manager = new StudentManager();
        boolean running = true;

        while (running) {
            printHeader();
            System.out.println(Colors.YELLOW + " 1. " + Colors.WHITE + "Add Student");
            System.out.println(Colors.YELLOW + " 2. " + Colors.WHITE + "View All Students");
            System.out.println(Colors.YELLOW + " 3. " + Colors.WHITE + "Update Student");
            System.out.println(Colors.YELLOW + " 4. " + Colors.WHITE + "Delete Student");
            System.out.println(Colors.YELLOW + " 5. " + Colors.WHITE + "Exit");
            System.out.println(Colors.CYAN + "=================================================================================================" + Colors.RESET);
            System.out.print(Colors.GREEN + " Enter your choice: " + Colors.RESET);

            String choice = scanner.nextLine();

            switch (choice.trim()) {
                case "1":
                    System.out.println(Colors.CYAN + "\n--- Add New Student ---" + Colors.RESET);
                    System.out.print("Enter ID: ");
                    String id = scanner.nextLine();
                    
                    if (id.trim().isEmpty()) {
                        System.out.println(Colors.RED + "ID cannot be empty." + Colors.RESET);
                        waitForEnter(scanner);
                        break;
                    }

                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    
                    int age = -1;
                    while(age < 0) {
                        System.out.print("Enter Age: ");
                        try {
                            age = Integer.parseInt(scanner.nextLine());
                            if(age < 0) {
                                System.out.println(Colors.RED + "Age cannot be negative." + Colors.RESET);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(Colors.RED + "Please enter a valid number for age." + Colors.RESET);
                        }
                    }

                    System.out.print("Enter Course: ");
                    String course = scanner.nextLine();
                    System.out.print("Enter Grade: ");
                    String grade = scanner.nextLine();

                    manager.addStudent(new Student(id, name, age, course, grade));
                    waitForEnter(scanner);
                    break;
                case "2":
                    System.out.println(Colors.CYAN + "\n--- All Students ---" + Colors.RESET);
                    manager.viewAllStudents();
                    waitForEnter(scanner);
                    break;
                case "3":
                    System.out.println(Colors.CYAN + "\n--- Update Student ---" + Colors.RESET);
                    System.out.print("Enter the ID of the student to update: ");
                    String updateId = scanner.nextLine();
                    manager.updateStudent(updateId, scanner);
                    waitForEnter(scanner);
                    break;
                case "4":
                    System.out.println(Colors.CYAN + "\n--- Delete Student ---" + Colors.RESET);
                    System.out.print("Enter the ID of the student to delete: ");
                    String deleteId = scanner.nextLine();
                    manager.deleteStudent(deleteId);
                    waitForEnter(scanner);
                    break;
                case "5":
                    System.out.println(Colors.PURPLE + "\nThank you for using the Student Management System. Goodbye!" + Colors.RESET);
                    running = false;
                    break;
                default:
                    System.out.println(Colors.RED + "Invalid choice. Please try again." + Colors.RESET);
                    waitForEnter(scanner);
            }
        }
        scanner.close();
    }

    private static void waitForEnter(Scanner scanner) {
        System.out.print(Colors.YELLOW + "\nPress Enter to continue..." + Colors.RESET);
        scanner.nextLine();
    }
}
