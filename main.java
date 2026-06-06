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
    
    public static final String BG_BLUE = "\033[44m";
    public static final String BOLD = "\033[1m";
    public static final String CLEAR_SCREEN = "\033[H\033[2J";
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
             System.out.println(Colors.RED + " [!] Error: Student with ID " + student.getId() + " already exists." + Colors.RESET);
             return;
        }
        students.add(student);
        saveStudents();
        System.out.println(Colors.GREEN + " [✓] Student added successfully!" + Colors.RESET);
    }

    public void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println(Colors.YELLOW + " [!] No students found in the system." + Colors.RESET);
            return;
        }
        printStudentTable(students);
    }

    public void searchStudent(String keyword) {
        List<Student> results = new ArrayList<>();
        for (Student s : students) {
            if (s.getName().toLowerCase().contains(keyword.toLowerCase()) || 
                s.getId().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(s);
            }
        }
        
        if (results.isEmpty()) {
            System.out.println(Colors.YELLOW + " [!] No students found matching: " + keyword + Colors.RESET);
        } else {
            System.out.println(Colors.GREEN + " [✓] Found " + results.size() + " matching student(s):" + Colors.RESET);
            printStudentTable(results);
        }
    }

    private void printStudentTable(List<Student> list) {
        System.out.println(Colors.CYAN + "╔════════════╦════════════════════════╦═══════╦══════════════════════╦═══════╗");
        System.out.printf("║ %-10s ║ %-22s ║ %-5s ║ %-20s ║ %-5s ║\n", "ID", "Name", "Age", "Course", "Grade");
        System.out.println("╠════════════╬════════════════════════╬═══════╬══════════════════════╬═══════╣" + Colors.RESET);
        
        for (Student s : list) {
            System.out.printf(Colors.CYAN + "║ " + Colors.WHITE + "%-10s" + Colors.CYAN + " ║ " + Colors.WHITE + "%-22s" + Colors.CYAN + " ║ " + Colors.WHITE + "%-5d" + Colors.CYAN + " ║ " + Colors.WHITE + "%-20s" + Colors.CYAN + " ║ " + Colors.WHITE + "%-5s" + Colors.CYAN + " ║\n" + Colors.RESET,
                    s.getId(), s.getName(), s.getAge(), s.getCourse(), s.getGrade());
        }
        System.out.println(Colors.CYAN + "╚════════════╩════════════════════════╩═══════╩══════════════════════╩═══════╝" + Colors.RESET);
    }

    public void updateStudent(String id, Scanner scanner) {
        Student s = getStudentById(id);
        if (s == null) {
            System.out.println(Colors.RED + " [!] Error: Student not found." + Colors.RESET);
            return;
        }

        System.out.print(Colors.CYAN + " Enter New Name (leave blank to keep '" + s.getName() + "'): " + Colors.RESET);
        String name = scanner.nextLine();
        if (!name.trim().isEmpty()) s.setName(name);

        System.out.print(Colors.CYAN + " Enter New Age (leave blank to keep '" + s.getAge() + "'): " + Colors.RESET);
        String ageStr = scanner.nextLine();
        if (!ageStr.trim().isEmpty()) {
            try {
                int newAge = Integer.parseInt(ageStr);
                if (newAge >= 0) {
                    s.setAge(newAge);
                } else {
                     System.out.println(Colors.RED + " [!] Invalid age. Keeping old age." + Colors.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(Colors.RED + " [!] Invalid age format. Keeping old age." + Colors.RESET);
            }
        }

        System.out.print(Colors.CYAN + " Enter New Course (leave blank to keep '" + s.getCourse() + "'): " + Colors.RESET);
        String course = scanner.nextLine();
        if (!course.trim().isEmpty()) s.setCourse(course);

        System.out.print(Colors.CYAN + " Enter New Grade (leave blank to keep '" + s.getGrade() + "'): " + Colors.RESET);
        String grade = scanner.nextLine();
        if (!grade.trim().isEmpty()) s.setGrade(grade);

        saveStudents();
        System.out.println(Colors.GREEN + " [✓] Student updated successfully!" + Colors.RESET);
    }

    public void deleteStudent(String id) {
        Student s = getStudentById(id);
        if (s != null) {
            students.remove(s);
            saveStudents();
            System.out.println(Colors.GREEN + " [✓] Student deleted successfully!" + Colors.RESET);
        } else {
            System.out.println(Colors.RED + " [!] Error: Student not found." + Colors.RESET);
        }
    }

    public void showStatistics() {
        if (students.isEmpty()) {
            System.out.println(Colors.YELLOW + " [!] No students available for statistics." + Colors.RESET);
            return;
        }
        double avgAge = students.stream().mapToInt(Student::getAge).average().orElse(0.0);
        System.out.println(Colors.PURPLE + "╔════════════════════════════════════════════════╗");
        System.out.println("║                SYSTEM STATISTICS               ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.printf("║ Total Enrolled Students : %-20d ║\n", students.size());
        System.out.printf("║ Average Student Age     : %-20.1f ║\n", avgAge);
        System.out.println("╚════════════════════════════════════════════════╝" + Colors.RESET);
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
                System.out.println(Colors.RED + " [!] Error loading student data." + Colors.RESET);
            }
        }
    }

    private void saveStudents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(students);
        } catch (IOException e) {
            System.out.println(Colors.RED + " [!] Error saving student data." + Colors.RESET);
        }
    }
}

