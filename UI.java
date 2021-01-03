package user;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

class baseUI extends  JFrame{
	private JButton btn_myData, btn_class, btn_Info, btn_logout, btn_scoreSys;

	public baseUI(user mainUser){
		int id = Integer.parseInt(mainUser.printUserInf()[1]);
		super.setTitle("資訊");
		super.setLayout(null);

		btn_myData = new JButton("我的資料");
		btn_myData.setBounds(10,5,90,20);
		super.add(btn_myData);
		btn_myData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				myDataUI stData = new myDataUI(mainUser);
				stData.setVisible(true);
				dispose();
			}
		});

		btn_class = new JButton("課程");
		btn_class.setBounds(105,5,90,20);
		super.add(btn_class);
		btn_class.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				classUI classUI = new classUI(mainUser, new int[]{1071});
				classUI.setVisible(true);
				dispose();
			}
		});

		if(id == 0) {
			btn_Info = new JButton("人員資訊");
			btn_scoreSys = new JButton("成績系統");
			btn_scoreSys.setBounds(295,5,90,20);
			super.add(btn_scoreSys);
			btn_scoreSys.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					scoreSysUI proUI = null;
					try {
						proUI = new scoreSysUI(mainUser);
					} catch (IOException ioException) {
						ioException.printStackTrace();
					}
					proUI.setVisible(true);
					dispose();
				}
			});
		}
		else if(id == 1)
			btn_Info = new JButton("成績系統");
		else
			btn_Info = new JButton("我的成績");
		btn_Info.setBounds(200,5,90,20);
		super.add(btn_Info);

		btn_Info.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(id==0) {
					PersonInfoUI adUI = new PersonInfoUI(mainUser);
					adUI.setVisible(true);
				}
				else if(id==1) {
					try {
						scoreSysUI proUI = new scoreSysUI(mainUser);
						proUI.setVisible(true);
					} catch (IOException ioException) {
						ioException.printStackTrace();
					}

				}
				else {
					try {
						studentScoreUI stUI =  new studentScoreUI(mainUser);
						stUI.setVisible(true);
					} catch (IOException ioException) {
						ioException.printStackTrace();
					}
				}
				dispose();
			}
		});

		btn_logout = new JButton("登出");
		btn_logout.setBounds(410,5,65,20);
		super.add(btn_logout);
		btn_logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginUI loginUI = new loginUI();
				loginUI.setVisible(true);
				dispose();
			}
		});

		super.setSize(500,500);
		super.setVisible(true);
		setDefaultCloseOperation(super.EXIT_ON_CLOSE);
	}
}

class loginUI extends JFrame{
	private JLabel lb_account, lb_password;
	private JTextField tf_account, tf_password;
	private JButton btn_login;
	
	public loginUI() {
		user mainUser = new classes();
		super.setTitle("登入");
		super.setLayout(null);
		
		lb_account = new JLabel("帳號");
		lb_account.setBounds(20,20,150,20);
		super.add(lb_account);
		tf_account = new JTextField("");
		tf_account.setBounds(20,45,150,20);
		super.add(tf_account);

		lb_password = new JLabel("密碼");
		lb_password.setBounds(20,70,150,20);
		super.add(lb_password);
		tf_password = new JTextField("");
		tf_password.setBounds(20,95,150,20);
		super.add(tf_password);

		btn_login = new JButton("登入");
		btn_login.setBounds(100,120,70,20);
		super.add(btn_login);
		
		super.setSize(300,300);
		super.setVisible(true);

		btn_login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String acc = tf_account.getText();
				String pass = tf_password.getText();
				String check = mainUser.checkAccount(acc,pass);
				int id = Integer.parseInt(mainUser.printUserInf()[1]);
				if(check.equals("密碼錯誤") || check.equals("帳號錯誤") || check.equals("無法找到檔案")){
					JOptionPane.showMessageDialog(null, check, "錯誤", JOptionPane.ERROR_MESSAGE);
				}
				else {
					myDataUI stData = new myDataUI(mainUser);
					stData.setVisible(true);
					dispose();
				}
			}
		});
		setDefaultCloseOperation(super.EXIT_ON_CLOSE);
	}
}

class myDataUI extends baseUI{
	private JLabel lb_stID, lb_name, lb_year;
	private JButton btn_changePassword;

	public myDataUI(user mainUser){
		super(mainUser);
		super.setTitle("我的資料");
		super.setLayout(null);

		if(Integer.parseInt(mainUser.printUserInf()[1]) == 0)
			lb_stID = new JLabel("身分 : 管理員");
		else if(Integer.parseInt(mainUser.printUserInf()[1]) == 1)
			lb_stID = new JLabel("身分 : 教授");
		else
			lb_stID = new JLabel("學號 : " + mainUser.printUserInf()[0]);
		lb_stID.setBounds(10,40,140,20);
		super.add(lb_stID);

		lb_name = new JLabel("姓名 : " + mainUser.printUserInf()[2]);
		lb_name.setBounds(10,65,140,20);
		super.add(lb_name);

		if(Integer.parseInt(mainUser.printUserInf()[1])>=107) {
			lb_year = new JLabel("入學年 :" + mainUser.printUserInf()[1]);
			lb_year.setBounds(10,90,140,20);
			super.add(lb_year);
		}

		btn_changePassword = new JButton("變更密碼");
		btn_changePassword.setBounds(10,115,120,20);
		super.add(btn_changePassword);
		btn_changePassword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePassword change = new changePassword(mainUser);
				change.setVisible(true);
				dispose();
			}
		});
	}
}

class classUI extends baseUI{

	classes cla = new classes();
	JTable tbl_classList = new JTable();
	String getYears;
	String getClass;


