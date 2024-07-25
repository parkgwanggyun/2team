package auction.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import auction.Item;


public class Auctioneer {
	static Scanner scan = new Scanner(System.in);
	static Instant finish;
	static Item item;
	public static List<Item> itemList = Collections.synchronizedList(new ArrayList<Item>());
	private static File file = new File("src/auction/server/itemData.txt");
	
	public static void main(String[] args) {
//		List<Item> itemList = new ArrayList<Item>();
		itemList = loadItemList(file);
		ServerManager sm = new ServerManager();
		sm.run();
	}
	/*public static void start() {
		int menu;
		System.out.println("1. 경매 물품 등록");
		System.out.println("2. 경매 낙찰 리스트");
		System.out.print("메뉴 선택 : ");
		
		menu = nextInt();
		runMenu(menu);
	}


	public static void runMenu(int menu) {
		switch (menu) {
		case 1 : 
			addItem();
			break;
		case 2 :
			showItemList();
			break;
		default :
			System.out.println("잘못된 메뉴 입니다.");
		}
	}
	
	public static void addItem() {
		//item = setItem();
		item = new Item("사과", 100, "20");
		System.out.print("진행 시간(분) : ");
		//int period = (scan.nextInt() * 60);
		int period = 6000;
		finish = Instant.now().plusSeconds(period);
		ZonedDateTime  auctionFinish = finish.atZone(ZoneId.of("Asia/Seoul"));
		System.out.println("종료 시간: " + auctionFinish.format(DateTimeFormatter.ofPattern("HH시 mm분 ss초")));
	}
	
	public static Item setItem() {
		System.out.println("[경매 물품 등록]");
		String name;
		int price;
		System.out.print("물품명: ");
		scan.nextLine();
		name = scan.nextLine();
		System.out.print("시작가 : ");
		price = scan.nextInt();
		
		return new Item(name, price, "");
	}
	
	public static void showItemList() {
		for(Item tmp : itemList) {
			if(tmp != null) {
				System.out.println("[ " + tmp.getName() + " | " +  tmp.getPrice() + " | " + tmp.getBidder() + " ]");
			}
		}
	}
	
	public static int nextInt() {
		try {
			return scan.nextInt();
		} catch (InputMismatchException e) {
			scan.nextLine();
			return Integer.MIN_VALUE;
		}
	}
	*/
	@SuppressWarnings("unchecked")
	private static List<Item> loadItemList(File file) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
			return  (List<Item>)ois.readObject();
		} catch (Exception e) {
			System.out.println("파일 불러오기 실패");
		}
		return null;
	}
}
