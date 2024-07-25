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
import java.util.List;
import java.util.Scanner;

import auction.Item;
import auction.config.Program;

public class ServerManager implements Program {
	static Scanner scan = new Scanner(System.in);
	public static List<Item> itemList = Collections.synchronizedList(new ArrayList<Item>());
	static Instant finish;
	static Item item;
	
	@Override
	public void printMenu() {
		System.out.println("1. 경매 물품 등록");
		System.out.println("2. 경매 낙찰 리스트");
		System.out.print("메뉴 선택 : ");
	}

	@Override
	public void runMenu(int menu) throws Exception {
		switch(menu) {
			case 1:
				printBorder();
				addItem();
				auctionStart();
				break;
			case 2:
				printBorder();
				
				printBorder();
				break;
			case 3:
				printBorder();
				//exit();
				break;
			default:
				//wrongMenu();
		}
	}

	public static void showItemList() {
		for(Item tmp : itemList) {
			if(tmp != null) {
				System.out.println("[ " + tmp.getName() + " | " +  tmp.getPrice() + " | " + tmp.getBidder() + " ]");
			}
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
	
	private void auctionStart() {
		List<ObjectOutputStream> list = new ArrayList<ObjectOutputStream>();
		
		int port = 6006;
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			System.out.println(ip + " " + port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		try(ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("<< 경매 서버 오픈 >>");
			while(true) {
				Socket socket = serverSocket.accept();
				if(socket.isConnected()) {
					System.out.println("[" + socket.getLocalAddress() + " : " + socket.getPort() + "에서 접속]");
				}
				Server server = Server.getServer(list, socket);
				server.timer(finish);
				server.receive(item, finish);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		int menu = 0;
		printMenu();
		menu = nextInt();
		try {
			runMenu(menu);
		} catch (Exception e) {
			System.out.println("오류가 발생했습니다");
		}
	}
}
