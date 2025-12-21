package com.ascent.util;

import java.util.*;
import java.io.*;
import com.ascent.bean.Product;
import com.ascent.bean.User;

/**
 * 产品数据访问器实现类
 * @author ascent
 * @version 1.0
 */
public class ProductDataAccessor extends DataAccessor {

	// ////////////////////////////////////////////////////
	//
	// 产品文件格式说明
	// 产品名称,化学药品编号,结构图,分子式,价格,库存,分类
	// ----------------------------------------------------
	//
	// ////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////
	//
	// 用户文件格式说明
	// 用户名,用户密码,用户权限
	// ----------------------------------------------------
	//
	// ////////////////////////////////////////////////////
	/**
	 * 产品信息数据文件名
	 */
	protected static final String PRODUCT_FILE_NAME = "product.db";

	/**
	 * 用户信息数据文件名
	 */
	protected static final String USER_FILE_NAME = "user.db";

	/**
	 * 数据记录的分隔符
	 */
	protected static final String RECORD_SEPARATOR = "----------";

	/**
	 * 默认构造方法
	 */
	public ProductDataAccessor() {
		load();
	}

	/**
	 * 加载数据的方法
	 */
	@Override
	public void load() {
		
		dataTable = new HashMap<String,ArrayList<Product>>();
		userTable = new HashMap<String,User>();

		ArrayList<Product> productArrayList = null;
		StringTokenizer st = null;

		Product productObject = null;
		User userObject = null;
		String line = "";

		String productName, cas, structure, formula, price, realstock, category;
		String userName, password, authority;

		try {
			log("读取文件: " + PRODUCT_FILE_NAME + "...");
			BufferedReader inputFromFile1 = new BufferedReader(new FileReader(PRODUCT_FILE_NAME));

			while ((line = inputFromFile1.readLine()) != null) {
				try {
					st = new StringTokenizer(line, ",");
					if (st.countTokens() >= 7) {
						productName = st.nextToken().trim();
						cas = st.nextToken().trim();
						structure = st.nextToken().trim();
						formula = st.nextToken().trim();
						price = st.nextToken().trim();
						realstock = st.nextToken().trim();
						category = st.nextToken().trim();

						productObject = getProductObject(productName, cas, structure,formula, price, realstock, category);

						if (dataTable.containsKey(category)) {
							productArrayList = dataTable.get(category);
						} else {
							productArrayList = new ArrayList<Product>();
							dataTable.put(category, productArrayList);
						}
						productArrayList.add(productObject);
					}
				} catch (NoSuchElementException e) {
					log("跳过格式错误的产品行: " + line);
				}
			}

			inputFromFile1.close();
			log("文件读取成功!");

			line = "";
			log("读取文件: " + USER_FILE_NAME + "...");
			BufferedReader inputFromFile2 = new BufferedReader(new FileReader(USER_FILE_NAME));
			while ((line = inputFromFile2.readLine()) != null) {
				try {
					st = new StringTokenizer(line, ",");
					if (st.countTokens() >= 3) {
						userName = st.nextToken().trim();
						password = st.nextToken().trim();
						authority = st.nextToken().trim();
						// 跳过空用户名
						if (!userName.isEmpty()) {
							userObject = new User(userName, password, Integer.parseInt(authority));
							if (!userTable.containsKey(userName)) {
								userTable.put(userName, userObject);
							}
						}
					}
				} catch (NoSuchElementException e) {
					log("跳过格式错误的用户行: " + line);
				} catch (NumberFormatException e) {
					log("跳过权限格式错误的用户行: " + line);
				}
			}

			inputFromFile2.close();
			log("文件读取成功!");
			log("准备就绪!\n");
		} catch (FileNotFoundException exc) {
			log("未找到文件: " + PRODUCT_FILE_NAME + " 和 "+USER_FILE_NAME+".");
			log(exc);
		} catch (IOException exc) {
			log("读取文件发生异常: " + PRODUCT_FILE_NAME+ " 和 "+USER_FILE_NAME+".");
			log(exc);
		}
	}

	/**
	 * 返回产品对象的方法
	 * @param productName 产品名称
	 * @param cas 化学药品编号
	 * @param structure 结构图名称
	 * @param formula 分子式
	 * @param price 价格
	 * @param realstock 库存
	 * @param category 分类
	 * @return 新的Product对象
	 */
	private Product getProductObject(String productName, String cas,
			String structure, String formula, String price, String realstock, String category) {
		return new Product(productName, cas, structure, formula, price, realstock, category);
	}

	/**
	 * 保存用户信息
	 */
	@Override
	public void save(User user) {
		log("读取文件: " + USER_FILE_NAME + "...");
		try {
			String userinfo = user.getUsername() + "," + user.getPassword() + "," + user.getAuthority();
			RandomAccessFile fos = new RandomAccessFile(USER_FILE_NAME, "rws");
			fos.seek(fos.length());
			fos.write(("\n" + userinfo).getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 日志方法.
	 */
	@Override
	protected void log(Object msg) {
		System.out.println("ProductDataAccessor: " + msg);
	}

	@Override
	public HashMap<String,User> getUsers() {
		this.load();
		return this.userTable;
	}
}