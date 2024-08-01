package auction.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import auction.controller.ItemController;
import auction.vo.Item;


public class Auctioneer {
	private static Scanner scan = new Scanner(System.in);
	static Instant finish;
	static Item item;
	public static List<Item> itemList = Collections.synchronizedList(new ArrayList<Item>());
	private static ItemController itemController = new ItemController(scan);
	
	public static void main(String[] args) {
		int port = 6006;
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			System.out.println(ip + "  " + port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		List<ObjectOutputStream> list = new ArrayList<ObjectOutputStream>();
//		List<Item> itemList = new ArrayList<Item>();
		
		try(ServerSocket serverSocket = new ServerSocket(port)) {
			//itemList = loadItemList(file);
			start();
			System.out.println("현재 경매 물품");
			System.out.println(item.getIt_num() + ". " + item.getIt_name() + " | 시작가 : " + item.getIt_start_price());
			itemController.startAuction(item);
			System.out.println("<< 경매 서버 오픈 >>");
			while(true) {
				Socket socket = serverSocket.accept();
				Server server = new Server(list, socket);
				server.receive(item, finish);
//				server.timer(finish);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void start() {
		int menu;
		System.out.println("1. 경매 물품 등록");
		System.out.println("2. 경매 낙찰 리스트");
		System.out.println("3. 경매 서버 열기");
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
		case 3 :
			openAuction();
			break;
		default :
			System.out.println("잘못된 메뉴 입니다.");
		}
	}
	
	private static void openAuction() {
		List<Item> tmpList = itemController.getItemList();
		for (Item tmp : tmpList)
			System.out.println(tmp.getIt_num() + ". " + tmp.getIt_name() + " | 시작가 : " + tmp.getIt_start_price());
		System.out.print("경매 물품 번호 선택 : ");
		int i = scan.nextInt();
		item = itemController.selectItem(i);
		
		System.out.print("진행 시간(분) : ");
		int period = (scan.nextInt() * 60);
		finish = Instant.now().plusSeconds(period);
		ZonedDateTime auctionFinish = finish.atZone(ZoneId.of("Asia/Seoul"));
		System.out.println("종료 시간: " + auctionFinish.format(DateTimeFormatter.ofPattern("HH시 mm분 ss초")));
	}

	public static void addItem() {
		item = setItem();
		itemController.insertItem(item);
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
				System.out.println("[ " + tmp.getIt_name() + " | " +  tmp.getPriceWon() + " | " + tmp.getIt_winning_bid() + " ]");
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
}
