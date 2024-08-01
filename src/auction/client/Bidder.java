package auction.client;

import java.net.Socket;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import auction.config.Program;
import auction.controller.ItemController;
import auction.vo.Item;

public class Bidder implements Program {
	
	private static Scanner scan = new Scanner(System.in);
	private ItemController itemController = new ItemController(scan);
	
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
			menu = scan.nextInt();
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
		String ip = scan.next();
		int port = scan.nextInt();
		System.out.print("아이디 입력 : ");
		String id = scan.next();
		
		System.out.println("현재 경매 중인 물품");
		List<Item> tmpList = itemController.getNowAuctionItemList();
		
		if (tmpList.size() == 0) {
			System.out.println("진행 중인 경매가 없습니다");
			return;
		}
		for (Item tmp : tmpList) System.out.println(tmp);
		
		
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
