package auction.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import auction.Item;


public class Auctioneer {

	static Scanner scan = new Scanner(System.in);
	static Instant finish;
	static Item item;
	public static List<Item> itemList = Collections.synchronizedList(new ArrayList<Item>());
	private static File file = new File("src/auction/server/itemData.txt");
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
			itemList = loadItemList(file);
			start();
			System.out.println("<< 경매 서버 오픈 >>");
			while(true) {
				Socket socket = serverSocket.accept();
				if(socket.isConnected()) {
					System.out.println("[" + socket.getLocalAddress() + " : " + socket.getPort() + "에서 접속]");
				}				
				Server server = new Server(list, socket);
				server.timer(finish);
				server.receive(item, finish);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void start() {
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
		item = setItem();
		System.out.print("진행 시간(분) : ");
		int period = (scan.nextInt() * 60);
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

	
	@SuppressWarnings("unchecked")
	private static List<Item> loadItemList(File file) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
			return  (List<Item>)ois.readObject();
		} catch (Exception e) {
			System.out.println("파일 불러오기 실패");
		}
		return null;
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
