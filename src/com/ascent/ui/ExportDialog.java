package com.ascent.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.ascent.bean.Product;
import com.ascent.util.ExportUtil;
import com.ascent.util.ExportUtil.ExportFormat;

/**
 * 数据导出对话框，提供导出格式和路径选择功能
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ExportDialog extends JDialog {
    
    /**
     * Excel格式单选按钮
     */
    private JRadioButton excelRadioButton;
    
    /**
     * CSV格式单选按钮
     */
    private JRadioButton csvRadioButton;
    
    /**
     * 文件路径文本框
     */
    private JTextField filePathTextField;
    
    /**
     * 导出按钮
     */
    private JButton exportButton;
    
    /**
     * 取消按钮
     */
    private JButton cancelButton;
    
    /**
     * 当前选择的导出格式
     */
    private ExportFormat selectedFormat;
    
    /**
     * 父窗口
     */
    private MainFrame parentFrame;
    
    /**
     * 构造方法
     * @param parent 父窗口
     */
    public ExportDialog(MainFrame parent) {
        super(parent, "数据导出", true);
        this.parentFrame = parent;
        this.selectedFormat = ExportFormat.CSV; // 默认选择CSV格式
        initUI();
        initListeners();
        pack();
        setLocationRelativeTo(parent);
    }
    
    /**
     * 初始化用户界面
     */
    private void initUI() {
        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());
        
        // 创建主面板
        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 1. 导出格式选择面板
        JPanel formatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel formatLabel = new JLabel("选择导出格式：");
        excelRadioButton = new JRadioButton("Excel (.xlsx)");
        csvRadioButton = new JRadioButton("CSV (.csv)");
        csvRadioButton.setSelected(true); // 默认选择CSV格式
        
        // 将单选按钮添加到按钮组
        ButtonGroup formatGroup = new ButtonGroup();
        formatGroup.add(excelRadioButton);
        formatGroup.add(csvRadioButton);
        
        formatPanel.add(formatLabel);
        formatPanel.add(excelRadioButton);
        formatPanel.add(csvRadioButton);
        
        // 2. 导出路径选择面板
        JPanel pathPanel = new JPanel(new BorderLayout(5, 5));
        JLabel pathLabel = new JLabel("导出路径：");
        filePathTextField = new JTextField();
        JButton browseButton = new JButton("浏览...");
        
        pathPanel.add(pathLabel, BorderLayout.WEST);
        pathPanel.add(filePathTextField, BorderLayout.CENTER);
        pathPanel.add(browseButton, BorderLayout.EAST);
        
        // 3. 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        exportButton = new JButton("导出");
        cancelButton = new JButton("取消");
        
        buttonPanel.add(exportButton);
        buttonPanel.add(cancelButton);
        
        // 将所有面板添加到主面板
        mainPanel.add(formatPanel);
        mainPanel.add(pathPanel);
        mainPanel.add(buttonPanel);
        
        // 将主面板添加到容器
        container.add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * 初始化事件监听器
     */
    private void initListeners() {
        // Excel单选按钮事件
        excelRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedFormat = ExportFormat.EXCEL;
            }
        });
        
        // CSV单选按钮事件
        csvRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedFormat = ExportFormat.CSV;
            }
        });
        
        // 浏览按钮事件
        JButton browseButton = (JButton) ((JPanel) ((JPanel) getContentPane().getComponent(0)).getComponent(1)).getComponent(2);
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath = ExportUtil.chooseExportFilePath(ExportDialog.this, selectedFormat);
                if (filePath != null) {
                    filePathTextField.setText(filePath);
                }
            }
        });
        
        // 导出按钮事件
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performExport();
            }
        });
        
        // 取消按钮事件
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    /**
     * 执行导出操作
     */
    private void performExport() {
        // 获取导出路径
        String filePath = filePathTextField.getText().trim();
        if (filePath.isEmpty()) {
            // 如果用户没有输入路径，打开文件选择器
            filePath = ExportUtil.chooseExportFilePath(this, selectedFormat);
            if (filePath == null) {
                return; // 用户取消了路径选择
            }
            filePathTextField.setText(filePath);
        }
        
        // 显示导出进度提示
        JOptionPane.showMessageDialog(this, "正在导出数据，请稍候...", "导出进度", JOptionPane.INFORMATION_MESSAGE);
        
        // 获取所有产品数据
        List<Product> products = ExportUtil.getAllProducts();
        
        // 执行导出
        boolean success = ExportUtil.exportProducts(products, selectedFormat, filePath);
        
        if (success) {
            // 导出成功，显示提示信息
            JOptionPane.showMessageDialog(this, "数据导出成功！", "导出成功", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // 关闭对话框
        } else {
            // 导出失败，显示错误信息
            JOptionPane.showMessageDialog(this, "数据导出失败！", "导出失败", JOptionPane.ERROR_MESSAGE);
        }
    }
}