	public classUI(user mainUser, int[] year){
		super(mainUser);

		super.setTitle("課程");
		super.setLayout(null);
		String[][] str = showTable(mainUser,year);

		tbl_classList.setBounds(10,80,350,120);
		super.add(tbl_classList);

		JComboBox cb_class = new JComboBox();
		cb_class.setBounds(100,35,80,20);
		super.add(cb_class);

		cb_class.addItem("請選擇");
		for(int i =1; i<cla.printClass(year[0]).length+1; i++) {
			cb_class.addItem(str[i][2]);
		}

		cb_class.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getClass = (String) cb_class.getSelectedItem();//get the selected item
			}
		});

		JComboBox cb_years = new JComboBox();
		cb_years.setBounds(10,35,80,20);
		super.add(cb_years);
		cb_years.addItem("請選擇");
		cb_years.addItem("107-1");
		cb_years.addItem("107-2");
		cb_years.addItem("108-1");
		cb_years.addItem("108-2");
		cb_years.addItem("109-1");
		cb_years.addItem("109-2");

		cb_years.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getYears = (String) cb_years.getSelectedItem();//get the selected item

				switch (getYears) {
					case "107-1":
						year[0] = 1071;
						showTable(mainUser,year);

						cb_class.removeAllItems();
						cb_class.addItem("請選擇");
						for(int i =1; i<cla.printClass(year[0]).length+1; i++) {
							cb_class.addItem(showTable(mainUser,year)[i][2]);
						}
						break;
					case "107-2":
						year[0] = 1072;
						showTable(mainUser,year);

						cb_class.removeAllItems();
						cb_class.addItem("請選擇");
						for(int i =1; i<cla.printClass(year[0]).length+1; i++) {
							cb_class.addItem(showTable(mainUser,year)[i][2]);
						}
						break;
					case "108-1":
						year[0] = 1081;
						showTable(mainUser,year);

						cb_class.removeAllItems();
						cb_class.addItem("請選擇");
						for(int i =1; i<cla.printClass(year[0]).length+1; i++) {
							cb_class.addItem(showTable(mainUser,year)[i][2]);
						}
						break;
					case "108-2":
						year[0] = 1082;
						showTable(mainUser,year);

						cb_class.removeAllItems();
						cb_class.addItem("請選擇");
						for(int i =1; i<cla.printClass(year[0]).length+1; i++) {
							cb_class.addItem(showTable(mainUser,year)[i][2]);
						}
						break;
					case "109-1":
						year[0] = 1091;
						showTable(mainUser,year);

						cb_class.removeAllItems();
						cb_class.addItem("請選擇");
						for(int i =1; i<cla.printClass(year[0]).length+1; i++) {
							cb_class.addItem(showTable(mainUser,year)[i][2]);
						}
						break;
					case "109-2":
						year[0] = 1092;
						showTable(mainUser,year);

						cb_class.removeAllItems();
						cb_class.addItem("請選擇");
						for(int i =1; i<cla.printClass(year[0]).length+1; i++) {
							cb_class.addItem(showTable(mainUser,year)[i][2]);
						}
						break;
				}
			}
		});

		JButton btn_search = new JButton("查詢");
		btn_search.setBounds(220,35,60,20);
		super.add(btn_search);
		btn_search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(getYears != null && getClass != null){
					if(getYears == "請選擇")
						JOptionPane.showMessageDialog(null, "請選擇學年", "錯誤", JOptionPane.ERROR_MESSAGE);
					else if(getClass == "請選擇")
						JOptionPane.showMessageDialog(null, "請選擇課程", "錯誤", JOptionPane.ERROR_MESSAGE);
					else{
						classDetailUI CD = new classDetailUI(mainUser, getYears, getClass);
						CD.setVisible(true);
						dispose();
					}

				}

			}
		});

		if(mainUser.printUserInf()[1].equals("0")) {
			JButton btn_delClass = new JButton("刪除課程");
			btn_delClass.setBounds(290,35,85,20);
			super.add(btn_delClass);
			btn_delClass.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					getYears = (String) cb_years.getSelectedItem();
					getClass = (String) cb_class.getSelectedItem();
					try {
						String re = cla.delClasses(Integer.parseInt(getYears.split("-")[0] + getYears.split("-")[1]),getClass);	//只有課程名稱,沒有課程代碼
						if(re.equals("刪除成功")) {
							JOptionPane.showMessageDialog(null, "刪除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
							classUI sc = new classUI(mainUser, year);
							sc.setVisible(true);
							dispose();
						}
						else if(re.equals("沒有此課程")) {
							JOptionPane.showMessageDialog(null, "沒有此課程", "錯誤", JOptionPane.ERROR_MESSAGE);
						}

					} catch (IOException ioException) {
						ioException.printStackTrace();
					}
				}
			});

			JLabel lb_inputYear = new JLabel("學年");
			lb_inputYear.setBounds(5, 330, 45, 22);
			super.add(lb_inputYear);
			JTextField tf_inputYear = new JTextField();
			tf_inputYear.setBounds(5, 355, 45, 22);
			super.add(tf_inputYear);

			JLabel lb_inputClaNum = new JLabel("課程代碼");
			lb_inputClaNum.setBounds(60, 330, 55, 22);
			super.add(lb_inputClaNum);
			JTextField tf_inputClaNum = new JTextField();
			tf_inputClaNum.setBounds(60, 355, 55, 22);
			super.add(tf_inputClaNum);

			JLabel lb_inputClaName = new JLabel("課程名稱");
			lb_inputClaName.setBounds(125, 330, 55, 22);
			super.add(lb_inputClaName);
			JTextField tf_inputClaName = new JTextField();
			tf_inputClaName.setBounds(125, 355, 55, 22);
			super.add(tf_inputClaName);

			JLabel lb_inputCredit = new JLabel("學分");
			lb_inputCredit.setBounds(190, 330, 35, 22);
			super.add(lb_inputCredit);
			JTextField tf_inputCredit = new JTextField();
			tf_inputCredit.setBounds(190, 355, 35, 22);
			super.add(tf_inputCredit);

			JLabel lb_inputTeacher = new JLabel("授課教授");
			lb_inputTeacher.setBounds(235, 330, 55, 22);
			super.add(lb_inputTeacher);
			JTextField tf_inputTeacher = new JTextField();
			tf_inputTeacher.setBounds(235, 355, 55, 22);
			super.add(tf_inputTeacher);

			JLabel lb_inputType = new JLabel("課別");
			lb_inputType.setBounds(300, 330, 35, 22);
			super.add(lb_inputType);
			JTextField tf_inputType = new JTextField();
			tf_inputType.setBounds(300, 355, 35, 22);
			super.add(tf_inputType);

			JButton btn_editClass = new JButton("新增課程");
			btn_editClass.setBounds(345, 355, 85, 22);
			super.add(btn_editClass);
			btn_editClass.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					String newYear = tf_inputYear.getText();
					String newClaNum = tf_inputClaNum.getText();
					String newClaName = tf_inputClaName.getText();
					String newCredit = tf_inputCredit.getText();
					String newTeacher = tf_inputTeacher.getText();
					String newType = tf_inputType.getText();
					if(newYear.equals("") || newClaNum.equals("") || newClaName.equals("") || newCredit.equals("") || newTeacher.equals("") || newType.equals(""))
						JOptionPane.showMessageDialog(null, "輸入框不能為空", "錯誤", JOptionPane.ERROR_MESSAGE);
					else {
						if(newYear.length()>4) {
							String[] tmp = newYear.split("-");
							newYear = tmp[0] + tmp[1];
						}
						try {
							String re = cla.addClasses(Integer.parseInt(newYear), Integer.parseInt(newClaNum), newClaName, Integer.parseInt(newYear), newTeacher, newType);
							if(re.equals("新增成功")) {
								JOptionPane.showMessageDialog(null, "新增成功", "完成", JOptionPane.INFORMATION_MESSAGE);
								classUI sc = new classUI(mainUser, year);
								sc.setVisible(true);
								dispose();
							}
							else {
								JOptionPane.showMessageDialog(null, "已有此課程", "錯誤", JOptionPane.ERROR_MESSAGE);
							}

						} catch (IOException ioException) {
							ioException.printStackTrace();
						}
					}

				}
			});
		}
	}

	private String[][] showTable(user mainUser, int[] year) {

		String[][] str = new String[cla.printClass(year[0]).length+1][];

		str[0] = new String[]{"學年", "課程代碼", "課程名稱", "學分", "授課教授", "課別"};
		for(int i = 1; i<cla.printClass(year[0]).length+1; i++)
			str[i] = cla.printClass(year[0])[i-1].split(" ");

		Object[] columnNames = {"學年", "課程代碼", "課程名稱", "學分", "授課教授", "課別"};
		Object[][] rowData = str;

		tbl_classList.setEnabled(false);

		TableModel dataModel = new DefaultTableModel(str, columnNames);
		tbl_classList.setModel(dataModel);

		return str;
	}
}
class classDetailUI extends baseUI{
	private JLabel lb_year, lb_classNumber, lb_className,lb_credit, lb_teacher, lb_type, lb_classPeople;
	private JTextArea ta_classPeople;
	private JButton btn_editClass, btn_addStudent, btn_delStudent;
	private JTextField tf_addSt;
	private JComboBox cb_delSt;

