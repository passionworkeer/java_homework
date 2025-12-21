package com.ascent.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.ascent.bean.User;
import com.ascent.util.UserDataClient;
import com.ascent.util.EncryptionUtil;

/**
 * 用户登录窗口
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class LoginFrame extends JFrame {

	protected JTextField userText;

	protected JPasswordField password;

	protected JLabel tip;

	protected UserDataClient userDataClient;

	/**
	 * 默认的构造方法，初始化登录窗口
	 */
	public LoginFrame() {

		setTitle("用户登录");

		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());

		JPanel loginPanel = new JPanel();

		JLabel userLabel = new JLabel("用户账号：");
		JLabel passwordLabel = new JLabel("用户密码：");

		userText = new JTextField(15);
		password = new JPasswordField(15);

		JButton loginButton = new JButton("登录");
		JButton regist = new JButton("注册");
		JButton exitButton = new JButton("退出");

		loginPanel.add(userLabel);
		loginPanel.add(new JScrollPane(userText));
		loginPanel.add(passwordLabel);
		loginPanel.add(new JScrollPane(password));
		loginPanel.add(loginButton);
		loginPanel.add(regist);
		loginPanel.add(exitButton);

		setResizable(false);
		setSize(260, 150);
		setLocation(300, 100);

		JPanel tipPanel = new JPanel();

		tip = new JLabel();

		tipPanel.add(tip);

		container.add(BorderLayout.CENTER, loginPanel);
		container.add(BorderLayout.NORTH, tip);

		exitButton.addActionListener(new ExitActionListener());
		loginButton.addActionListener(new LoginActionListener());
		regist.addActionListener(new RegistActionListener());
		this.addWindowListener(new WindowCloser());
		try {
			userDataClient = new UserDataClient();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理"退出"按钮事件的监听器
	 */
	class ExitActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			setVisible(false);
			dispose();
			userDataClient.closeSocKet();
		}
	}

	/**
	 * 处理"登录"按钮事件的监听器
	 */
	class LoginActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			boolean bo = false;
			User userObject = null;
			System.out.println("开始登录验证...");
			HashMap userTable = userDataClient.getUsers();
			if (userTable != null) {
				System.out.println("用户数据获取成功，用户数量: " + userTable.size());
				System.out.println("尝试登录的用户名: " + userText.getText());
				if (userTable.containsKey(userText.getText())) {
					userObject = (User) userTable.get(userText.getText());
					char[] chr = password.getPassword();
					String pwd = new String(chr);
					System.out.println("输入的密码: " + pwd);
					System.out.println("存储的密码: " + userObject.getPassword());
					if (EncryptionUtil.verifyPassword(pwd, userObject.getPassword())) {
						bo = true;
						System.out.println("密码匹配成功");
					} else {
						System.out.println("密码不匹配");
					}
				} else {
					System.out.println("用户名不存在");
				}
				if (bo) {
					System.out.println("登录成功，关闭连接");
					userDataClient.closeSocKet();
					System.out.println("隐藏登录窗口");
					setVisible(false);
					System.out.println("销毁登录窗口");
					dispose();
					System.out.println("创建主界面，传递用户信息");
					MainFrame myFrame = new MainFrame(userObject);
					System.out.println("设置主界面可见");
					myFrame.setVisible(true);
					System.out.println("登录流程完成");
				} else {
					System.out.println("登录失败，显示错误提示");
					tip.setText("账号密码错误，请重新输入。");
				}
			} else {
				System.out.println("获取用户数据失败");
				tip.setText("获取用户数据失败，请稍后再试。");
			}
		}
	}

	/**
	 * 处理"注册"按钮事件的监听器.
	 */
	class RegistActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// 打开注册用户的窗口
			RegistFrame registFrame = new RegistFrame();
			registFrame.setVisible(true);
		}
	}

	/**
	 * 处理"关闭窗口"事件的监听器.
	 */
	class WindowCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			setVisible(false);
			dispose();
			userDataClient.closeSocKet();
		}
	}
}
