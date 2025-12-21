package com.ascent.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.ascent.bean.User;

/**
 * 艾斯医药系统主界面
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	/**
	 * 标签页面板
	 */
	protected JTabbedPane tabbedPane;

	/**
	 * 产品面板
	 */
	protected ProductPanel productPanel;

	/**
	 * 当前登录用户
	 */
	protected User currentUser;

	/**
	 * 管理员权限常量
	 */
	private static final int ADMIN_AUTHORITY = 1;

	/**
	 * 普通用户权限常量
	 */
	private static final int USER_AUTHORITY = 0;

	/**
	 * 默认构造方法，用于测试
	 */
	public MainFrame() {
		this(null);
	}

	/**
	 * 带用户参数的构造方法
	 * @param user 当前登录用户
	 */
	public MainFrame(User user) {
		this.currentUser = user;
		initUI();
	}

	/**
	 * 初始化用户界面
	 */
	private void initUI() {

		setTitle("欢迎使用AscentSys应用! ");

		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());

		tabbedPane = new JTabbedPane();

		productPanel = new ProductPanel(this);
		tabbedPane.addTab("产品", productPanel);

		container.add(BorderLayout.CENTER, tabbedPane);

		JMenuBar myMenuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("文件");

		JMenu openMenu = new JMenu("打开");
		JMenuItem localMenuItem = new JMenuItem("本地文件...");
		openMenu.add(localMenuItem);

		JMenuItem networkMenuItem = new JMenuItem("网络...");
		openMenu.add(networkMenuItem);

		JMenuItem webMenuItem = new JMenuItem("网页...");
		openMenu.add(webMenuItem);
		fileMenu.add(openMenu);

		JMenuItem saveMenuItem = new JMenuItem("保存");
		fileMenu.add(saveMenuItem);

		JMenuItem exitMenuItem = new JMenuItem("退出");
		fileMenu.add(exitMenuItem);

		myMenuBar.add(fileMenu);

		exitMenuItem.addActionListener(new ExitActionListener());

		setupLookAndFeelMenu(myMenuBar);

		JMenu helpMenu = new JMenu("帮助");
		JMenuItem aboutMenuItem = new JMenuItem("关于");
		JMenuItem customerServiceMenuItem = new JMenuItem("智能客服");
		helpMenu.add(aboutMenuItem);
		helpMenu.add(customerServiceMenuItem);
		myMenuBar.add(helpMenu);

		// 添加数据菜单
		JMenu dataMenu = new JMenu("数据");
		JMenuItem exportMenuItem = new JMenuItem("导出数据");
		dataMenu.add(exportMenuItem);
		myMenuBar.add(dataMenu);

		// 设置菜单项的事件监听器
		aboutMenuItem.addActionListener(new AboutActionListener());
		customerServiceMenuItem.addActionListener(new CustomerServiceActionListener());
		exportMenuItem.addActionListener(new ExportDataActionListener());

		this.setJMenuBar(myMenuBar);

		setSize(500, 400);
		setLocation(100, 100);

		this.addWindowListener(new WindowCloser());

		fileMenu.setMnemonic('f');
		exitMenuItem.setMnemonic('x');
		helpMenu.setMnemonic('h');
		aboutMenuItem.setMnemonic('a');

		// 设置快捷键
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.CTRL_MASK));

		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));

		aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.CTRL_MASK));
		
		// 根据用户权限显示/隐藏功能按钮
		setupFunctionalityBasedOnAuthority();
	}
	
	/**
	 * 根据用户权限设置功能可用性
	 */
	private void setupFunctionalityBasedOnAuthority() {
		// 如果没有用户信息（测试模式），默认使用普通用户权限
		int authority = (currentUser != null) ? currentUser.getAuthority() : USER_AUTHORITY;
		
		System.out.println("当前用户权限: " + authority);
		
		// 示例：根据权限显示/隐藏菜单或功能按钮
		// 这里可以根据实际需求添加更多的权限控制逻辑
		
		// 目前系统中主要的功能是产品查看和智能客服
		// 假设管理员可以访问更多功能，普通用户只能查看产品和使用客服
		
		// 可以在后续扩展中添加更多权限控制，例如：
		// - 管理员可以添加/编辑/删除产品
		// - 管理员可以管理用户
		// - 管理员可以查看销售报表
		// - 普通用户只能查看产品和购买
	}
	
	/**
	 * 获取当前登录用户
	 * @return 当前登录用户
	 */
	public User getCurrentUser() {
		return currentUser;
	}

	/**
	 * 设置外观主题菜单
	 */
	protected void setupLookAndFeelMenu(JMenuBar theMenuBar) {

		UIManager.LookAndFeelInfo[] lookAndFeelInfo = UIManager
				.getInstalledLookAndFeels();
		JMenu lookAndFeelMenu = new JMenu("外观");
		JMenuItem anItem = null;
		LookAndFeelListener myListener = new LookAndFeelListener();

		try {
			for (int i = 0; i < lookAndFeelInfo.length; i++) {
				anItem = new JMenuItem(lookAndFeelInfo[i].getName() + " 主题");
				anItem.setActionCommand(lookAndFeelInfo[i].getClassName());
				anItem.addActionListener(myListener);

				lookAndFeelMenu.add(anItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		theMenuBar.add(lookAndFeelMenu);
	}

	/**
	 * 退出应用程序.
	 */
	public void exit() {
		setVisible(false);
		dispose();
		System.exit(0);
	}

	/**
	 * "退出"事件监听器.
	 */
	class ExitActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			exit();
		}
	}

	/**
	 * 窗口"关闭"事件监听器.
	 */
	class WindowCloser extends WindowAdapter {

		/**
		 * 调用exit()方法退出应用
		 */
		public void windowClosing(WindowEvent e) {
			exit();
		}
	}

	/**
	 * "外观"选择事件监听器
	 */
	class LookAndFeelListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String className = event.getActionCommand();
			try {
				UIManager.setLookAndFeel(className);
				SwingUtilities.updateComponentTreeUI(MainFrame.this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * "关于"菜单事件监听器
	 */
	class AboutActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String msg = "感谢使用!";
			JOptionPane.showMessageDialog(MainFrame.this, msg);
		}
	}

	/**
	 * "智能客服"菜单事件监听器
	 */
	class CustomerServiceActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			// 创建并显示智能客服窗口
			CustomerServiceFrame dialog = new CustomerServiceFrame(MainFrame.this);
			dialog.setVisible(true);
		}
	}
	
	/**
	 * "导出数据"菜单事件监听器
	 */
	class ExportDataActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			// 创建并显示数据导出对话框
			ExportDialog dialog = new ExportDialog(MainFrame.this);
			dialog.setVisible(true);
		}
	}
}