package com.ascent.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.ascent.util.ProductDataAccessor;
import com.ascent.bean.Product;
import com.ascent.ai.OpenAIClient;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 智能客服对话框
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SmartCustomerServiceDialog extends JDialog {
    
    // 消息展示区域
    private JTextArea messageArea;
    // 消息输入框
    private JTextField inputField;
    // 发送按钮
    private JButton sendButton;
    // 智能客服核心逻辑
    private SmartCustomerService smartService;
    // 父窗口
    private JFrame parentFrame;
    
    /**
     * 构造方法
     * @param parent 父窗口
     */
    public SmartCustomerServiceDialog(JFrame parent) {
        super(parent, "智能客服", true);
        this.parentFrame = parent;
        this.smartService = new SmartCustomerService();
        
        // 设置对话框属性
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setResizable(true);
        
        // 初始化界面
        initUI();
    }
    
    /**
     * 初始化界面
     */
    private void initUI() {
        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // 创建消息展示区域
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // 创建底部输入面板
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("发送");
        
        // 添加组件
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        
        // 添加事件监听
        sendButton.addActionListener(new SendButtonListener());
        inputField.addActionListener(new SendButtonListener()); // 支持回车键发送
        
        // 添加初始欢迎消息
        addMessage("客服", "您好！欢迎使用艾斯医药系统智能客服，请问有什么可以帮助您的？");
        
        // 设置内容面板
        setContentPane(mainPanel);
    }
    
    /**
     * 添加消息到展示区域
     * @param sender 发送者（"用户"或"客服"）
     * @param content 消息内容
     */
    private void addMessage(String sender, String content) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());
        String message = "[" + sender + "] " + time + ": " + content + "\n";
        
        messageArea.append(message);
        messageArea.setCaretPosition(messageArea.getDocument().getLength()); // 自动滚动到底部
    }
    
    /**
     * 发送按钮事件监听器
     */
    class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userMessage = inputField.getText().trim();
            if (!userMessage.isEmpty()) {
                // 添加用户消息到展示区域
                addMessage("用户", userMessage);
                
                // 清空输入框
                inputField.setText("");
                
                // 调用智能客服处理消息并获取回复
                String reply = smartService.getReply(userMessage);
                
                // 添加客服回复到展示区域
                addMessage("客服", reply);
            }
        }
    }
}