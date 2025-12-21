package com.ascent.util;

import java.util.*;
import com.ascent.bean.Product;
import com.ascent.bean.User;

/**
 * 数据访问抽象类，用于从文件中获取产品和用户数据
 * 提供的方法包括获取产品类别、获取产品信息等
 * @author ascent
 * @version 1.0
 */
public abstract class DataAccessor {

	/**
	 * 存储产品信息的HashMap
	 */
	protected HashMap<String,ArrayList<Product>> dataTable;

	/**
	 * 存储用户信息的HashMap
	 */
	protected HashMap<String,User> userTable;

	/**
	 * 最近添加的产品列表
	 */
	protected ArrayList<Product> recentProductList;

	/**
	 * 默认构造方法
	 */
	public DataAccessor() {
		dataTable = new HashMap<String,ArrayList<Product>>();
		userTable = new HashMap<String,User>();
		recentProductList = new ArrayList<Product>();
	}

	/**
	 * 获取产品类别的集合
	 * @return categories 产品类别集合
	 */
	public ArrayList<String> getCategories() {
		Set<String> categorySet = dataTable.keySet();
		log("获取类别...");
		ArrayList<String> categories = new ArrayList<String>(categorySet);
		// 排序
		Collections.sort(categories);
		log("完成获取类别!\n");
		return categories;
	}

	/**
	 * 获取某类产品的列表
	 * @param category 产品类别
	 * @return productList 产品列表
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Product> getProducts(String category) {
		log("获取产品详细信息，类别: " + category);
		ArrayList<Product> productList = dataTable.get(category);
		log("产品数量: " + productList.size());
		// 排序
		Collections.sort(productList);
		log("完成获取产品详细信息!\n");
		return productList;
	}

	/**
	 * 获取用户列表
	 * @return userTable Key:用户名 Value:用户对象
	 */
	public HashMap<String,User> getUsers() {
		return this.userTable;
	}

	/**
	 * 添加新的产品
	 * @param theProduct 要添加到购物车的产品
	 */
	public void addProduct(Product theProduct) {
		String category = theProduct.getCategory();
		log("添加新产品:  " + theProduct);
		ArrayList<Product> productList = dataTable.get(category);
		productList.add(theProduct);
		recentProductList.add(theProduct);
		log("完成添加新产品!\n");
	}

	/**
	 * 从文件中读取数据
	 */
	public abstract void load();

	/**
	 * 将数据保存到文件
	 */
	public abstract void save(User user);

	/**
	 * 日志方法
	 */
	protected void log(Object msg) {
		System.out.println("数据访问类(Data Accessor):  " + msg);
	}
}