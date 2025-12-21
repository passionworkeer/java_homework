package com.ascent.ui;

import com.ascent.ai.OpenAIClient;

/**
 * 智能客服核心逻辑类
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SmartCustomerService {
    
    // OpenAI客户端，用于调用LLM生成智能回复
    private OpenAIClient openAIClient;
    
    /**
     * 构造方法
     */
    public SmartCustomerService() {
        // 初始化OpenAI客户端
        this.openAIClient = new OpenAIClient();
    }
    
    /**
     * 获取智能回复
     * @param userMessage 用户输入的消息
     * @return 智能回复
     */
    public String getReply(String userMessage) {
        // 直接调用OpenAI客户端生成智能回复
        return openAIClient.generateResponse(userMessage);
    }
}