public class main {
    private static void clearScreen() {
        System.out.print(Colors.CLEAR_SCREEN);
        System.out.flush();
    }

    private static void printHeader() {
        System.out.println(Colors.BLUE + Colors.BOLD + 
                "   _____ _             _             _   \n" +
                "  / ____| |           | |           | |  \n" +
                " | (___ | |_ _   _  __| | ___ _ __ | |_ \n" +
                "  \\___ \\| __| | | |/ _` |/ _ \\ '_ \\| __|\n" +
                "  ____) | |_| |_| | (_| |  __/ | | | |_ \n" +
                " |_____/ \\__|\\__,_|\\__,_|\\___|_| |_|\\__|\n" +
                "                                        \n" + Colors.RESET);
        
        System.out.println(Colors.CYAN + " ╔═════════════════════════════════════════════════════════╗");
        System.out.println(" ║" + Colors.PURPLE + Colors.BOLD + "            ADVANCED STUDENT MANAGEMENT SYSTEM           " + Colors.CYAN + "║");
        System.out.println(" ╚═════════════════════════════════════════════════════════╝" + Colors.RESET);
    }

    private static void simulateLoading() {
        System.out.print(Colors.YELLOW + " Processing ");
        for (int i = 0; i < 3; i++) {
            try { Thread.sleep(200); } catch (InterruptedException e) {}
            System.out.print(".");
        }
        System.out.println(Colors.RESET);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentManager manager = new StudentManager();
        boolean running = true;

        while (running) {
            clearScreen();
            printHeader();
            System.out.println(Colors.YELLOW + "  [1] " + Colors.WHITE + "Enroll New Student");
            System.out.println(Colors.YELLOW + "  [2] " + Colors.WHITE + "View All Students " + Colors.GREEN + "(Table View)");
            System.out.println(Colors.YELLOW + "  [3] " + Colors.WHITE + "Search Student " + Colors.CYAN + "(By ID/Name)");
            System.out.println(Colors.YELLOW + "  [4] " + Colors.WHITE + "Update Student Record");
            System.out.println(Colors.YELLOW + "  [5] " + Colors.WHITE + "Delete Student");
            System.out.println(Colors.YELLOW + "  [6] " + Colors.WHITE + "View System Statistics");
            System.out.println(Colors.YELLOW + "  [0] " + Colors.WHITE + "Exit Application");
            System.out.println(Colors.CYAN + " ───────────────────────────────────────────────────────────" + Colors.RESET);
            System.out.print(Colors.GREEN + "  ➤ Select an option: " + Colors.RESET);

            String choice = scanner.nextLine();

            switch (choice.trim()) {
                case "1":
                    clearScreen();
                    System.out.println(Colors.BG_BLUE + Colors.WHITE + " [ ENROLL NEW STUDENT ] " + Colors.RESET + "\n");
                    System.out.print(Colors.CYAN + " Enter ID: " + Colors.RESET);
                    String id = scanner.nextLine();
                    if (id.trim().isEmpty()) {
                        System.out.println(Colors.RED + " [!] ID cannot be empty." + Colors.RESET);
                        waitForEnter(scanner);
                        break;
                    }

                    System.out.print(Colors.CYAN + " Enter Name: " + Colors.RESET);
                    String name = scanner.nextLine();
                    
                    int age = -1;
                    while(age < 0) {
                        System.out.print(Colors.CYAN + " Enter Age: " + Colors.RESET);
                        try {
                            age = Integer.parseInt(scanner.nextLine());
                            if(age < 0) {
                                System.out.println(Colors.RED + " [!] Age cannot be negative." + Colors.RESET);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(Colors.RED + " [!] Please enter a valid number for age." + Colors.RESET);
                        }
                    }

                    System.out.print(Colors.CYAN + " Enter Course: " + Colors.RESET);
                    String course = scanner.nextLine();
                    System.out.print(Colors.CYAN + " Enter Grade: " + Colors.RESET);
                    String grade = scanner.nextLine();

                    simulateLoading();
                    manager.addStudent(new Student(id, name, age, course, grade));
                    waitForEnter(scanner);
                    break;
                case "2":
                    clearScreen();
                    System.out.println(Colors.BG_BLUE + Colors.WHITE + " [ ALL ENROLLED STUDENTS ] " + Colors.RESET + "\n");
                    simulateLoading();
                    manager.viewAllStudents();
                    waitForEnter(scanner);
                    break;
                case "3":
                    clearScreen();
                    System.out.println(Colors.BG_BLUE + Colors.WHITE + " [ SEARCH DATABASE ] " + Colors.RESET + "\n");
                    System.out.print(Colors.CYAN + " Enter Name or ID to search: " + Colors.RESET);
                    String keyword = scanner.nextLine();
                    simulateLoading();
                    manager.searchStudent(keyword);
                    waitForEnter(scanner);
                    break;
                case "4":
                    clearScreen();
                    System.out.println(Colors.BG_BLUE + Colors.WHITE + " [ UPDATE STUDENT RECORD ] " + Colors.RESET + "\n");
                    System.out.print(Colors.CYAN + " Enter the ID of the student to update: " + Colors.RESET);
                    String updateId = scanner.nextLine();
                    manager.updateStudent(updateId, scanner);
                    waitForEnter(scanner);
                    break;
                case "5":
                    clearScreen();
                    System.out.println(Colors.BG_BLUE + Colors.WHITE + " [ DELETE STUDENT RECORD ] " + Colors.RESET + "\n");
                    System.out.print(Colors.CYAN + " Enter the ID of the student to delete: " + Colors.RESET);
                    String deleteId = scanner.nextLine();
                    simulateLoading();
                    manager.deleteStudent(deleteId);
                    waitForEnter(scanner);
                    break;
                case "6":
                    clearScreen();
                    System.out.println(Colors.BG_BLUE + Colors.WHITE + " [ SYSTEM STATISTICS ] " + Colors.RESET + "\n");
                    simulateLoading();
                    manager.showStatistics();
                    waitForEnter(scanner);
                    break;
                case "0":
                    clearScreen();
                    System.out.println(Colors.PURPLE + "\n  Saving database..." + Colors.RESET);
                    simulateLoading();
                    System.out.println(Colors.GREEN + "  Goodbye! Have a great day.\n" + Colors.RESET);
                    running = false;
                    break;
                default:
                    System.out.println(Colors.RED + " [!] Invalid choice. Please try again." + Colors.RESET);
                    waitForEnter(scanner);
            }
        }
        scanner.close();
    }

    private static void waitForEnter(Scanner scanner) {
        System.out.print(Colors.PURPLE + "\n ➤ Press Enter to return to the main menu..." + Colors.RESET);
        scanner.nextLine();
    }
}