	public classDetailUI(user mainUser, String year, String Class) {
		super(mainUser);
		super.setTitle("課程資訊");

		classes cla = new classes();
		String[] sp = year.split("-");
		String[] classes = cla.returnClass(Integer.parseInt(sp[0]+sp[1]),Class);

		lb_year = new JLabel("學年 : " + year);
		lb_year.setBounds(5,40,200,20);
		super.add(lb_year);
		lb_classNumber = new JLabel("課程代碼 : " + String.format("%06d",Integer.parseInt(classes[1])));
		lb_classNumber.setBounds(5,65,200,20);
		super.add(lb_classNumber);
		lb_className = new JLabel("課程名稱 : " + classes[2]);
		lb_className.setBounds(5,90,200,20);
		super.add(lb_className);
		lb_credit = new JLabel("學分 : " + classes[3]);
		lb_credit.setBounds(5,115,200,20);
		super.add(lb_credit);
		lb_teacher = new JLabel("授課教授 : " + classes[4]);
		lb_teacher.setBounds(5,140,200,20);
		super.add(lb_teacher);
		lb_type = new JLabel("課別 : " + classes[5]);
		lb_type.setBounds(5,165,200,20);
		super.add(lb_type);

		String classPeople = "";
		String[] student = cla.printClassStudent(Integer.parseInt(classes[0]),Integer.parseInt(classes[1]));
		for(int i = 0; i<student.length; i+=2) {
			if(student[i] == null)
				break;
			classPeople += student[i] + " ";
			classPeople += student[i+1] + "    ";
			if(i%6 == 4) {
				classPeople += "\n";
			}
		}

		ta_classPeople = new JTextArea();
		ta_classPeople.setText(classPeople);
		ta_classPeople.setBounds(10,220,330,100);
		ta_classPeople.setEnabled(false);
		super.add(ta_classPeople);

		lb_classPeople = new JLabel("修課學生 : ");
		lb_classPeople.setBounds(5,190,110,20);
		super.add(lb_classPeople);

		if(Integer.parseInt(mainUser.printUserInf()[1]) == 0) {
			tf_addSt = new JTextField();
			tf_addSt.setBounds(80,190, 80, 20);
			super.add(tf_addSt);

			btn_addStudent = new JButton("新增學生");
			btn_addStudent.setBounds(165,190, 85, 20);
			super.add(btn_addStudent);
			btn_addStudent.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String getNum = tf_addSt.getText();
					if(getNum.equals("")) {
						JOptionPane.showMessageDialog(null, "輸入框不能為空", "錯誤", JOptionPane.ERROR_MESSAGE);
						return;
					}

					String returnStr = cla.addClassStudent(Integer.parseInt(classes[0]) ,Integer.parseInt(classes[1]), Integer.parseInt(getNum));
					if(returnStr.equals("沒有此學生")) {
						JOptionPane.showMessageDialog(null, "沒有此學生", "錯誤", JOptionPane.ERROR_MESSAGE);
					}
					else if(returnStr.equals("已有此學生")) {
						JOptionPane.showMessageDialog(null, "已有此學生", "錯誤", JOptionPane.ERROR_MESSAGE);
					}
					else {
						JOptionPane.showMessageDialog(null, "新增成功", "成功", JOptionPane.INFORMATION_MESSAGE);
						classDetailUI cd = new classDetailUI( mainUser,  year,  Class);
						cd.setVisible(true);
						dispose();
					}
				}
			});

			cb_delSt = new JComboBox();
			cb_delSt.setBounds(275,190, 110, 20);
			cb_delSt.addItem("請選擇學生");
			for(int i = 0; i<student.length; i+=2) {
				if(student[i] == null)
					break;
				cb_delSt.addItem(student[i] + " " + student[i+1]);
			}
			super.add(cb_delSt);

			btn_delStudent = new JButton("刪除學生");
			btn_delStudent.setBounds(390,190, 85, 20);
			super.add(btn_delStudent);
			btn_delStudent.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String delSt = (String) cb_delSt.getSelectedItem();
					if(delSt.equals("請選擇學生")) {
						return;
					}

					String returnStr = cla.delClassStudent(Integer.parseInt(classes[0]) ,Integer.parseInt(classes[1]), Integer.parseInt(delSt.split(" ")[0]));
					if(returnStr.equals("沒有此課程或人")) {
						JOptionPane.showMessageDialog(null, "沒有此課程或人", "錯誤", JOptionPane.ERROR_MESSAGE);
					}
					else {
						JOptionPane.showMessageDialog(null, "刪除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
						classDetailUI cd = new classDetailUI( mainUser,  year,  Class);
						cd.setVisible(true);
						dispose();
					}
				}
			});

			JLabel lb_inputYear = new JLabel("學年");
			lb_inputYear.setBounds(5, 330, 45, 22);
			super.add(lb_inputYear);
			JTextField tf_inputYear = new JTextField(year);
			tf_inputYear.setBounds(5, 355, 45, 22);
			super.add(tf_inputYear);

			JLabel lb_inputClaNum = new JLabel("課程代碼");
			lb_inputClaNum.setBounds(60, 330, 55, 22);
			super.add(lb_inputClaNum);
			JTextField tf_inputClaNum = new JTextField(String.format("%06d",Integer.parseInt(classes[1])));
			tf_inputClaNum.setBounds(60, 355, 55, 22);
			super.add(tf_inputClaNum);

			JLabel lb_inputClaName = new JLabel("課程名稱");
			lb_inputClaName.setBounds(125, 330, 55, 22);
			super.add(lb_inputClaName);
			JTextField tf_inputClaName = new JTextField(classes[2]);
			tf_inputClaName.setBounds(125, 355, 55, 22);
			super.add(tf_inputClaName);

			JLabel lb_inputCredit = new JLabel("學分");
			lb_inputCredit.setBounds(190, 330, 35, 22);
			super.add(lb_inputCredit);
			JTextField tf_inputCredit = new JTextField(classes[3]);
			tf_inputCredit.setBounds(190, 355, 35, 22);
			super.add(tf_inputCredit);

			JLabel lb_inputTeacher = new JLabel("授課教授");
			lb_inputTeacher.setBounds(235, 330, 55, 22);
			super.add(lb_inputTeacher);
			JTextField tf_inputTeacher = new JTextField(classes[4]);
			tf_inputTeacher.setBounds(235, 355, 55, 22);
			super.add(tf_inputTeacher);

			JLabel lb_inputType = new JLabel("課別");
			lb_inputType.setBounds(300, 330, 35, 22);
			super.add(lb_inputType);
			JTextField tf_inputType = new JTextField(classes[5]);
			tf_inputType.setBounds(300, 355, 35, 22);
			super.add(tf_inputType);

			btn_editClass = new JButton("確認修改");
			btn_editClass.setBounds(345, 355, 85, 22);
			super.add(btn_editClass);
			btn_editClass.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					String newYear = tf_inputYear.getText();
					String newClaNum = tf_inputClaNum.getText();
					String newClaName = tf_inputClaName.getText();
					String newCredit = tf_inputCredit.getText();
					String newTeacher = tf_inputTeacher.getText();
					String newType = tf_inputType.getText();
					if(newYear.equals("") || newClaNum.equals("") || newClaName.equals("") || newCredit.equals("") || newTeacher.equals("") || newType.equals(""))
						JOptionPane.showMessageDialog(null, "輸入框不能為空", "錯誤", JOptionPane.ERROR_MESSAGE);
					else {
						String[] tmp = newYear.split("-");
						try {
							cla.changeClasses(Integer.parseInt(classes[1]), Integer.parseInt(newClaNum), Integer.parseInt(tmp[0]+tmp[1]), newClaName, Integer.parseInt(newCredit), newTeacher, newType);
						} catch (IOException ioException) {
							ioException.printStackTrace();
						}
					}


					lb_year.setText("學年 : " + newYear);
					lb_classNumber.setText("課程代碼 : " + newClaNum);
					lb_className.setText("課程名稱 : " + newClaName);
					lb_credit.setText("學分 : " + newCredit);
					lb_teacher.setText("授課教授 : " + newTeacher);
					lb_type.setText("課別 : " + newType);

					JOptionPane.showMessageDialog(null, "修改完成", "完成", JOptionPane.INFORMATION_MESSAGE);
				}
			});
		}
	}
}

