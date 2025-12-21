package com.ascent.ai;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import com.ascent.util.ProductDataAccessor;
import com.ascent.bean.Product;

/**
 * 智能客服聊天窗口
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ChatClientFrame extends JFrame {

    // 消息展示区域
    private JTextArea chatArea;
    // 消息输入区域
    private JTextField inputField;
    // 发送按钮
    private JButton sendButton;
    // 智能客服核心逻辑
    private SmartCustomerService smartService;
    // FAQ知识库
    private HashMap<String, String> faqMap;
    // 产品数据访问器
    private ProductDataAccessor productAccessor;

    /**
     * 构造方法
     */
    public ChatClientFrame() {
        // 初始化FAQ知识库
        initFAQ();
        // 初始化产品数据访问器
        productAccessor = new ProductDataAccessor();
        // 初始化智能客服服务
        smartService = new SmartCustomerService(faqMap, productAccessor);
        // 初始化界面
        initUI();
    }

    /**
     * 初始化FAQ知识库
     */
    private void initFAQ() {
        faqMap = new HashMap<>();
        // 系统操作相关FAQ
        faqMap.put("如何查询药品", "您可以在主界面的产品分类下拉框中选择分类，查看对应的药品列表。");
        faqMap.put("如何查看药品详情", "在药品列表中选择一个药品，然后点击'详情'按钮即可查看详细信息。");
        faqMap.put("如何购买药品", "在药品详情页面，点击'购买'按钮即可将药品加入购物车。");
        faqMap.put("如何查看购物车", "点击产品面板中的'查看购物车'按钮即可查看购物车中的商品。");
        faqMap.put("如何注册新用户", "在登录界面点击'注册'按钮，填写用户名和密码即可注册新用户。");
        faqMap.put("如何修改密码", "目前系统暂不支持直接修改密码，您可以联系管理员处理。");
        
        // 关于系统
        faqMap.put("系统名称", "本系统名为AscentSys，是一款药品管理系统。");
        faqMap.put("系统版本", "当前系统版本为1.0。");
        faqMap.put("系统功能", "本系统支持药品查询、购买、购物车管理等功能。");
        
        // 默认回复
        faqMap.put("默认", "抱歉，我不太理解您的问题。您可以尝试问：如何查询药品、如何购买药品、系统功能等。");
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        // 设置窗口标题
        setTitle("智能客服");
        // 设置窗口大小
        setSize(500, 400);
        // 设置窗口居中
        setLocationRelativeTo(null);
        // 设置窗口关闭方式
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 设置窗口不可调整大小
        setResizable(false);

        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(mainPanel);

        // 创建聊天区域
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("SimSun", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("聊天记录"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 创建输入面板
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("输入消息"));
        
        // 创建输入框
        inputField = new JTextField();
        inputField.setFont(new Font("SimSun", Font.PLAIN, 14));
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        
        // 创建发送按钮
        sendButton = new JButton("发送");
        sendButton.setFont(new Font("SimSun", Font.PLAIN, 14));
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        // 添加欢迎消息
        appendMessage("客服", "您好！我是AscentSys智能客服，有什么可以帮助您的？");
    }

    /**
     * 发送消息
     */
    private void sendMessage() {
        String userMessage = inputField.getText().trim();
        if (userMessage.isEmpty()) {
            return;
        }
        
        // 添加用户消息到聊天区域
        appendMessage("用户", userMessage);
        
        // 清空输入框
        inputField.setText("");
        
        // 获取智能客服回复
        String reply = smartService.getReply(userMessage);
        
        // 添加客服回复到聊天区域
        appendMessage("客服", reply);
    }

    /**
     * 添加消息到聊天区域
     * @param role 角色（用户/客服）
     * @param message 消息内容
     */
    private void appendMessage(String role, String message) {
        // 格式化时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time = LocalDateTime.now().format(formatter);
        
        // 构建消息格式
        String formattedMessage = String.format("[%s] %s: %s\n", time, role, message);
        
        // 添加到聊天区域
        chatArea.append(formattedMessage);
        
        // 滚动到底部
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    /**
     * 智能客服核心逻辑类
     */
    private class SmartCustomerService {
        private HashMap<String, String> faqMap;
        private ProductDataAccessor productAccessor;

        public SmartCustomerService(HashMap<String, String> faqMap, ProductDataAccessor productAccessor) {
            this.faqMap = faqMap;
            this.productAccessor = productAccessor;
        }

        /**
         * 获取回复
         * @param userMessage 用户消息
         * @return 客服回复
         */
        public String getReply(String userMessage) {
            // 转换为小写，便于匹配
            String lowerMessage = userMessage.toLowerCase();
            
            // 检查是否为药品查询
            if (lowerMessage.contains("药品") || lowerMessage.contains("库存") || lowerMessage.contains("价格")) {
                return handleProductQuery(lowerMessage, userMessage);
            }
            
            // 检查FAQ匹配
            for (String key : faqMap.keySet()) {
                if (lowerMessage.contains(key)) {
                    return faqMap.get(key);
                }
            }
            
            // 默认回复
            return faqMap.get("默认");
        }

        /**
         * 处理药品查询
         * @param lowerMessage 小写消息
         * @param originalMessage 原始消息
         * @return 查询结果
         */
        private String handleProductQuery(String lowerMessage, String originalMessage) {
            try {
                // 提取产品名称（简单实现：查找"药品"后面的内容）
                String productName = extractProductName(originalMessage);
                
                // 查询所有产品
                java.util.ArrayList<Product> allProducts = new java.util.ArrayList<>();
                
                // 获取所有产品类别
                java.util.ArrayList<String> categories = productAccessor.getCategories();
                for (String category : categories) {
                    java.util.ArrayList<Product> products = productAccessor.getProducts(category);
                    allProducts.addAll(products);
                }
                
                // 查找匹配的产品
                Product foundProduct = null;
                for (Product product : allProducts) {
                    if (product.getProductname().toLowerCase().contains(productName.toLowerCase())) {
                        foundProduct = product;
                        break;
                    }
                }
                
                if (foundProduct != null) {
                    // 构建产品信息回复
                    StringBuilder reply = new StringBuilder();
                    reply.append("找到匹配的药品信息：\n");
                    reply.append("名称：").append(foundProduct.getProductname()).append("\n");
                    reply.append("CAS号：").append(foundProduct.getCas()).append("\n");
                    reply.append("分子式：").append(foundProduct.getFormula()).append("\n");
                    reply.append("价格：").append(foundProduct.getPrice()).append("\n");
                    reply.append("库存：").append(foundProduct.getRealstock()).append("\n");
                    reply.append("分类：").append(foundProduct.getCategory());
                    return reply.toString();
                } else {
                    return "未找到匹配的药品信息，请尝试其他关键词。";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "查询药品信息时发生错误，请稍后再试。";
            }
        }

        /**
         * 提取产品名称
         * @param message 用户消息
         * @return 产品名称
         */
        private String extractProductName(String message) {
            // 简单实现：截取"药品"后面的内容
            if (message.contains("药品")) {
                int index = message.indexOf("药品") + 2;
                return message.substring(index).trim();
            }
            // 或者直接返回整个消息
            return message;
        }
    }

    /**
     * 主方法，用于测试
     * @param args 参数
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ChatClientFrame frame = new ChatClientFrame();
                frame.setVisible(true);
            }
        });
    }
}