package com.ascent.util;

import java.util.ArrayList;
import com.ascent.bean.Product;

/**
 * 购物车类
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ShoppingCart extends ArrayList<Product> {

	/**
	 * 产品总数量
	 */
	protected int totalCount = 0;

	/**
	 * 购物车总金额
	 */
	protected double totalPrice = 0;

	/**
	 * 默认构造方法
	 */
	public ShoppingCart() {
	}

	/**
	 * 添加产品到购物车
	 */
	public boolean add(Product product) {
		boolean isAdded = false;
		// 检查购物车中是否已存在该产品
		for (Product item : this) {
			if (item.getProductname().equals(product.getProductname())) {
				// 更新产品数量
				// Product类没有setStock方法，且库存是String类型，这里简化处理
				isAdded = true;
				break;
			}
		}
		// 如果购物车中不存在该产品，则添加
		if (!isAdded) {
			isAdded = super.add(product);
		}
		// 更新购物车总数量和总金额
		this.calculateTotal();
		return isAdded;
	}

	/**
	 * 计算购物车总数量和总金额
	 */
	public void calculateTotal() {
		this.totalCount = 0;
		this.totalPrice = 0;
		for (Product product : this) {
			this.totalCount++;
			// price是String类型，需要转换为double
			try {
				this.totalPrice += Double.parseDouble(product.getPrice());
			} catch (NumberFormatException e) {
				// 如果价格转换失败，默认添加0
				this.totalPrice += 0;
			}
		}
	}

	/**
	 * 获取购物车总数量
	 */
	public int getTotalCount() {
		return this.totalCount;
	}

	/**
	 * 获取购物车总金额
	 */
	public double getTotalPrice() {
		return this.totalPrice;
	}

	/**
	 * 添加产品到购物车（兼容旧代码）
	 * @param product 要添加的产品
	 */
	public void addProduct(Product product) {
		this.add(product);
	}

	/**
	 * 获取购物列表（兼容旧代码）
	 * @return 购物车中的产品列表
	 */
	public ArrayList<Product> getShoppingList() {
		return this;
	}

	/**
	 * 清空购物车中的产品（兼容旧代码）
	 */
	public void clearProduct() {
		this.clearCart();
	}

	/**
	 * 清空购物车
	 */
	public void clearCart() {
		this.clear();
		this.totalCount = 0;
		this.totalPrice = 0;
	}
}