class PersonInfoUI extends baseUI{
	private JButton btn_newAcc;

	ArrayList[] users;

	public PersonInfoUI(user mainUser){
		super(mainUser);
		super.setTitle("人員資訊");
		super.setLayout(null);

		users = mainUser.printUsers();
		JButton[] btn_users = new JButton[users[0].size()];

		btn_newAcc = new JButton("新增帳戶");
		btn_newAcc.setBounds(10,35,90,25);
		super.add(btn_newAcc);
		btn_newAcc.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
				try {
					addAccountUI adAcc = new addAccountUI(mainUser);
					adAcc.setVisible(true);
					dispose();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
	    });

		JTable tbl_users = new JTable();
		tbl_users.setBounds(10,65,350,400);
		tbl_users.setEnabled(false);
		Object[] columnNames = {"帳號", "密碼", "姓名", "身分"};
		String[][] str = new String[users[0].size()+1][4];
		str[0] = new String[]{"帳號", "密碼", "姓名", "身分"};

		for(int i = 0; i<users[0].size(); i++) {
			String tmp = " ";
			for (int j = 0; j < 4; j++) {
				str[i + 1][j] = users[j].get(i) + "";
				tmp += users[j].get(i) + " ";
			}

			btn_users[i] = new JButton("修改");
			btn_users[i].setBounds(370,81 + 16*i,60,15);
			int finalI = i;
			String finalTmp = tmp;
			btn_users[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						changeClassUI ch = new changeClassUI(mainUser, finalTmp, users[0].get(finalI)+"");
					} catch (IOException ioException) {
						ioException.printStackTrace();
					}
					dispose();
				}
			});
			super.add(btn_users[i]);
		}

		TableModel dataModel = new DefaultTableModel(str, columnNames);
		tbl_users.setModel(dataModel);
		super.add(tbl_users);
	}
}

