package com.ascent.util;

import java.io.*;
import java.net.*;
import java.util.*;

import com.ascent.bean.Product;
import com.ascent.bean.User;

/**
 * 处理socket连接的线程类 例如:
 * <pre>
 * Handler aHandler = new Handler(clientSocket, myProductDataAccessor);
 * aHandler.start();
 * </pre>
 * @author ascent
 * @version 1.0
 */
public class Handler extends Thread implements ProtocolPort {

	protected Socket clientSocket;

	protected ObjectOutputStream outputToClient;

	protected ObjectInputStream inputFromClient;

	protected ProductDataAccessor myProductDataAccessor;

	protected boolean done;

	/**
	 * �����������Ĺ��췽��
	 * @param theClientSocket �ͻ���Socket����
	 * @param theProductDataAccessor ������Ʒ���ݵĶ���
	 * @throws IOException �������ʱ���ܷ���IOException�쳣
	 */
	public Handler(Socket theClientSocket,ProductDataAccessor theProductDataAccessor) throws IOException {
		clientSocket = theClientSocket;
		outputToClient = new ObjectOutputStream(clientSocket.getOutputStream());
		inputFromClient = new ObjectInputStream(clientSocket.getInputStream());
		myProductDataAccessor = theProductDataAccessor;
		done = false;
	}

	/**
	 * ִ�ж��̵߳�run()�����������ͻ��˷��͵�����
	 */
	@Override
	public void run() {

		try {
			while (!done) {

				log("�ȴ�����...");

				int opCode = inputFromClient.readInt();
				log("opCode = " + opCode);

				switch (opCode) {
				case ProtocolPort.OP_GET_PRODUCT_CATEGORIES:
					opGetProductCategories();
					break;
				case ProtocolPort.OP_GET_PRODUCTS:
					opGetProducts();
					break;
				case ProtocolPort.OP_GET_USERS:
					opGetUsers();
					break;
				case ProtocolPort.OP_ADD_USERS:
					opAddUser();
					break;
				default:
					System.out.println("�������");
				}
			}
		} catch (IOException exc) {
			log(exc);
		}
	}

	/**
	 * �����û���Ϣ
	 */
	private void opGetUsers() {
		try {
			HashMap<String,User> userTable = myProductDataAccessor.getUsers();
			outputToClient.writeObject(userTable);
			outputToClient.flush();
		} catch (IOException exe) {
			log("�����쳣��" + exe);
		}
	}

	/**
	 * ���ط�������
	 */
	protected void opGetProductCategories() {
		try {
			ArrayList<String> categoryList = myProductDataAccessor.getCategories();
			outputToClient.writeObject(categoryList);
			outputToClient.flush();
			log("���� " + categoryList.size() + " �����Ϣ���ͻ���");
		} catch (IOException exc) {
			log("�����쳣:  " + exc);
		}
	}

	/**
	 * ����ĳ���������Ƶ�������Ʒ
	 */
	protected void opGetProducts() {
		try {
			log("��ȡ������Ϣ");
			String category = (String) inputFromClient.readObject();
			log("����� " + category);

			ArrayList<Product> recordingList = myProductDataAccessor.getProducts(category);

			outputToClient.writeObject(recordingList);
			outputToClient.flush();
			log("���� " + recordingList.size() + "����Ʒ��Ϣ���ͻ���.");
		} catch (IOException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}
	}

	/**
	 * �����û�ע��
	 */
	public void opAddUser() {
		try {
			User user = (User) this.inputFromClient.readObject();
			this.myProductDataAccessor.save(user);
		} catch (IOException e) {
			log("�����쳣:  " + e);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			log("�����쳣:  " + e);
			e.printStackTrace();
		}
	}

	/**
	 * �����߳�����ʱ��־
	 * @param flag
	 */
	public void setDone(boolean flag) {
		done = flag;
	}

	/**
	 * ��ӡ��Ϣ������̨
	 * @param msg
	 */
	protected void log(Object msg) {
		System.out.println("������: " + msg);
	}

}