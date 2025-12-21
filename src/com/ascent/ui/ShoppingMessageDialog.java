package com.ascent.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 购物消息对话框
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ShoppingMessageDialog extends JDialog {

    /**
     * 主面板
     */
    protected JPanel mainPanel;

    /**
     * OK按钮
     */
    protected JButton okButton;

    /**
     * 信息标签
     */
    protected JLabel messageLabel;

    /**
     * 构造函数
     * @param theParentFrame 父窗口
     * @param message 消息内容
     */
    public ShoppingMessageDialog(JFrame theParentFrame, String message) {
        super(theParentFrame, "购物信息", true);
        setSize(300, 200);
        setLocationRelativeTo(theParentFrame);
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1));
        messageLabel = new JLabel(message, JLabel.CENTER);
        mainPanel.add(messageLabel);
        JPanel buttonPanel = new JPanel();
        okButton = new JButton("确定");
        okButton.addActionListener(new OkButtonActionListener());
        buttonPanel.add(okButton);
        mainPanel.add(buttonPanel);
        this.add(mainPanel);
    }

    /**
     * OK按钮事件监听器
     */
    class OkButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            setVisible(false);
            dispose();
        }
    }
}