class addAccountUI extends JFrame{
	private JLabel lb_title, lb_acc, lb_pass, lb_name, lb_year;
	private JTextField tf_acc, tf_pass, tf_name, tf_year;
	private JButton btn_change, btn_cancel;

	addAccountUI(user mainUser) throws IOException {

		super.setTitle("新增帳戶");
		super.setLayout(null);
		lb_title = new JLabel("新增帳戶");
		lb_title.setBounds(10,5,250,25);
		super.add(lb_title);

		lb_acc = new JLabel("帳號 : ");
		lb_pass = new JLabel("密碼 : ");
		lb_name = new JLabel("姓名 : ");
		lb_year = new JLabel("學年 : ");
		lb_acc.setBounds(10,35,70,20);
		lb_pass.setBounds(90,35,70,20);
		lb_name.setBounds(170,35,70,20);
		lb_year.setBounds(250,35,70,20);
		super.add(lb_acc);
		super.add(lb_pass);
		super.add(lb_name);
		super.add(lb_year);

		tf_acc = new JTextField();
		tf_pass = new JTextField();
		tf_name = new JTextField();
		tf_year = new JTextField();
		tf_acc.setBounds(10,60,70,20);
		tf_pass.setBounds(90,60,70,20);
		tf_name.setBounds(170,60,70,20);
		tf_year.setBounds(250,60,70,20);
		super.add(tf_acc);
		super.add(tf_pass);
		super.add(tf_name);
		super.add(tf_year);

		btn_change = new JButton("新增");
		btn_change.setBounds(330,60,60,20);
		super.add(btn_change);
		btn_change.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!tf_acc.getText().equals("")  && !tf_pass.getText().equals("") && !tf_name.getText().equals("") &&!tf_year.getText().equals("")) {
					try {
						if(mainUser.addUser(tf_acc.getText(), tf_pass.getText(), tf_name.getText(),Integer.parseInt(tf_year.getText())).equals("已有此學號")) {
							JOptionPane.showMessageDialog(null, "已有此學號", "錯誤", JOptionPane.ERROR_MESSAGE);
						}
						else {
							PersonInfoUI ad = new PersonInfoUI(mainUser);
							ad.setVisible(true);
							dispose();
						}

					} catch (IOException ioException) {
						ioException.printStackTrace();
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "輸入錯誤", "錯誤", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btn_cancel = new JButton("取消");
		btn_cancel.setBounds(400,60,60,20);
		super.add(btn_cancel);
		btn_cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PersonInfoUI ad = new PersonInfoUI(mainUser);
				ad.setVisible(true);
				dispose();
			}
		});

		super.setSize(500,150);
		setDefaultCloseOperation(super.EXIT_ON_CLOSE);
		super.setVisible(true);
	}
}

class changeClassUI extends JFrame{
	private JLabel lb_oldInfo;
	private JTextField tf_acc, tf_pass, tf_name, tf_year;
	private JButton btn_change, btn_del;

	changeClassUI(user mainUser, String old_Info, String old_acc) throws IOException {

		String[] old_InfoSP = old_Info.split(" ");
		super.setTitle("修改");
		super.setLayout(null);
		lb_oldInfo = new JLabel(old_Info);
		lb_oldInfo.setBounds(10,10,250,25);
		super.add(lb_oldInfo);

		tf_acc = new JTextField(old_InfoSP[1]);
		tf_pass = new JTextField(old_InfoSP[2]);
		tf_name = new JTextField(old_InfoSP[3]);
		tf_year = new JTextField(old_InfoSP[4]);
		tf_acc.setBounds(10,60,70,20);
		tf_pass.setBounds(90,60,70,20);
		tf_name.setBounds(170,60,70,20);
		tf_year.setBounds(250,60,70,20);
		super.add(tf_acc);
		super.add(tf_pass);
		super.add(tf_name);
		super.add(tf_year);

		btn_change = new JButton("修改");
		btn_change.setBounds(330,60,60,20);
		super.add(btn_change);

		btn_change.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!tf_acc.getText().equals("")  && !tf_pass.getText().equals("") && !tf_name.getText().equals("") &&!tf_year.getText().equals("")) {
					try {
						if(mainUser.changeStudentInf(old_acc,tf_acc.getText(),tf_pass.getText(),tf_name.getText(),Integer.parseInt(tf_year.getText())).equals("已有此編號")) {
							JOptionPane.showMessageDialog(null, "已有此帳號", "錯誤", JOptionPane.ERROR_MESSAGE);
						}
						else {
							PersonInfoUI ad = new PersonInfoUI(mainUser);
							ad.setVisible(true);
							dispose();
						}
					} catch (IOException ioException) {
						ioException.printStackTrace();
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "輸入錯誤", "錯誤", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btn_del = new JButton("刪除");
		btn_del.setBounds(400,60,60,20);
		super.add(btn_del);
		btn_del.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					mainUser.delUser(old_acc);
					PersonInfoUI ad = new PersonInfoUI(mainUser);
					ad.setVisible(true);
					dispose();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
		});

