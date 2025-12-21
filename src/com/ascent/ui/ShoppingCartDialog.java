package com.ascent.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.ascent.bean.Product;
import com.ascent.util.ShoppingCart;

/**
 * 购物车对话框，显示购物车中的产品信息
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ShoppingCartDialog extends JDialog {

	protected ShoppingCart shoppingCart;

	protected Frame parentFrame;

	private JButton shoppingButton;

	private HashMap<String,JTextField> textMap;

	private JLabel tipLabel;

	/**
	 * 构造方法
	 * @param theParentFrame 父窗口
	 * @param shoppingButton 查看购物车按钮
	 */
	public ShoppingCartDialog(Frame theParentFrame, JButton shoppingButton) {
		super(theParentFrame, "购物车", true);
		textMap = new HashMap<String,JTextField>();
		parentFrame = theParentFrame;
		this.shoppingButton = shoppingButton;

		lookShoppingCar();
	}

	/**
	 * 显示购物车中产品信息的方法
	 */
	public void lookShoppingCar() {
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());

		JPanel infoPanel = new JPanel();
		tipLabel = new JLabel("");
		infoPanel.add(tipLabel);
		infoPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
		infoPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 0, 2, 10);

		shoppingCart = new ShoppingCart();
		ArrayList<Product> shoppingList = shoppingCart.getShoppingList();

		JLabel pruductLabel;
		Product product = null;
		for (int i = 0; i < shoppingList.size(); i++) {
			c.gridy = c.gridy + 2;
			String str = "";
			product = shoppingList.get(i);
			str = str + "产品名称：" + product.getProductname() + "    ";
			str = str + "CAS号：" + product.getCas() + "    ";
			str = str + "分子式：" + product.getFormula() + "    ";
			str = str + "类别：" + product.getCategory();
			pruductLabel = new JLabel(str);
			JPanel panel = new JPanel(new FlowLayout());
			JLabel l = new JLabel("购买数量：");
			JTextField jtf = new JTextField(7);
			jtf.setText("1");
			panel.add(pruductLabel);
			panel.add(l);
			panel.add(jtf);
			textMap.put(product.getProductname(), jtf);
			pruductLabel.setForeground(Color.black);
			infoPanel.add(panel, c);
		}

		container.add(BorderLayout.NORTH, infoPanel);

		JPanel bottomPanel = new JPanel();
		JButton okButton = new JButton("提交订单");
		bottomPanel.add(okButton);
		JButton clearButton = new JButton("清空");
		bottomPanel.add(clearButton);

		container.add(BorderLayout.SOUTH, bottomPanel);

		okButton.addActionListener(new OkButtonActionListener());
		clearButton.addActionListener(new ClearButtonActionListener());
		setResizable(false);
		this.pack();
		Point parentLocation = parentFrame.getLocation();
		this.setLocation(parentLocation.x + 50, parentLocation.y + 50);
	}

	/**
	 * "提交订单"按钮监听器
	 * @author ascent
	 */
	class OkButtonActionListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			Set set = textMap.keySet();
			for (Object o : set) {
				JTextField t = (JTextField) textMap.get((String) o);
				if ("".equals(t.getText())) {
					tipLabel.setText("购买数量不能为空！");
					return;
				}
			}
			setVisible(false);
			ShoppingMessageDialog myMessageDialog = new ShoppingMessageDialog((JFrame)parentFrame, "订单提交成功！");
			myMessageDialog.setVisible(true);
		}
	}

	/**
	 * "清空"按钮监听器
	 * @author ascent
	 */
	class ClearButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			ShoppingCart shopping = new ShoppingCart();
			shopping.clearProduct();
			shoppingButton.setEnabled(false);
			setVisible(false);
		}
	}
}