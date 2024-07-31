package auction.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import auction.dao.ItemDAO;
import auction.vo.Item;

public class ItemService {
	public static Scanner sc = new Scanner(System.in);
	private ItemDAO itemDao;
	
	public ItemService() {
		String resource = "auction/config/mybatis-config.xml";
		InputStream inputStream;
		SqlSession session;
		
		try {
			inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			session = sessionFactory.openSession(true);
			itemDao = session.getMapper(ItemDAO.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean insertItem(Item item) {
		return itemDao.insertItem(item);
	}

	public Item selectItem(String name) {
		return itemDao.selectItem(name);
	}

	public List<Item> selectItemList() {
		return itemDao.selectItemList();
	}
}