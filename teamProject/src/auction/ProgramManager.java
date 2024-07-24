package auction;

import java.util.Scanner;

import auction.client.Bidder;

public class ProgramManager {
	public static Scanner sc = new Scanner(System.in);
	
	public void printMenu() {
		System.out.println("메뉴");
		System.out.print("메뉴 선택 : ");
	}
	
	public void runMenu(int menu) throws Exception {
		switch(menu) {
			case 1:
				//printBorder();
				//insertItem();
				break;
			case 2:
				//printBorder();
				Bidder.main(null);
				//printBorder();
				break;
			case 3:
				//exit();
				break;
			default:
				//wrongMenu();
		}
	}
	
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
}
