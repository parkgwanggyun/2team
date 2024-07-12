package auction.server;

import java.io.File;
import java.io.FileOutputStream;
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
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import auction.Item;


public class Auctioneer {

	static Scanner scan = new Scanner(System.in);
	public static void main(String[] args) {
		int port = 6006;
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			System.out.println(ip + "  " + port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		List<ObjectOutputStream> list = new ArrayList<ObjectOutputStream>();
		Item item = addItem();
		System.out.print("진행 시간(분) : ");
		int period = (scan.nextInt() * 60);
		Instant finish = Instant.now().plusSeconds(period);
		ZonedDateTime  auctionFinish = finish.atZone(ZoneId.of("Asia/Seoul"));
		try(ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("<< 경매 서버 오픈 >> 종료 시간: " + auctionFinish.format(DateTimeFormatter.ofPattern("HH시 mm분 ss초")));
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
	
	public static Item addItem() {
		System.out.println("[경매 물품 등록]");
		String name;
		int price;
		System.out.print("물품명: ");
		name = scan.nextLine();
		System.out.print("시작가 : ");
		price = scan.nextInt();
		
		
		
		
		return new Item(name, price, "");
	}


}
