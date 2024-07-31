package auction.client;

import java.net.Socket;
import java.util.Scanner;

import auction.config.Program;

public class Bidder implements Program {
	
	public static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		Bidder bidder = new Bidder();
		bidder.run();
	}
	
	@Override
	public void printMenu() {
		System.out.println("메뉴");
		System.out.println("1. 회원가입");
		System.out.println("2. 로그인");
		System.out.print("메뉴 선택 : ");
	}
	@Override
	public void runMenu(int menu) throws Exception {
		switch(menu) {
			case 1:
				printBorder();
				//insertItem();
				break;
			case 2:
				printBorder();
				start();
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
	
	@Override
	public void run() {
		int menu = 0;
		do {
			printMenu();
			menu = sc.nextInt();
			try {
				runMenu(menu);
			} catch (Exception e) {
				System.out.println("오류가 발생했습니다");
				return;
			}
		} while(menu != 3);
	}
	
	public void start() {
		System.out.print("접속할 IP와 port번호 입력 : ");
		String ip = sc.next();
		int port = sc.nextInt();
		System.out.print("아이디 입력 : ");
		String id = sc.next();
		
		try {
			Socket socket = new Socket(ip, port);
			System.out.println("[경매 서버에 연결]");
			Client client = new Client(socket, id);
			client.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[연결에 실패하였습니다.]");
		}
	}
}
