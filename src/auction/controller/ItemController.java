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
	
	public List<Item> getItemList() {
		return itemService.selectItemList();
	}

	public Item selectItem(int i) {
		return itemService.selectItem(i);
	}

	public void startAuction(Item item) {
		if (itemService.updateAuctionNow(item)) System.out.println("[경매 시작 준비 완료]");
		else System.out.println("[경매 시작 준비 실패]");
	}

	public List<Item> getNowAuctionItemList() {
		return itemService.getNowAuctionItemList();
	}

	public Item getItem(int num) {
		return itemService.getItem(num);
	}
}