		super.setSize(500,150);
		setDefaultCloseOperation(super.EXIT_ON_CLOSE);
		super.setVisible(true);
	}
}

class scoreSysUI extends baseUI{
	private JComboBox cb_classes, cb_student;
	private JTable tb_classes;
	score sc = new score();
	classes cla = new classes();

	public scoreSysUI(user mainUser) throws IOException {
		super(mainUser);
		super.setTitle("成績系統");
		super.setLayout(null);

		cb_classes = new JComboBox();
		cb_classes.setBounds(10,35,100,20);
		cb_classes.addItem("請選擇課程");		// 下拉選單顯示此教授所有的課程


		if(mainUser.printUserInf()[1].equals("1")) {
			String[] tmpClass = new String[sc.generateAllCourseGrade(mainUser.printUserInf()[2]).length];
			for(int i=0; i<sc.generateAllCourseGrade(mainUser.printUserInf()[2]).length; i++) {
				cb_classes.addItem(sc.generateAllCourseGrade(mainUser.printUserInf()[2])[i].split(" ")[0] + "-" + sc.generateAllCourseGrade(mainUser.printUserInf()[2])[i].split(" ")[2]);
			}
		}
		else if(mainUser.printUserInf()[1].equals("0")) {
			String[] tmpClass = cla.printClassAll();
			for(int i=0; i<tmpClass.length; i++) {
				cb_classes.addItem(tmpClass[i]);
			}

			cb_student = new JComboBox();
			cb_student.setBounds(300,35,150,20);
			cb_student.addItem("請選擇學生");
			ArrayList<String> stArr = mainUser.printStudent();
			for(int i = 0; i<stArr.size(); i++) {
				cb_student.addItem(stArr.get(i));
			}
			cb_student.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String getStudent = (String)cb_student.getSelectedItem();
					try {
						changeScoreUI cs = new changeScoreUI(mainUser, getStudent, getStudent);
						cs.setVisible(true);
						dispose();
					} catch (IOException ioException) {
						ioException.printStackTrace();
					}
				}
			});
			super.add(cb_student);
		}
		cb_classes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String getClass = (String) cb_classes.getSelectedItem(); //get the selected item
				String show = "";
				if(getClass.equals("請選擇課程"))
					return;
				String[] Class = getClass.split("-");

				try {
					String[][] tmp = new String[sc.getGrade().length][];

					for(int i = 0; i<tmp.length; i++) {
						tmp[i] = sc.getGrade()[i].split(" ");
					}
					for(int i = 0; i<tmp.length; i++) {
						if(tmp[i][0].equals(Class[0]) && tmp[i][2].equals(Class[1])) {
							for(int j = 0; j<tmp[i].length; j++) {
								show += tmp[i][j] + " ";
							}
						}
					}
					changeScoreUI cs = new changeScoreUI(mainUser, getClass, show);
					cs.setVisible(true);
					dispose();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
		});
		super.add(cb_classes);
	}
}

class changeScoreUI extends baseUI{
	private JLabel lb_class;
	private JComboBox cb_classes, cb_student;
	score sc = new score();
	classes cla = new classes();

