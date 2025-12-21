package com.ascent.ui;

import java.util.HashMap;
import java.util.ArrayList;
import com.ascent.util.ProductDataAccessor;
import com.ascent.bean.Product;

/**
 * 智能客服核心逻辑类
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SmartCustomerService {
    
    // FAQ知识库
    private HashMap<String, String> faqMap;
    // 产品数据访问器
    private ProductDataAccessor productAccessor;
    
    /**
     * 构造方法
     */
    public SmartCustomerService() {
        this.faqMap = new HashMap<>();
        this.productAccessor = new ProductDataAccessor();
        initFAQ();
    }
    
    /**
     * 初始化FAQ知识库
     */
    private void initFAQ() {
        // 系统操作相关
        faqMap.put("如何查询药品", "您可以在产品面板中通过分类下拉框选择药品类别，查看对应的药品列表。");
        faqMap.put("如何查看药品详情", "在产品列表中选择药品，然后点击'详情'按钮，即可查看药品的详细信息。");
        faqMap.put("如何购买药品", "在药品详情对话框中，点击'购买'按钮，即可将药品加入购物车。");
        faqMap.put("如何查看购物车", "在产品面板中，点击'查看购物车'按钮，即可查看购物车中的商品。");
        
        // 系统信息相关
        faqMap.put("系统名称", "本系统是艾斯医药管理系统，用于药品的管理和销售。");
        faqMap.put("系统版本", "当前系统版本为1.0。");
        faqMap.put("系统功能", "本系统支持药品的查询、浏览、购买等功能。");
        
        // 其他常见问题
        faqMap.put("你好", "您好！请问有什么可以帮助您的？");
        faqMap.put("谢谢", "不客气，祝您使用愉快！");
        faqMap.put("再见", "再见，期待下次为您服务！");
    }
    
    /**
     * 获取智能回复
     * @param userMessage 用户输入的消息
     * @return 智能回复
     */
    public String getReply(String userMessage) {
        // 转换为小写，方便匹配
        String lowerMessage = userMessage.toLowerCase();
        
        // 1. 检查是否匹配FAQ
        for (String question : faqMap.keySet()) {
            if (lowerMessage.contains(question.toLowerCase())) {
                return faqMap.get(question);
            }
        }
        
        // 2. 检查是否为药品查询
        if (lowerMessage.contains("药品") || lowerMessage.contains("库存") || lowerMessage.contains("价格")) {
            return handleProductQuery(userMessage);
        }
        
        // 3. 默认回复
        return "抱歉，我不太理解您的问题。您可以尝试问一些关于药品查询或系统操作的问题，例如：'如何查询药品'、'阿司匹林的库存是多少'等。";
    }
    
    /**
     * 处理药品查询
     * @param userMessage 用户输入的查询信息
     * @return 查询结果
     */
    private String handleProductQuery(String userMessage) {
        try {
            // 提取查询的药品名称
            String productName = extractProductName(userMessage);
            
            // 获取所有产品分类
            ArrayList<String> categories = productAccessor.getCategories();
            ArrayList<Product> allProducts = new ArrayList<>();
            
            // 获取所有产品
            for (String category : categories) {
                if (!category.startsWith("---")) {
                    ArrayList<Product> products = productAccessor.getProducts(category);
                    allProducts.addAll(products);
                }
            }
            
            // 查找匹配的产品
            ArrayList<Product> matchedProducts = new ArrayList<>();
            for (Product product : allProducts) {
                if (product.getProductname().toLowerCase().contains(productName.toLowerCase())) {
                    matchedProducts.add(product);
                }
            }
            
            // 构建回复
            if (matchedProducts.isEmpty()) {
                return "未找到名称包含'" + productName + "'的药品。";
            } else {
                StringBuilder reply = new StringBuilder();
                reply.append("找到").append(matchedProducts.size()).append("个匹配的药品：\n");
                
                for (Product product : matchedProducts) {
                    reply.append("\n1. 药品名称：").append(product.getProductname());
                    reply.append("\n   CAS号：").append(product.getCas());
                    reply.append("\n   价格：").append(product.getPrice());
                    reply.append("\n   库存：").append(product.getRealstock());
                    reply.append("\n   类别：").append(product.getCategory());
                }
                
                return reply.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "查询药品信息时发生错误，请稍后再试。";
        }
    }
    
    /**
     * 从用户输入中提取药品名称
     * @param userMessage 用户输入
     * @return 提取的药品名称
     */
    private String extractProductName(String userMessage) {
        // 简单的提取逻辑，实际项目中可以使用更复杂的NLP算法
        String[] keywords = {"药品", "库存", "价格", "的", "是", "多少"};
        
        String productName = userMessage;
        for (String keyword : keywords) {
            productName = productName.replace(keyword, "");
        }
        
        return productName.trim();
    }
}