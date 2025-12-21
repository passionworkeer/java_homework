package com.ascent.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.ascent.bean.Product;

/**
 * 数据导出工具类，支持Excel和CSV格式的数据导出
 * @author ascent
 * @version 1.0
 */
public class ExportUtil {
    
    /**
     * 导出格式枚举
     */
    public enum ExportFormat {
        EXCEL, CSV
    }
    
    /**
     * 导出产品数据
     * @param products 产品列表
     * @param format 导出格式
     * @param filePath 导出文件路径
     * @return 是否导出成功
     */
    public static boolean exportProducts(List<Product> products, ExportFormat format, String filePath) {
        if (products == null || products.isEmpty()) {
            JOptionPane.showMessageDialog(null, "没有可导出的数据！", "提示", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        
        try {
            switch (format) {
                case CSV:
                    return exportToCSV(products, filePath);
                case EXCEL:
                    return exportToExcel(products, filePath);
                default:
                    JOptionPane.showMessageDialog(null, "不支持的导出格式！", "错误", JOptionPane.ERROR_MESSAGE);
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "导出失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * 将产品数据导出为CSV格式
     * @param products 产品列表
     * @param filePath 导出文件路径
     * @return 是否导出成功
     */
    private static boolean exportToCSV(List<Product> products, String filePath) throws IOException {
        // 添加.csv后缀（如果没有）
        if (!filePath.toLowerCase().endsWith(".csv")) {
            filePath += ".csv";
        }
        
        // 创建文件输出流
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath), "UTF-8"));) {
            
            // 写入CSV表头
            writer.write("产品名称,化学药品编号,结构图,分子式,价格,库存,分类");
            writer.newLine();
            
            // 写入产品数据
            for (Product product : products) {
                writer.write(formatCSVField(product.getProductname()) + "," +
                             formatCSVField(product.getCas()) + "," +
                             formatCSVField(product.getStructure()) + "," +
                             formatCSVField(product.getFormula()) + "," +
                             formatCSVField(product.getPrice()) + "," +
                             formatCSVField(product.getRealstock()) + "," +
                             formatCSVField(product.getCategory()));
                writer.newLine();
            }
        }
        
        JOptionPane.showMessageDialog(null, "CSV文件导出成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }
    
    /**
     * 格式化CSV字段，处理包含逗号和引号的情况
     * @param field 字段值
     * @return 格式化后的字段值
     */
    private static String formatCSVField(String field) {
        if (field == null) {
            return "";
        }
        
        // 如果字段包含逗号、引号或换行符，需要用引号括起来
        if (field.contains(",") || field.contains("\")") || field.contains("\n") || field.contains("\r")) {
            // 转义双引号
            field = field.replaceAll("\"", "\"\"");
            // 用双引号括起来
            field = "\"" + field + "\"";
        }
        
        return field;
    }
    
    /**
     * 将产品数据导出为Excel格式
     * @param products 产品列表
     * @param filePath 导出文件路径
     * @return 是否导出成功
     */
    private static boolean exportToExcel(List<Product> products, String filePath) throws IOException {
        // 添加.xlsx后缀（如果没有）
        if (!filePath.toLowerCase().endsWith(".xlsx")) {
            filePath += ".xlsx";
        }
        
        // 注意：由于当前项目中没有引入Apache POI库，
        // 这里只提供一个简单的实现框架，实际使用时需要添加POI库依赖
        // 并实现完整的Excel导出逻辑
        
        // 临时实现：创建一个简单的文本文件，提示需要Apache POI库
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath), "UTF-8"));) {
            
            writer.write("# 药品数据导出");
            writer.newLine();
            writer.newLine();
            writer.write("产品名称,化学药品编号,结构图,分子式,价格,库存,分类");
            writer.newLine();
            
            // 写入产品数据
            for (Product product : products) {
                writer.write(product.getProductname() + "," +
                             product.getCas() + "," +
                             product.getStructure() + "," +
                             product.getFormula() + "," +
                             product.getPrice() + "," +
                             product.getRealstock() + "," +
                             product.getCategory());
                writer.newLine();
            }
        }
        
        JOptionPane.showMessageDialog(null, "Excel文件导出框架已实现，实际使用时需要添加Apache POI库依赖！", "提示", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }
    
    /**
     * 选择导出文件路径
     * @param parent 父窗口
     * @param format 导出格式
     * @return 选择的文件路径，或null如果用户取消选择
     */
    public static String chooseExportFilePath(java.awt.Component parent, ExportFormat format) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("选择导出文件路径");
        
        // 设置文件过滤器
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                format == ExportFormat.CSV ? "CSV文件 (*.csv)" : "Excel文件 (*.xlsx)",
                format == ExportFormat.CSV ? "csv" : "xlsx"));
        
        // 显示保存对话框
        int result = fileChooser.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        
        return null;
    }
    
    /**
     * 获取所有产品数据（从数据访问器中获取）
     * @return 所有产品数据列表
     */
    public static List<Product> getAllProducts() {
        List<Product> allProducts = new ArrayList<>();
        
        // 获取数据访问器实例
        ProductDataAccessor dataAccessor = new ProductDataAccessor();
        
        // 获取所有分类
        List<String> categories = dataAccessor.getCategories();
        
        // 遍历所有分类，获取产品列表
        for (String category : categories) {
            List<Product> products = dataAccessor.getProducts(category);
            if (products != null) {
                allProducts.addAll(products);
            }
        }
        
        return allProducts;
    }
}