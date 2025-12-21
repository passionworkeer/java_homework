package com.ascent.ai;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 智能客服聊天窗口
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SmartCustomerServiceFrame extends JFrame {
    
    /**
     * 消息显示区域
     */
    private JTextArea messageArea;
    
    /**
     * 消息输入区域
     */
    private JTextField inputField;
    
    /**
     * 发送按钮
     */
    private JButton sendButton;
    
    /**
     * 智能客服核心实例
     */
    private SmartCustomerService smartCustomerService;
    
    /**
     * 默认构造方法
     */
    public SmartCustomerServiceFrame() {
        this.setTitle("智能客服");
        this.setSize(600, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        this.smartCustomerService = new SmartCustomerService();
        
        // 初始化界面
        this.initUI();
        
        // 显示欢迎消息
        this.addMessage("系统", "欢迎使用智能客服！我可以为您解答关于产品、库存、价格等方面的问题。");
    }
    
    /**
     * 初始化界面
     */
    private void initUI() {
        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // 1. 消息显示区域
        this.messageArea = new JTextArea();
        this.messageArea.setEditable(false);
        this.messageArea.setLineWrap(true);
        this.messageArea.setWrapStyleWord(true);
        this.messageArea.setFont(new Font("宋体", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(this.messageArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // 2. 输入区域
        JPanel inputPanel = new JPanel(new BorderLayout());
        
        this.inputField = new JTextField();
        this.inputField.setFont(new Font("宋体", Font.PLAIN, 14));
        this.inputField.addKeyListener(new InputKeyListener());
        
        this.sendButton = new JButton("发送");
        this.sendButton.setFont(new Font("宋体", Font.PLAIN, 14));
        this.sendButton.addActionListener(new SendButtonListener());
        
        inputPanel.add(this.inputField, BorderLayout.CENTER);
        inputPanel.add(this.sendButton, BorderLayout.EAST);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 3. 组装界面
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        
        this.setContentPane(mainPanel);
    }
    
    /**
     * 添加消息到显示区域
     * @param sender 发送者
     * @param message 消息内容
     */
    private void addMessage(String sender, String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());
        
        String formattedMessage = "[" + sender + "] " + time + "\n" + message + "\n\n";
        this.messageArea.append(formattedMessage);
        this.messageArea.setCaretPosition(this.messageArea.getDocument().getLength());
    }
    
    /**
     * 处理发送消息
     */
    private void handleSendMessage() {
        String userMessage = this.inputField.getText().trim();
        if (!userMessage.isEmpty()) {
            // 显示用户消息
            this.addMessage("用户", userMessage);
            
            // 获取智能回复
            String reply = this.smartCustomerService.handleMessage(userMessage);
            
            // 显示智能回复
            this.addMessage("智能客服", reply);
            
            // 清空输入框
            this.inputField.setText("");
        }
    }
    
    /**
     * 发送按钮监听器
     */
    class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleSendMessage();
        }
    }
    
    /**
     * 输入框键盘监听器
     */
    class InputKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {}
        
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                handleSendMessage();
            }
        }
        
        @Override
        public void keyReleased(KeyEvent e) {}
    }
}