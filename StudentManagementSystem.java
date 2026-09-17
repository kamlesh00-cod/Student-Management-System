import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
class Student {
	private String name;
	private int age;
	private int rollNo;
	private String course;

	Student() {
	}
	Student(int rollNo, String name, int age, String course) {
		this.name = name;
		this.age = age;
		this.rollNo = rollNo;
		this.course = course;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getRollNo() {
		return rollNo;
	}
	public void setRollNo(int rollNo) {
		this.rollNo = rollNo;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}


}
class StudentService {
	private ArrayList<Student> stdList = new ArrayList<>();

	//menu
	public void serviceMenu() {
		System.out.println("\t======== Menu ========");
		System.out.println("1.Add Student");
		System.out.println("2.View Students");
		System.out.println("3.Search Student");
		System.out.println("4.Update Student");
		System.out.println("5.Delete Student");
		System.out.println("6.Exit\n");
	}
	

	// add student
	public boolean addStudent(Student s) throws StudentAlreadyExistsException {
		for (Student std : stdList) {

			if (std.getRollNo() == s.getRollNo()) {
				throw new StudentAlreadyExistsException(
					"Student ID already exists: " + s.getRollNo()
				);
			}
		}

		stdList.add(s);
		return true;
	}

	// view all students
	public boolean viewStudents() {
		if (!stdList.isEmpty()) {
			for (Student s : stdList) {
				System.out.println(s.getRollNo() + " | " + s.getName() + " | " + s.getAge() + " | " + s.getCourse());
			}
			return true;
		} else {
			return false;
		}
	}

	// search student
	public Student searchStudent(int rollNo) {
		for (Student s : stdList) {
			if (s.getRollNo() == rollNo) {
				return s;
			}
		}

		return null;
	}

	//update student info
	public boolean updateStudent(Student std) {
		for (Student s : stdList) {
			if (s.getRollNo() == std.getRollNo()) {
				s.setName(std.getName());
				s.setAge(std.getAge());
				s.setCourse(std.getCourse());

				return true;
			}
		}
		return false;
	}

	//delete student
	public boolean deleteStudent(Student s) {
		for (int i = 0; i < stdList.size(); i++) {

			if (stdList.get(i).getRollNo() == s.getRollNo()) {

				stdList.remove(i);
				return true;
			}
		}
		return false;
	}