	changeScoreUI(user mainUser, String Class,  String show) throws IOException {
		super(mainUser);

		lb_class = new JLabel(Class);
		lb_class.setBounds(130,35,200,20);
		super.add(lb_class);
		cb_classes = new JComboBox();
		cb_classes.setBounds(10,35,100,20);
		cb_classes.addItem("請選擇課程");		// 下拉選單顯示此教授所有的課程

		if(mainUser.printUserInf()[1].equals("1"))
		{
			String[] tmpClass = new String[sc.generateAllCourseGrade(mainUser.printUserInf()[2]).length];
			for(int i=0; i<sc.generateAllCourseGrade(mainUser.printUserInf()[2]).length; i++) {
				cb_classes.addItem(sc.generateAllCourseGrade(mainUser.printUserInf()[2])[i].split(" ")[0] + "-" + sc.generateAllCourseGrade(mainUser.printUserInf()[2])[i].split(" ")[2]);
			}
		}
		else if(mainUser.printUserInf()[1].equals("0")) {
			String[] tmpClass = cla.printClassAll();
			for(int i=0; i<tmpClass.length; i++) {
				cb_classes.addItem(tmpClass[i]);
			}

			cb_student = new JComboBox();
			cb_student.setBounds(300,35,150,20);
			cb_student.addItem("請選擇學生");
			ArrayList<String> stArr = mainUser.printStudent();
			for(int i = 0; i<stArr.size(); i++) {
				cb_student.addItem(stArr.get(i));
			}
			cb_student.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String getStudent = (String)cb_student.getSelectedItem();
					try {
						changeScoreUI cs = new changeScoreUI(mainUser, getStudent, getStudent);
						cs.setVisible(true);
						dispose();
					} catch (IOException ioException) {
						ioException.printStackTrace();
					}
				}
		 	});
			super.add(cb_student);
		}

		cb_classes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String getClass = (String) cb_classes.getSelectedItem(); //get the selected item

				if(getClass.equals("請選擇課程"))
					return;
				String[] Class = getClass.split("-");

				try {
					String[][] tmp = new String[sc.getGrade().length][];
					String show = "";
					for(int i = 0; i<tmp.length; i++) {
						tmp[i] = sc.getGrade()[i].split(" ");
					}
					for(int i = 0; i<tmp.length; i++) {
						if(tmp[i][0].equals(Class[0]) && tmp[i][2].equals(Class[1])) {
							for(int j = 0; j<tmp[i].length; j++) {
								show += tmp[i][j] + " ";
							}
						}
					}
					changeScoreUI cs = new changeScoreUI(mainUser, getClass, show);
					cs.setVisible(true);
					dispose();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
		});

		super.add(cb_classes);

		if(show.charAt(9) == ' ') {
			JTable tb_score;
			String studentID = Class.split(" ")[0];
			String[][] str = new String[sc.printGrade(studentID).length+1][3];

			for(int i=0; i<sc.printGrade(studentID).length; i++) {
				str[i+1] = sc.printGrade(studentID)[i].split(" ");
			}

			Object[] columnNames = {"學年", "課程名稱", "成績"};
			str[0] = new String[]{"學年", "課程名稱", "成績"};

			tb_score = new JTable(str,columnNames);
			tb_score.setBounds(10,65,350,200);
			tb_score.setEnabled(false);
			super.add(tb_score);
		}
		else {
			String[] showArr = show.split(" ");
			String[][] str = new String[showArr.length-2][4];
			JButton[] btn_changeArr = new JButton[showArr.length-3];

			for(int i = 3; i<showArr.length; i++) {

				char[] tmp = showArr[i].toCharArray();
				String tmpStr = "";
				str[i-2][0] = i-2 + "";
				for(int j = 0; j<9; j++) {
					tmpStr += tmp[j];
				}
				str[i-2][1] = tmpStr;
				tmpStr = "";
				for(int j = 9; j<showArr[i].split("-")[0].length(); j++) {
					tmpStr += tmp[j];
				}
				str[i-2][2] = tmpStr;
				str[i-2][3] = showArr[i].split("-")[1];

				btn_changeArr[i-3] = new JButton("修改成績");
				btn_changeArr[i-3].setBounds(370,65+16*(i-2),90,15);
				super.add(btn_changeArr[i-3]);
				int finalI = i;
				btn_changeArr[i-3].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						changeStudentScoreUI css = new changeStudentScoreUI(mainUser, str[finalI-2], str[finalI-2][3], Class);
						css.setVisible(true);
					}
				});
			}

			JTable tbl_users = new JTable();
			tbl_users.setBounds(10,65,350,200);
			tbl_users.setEnabled(false);
			Object[] columnNames = {"序號", "學號", "姓名", "成績"};
			str[0] = new String[]{"序號", "學號", "姓名", "成績"};

			TableModel dataModel = new DefaultTableModel(str, columnNames);
			tbl_users.setModel(dataModel);
			super.add(tbl_users);
		}
	}
}

class changeStudentScoreUI extends JFrame {
	private  JLabel lb_info, lb_classInfo, lb_enterScore;
	private  JTextField tf_newScore;
	private  JButton btn_save;
	score sc = new score();

	public changeStudentScoreUI(user mainUser, String[] info, String oldScore, String classInfo) {
		lb_classInfo = new JLabel(classInfo);
		lb_classInfo.setBounds(15,5,250,20);
		super.add(lb_classInfo);

		lb_info = new JLabel(info[1] + " " + info[2] + "  分數 : " + info[3]);
		lb_info.setBounds(15,30,250,20);
		super.add(lb_info);

		lb_enterScore = new JLabel("輸入成績 : ");
		lb_enterScore.setBounds(15,60,65,20);
		super.add(lb_enterScore);

		tf_newScore = new JTextField(oldScore);
		tf_newScore.setBounds(80,60,80,20);
		super.add(tf_newScore);

		btn_save = new JButton("儲存");
		btn_save.setBounds(170,60,60,20);
		super.add(btn_save);
		btn_save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String newScore = tf_newScore.getText();
				if(newScore.equals("")) {
					JOptionPane.showMessageDialog(null, "請輸入成績", "錯誤", JOptionPane.ERROR_MESSAGE);
				}
				else {
					String[] tmp = classInfo.split("-");
					try {
						String re = sc.updateScore(tmp[0], tmp[1], info[2], Integer.parseInt(newScore));
						if (re.equals("成功")) {
							JOptionPane.showMessageDialog(null, "修改成功", "成功", JOptionPane.INFORMATION_MESSAGE);
							dispose();
						}

					} catch (IOException ioException) {
						ioException.printStackTrace();
					}

				}
			}
		});

		super.setTitle("修改成績");
		super.setLayout(null);
		super.setSize(300,200);
		super.setVisible(true);
	}
}

class studentScoreUI extends baseUI {
	private JTable tb_score;
	private JButton btn_search;
	private JComboBox cb_years;

