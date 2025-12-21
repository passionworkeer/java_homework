package com.ascent.ai;

import java.util.HashMap;
import java.util.Map;
import com.ascent.bean.Product;
import com.ascent.util.ProductDataAccessor;

/**
 * 智能客服核心类
 * @author ascent
 * @version 1.0
 */
public class SmartCustomerService {
    
    /**
     * FAQ知识库，存储常见问题和答案
     */
    private Map<String, String> faqKnowledgeBase;
    
    /**
     * 产品数据访问器
     */
    private ProductDataAccessor productDataAccessor;
    
    /**
     * 默认构造方法
     */
    public SmartCustomerService() {
        this.productDataAccessor = new ProductDataAccessor();
        this.initFaqKnowledgeBase();
    }
    
    /**
     * 初始化FAQ知识库
     */
    private void initFaqKnowledgeBase() {
        faqKnowledgeBase = new HashMap<>();
        
        // 系统使用相关FAQ
        faqKnowledgeBase.put("如何登录系统", "请在登录界面输入您的用户名和密码，然后点击登录按钮。");
        faqKnowledgeBase.put("如何注册用户", "请在登录界面点击注册按钮，填写用户名和密码后提交。");
        faqKnowledgeBase.put("如何查看产品", "登录系统后，在主界面选择产品类别，即可查看对应类别的产品列表。");
        faqKnowledgeBase.put("如何购买产品", "在产品列表中选择产品，点击详情按钮，然后点击购买按钮。");
        faqKnowledgeBase.put("如何查看购物车", "在产品面板点击查看购物车按钮，即可查看已添加的商品。");
        
        // 产品相关FAQ
        faqKnowledgeBase.put("产品库存", "您可以在产品详情中查看产品的当前库存。");
        faqKnowledgeBase.put("产品价格", "产品价格显示在产品列表和产品详情中。");
        faqKnowledgeBase.put("产品分类", "系统将产品分为多个类别，您可以在主界面选择类别查看对应产品。");
        
        // 其他常见问题
        faqKnowledgeBase.put("系统帮助", "您可以通过查看产品详情、购物车等功能来使用系统。");
        faqKnowledgeBase.put("联系客服", "如果您有其他问题，请联系系统管理员。");
    }
    
    /**
     * 处理用户消息，返回智能回复
     * @param userMessage 用户输入的消息
     * @return 智能客服的回复
     */
    public String handleMessage(String userMessage) {
        String reply = "";
        
        // 1. 检查是否为产品相关查询
        if (isProductQuery(userMessage)) {
            reply = handleProductQuery(userMessage);
        } else {
            // 2. 检查是否为FAQ问题
            reply = handleFaqQuery(userMessage);
        }
        
        // 3. 如果没有匹配到任何回答，返回默认回复
        if (reply.isEmpty()) {
            reply = "对不起，我不太理解您的问题。请尝试提问其他问题，或者联系系统管理员。";
        }
        
        return reply;
    }
    
    /**
     * 判断是否为产品相关查询
     * @param userMessage 用户输入的消息
     * @return 是否为产品相关查询
     */
    private boolean isProductQuery(String userMessage) {
        userMessage = userMessage.toLowerCase();
        return userMessage.contains("产品") || userMessage.contains("库存") || userMessage.contains("价格");
    }
    
    /**
     * 处理产品相关查询
     * @param userMessage 用户输入的消息
     * @return 产品相关查询的回复
     */
    private String handleProductQuery(String userMessage) {
        String reply = "";
        userMessage = userMessage.toLowerCase();
        
        // 提取产品名称
        String productName = extractProductName(userMessage);
        if (!productName.isEmpty()) {
            // 查询产品信息
            Product product = getProductInfo(productName);
            if (product != null) {
                // 根据查询类型返回不同的回复
                if (userMessage.contains("库存")) {
                    reply = product.getProductname() + "的当前库存为：" + product.getRealstock();
                } else if (userMessage.contains("价格")) {
                    reply = product.getProductname() + "的价格为：" + product.getPrice();
                } else {
                    // 返回完整的产品信息
                    reply = product.getProductname() + "的详细信息：\n" +
                            "CAS号：" + product.getCas() + "\n" +
                            "结构式：" + product.getStructure() + "\n" +
                            "分子式：" + product.getFormula() + "\n" +
                            "价格：" + product.getPrice() + "\n" +
                            "库存：" + product.getRealstock() + "\n" +
                            "类别：" + product.getCategory();
                }
            } else {
                reply = "未找到名为\"" + productName + "\"的产品。";
            }
        }
        
        return reply;
    }
    
    /**
     * 从用户消息中提取产品名称
     * @param userMessage 用户输入的消息
     * @return 提取的产品名称
     */
    private String extractProductName(String userMessage) {
        // 简单的产品名称提取逻辑，可以根据实际需求优化
        String[] keywords = {"阿司匹林", "布洛芬", "青霉素", "头孢", "阿莫西林", "感冒药", "退烧药"};
        for (String keyword : keywords) {
            if (userMessage.contains(keyword)) {
                return keyword;
            }
        }
        return "";
    }
    
    /**
     * 根据产品名称获取产品信息
     * @param productName 产品名称
     * @return 产品信息
     */
    private Product getProductInfo(String productName) {
        // 从产品数据访问器中获取产品信息
        try {
            // 这里需要根据实际的ProductDataAccessor方法进行调整
            // 暂时返回null，后续需要根据实际情况实现
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 处理FAQ相关查询
     * @param userMessage 用户输入的消息
     * @return FAQ查询的回复
     */
    private String handleFaqQuery(String userMessage) {
        String reply = "";
        userMessage = userMessage.toLowerCase();
        
        // 遍历FAQ知识库，查找匹配的问题
        for (Map.Entry<String, String> entry : faqKnowledgeBase.entrySet()) {
            String question = entry.getKey().toLowerCase();
            if (userMessage.contains(question) || question.contains(userMessage)) {
                reply = entry.getValue();
                break;
            }
        }
        
        return reply;
    }
}