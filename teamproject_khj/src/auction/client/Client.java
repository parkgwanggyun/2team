package auction.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

import auction.Item;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Client {
	
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private String id;
	public final static String EXIT = "-quit";
	public static Scanner scan = new Scanner(System.in);
	public static int checkBid;
	public static Instant finishAuction;
	public static final int INCREMENT = 100; //최소 인상액
	public Client(Socket socket, String id) {
		this.id = id;
		this.socket = socket;
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (Exception e) {
		}
	}
	
	public void start() {
		try {
			oos.writeUTF(id);
			oos.flush();
			Item item =	(Item)ois.readObject();
			Instant finish = (Instant)ois.readObject();
			finishAuction = finish;
			checkBid = item.getPrice();
			ZonedDateTime  auctionFinish = finish.atZone(ZoneId.of("Asia/Seoul"));
			System.out.println("진행중인 경매 [물품명: " + item.getName() 
							 + ", 시작가: " + item.getPriceWon() + ", 종료 시간: " 
						 	 + auctionFinish.format(DateTimeFormatter.ofPattern("HH시 mm분 ss초")) + "]");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int menu;
			System.out.println("1. 경매 참가");
			System.out.println("2. 종료");
			System.out.print("메뉴 선택 : ");
			menu = nextInt();
			runMenu(menu);
	}
	
	public void runMenu(int menu) {
		switch (menu) {
		case 1 : 
			send();
			receiveItem();
			auctionTimer();
			break;
		case 2 :
			break;
		default :
			System.out.println("잘못된 메뉴 입니다.");
			
		}
	}
	
	public void auctionTimer() {
		Thread t = new Thread(()->{
			int count = 10;
			while(true) {
				if(finishAuction.minusSeconds(count).isBefore(Instant.now())) {
					System.out.println("경매 종료까지 " + count + "초 남았습니다.");
					count--;
				}
				if(count == 0) {
					break;
				}
			}
		});
		t.start();
	}
	
	public void receiveItem() {
		Thread t = new Thread(()->{
			try {
				Item item;
				while(true) {
					try {
						item = (Item)ois.readObject();
						if(item == null) {
							System.out.println("[경매가 종료되었습니다.]");
							break;
						}else {
							System.out.println(item);
							checkBid = item.getPrice();
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					
				}
				item = (Item)ois.readObject();
				if(item != null) {
					System.out.println(item.getBidder() + "님 낙찰 축하합니다.");
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		});
		t.start();
	}
	
	//문자열을 입력해서 소켓으로 전송하는 쓰레드를 생성하고 실행하는 메소드
	public void send() {
		Thread t = new Thread(()->{
			try {
				while(true){
					System.out.print("희망 입찰가 입력: ");
					String str = scan.next();
					
					if(str.equals(EXIT)) {
						break;
					}
					int bid = Integer.parseInt(str);
					if(bid < (checkBid + INCREMENT)) { //입찰시 증가되는 최소 인상액을 더해서 현 가격과 비교
						System.out.println("이 가격으론 입찰이 불가합니다. ("+(checkBid + INCREMENT)+")");
						continue;
					} else {
						oos.writeUTF(id);
						oos.writeUTF(str);
						oos.flush();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		t.start();
	}
	
	//정수 말고 다른거 입력했을 때 예외 처리
	public int nextInt() {
		try {
			return scan.nextInt();
		} catch (InputMismatchException e) {
			scan.nextLine();
			return Integer.MIN_VALUE;
		}
	}
}
