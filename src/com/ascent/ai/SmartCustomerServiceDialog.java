package com.ascent.ai;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 智能客服对话框
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SmartCustomerServiceDialog extends JDialog {

    /**
     * 消息显示区域
     */
    private JTextArea messageArea;

    /**
     * 消息输入框
     */
    private JTextField inputField;

    /**
     * 智能客服核心逻辑
     */
    private SmartCustomerService smartService;

    /**
     * 日期格式化器
     */
    private SimpleDateFormat dateFormat;

    /**
     * 构造方法
     * @param parent 父窗口
     */
    public SmartCustomerServiceDialog(JFrame parent) {
        super(parent, "智能客服", true);
        
        // 初始化组件
        smartService = new SmartCustomerService();
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        
        // 设置对话框属性
        setSize(400, 500);
        setLocationRelativeTo(parent);
        setResizable(true);
        
        // 初始化UI
        initUI();
        
        // 显示欢迎消息
        addSystemMessage("欢迎使用智能客服！我可以为您提供药品查询、系统操作指引等服务。");
    }

    /**
     * 初始化UI界面
     */
    private void initUI() {
        // 设置布局
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        
        // 创建消息显示区域
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("宋体", Font.PLAIN, 14));
        
        // 添加滚动面板
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        container.add(BorderLayout.CENTER, scrollPane);
        
        // 创建底部输入区域
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // 创建输入框
        inputField = new JTextField();
        inputField.setFont(new Font("宋体", Font.PLAIN, 14));
        
        // 创建发送按钮
        JButton sendButton = new JButton("发送");
        sendButton.setFont(new Font("宋体", Font.PLAIN, 14));
        
        // 添加组件到输入面板
        inputPanel.add(BorderLayout.CENTER, inputField);
        inputPanel.add(BorderLayout.EAST, sendButton);
        
        // 添加输入面板到容器
        container.add(BorderLayout.SOUTH, inputPanel);
        
        // 添加事件监听器
        sendButton.addActionListener(new SendButtonListener());
        inputField.addKeyListener(new InputKeyListener());
    }

    /**
     * 添加系统消息
     * @param message 消息内容
     */
    private void addSystemMessage(String message) {
        messageArea.append("[系统] " + dateFormat.format(new Date()) + ": " + message + "\n\n");
        messageArea.setCaretPosition(messageArea.getDocument().getLength());
    }

    /**
     * 添加用户消息
     * @param message 消息内容
     */
    private void addUserMessage(String message) {
        messageArea.append("[用户] " + dateFormat.format(new Date()) + ": " + message + "\n\n");
        messageArea.setCaretPosition(messageArea.getDocument().getLength());
    }

    /**
     * 添加客服回复
     * @param message 回复内容
     */
    private void addServiceReply(String message) {
        messageArea.append("[客服] " + dateFormat.format(new Date()) + ": " + message + "\n\n");
        messageArea.setCaretPosition(messageArea.getDocument().getLength());
    }

    /**
     * 发送按钮事件监听器
     */
    class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            sendMessage();
        }
    }

    /**
     * 输入框回车键监听器
     */
    class InputKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                sendMessage();
            }
        }
    }

    /**
     * 发送消息处理
     */
    private void sendMessage() {
        String message = inputField.getText().trim();
        if (message.isEmpty()) {
            return;
        }
        
        // 添加用户消息到显示区域
        addUserMessage(message);
        
        // 清空输入框
        inputField.setText("");
        
        // 获取客服回复
        String reply = smartService.handleMessage(message);
        
        // 添加客服回复到显示区域
        addServiceReply(reply);
    }
}