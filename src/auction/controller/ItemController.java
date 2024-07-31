package auction.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import auction.service.ItemService;
import auction.vo.Item;

public class ItemController {
	private ItemService itemService = new ItemService();
	private Scanner sc;
	
	public ItemController(Scanner sc) {
		this.sc = sc;
	}

	public ArrayList<Item> loadItemList() {
		ArrayList<Item> list = new ArrayList<Item>();
		
		//list = itemDao.selectStudentList();
		
		return list;
	}

	public void saveItemList() {
		// TODO Auto-generated method stub
		
	}

	public Item setItem() {
		sc.nextLine();
		System.out.println("[경매 물품 등록]");
		System.out.print("물품명: ");
		String name = sc.nextLine();
		System.out.print("시작가 : ");
		int price = sc.nextInt();
		
		return new Item(name, price, "");
	}
	
	public void insertItem(Item item) {
		if (itemService.insertItem(item)) System.out.println("[경매 물품 등록 완료]");
		else System.out.println("[경매 물품 등록 실패]");
	}
	
	public Item selectItem(String name) {
		Item item = itemService.selectItem(name);
		if (item == null) {
			System.out.println("[존재하지 않는 물품]");
			return null;
		}
		return item;
	}

	public List<Item> getItemList() {
		return itemService.selectItemList();
	}
}
