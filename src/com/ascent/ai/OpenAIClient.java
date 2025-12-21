package com.ascent.ai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * OpenAI客户端，用于处理OpenAI API请求
 * @author ascent
 * @version 1.0
 */
public class OpenAIClient {
    
    // API密钥
    private static final String API_KEY = "ms-22434146-80f6-4669-8473-9aa69b26c218";
    
    // API基础URL
    private static final String BASE_URL = "https://api-inference.modelscope.cn/v1";
    
    // 模型名称
    private static final String MODEL_NAME = "Qwen/Qwen3-Next-80B-A3B-Instruct";
    
    // Gson实例，用于JSON序列化和反序列化
    private final Gson gson;
    
    /**
     * 构造方法
     */
    public OpenAIClient() {
        this.gson = new Gson();
    }
    
    /**
     * 生成AI回复
     * @param userInput 用户输入
     * @return AI回复
     */
    public String generateResponse(String userInput) {
        try {
            // 创建请求URL
            URL url = new URL(BASE_URL + "/chat/completions");
            
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // 设置请求方法和头信息
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setDoOutput(true);
            
            // 创建请求体
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", MODEL_NAME);
            
            // 创建消息数组
            JsonArray messages = new JsonArray();
            
            // 添加系统消息
            JsonObject systemMessage = new JsonObject();
            systemMessage.addProperty("role", "system");
            systemMessage.addProperty("content", "你是一个智能客服助手，专门为医药管理系统提供帮助。请用友好、专业的语言回答用户的问题。");
            messages.add(systemMessage);
            
            // 添加用户消息
            JsonObject userMessage = new JsonObject();
            userMessage.addProperty("role", "user");
            userMessage.addProperty("content", userInput);
            messages.add(userMessage);
            
            requestBody.add("messages", messages);
            
            // 发送请求
            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8))) {
                writer.write(gson.toJson(requestBody));
                writer.flush();
            }
            
            // 读取响应
            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            responseCode == HttpURLConnection.HTTP_OK ? connection.getInputStream() : connection.getErrorStream(), 
                            StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
            
            // 打印响应，用于调试
            System.out.println("OpenAI API响应码：" + responseCode);
            System.out.println("OpenAI API响应内容：" + response.toString());
            
            // 解析响应
            if (responseCode == HttpURLConnection.HTTP_OK) {
                JsonObject responseJson = gson.fromJson(response.toString(), JsonObject.class);
                JsonArray choices = responseJson.getAsJsonArray("choices");
                if (choices != null && choices.size() > 0) {
                    JsonObject choice = choices.get(0).getAsJsonObject();
                    JsonObject message = choice.getAsJsonObject("message");
                    if (message != null) {
                        return message.get("content").getAsString().trim();
                    }
                }
            } else {
                System.err.println("OpenAI API请求失败，响应码：" + responseCode + "，响应内容：" + response.toString());
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // 如果请求失败，返回默认回复
        return "抱歉，我暂时无法为您提供帮助。请稍后再试。";
    }
    
    /**
     * 获取智能回复（与generateResponse方法功能相同，作为别名）
     * @param userInput 用户输入
     * @return 智能回复
     */
    public String getSmartReply(String userInput) {
        return generateResponse(userInput);
    }
}