	//Save student data in file
	public void saveStudents() {
		File file = new File("students.txt");
		try{
		FileWriter writer = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(writer) ;
			for (Student s : stdList) {
				bw.write(s.getRollNo() + " | " + s.getName() + " | " + s.getAge() + " | " + s.getCourse());
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {

			System.out.println("Unable to save students.");
		}
	}

	//load file data
	public void loadStudents() {

		File file = new File("students.txt");
		if (!file.exists()) {
			return;
		}
		String line=null;
		try{
	    FileReader fr=new FileReader(file);
		
		BufferedReader br = new BufferedReader(fr);
			
			while ((line = br.readLine()) != null) {
				// Ignore empty lines
				if (line.trim().isEmpty()) {
					continue;
				}
				String [] data = line.split("\\s*\\|\\s*");
				// Check number of fields
				if (data.length != 4) {
					System.out.println(
						"Corrupted record skipped: " + line
					);
					continue;
				}
				int roll = Integer.parseInt(data[0]);
				String name = data[1];
				int age = Integer.parseInt(data[2]);
				String course = data[3];
				if (age < 1 || age > 100) {
					System.out.println(
						"Invalid age. Record skipped: " + line
					);
					continue;
				}
				Student s = new Student(roll, name, age, course);
				addStudent(s);
			}
            br.close();
		} catch (FileNotFoundException e) {
			System.out.println("Student file not found. Starting with empty list.");
		} catch (IOException e) {
			System.out.println("Error reading student file.");
		} catch (NumberFormatException e) {
			System.out.println("Invalid student data in file.");
		} catch (StudentAlreadyExistsException e) {
			System.out.println(
				"Duplicate student. Record skipped: " + line);
		}

	}

	// name
	public String capitalizeName(String name) {

		String[] words = name.trim().toLowerCase().split("\\s+");
		StringBuilder result = new StringBuilder();

		for (String word : words) {
			result.append(
				Character.toUpperCase(word.charAt(0))
			);

			result.append(word.substring(1));
			result.append(" ");
		}
		return result.toString().trim();
	}

}
class StudentAlreadyExistsException extends Exception {

	public StudentAlreadyExistsException(String message) {
		super(message);
	}
}

public class StudentManagementSystem {
	public static void main(String[] args) {
		System.out.println("\t ===== Welcome =====\n");
		StudentService srv = new StudentService();
		srv.loadStudents();
		Scanner sc = new Scanner(System.in);
		int ch ;
		boolean flag = true;
		while (flag) {
			srv.serviceMenu();
			try{
				System.out.print("Select by number: ");
			ch = Integer.parseInt(sc.next());
			
			switch (ch) {
			case 1:
				System.out.println("==== Add Student ====");
				while (true) {
					System.out.print("Enter Roll Number:");
					try {
						int roll = sc.nextInt();
                        sc.nextLine();
						System.out.print("Enter Student Name: ");
						String name = srv.capitalizeName(sc.nextLine());
						int age;
						while (true) {

							System.out.print("Enter Age:");
						    age = Integer.parseInt(sc.nextLine());
							if (age < 1 || age > 100) {
								System.out.println("Age must be between 1 and 100.");
							} else {
								break;
							}

						}
						System.out.print("Enter Course:");
						String course = sc.nextLine().toUpperCase();
						Student s = new Student(roll, name, age, course);

						if (srv.addStudent(s)) {
							System.out.println("Student Added!");
						} else {
							System.out.println("Student not added ! Try Again...");
						}
					} catch (StudentAlreadyExistsException e) {
						System.out.println(e.getMessage());
					} catch (NumberFormatException e) {
						System.out.println("Please enter a valid number.");
					}

					System.out.println("Add student(press 'Y'/'N')\t\tExit('E')");
					String btn = sc.next();
					if (btn.equalsIgnoreCase("n")) {
						break;//for goto menu
					}
					if (btn.equalsIgnoreCase("e")) {
						flag = false; //for exit
						break;
					}
					if (btn.equalsIgnoreCase("y")) {
						continue;
					}
				}
				srv.saveStudents();
				break;
			case 2:
				System.out.println("==== Students List ====");
				if (srv.viewStudents()) {
					System.out.println("Press Any button");
					sc.next();
				} else {
					System.out.println("List is EMPTY!");
					sc.next();
				}
				break;
			case 3:
				System.out.println("==== Search Student ====");
				while (true) {
					System.out.print("Enter Student Roll Number: ");
					int roll = sc.nextInt();
					Student s = srv.searchStudent(roll);
					if (s != null) {
						System.out.println("Student found!");
						System.out.println(s.getRollNo() + " | " + s.getName() + " | " + s.getAge() + " | " + s.getCourse());

					} else {
						System.out.println("Student not found! Check roll number  or Student does not exist....");

					}
					System.out.println("Search(Press 'Y'/'N') \tExit(Press 'E')");
					String btn = sc.next();
					if (btn.equalsIgnoreCase("n")) {
						break;//for goto menu
					}
					if (btn.equalsIgnoreCase("e")) {
						flag = false; //for exit
						break;
					}
					if (btn.equalsIgnoreCase("y")) {
						continue;
					}
				}
				break;
			case 4:
				System.out.println("==== Update Student ====");
				while (true) {
					System.out.print("Enter Student Roll Number: ");
					int roll = sc.nextInt();
					sc.nextLine();
					Student s = srv.searchStudent(roll);
					if (s != null) {
						System.out.println("Student found!");
						System.out.println(s.getRollNo() + " | " + s.getName() + " | " + s.getAge() + " | " + s.getCourse());

						System.out.println("---------------\t---------------");

						System.out.print("Enter New Name: ");
						String name =srv.capitalizeName(sc.nextLine());

						System.out.print("Enter New Age:");
						int age ;
						while(true){
						       age=Integer.parseInt(sc.nextLine());
						       if (age < 1 || age > 100) {
								System.out.println("Age must be between 1 and 100.");
							} else {
								break;
							}
						}
						System.out.print("Enter New Course:");
						String course = sc.nextLine().toUpperCase();
						Student std = new Student(roll, name, age, course);
						if (srv.updateStudent(std)) {
							System.out.println("Student info updated!");
							srv.saveStudents();
						} else {
							System.out.println("Technical issue! try again...");
						}
					} else {
						System.out.println("Student not found! Check roll number  or Student does not exist....");

					}
					System.out.println("Update(Press 'Y'/'N') \tExit(Press 'E')");
					String btn = sc.next();
					if (btn.equalsIgnoreCase("n")) {
						break;//for goto menu
					}
					if (btn.equalsIgnoreCase("e")) {
						flag = false; //for exit
						break;
					}
					if (btn.equalsIgnoreCase("y")) {
						continue;
					}
				}
				break;
			case 5:
				System.out.println("==== Delete Student ====");

				while (true) {
					System.out.print("Enter Student Roll Number: ");
					int roll = sc.nextInt();
					sc.nextLine();
					Student s = srv.searchStudent(roll);
					if (s != null) {
						System.out.println("Student found!");
						System.out.println(s.getRollNo() + " | " + s.getName() + " | " + s.getAge() + " | " + s.getCourse());

						System.out.println("---------------\t---------------");

						System.out.print("Do you want to delete this student(Y/N): ");
						String dlt = sc.nextLine();

						if (dlt.equalsIgnoreCase("y")) {
							if (srv.deleteStudent(s)) {
								System.out.println("Student deleted!");
								srv.saveStudents();
							} else {
								System.out.println("Technical issue! try again...");
							}
						} else {
							System.out.println("Student was not deleted.");
						}

					} else {
						System.out.println("Student not found! Check roll number  or Student does not exist....");

					}
					System.out.println("Delete another student(Press 'Y'/'N') \tExit(Press 'E')");
					String btn = sc.next();
					if (btn.equalsIgnoreCase("n")) {
						break;//for goto menu
					}
					if (btn.equalsIgnoreCase("e")) {
						flag = false; //for exit
						break;
					}
					if (btn.equalsIgnoreCase("y")) {
						continue;
					}
				}
				break;

			case 6:
				System.out.println("\t Application Closed ! ");
				flag = false;
				break;
			default:
				System.out.println("Please choose right option from shown MENU \n Choose again...");
			}
			}catch(NumberFormatException e){
				System.out.println("Enter number please!");
				
			}
		}
	}
}
