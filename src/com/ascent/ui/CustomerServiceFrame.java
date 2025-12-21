package com.ascent.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 智能客服聊天窗口
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class CustomerServiceFrame extends JFrame {

    /**
     * 智能客服核心逻辑
     */
    private SmartCustomerService smartService;

    /**
     * 消息展示区
     */
    private JTextArea messageArea;

    /**
     * 消息输入框
     */
    private JTextField inputField;

    /**
     * 发送按钮
     */
    private JButton sendButton;

    /**
     * 构造方法
     * @param parentFrame 父窗口
     */
    public CustomerServiceFrame(JFrame parentFrame) {
        // 初始化智能客服
        smartService = new SmartCustomerService();

        // 设置窗口属性
        setTitle("智能客服");
        setSize(500, 400);
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);

        // 初始化界面组件
        initComponents();

        // 添加初始欢迎消息
        addWelcomeMessage();
    }

    /**
     * 初始化界面组件
     */
    private void initComponents() {
        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 创建消息展示区
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 创建输入面板
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(Color.LIGHT_GRAY);

        // 创建输入框
        inputField = new JTextField();
        inputField.setFont(new Font("宋体", Font.PLAIN, 14));
        inputPanel.add(inputField, BorderLayout.CENTER);

        // 创建发送按钮
        sendButton = new JButton("发送");
        sendButton.setFont(new Font("宋体", Font.BOLD, 14));
        sendButton.setBackground(Color.BLUE);
        sendButton.setForeground(Color.WHITE);
        sendButton.setPreferredSize(new Dimension(80, 30));
        inputPanel.add(sendButton, BorderLayout.EAST);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        // 添加事件监听
        addEventListeners();

        // 设置主面板
        setContentPane(mainPanel);
    }

    /**
     * 添加事件监听
     */
    private void addEventListeners() {
        // 发送按钮点击事件
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // 输入框回车键事件
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
    }

    /**
     * 发送消息
     */
    private void sendMessage() {
        // 获取用户输入
        String userMessage = inputField.getText().trim();
        if (userMessage.isEmpty()) {
            return;
        }

        // 清空输入框
        inputField.setText("");

        // 添加用户消息到展示区
        addMessageToDisplay("用户", userMessage);

        // 处理用户消息，获取客服回复
        String reply = smartService.getReply(userMessage);

        // 添加客服回复到展示区
        addMessageToDisplay("客服", reply);
    }

    /**
     * 添加消息到展示区
     * @param role 角色（用户/客服）
     * @param content 消息内容
     */
    private void addMessageToDisplay(String role, String content) {
        // 获取当前时间
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());

        // 构建消息格式
        StringBuilder message = new StringBuilder();
        message.append("[").append(role).append("] ").append(time).append("\n");
        message.append(content).append("\n\n");

        // 添加到展示区
        messageArea.append(message.toString());

        // 滚动到底部
        messageArea.setCaretPosition(messageArea.getDocument().getLength());
    }

    /**
     * 添加初始欢迎消息
     */
    private void addWelcomeMessage() {
        String welcomeMessage = "欢迎使用艾斯医药系统智能客服！\n我可以为您提供药品查询、系统操作指引等服务。\n\n您可以问我：\n- 如何查询药品\n- 如何查看药品详情\n- 药品X的库存是多少\n- 系统有哪些功能\n\n请输入您的问题...";
        addMessageToDisplay("客服", welcomeMessage);
    }
}