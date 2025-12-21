package com.ascent.util;

import java.io.*;
import java.net.*;

/**
 * 数据服务服务器
 * @author ascent
 * @version 1.0
 */
public class ProductDataServer implements ProtocolPort {

	protected ServerSocket myServerSocket;

	protected ProductDataAccessor myProductDataAccessor;

	protected boolean done;

	/**
	 * 默认构造方法
	 */
	public ProductDataServer() {
		this(ProtocolPort.DEFAULT_PORT);
	}

	/**
	 * 另一个构造方法
	 * @param thePort 服务端口号
	 */
	public ProductDataServer(int thePort) {

		try {
			done = false;
			log("启动服务器 " + thePort);
			myServerSocket = new ServerSocket(thePort);
			myProductDataAccessor = new ProductDataAccessor();

			log("\n服务器准备就绪!");
			listenForConnections();
		} catch (IOException exc) {
			log(exc);
			System.exit(1);
		}
	}

	/**
	 * 监听客户端连接请求
	 */
	protected void listenForConnections() {
		Socket clientSocket = null;
		Handler aHandler = null;

		try {
			while (!done) {
				log("\n等待连接...");
				clientSocket = myServerSocket.accept();

				String clientHostName = clientSocket.getInetAddress().getHostName();

				log("收到连接: " + clientHostName);
				aHandler = new Handler(clientSocket, myProductDataAccessor);
				aHandler.start();
			}
		} catch (IOException exc) {
			log("listenForConnections()方法中发生异常:  " + exc);
		}
	}

	/**
	 * 日志方法
	 * @param msg 打印的日志信息
	 */
	protected void log(Object msg) {
		System.out.println("ProductDataServer> " + msg);
	}

	/**
	 * 服务器主入口方法
	 * @param args 参数
	 */
	public static void main(String[] args) {

		@SuppressWarnings("unused")
		ProductDataServer myServer = null;

		if (args.length == 1) {
			int port = Integer.parseInt(args[0]);
			myServer = new ProductDataServer(port);
		} else {
			myServer = new ProductDataServer();
		}
	}
}