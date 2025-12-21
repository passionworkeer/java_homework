package com.ascent.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 智能客服聊天对话框
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class CustomerServiceDialog extends JDialog {

    protected JTextArea chatArea;
    protected JTextField inputField;
    protected JButton sendButton;
    protected JButton closeButton;
    protected JPanel chatPanel;
    protected JPanel inputPanel;

    /**
     * 构造函数
     * @param parent 父窗口
     */
    public CustomerServiceDialog(JFrame parent) {
        super(parent, "智能客服", true);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setResizable(true);

        // 初始化组件
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField(30);
        sendButton = new JButton("发送");
        closeButton = new JButton("关闭");

        // 设置布局
        chatPanel = new JPanel(new BorderLayout());
        chatPanel.add(scrollPane, BorderLayout.CENTER);

        inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(inputField);
        inputPanel.add(sendButton);
        inputPanel.add(closeButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(chatPanel, BorderLayout.CENTER);
        getContentPane().add(inputPanel, BorderLayout.SOUTH);

        // 添加事件监听器
        sendButton.addActionListener(new SendButtonActionListener());
        closeButton.addActionListener(new CloseButtonActionListener());
        inputField.addActionListener(new SendButtonActionListener()); // 回车键发送

        // 初始化聊天区域
        appendMessage("客服", "您好！我是智能客服，请问有什么可以帮助您的？");
    }

    /**
     * 追加消息到聊天区域
     * @param role 角色（客服/用户）
     * @param content 消息内容
     */
    protected void appendMessage(String role, String content) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());
        chatArea.append("[" + role + "] " + time + ": " + content + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength()); // 滚动到底部
    }

    /**
     * 处理用户输入，生成回复
     * @param userInput 用户输入
     * @return 客服回复
     */
    protected String generateReply(String userInput) {
        // 简单的关键词匹配回复
        userInput = userInput.toLowerCase();
        
        if (userInput.contains("你好") || userInput.contains("您好")) {
            return "您好！请问有什么可以帮助您的？";
        } else if (userInput.contains("登录")) {
            return "请输入用户名和密码进行登录，如果没有账号可以点击注册按钮注册新账号。";
        } else if (userInput.contains("注册")) {
            return "请点击登录界面的注册按钮，填写用户名和密码完成注册。";
        } else if (userInput.contains("药品") || userInput.contains("产品")) {
            return "您可以在主界面的产品列表中浏览和搜索药品，点击详情按钮查看药品详细信息。";
        } else if (userInput.contains("购物车")) {
            return "您可以在产品详情界面点击购买按钮将药品加入购物车，然后点击购物车按钮查看和管理购物车。";
        } else if (userInput.contains("退出")) {
            return "您可以点击窗口右上角的关闭按钮或选择菜单中的退出选项退出系统。";
        } else {
            return "抱歉，我不太理解您的问题，请尝试换一种方式提问。";
        }
    }

    /**
     * 发送按钮事件监听器
     */
    class SendButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String userInput = inputField.getText().trim();
            if (!userInput.isEmpty()) {
                // 显示用户消息
                appendMessage("用户", userInput);
                // 生成并显示客服回复
                String reply = generateReply(userInput);
                appendMessage("客服", reply);
                // 清空输入框
                inputField.setText("");
            }
        }
    }

    /**
     * 关闭按钮事件监听器
     */
    class CloseButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            dispose();
        }
    }
}