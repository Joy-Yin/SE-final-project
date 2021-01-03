package user;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class user {
	private ArrayList<String> account = new ArrayList<String>();
	private ArrayList<String> password = new ArrayList<String>();
	private ArrayList<String> name = new ArrayList<String>();
	private ArrayList<Integer> idenity = new ArrayList<Integer>();
	private Scanner sc;
	private File accounts = new File("account.txt");
	private String userAccount;
	private int userIdenity;
	private String userName;
	user(){
		openAccountfile();
	}
	
	
	public boolean checkPass(String checkPass) {
		if (password.get(account.indexOf(userAccount)).equals(checkPass)) return true;
		return false;
	}
	
	public ArrayList<String> printStudent() {
		ArrayList<String> arr = new ArrayList<String>();
		for (int i=0; i<account.size(); i++) {
			if (idenity.get(i)!=0 && idenity.get(i)!=1) {
				arr.add(account.get(i)+" "+name.get(i));
			}
		}
		return arr;
	}
	
	public ArrayList[] printUsers() {
		ArrayList[] str = new ArrayList[4];
		str[0] = account;
		str[1] = password;
		str[2] = name;
		str[3] = idenity;
		return str;
	}
	
	public String[] printUserInf() {
		String[] str = {this.userAccount, userIdenity+"", userName};
		return str;
	}

	public String checkStudent(String studentNum) {
		if (account.indexOf(studentNum) != -1) {
			return name.get(account.indexOf(studentNum));
		}
		return "沒有這個人";
	}
	
	public String changeStudentInf(String oldStudentNum, String chaStudentNum, 
			String chaStudentPass, String chaStudentName, int chaStudentYear) throws IOException {
		if (!oldStudentNum.equals(chaStudentNum) && this.account.indexOf(chaStudentNum)!=-1) {
			return "已有此編號";
		}
		int x = this.account.indexOf(oldStudentNum);
		account.set(x,  chaStudentNum);
		password.set(x, chaStudentPass);
		name.set(x, chaStudentName);
		idenity.set(x, chaStudentYear);
		
		writeAccountfile();
		return "更改成功";
	}
	
	public String changeUser(String userName, String newAccount, String newPassword) throws IOException {
		if (name.indexOf(userName) == -1) {
			return "沒有這個用戶";
		}
		int a = name.indexOf(userName);
		account.set(a,  newAccount);
		password.set(a,  newPassword);
		
		writeAccountfile();
		return "修改成功";
	}
	
	public String delUser(String delAccount) throws IOException {
		if (this.account.indexOf(delAccount) == -1) {
			return "沒有這個人";
		}
		if (delAccount.equals(delAccount)) return "你無法刪除自己";
		int x = this.account.indexOf(delAccount);
		this.name.remove(x);
		this.password.remove(x);
		this.account.remove(x);
		this.idenity.remove(x);
		classes cl = new classes();
		ArrayList<ArrayList<Integer>> arr = cl.returnClass();
		for (int i=0; i<arr.get(0).size(); i++) {
			cl.delClassStudent(arr.get(0).get(i), arr.get(1).get(i), Integer.parseInt(delAccount));
		}
		writeAccountfile();
		return "刪除成功";
	}
	
	public String addUser(String account, String password, String name, int idenity) throws IOException {
		if (this.account.indexOf(account) != -1) {
			return "已有此學號";
		}
		int x=0;
		while (x<=this.account.size()-1 && this.idenity.get(x)<=idenity && Integer.parseInt(this.account.get(x))<Integer.parseInt(account)) x++;
		this.account.add(x, account);
		this.password.add(x, password);
		this.name.add(x, name);
		this.idenity.add(x, idenity);
		
		writeAccountfile();
		return "新增成功";
	}
	
	public String changePassword(String newPassword) throws IOException {
		password.set(account.indexOf(this.userAccount), newPassword);
		
		writeAccountfile();
		return "更改成功";
	}
	
	public String checkAccount(String num1, String num2) {
		if (account.contains(num1)) {
			if (password.get(account.indexOf(num1)).equals(num2)) {
				this.userAccount = num1;
				this.userIdenity = idenity.get(account.indexOf(num1));
				this.userName = name.get(account.indexOf(num1));
		 		return name.get(account.indexOf(num1));
			}
			return "密碼錯誤";
		}
		return "帳號錯誤";
	}
	
	public boolean openAccountfile() {
		try {
			sc = new Scanner(accounts);
			while (sc.hasNext()) {
				account.add(sc.next());
				password.add(sc.next());
				name.add(sc.next());
				idenity.add(sc.nextInt());
			}
			sc.close();
			return true;
		}
		catch (FileNotFoundException e) {
			return false;
		}
	}
	
	public void writeAccountfile() {
		FileWriter files;
		try {
			files = new FileWriter("account.txt");
			for(int i=0; i<account.size(); i++){
				files.write(this.account.get(i)+" "+this.password.get(i)+" "+
						this.name.get(i)+" "+this.idenity.get(i)+"\n");
			}
			files.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