	public studentScoreUI(user mainUser) throws IOException {
		super(mainUser);
		score sc = new score();

		String[] sem = sc.checkSemester(mainUser.printUserInf()[0]);
		cb_years = new JComboBox();
		cb_years.addItem("請選擇學年");
		for(int i=0; i<sem.length; i++) {
			cb_years.addItem(sem[i]);
		}
		cb_years.addItem("總成績");
		cb_years.setBounds(10,40,100,20);
		super.add(cb_years);

		btn_search = new JButton("查詢成績");
		btn_search.setBounds(130,40,90,20);
		super.add(btn_search);

		btn_search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cb_years.getSelectedItem().equals("請選擇學年")) {
					JOptionPane.showMessageDialog(null, "請選擇學年", "錯誤", JOptionPane.ERROR_MESSAGE);
				}
				else if(cb_years.getSelectedItem().equals("總成績")) {
					try {
						studentScoreUI ss = new studentScoreUI(mainUser);
						ss.setVisible(true);
						dispose();
					} catch (IOException ioException) {
						ioException.printStackTrace();
					}
				}
				else {
					try {
						yearScoreUI ys = new yearScoreUI(mainUser, (String)cb_years.getSelectedItem());
						ys.setVisible(true);
						dispose();
					} catch (IOException ioException) {
						ioException.printStackTrace();
					}
				}
			}
		});

		String[][] str = new String[sc.printGrade(mainUser.printUserInf()[0]).length+1][3];

		for(int i=0; i<sc.printGrade(mainUser.printUserInf()[0]).length; i++) {
			str[i+1] = sc.printGrade(mainUser.printUserInf()[0])[i].split(" ");
		}

		Object[] columnNames = {"學年", "課程名稱", "成績"};
		str[0] = new String[]{"學年", "課程名稱", "成績"};

		tb_score = new JTable(str,columnNames);
		tb_score.setBounds(10,95,350,200);
		tb_score.setEnabled(false);
		super.add(tb_score);
		super.setTitle("我的成績");
		super.setLayout(null);
	}
}

class yearScoreUI extends baseUI{
	private JLabel lb_info;
	private JTable tb_score;
	private JComboBox cb_years;
	private JButton btn_search;

	public yearScoreUI(user mainUser, String year) throws IOException {
		super(mainUser);
		super.setTitle("學年成績");
		super.setLayout(null);

		score sc = new score();
		lb_info = new JLabel(year + " 學期成績");

		String[] sem = sc.checkSemester(mainUser.printUserInf()[0]);
		cb_years = new JComboBox();
		cb_years.addItem("請選擇學年");
		for(int i=0; i<sem.length; i++) {
			cb_years.addItem(sem[i]);
		}
		cb_years.setBounds(10,40,100,20);
		cb_years.addItem("總成績");
		super.add(cb_years);

		btn_search = new JButton("查詢成績");
		btn_search.setBounds(130,40,90,20);
		super.add(btn_search);

		btn_search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cb_years.getSelectedItem().equals("請選擇學年")) {
					JOptionPane.showMessageDialog(null, "請選擇學年", "錯誤", JOptionPane.ERROR_MESSAGE);
				}
				else if(cb_years.getSelectedItem().equals("總成績")) {
					try {
						studentScoreUI ss = new studentScoreUI(mainUser);
						ss.setVisible(true);
						dispose();
					} catch (IOException ioException) {
						ioException.printStackTrace();
					}
				}
				else {
					try {
						yearScoreUI ys = new yearScoreUI(mainUser, (String)cb_years.getSelectedItem());
						ys.setVisible(true);
						dispose();
					} catch (IOException ioException) {
						ioException.printStackTrace();
					}
				}
			}
		});

		String[][] str = new String[sc.checkGrade(year, mainUser.printUserInf()[0]).length+1][3];

		for(int i=0; i<sc.checkGrade(year, mainUser.printUserInf()[0]).length; i++) {
			str[i+1] = sc.checkGrade(year, mainUser.printUserInf()[0])[i].split(" ");
		}

		Object[] columnNames = {"學年", "課程名稱", "成績"};
		str[0] = new String[]{"學年", "課程名稱", "成績"};

		tb_score = new JTable(str,columnNames);

		tb_score.setBounds(10,75,350,200);
		tb_score.setEnabled(false);
		super.add(tb_score);
	}
}

class changePassword extends baseUI{
	private JLabel lb_oldPassword, lb_newPassword, lb_check;
	private JTextField tf_oldPassword, tf_newPassword, tf_check;
	private JButton btn_update;

	public changePassword(user mainUser) {
		super(mainUser);

		super.setTitle("變更密碼");
		super.setLayout(null);

		lb_oldPassword = new JLabel("密碼");
		lb_oldPassword.setBounds(5,25,200,20);
		super.add(lb_oldPassword);
		tf_oldPassword = new JTextField("");
		tf_oldPassword.setBounds(5,50,200,20);
		super.add(tf_oldPassword);
		lb_newPassword = new JLabel("新密碼");
		lb_newPassword.setBounds(5,75,200,20);
		super.add(lb_newPassword);
		tf_newPassword = new JTextField("");
		tf_newPassword.setBounds(5,100,200,20);
		super.add(tf_newPassword);
		lb_check = new JLabel("確認密碼");
		lb_check.setBounds(5,125,200,20);
		super.add(lb_check);
		tf_check = new JTextField("");
		tf_check.setBounds(5,150,200,20);
		super.add(tf_check);

		btn_update = new JButton("變更密碼");
		btn_update.setBounds(120,175,85,20);
		super.add(btn_update);

		super.setSize(500,400);
		super.setVisible(true);

		btn_update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String oldPass = tf_oldPassword.getText();
				String newPass = tf_newPassword.getText();
				String check = tf_check.getText();

				if(mainUser.checkPass(oldPass)) {
					if(newPass.equals(check)) {
						String msg = null;
						try {
							msg = mainUser.changePassword(newPass);
						} catch (IOException ioException) {
							ioException.printStackTrace();
						}
						JOptionPane.showMessageDialog(null, msg, "完成", JOptionPane.INFORMATION_MESSAGE);
						myDataUI stData = new myDataUI(mainUser);
						stData.setVisible(true);
						dispose();
					}
					else {
						JOptionPane.showMessageDialog(null, "密碼錯誤", "錯誤", JOptionPane.ERROR_MESSAGE);
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "密碼錯誤", "錯誤", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}

public class UI {
	public static void main(String[] args) {
		loginUI mainWindow = new loginUI();

		mainWindow.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
	}
}
