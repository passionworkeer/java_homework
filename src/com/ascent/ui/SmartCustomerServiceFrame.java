package com.ascent.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import com.ascent.util.ProductDataAccessor;
import com.ascent.bean.Product;

/**
 * 智能客服聊天窗口
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SmartCustomerServiceFrame extends JDialog {

    // 聊天消息展示区
    private JTextArea messageArea;
    // 消息输入框
    private JTextField inputField;
    // 发送按钮
    private JButton sendButton;
    // FAQ知识库
    private Map<String, String> faqMap;
    // 产品数据访问对象
    private ProductDataAccessor productDataAccessor;

    /**
     * 构造方法
     * @param parent 父窗口
     */
    public SmartCustomerServiceFrame(JFrame parent) {
        super(parent, "智能客服", true);
        
        // 初始化FAQ知识库
        initFAQ();
        
        // 初始化产品数据访问对象
        productDataAccessor = new ProductDataAccessor();
        
        // 初始化界面
        initUI();
        
        // 设置窗口属性
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * 初始化FAQ知识库
     */
    private void initFAQ() {
        faqMap = new HashMap<>();
        
        // 系统操作相关
        faqMap.put("如何登录", "请在登录界面输入用户名和密码，点击登录按钮即可。");
        faqMap.put("如何注册", "请在登录界面点击注册按钮，填写相关信息即可注册新用户。");
        faqMap.put("如何查看产品", "在主界面选择产品类别，然后在产品列表中查看产品信息。");
        faqMap.put("如何购买产品", "选中产品后点击详情按钮，然后点击购买按钮即可。");
        
        // 产品相关
        faqMap.put("产品分类", "系统中的产品分为多个类别，您可以通过类别下拉框选择查看。");
        faqMap.put("产品库存", "每个产品都有对应的库存信息，您可以查看产品详情了解具体库存。");
        faqMap.put("产品价格", "产品详情中显示了产品的价格信息。");
        
        // 其他
        faqMap.put("系统介绍", "这是一个医药管理系统，用于管理药品信息和销售。");
        faqMap.put("帮助", "您可以通过本智能客服获取系统使用帮助，或者查看系统菜单中的帮助选项。");
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // 创建消息展示区
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // 创建输入面板
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("发送");
        
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        
        // 添加事件监听器
        sendButton.addActionListener(new SendButtonListener());
        inputField.addActionListener(new SendButtonListener()); // 支持回车键发送
        
        // 添加欢迎消息
        addMessage("客服", "您好！我是智能客服，有什么可以帮助您的吗？");
        
        // 设置内容面板
        setContentPane(mainPanel);
    }

    /**
     * 发送按钮监听器
     */
    class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userInput = inputField.getText().trim();
            if (!userInput.isEmpty()) {
                // 显示用户消息
                addMessage("用户", userInput);
                
                // 生成并显示客服回复
                String reply = generateReply(userInput);
                addMessage("客服", reply);
                
                // 清空输入框
                inputField.setText("");
            }
        }
    }

    /**
     * 生成客服回复
     * @param userInput 用户输入
     * @return 客服回复
     */
    private String generateReply(String userInput) {
        // 使用OpenAI API获取智能回复
        com.ascent.ai.OpenAIClient openAIClient = new com.ascent.ai.OpenAIClient();
        return openAIClient.getSmartReply(userInput);
    }

    /**
     * 处理产品查询
     * @param input 用户输入
     * @return 查询结果
     */
    private String handleProductQuery(String input) {
        try {
            // 简单提取产品名称（这里使用简单的关键词匹配）
            String productName = extractProductName(input);
            if (productName.isEmpty()) {
                return "请您提供具体的产品名称，以便我为您查询。";
            }
            
            // 查询所有产品
            java.util.List<Product> allProducts = getAllProducts();
            
            // 查找匹配的产品
            for (Product product : allProducts) {
                if (product.getProductname().toLowerCase().contains(productName)) {
                    // 生成产品信息回复
                    return generateProductInfoReply(product, input);
                }
            }
            
            return "未找到名称包含\"" + productName + "\"的产品。";
        } catch (Exception e) {
            return "查询产品信息时发生错误，请稍后再试。";
        }
    }

    /**
     * 提取产品名称
     * @param input 用户输入
     * @return 提取的产品名称
     */
    private String extractProductName(String input) {
        // 简单处理：移除常见查询词，提取可能的产品名称
        String[] queryWords = {"库存", "价格", "产品", "的", "是", "多少", "查询", "查一下"};
        String productName = input;
        
        for (String word : queryWords) {
            productName = productName.replace(word, "");
        }
        
        return productName.trim();
    }

    /**
     * 获取所有产品
     * @return 所有产品列表
     */
    private java.util.List<Product> getAllProducts() {
        java.util.List<Product> allProducts = new ArrayList<>();
        try {
            // 从产品数据访问对象获取所有产品
            // 这里需要根据实际情况调整，获取所有类别的产品
            java.util.List<String> categories = productDataAccessor.getCategories();
            for (String category : categories) {
                java.util.List<Product> products = productDataAccessor.getProducts(category);
                if (products != null) {
                    allProducts.addAll(products);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allProducts;
    }

    /**
     * 生成产品信息回复
     * @param product 产品对象
     * @param input 用户输入
     * @return 产品信息回复
     */
    private String generateProductInfoReply(Product product, String input) {
        StringBuilder reply = new StringBuilder();
        reply.append("产品名称：").append(product.getProductname()).append("\n");
        
        if (input.contains("价格")) {
            reply.append("价格：").append(product.getPrice()).append("\n");
        }
        
        if (input.contains("库存")) {
            reply.append("库存：").append(product.getRealstock()).append("\n");
        }
        
        // 可以根据需要添加更多产品信息
        reply.append("CAS号：").append(product.getCas()).append("\n");
        reply.append("类别：").append(product.getCategory()).append("\n");
        
        return reply.toString();
    }

    /**
     * 添加消息到聊天窗口
     * @param sender 发送者
     * @param message 消息内容
     */
    private void addMessage(String sender, String message) {
        // 获取当前时间
        Date now = new Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(now);
        
        // 格式化消息
        String formattedMessage = "[" + sender + "] " + time + "：" + message + "\n\n";
        
        // 添加到消息区
        messageArea.append(formattedMessage);
        
        // 滚动到底部
        messageArea.setCaretPosition(messageArea.getDocument().getLength());
    }
}