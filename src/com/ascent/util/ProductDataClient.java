package com.ascent.util;

import java.io.*;
import java.net.*;
import java.util.*;
import com.ascent.bean.Product;

/**
 * 产品数据客户端实现类
 * @author ascent
 * @version 1.0
 */
public class ProductDataClient implements ProtocolPort {

	/**
	 * socket连接
	 */
	protected Socket hostSocket;

	/**
	 * 输出到服务器
	 */
	protected ObjectOutputStream outputToServer;

	/**
	 * 从服务器输入
	 */
	protected ObjectInputStream inputFromServer;

	/**
	 * 默认构造方法
	 */
	public ProductDataClient() throws IOException {
		this(ProtocolPort.DEFAULT_HOST, ProtocolPort.DEFAULT_PORT);
	}

	/**
	 * 带服务器和端口的构造方法
	 */
	public ProductDataClient(String hostName, int port) throws IOException {

		log("连接到产品数据服务器..." + hostName + ":" + port);

		hostSocket = new Socket(hostName, port);
		outputToServer = new ObjectOutputStream(hostSocket.getOutputStream());
		inputFromServer = new ObjectInputStream(hostSocket.getInputStream());

		log("连接成功.");
	}

	/**
	 * 获取所有分类
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> getCategories() throws IOException {

		ArrayList<String> categoryList = null;

		try {
			log("发送命令: OP_GET_PRODUCT_CATEGORIES");
			outputToServer.writeInt(ProtocolPort.OP_GET_PRODUCT_CATEGORIES);
			outputToServer.flush();

			log("等待响应...");
			categoryList = (ArrayList<String>) inputFromServer.readObject();
			log("收到 " + categoryList.size() + " 个分类.");
		} catch (ClassNotFoundException exc) {
			log("=====>>>  异常: " + exc);
			throw new IOException("获取分类失败");
		}

		return categoryList;
	}

	/**
	 * 获取产品列表
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Product> getProducts(String category) throws IOException {

		ArrayList<Product> productList = null;

		try {
			log("发送命令: OP_GET_PRODUCTS  分类 = " + category);
			outputToServer.writeInt(ProtocolPort.OP_GET_PRODUCTS);
			outputToServer.writeObject(category);
			outputToServer.flush();

			log("等待响应...");
			productList = (ArrayList<Product>)inputFromServer.readObject();
			log("收到 " + productList.size() + " 个产品.");
		} catch (ClassNotFoundException exc) {
			log("=====>>>  异常: " + exc);
			throw new IOException("获取产品失败");
		}

		return productList;
	}

	/**
	 * 日志方法.
	 */
	protected void log(Object msg) {
		System.out.println("ProductDataClient: " + msg);
	}
}