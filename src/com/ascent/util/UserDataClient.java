package com.ascent.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import com.ascent.bean.User;

/**
 * 用户数据客户端实现类
 * @author ascent
 * @version 1.0
 */
public class UserDataClient implements ProtocolPort {

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
	public UserDataClient() throws IOException {
		this(ProtocolPort.DEFAULT_HOST, ProtocolPort.DEFAULT_PORT);
	}

	/**
	 * 带服务器和端口号的构造方法
	 */
	public UserDataClient(String hostName, int port) throws IOException {

		log("连接到用户数据服务器..." + hostName + ":" + port);
		try {
			hostSocket = new Socket(hostName, port);
			outputToServer = new ObjectOutputStream(hostSocket.getOutputStream());
			inputFromServer = new ObjectInputStream(hostSocket.getInputStream());
			log("连接成功.");
		} catch (Exception e) {
			log("连接失败.");
			throw e;
		}
	}

	/**
	 * 获取用户
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String,User> getUsers() {
		HashMap<String,User> userTable = null;

		try {
			log("发送命令: OP_GET_USERS  ");
			
			// 检查是否已连接到服务器
			if (outputToServer == null || inputFromServer == null) {
				log("未连接到服务器，无法获取用户数据");
				return userTable;
			}
			
			outputToServer.writeInt(ProtocolPort.OP_GET_USERS);
			outputToServer.flush();

			log("等待响应...");
			userTable = (HashMap<String,User>) inputFromServer.readObject();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userTable;
	}

	/**
	 * 关闭当前Socket
	 */
	public void closeSocKet() {
		try {
			log("关闭Socket连接.");
			hostSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 注册用户
	 * @param username 用户名
	 * @param password 密码
	 * @return boolean true:注册成功 false:注册失败
	 */
	public boolean addUser(String username, String password) {
		try {
			log("发送命令: OP_ADD_USERS  ");
			
			// 检查是否已连接到服务器
			if (outputToServer == null || inputFromServer == null) {
				log("未连接到服务器，无法注册用户");
				return false;
			}
			
			outputToServer.writeInt(ProtocolPort.OP_ADD_USERS);
			outputToServer.writeObject(new User(username, password, 0));
			outputToServer.flush();
			log("注册成功.");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 日志方法.
	 */
	protected void log(Object msg) {
		System.out.println("UserDataClient: " + msg);
	}
}