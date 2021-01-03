import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class score {
	
	//管理員產生每個學生各個學期的成績單(10)
	public void generatePersonalGrade(String semester, String studentID) throws IOException, FileNotFoundException{
		String personalGrade = semester + studentID + ".txt";
		String enterScore = "";
		FileReader reader = new FileReader("classes.txt");
		Scanner scFile = new Scanner(reader);
		nextCourse:
		while(scFile.hasNext()) {
			String[] data = scFile.nextLine().split(" ");
			for(int i = 6; i < data.length; i++) {
				String[] score = data[i].split("-");
				if(studentID.equals(score[0]) && semester.equals(data[0])) {
					enterScore += semester + " " + data[2] + " " + score[1] + "\n";
					continue nextCourse;
				}
			}
		}
		scFile.close();
		FileWriter writer = new FileWriter(personalGrade);
		if(enterScore.equals(""))
			writer.write("無選修課程");
		else
			writer.write(enterScore);
		writer.flush();
		writer.close();
	}
	
	//查詢某學生修課學期
	public String[] checkSemester(String studentID) throws FileNotFoundException {
		String[] semester;
		ArrayList<String> temp = new ArrayList<String>();
		FileReader reader = new FileReader("classes.txt");
		Scanner scFile = new Scanner(reader);
		nextCourse:
		while(scFile.hasNext()) {
			String[] data = scFile.nextLine().split(" ");
			for(int i = 6; i < data.length; i++) {
				String[] score = data[i].split("-");
				if(score[0].equals(studentID)) {
					if(!isExist(temp,data[0])) {
						temp.add(data[0]);
						continue nextCourse;
					}
				}
			}
		}
		semester = new String[temp.size()];
		for(int i = 0; i < temp.size(); i++) {
			semester[i] = temp.get(i);
		}
		scFile.close();
		return semester;
	}
	
	//檢查陣列是否重複
	private boolean isExist(ArrayList<String> list, String s) {
		for(int i = 0; i < list.size(); i++) {
			if(s.equals(list.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	//學生檢查各學期各課程成績(11)
	public String[] checkGrade(String semester, String studentID) throws IOException{
		String personalGrade = semester + studentID + ".txt";
		String[] grade;
		generatePersonalGrade(semester, studentID);
		FileReader reader = new FileReader(personalGrade);
		Scanner scFile = new Scanner(reader);
		ArrayList<String> temp = new ArrayList<String>();
		String print = scFile.nextLine();
		if(print.equals("無選修課程")) {
			reader.close();
			scFile.close();
			grade = new String[temp.size()];
			for(int i = 0; i < temp.size(); i++) {
				grade[i] = temp.get(i);
			}
			return grade;
		}
		temp.add(print);
		while (scFile.hasNext())
			temp.add(scFile.nextLine());
		reader.close();
		scFile.close();
		grade = new String[temp.size()];
		for(int i = 0; i < temp.size(); i++) {
			grade[i] = temp.get(i);
		}
		return grade;
	}
	
	//學生列印所有學期選修課程的成績單(12)
	public String[] printGrade(String studentID) throws IOException {
		FileReader reader = new FileReader("account.txt");
		Scanner scFile = new Scanner(reader);
		ArrayList<String> temp = new ArrayList<String>();
		String[] grade;
		while(scFile.hasNext()) {
			String[] student = scFile.nextLine().split(" ");
			if(studentID.equals(student[0])) {
				for(int i = Integer.parseInt(student[3]); i < Integer.parseInt(student[3]) + 4; i++) {
					for(int j = 1; j < 3; j++) {
						String semester = Integer.toString(i) + Integer.toString(j);
						String[] aSemester = checkGrade(semester, studentID);
						for(int k = 0; k < aSemester.length; k++) {
							temp.add(aSemester[k]);
						}
					}
				}
			}
		}
		grade = new String[temp.size()];
		for(int i = 0; i < temp.size(); i++) {
			grade[i] = temp.get(i);
		}
		scFile.close();
		return grade;
	}
	
	//教授產生其授課課程之成績(14)(全部科目)
	public String[] generateAllCourseGrade(String professorName) throws IOException {
		FileReader reader = new FileReader("classes.txt");
		Scanner scFile = new Scanner(reader);
		String[] courseList;
		ArrayList<String> temp = new ArrayList<String>();
		while(scFile.hasNext()) {
			String[] data = scFile.nextLine().split(" ");
			if(data[4].equals(professorName)) {
				String courseGrade = "";
				courseGrade += data[0] + " " + data[1] + " " + data[2] + " ";
				for(int i = 6; i < data.length; i++) {
					String[] studentInfo = data[i].split("-");
					String studentName = searchName(studentInfo[0]);
					courseGrade += studentInfo[0] + studentName + "-" + studentInfo[1] + " ";
				}
				temp.add(courseGrade);
			}
		}
		courseList = new String[temp.size()];
		for(int i = 0; i < temp.size(); i++) {
			courseList[i] = temp.get(i);
		}
		scFile.close();
		return courseList;
	}
	
	//管理員、教授輸入或更新某個學生的成績(9, 13)
	public String updateScore(String semester, String subjectName, String studentName, int studentScore) throws IOException{
		FileReader reader = new FileReader("classes.txt");
		Scanner scFile = new Scanner(reader);
		String enterScore = "";
		String studentID = searchID(studentName);
		while(scFile.hasNext()) {
			String[] data = scFile.nextLine().split(" ");
			if(data[0].equals(semester) && data[2].equals(subjectName)) {//找到要修改的科目
				for(int i = 0; i < 6; i++) {
					enterScore += data[i] + " ";
				}
				for(int i = 6; i < data.length; i++) {
					String[] student = data[i].split("-");
					if(student[0].equals(studentID)) {//找到要修改的學生
						student[1] = Integer.toString(studentScore);
					}
					enterScore += student[0] + "-" + student[1] + " ";
				}
				enterScore += "\n";
			}
			else {
				for(String info: data) {
					enterScore += info + " ";
				}
				enterScore += "\n";
			}
		}
		reader.close();
		scFile.close();
		FileWriter writer = new FileWriter("classes.txt");
		writer.write(enterScore);
		writer.flush();
		writer.close();
		return "成功";
	}
	
	//透過學號搜尋名字
	private String searchName(String ID) throws IOException {
		FileReader reader = new FileReader("account.txt");
		Scanner scFile = new Scanner(reader);
		while(scFile.hasNext()) {
			String[] data = scFile.nextLine().split(" ");
			if(ID.equals(data[0])) {
				scFile.close();
				return data[2];
			}
		}
		scFile.close();
		return "無效輸入";
	}
	
	//透過名字搜尋學號
	private String searchID(String studentName) throws IOException {
		FileReader reader = new FileReader("account.txt");
		Scanner scFile = new Scanner(reader);
		while(scFile.hasNext()) {
			String[] data = scFile.nextLine().split(" ");
			if(studentName.equals(data[2])) {
				scFile.close();
				return data[0];
			}
		}
		scFile.close();
		return "無效輸入";
	}
	
}
