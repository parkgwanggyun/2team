package auction;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import auction.community.ClientMain;
import program.Program;

public class ProgramManager implements Program {
	public static List<Member> memberList = new ArrayList<Member>();
	public static List<Item> itemList = new ArrayList<Item>();
	
	@Override
	public void printMenu() {
		System.out.println("메뉴");
		System.out.println("1. 아이템 추가");
		System.out.println("2. 경매 참여");
		System.out.println("3. 프로그램 종료");
		System.out.print("메뉴 선택 : ");
	}

	@Override
	public void runMenu(int menu) throws Exception {
		switch(menu) {
			case 1:
				printBorder();
				insertItem();
				break;
			case 2:
				printBorder();
				ClientMain.main(null);
				printBorder();
				break;
			case 3:
				exit();
				break;
			default:
				wrongMenu();
		}
	}

	private void insertItem() {
		Config.sc.nextLine();
		System.out.println("물품 추가");
		System.out.print("물품 이름 : ");
		String name = Config.sc.nextLine();
		System.out.print("시작가 : ");
		int start = Config.sc.nextInt();
		System.out.print("제한시간 : ");
		int time = Config.sc.nextInt();
		System.out.print("단위가 : ");
		int unit = Config.sc.nextInt();
		
		itemList.add(new Item(name, start, time, unit));
		System.out.println("물품을 추가했습니다");
		printBorder();
	}

	@Override
	public void run() {
		dataLoad(Config.fileName, memberList);
		dataLoad(Config.itemFileName, itemList);
		System.out.println("item list");
		System.out.println(itemList);
		int menu = 0;
		do {
			printMenu();
			menu = Config.sc.nextInt();
			try {
				runMenu(menu);
			} catch (Exception e) {
				System.out.println("오류가 발생했습니다");
				return;
			}
		} while(menu != 3);
		dataSave(Config.fileName, memberList);
		dataSave(Config.itemFileName, itemList);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> void dataLoad(String fileName, List<T> list) {
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
			if (fileName.contains("item"))
				itemList = (List<Item>)ois.readObject();
			else
				memberList = (List<Member>)ois.readObject();
		} catch (Exception e) {
		}
	}

	@Override
	public <T> void dataSave(String fileName, List<T> list) {
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
			if (fileName.contains("item"))
				oos.writeObject(itemList);
			else
				oos.writeObject(memberList);
		} catch (Exception e) {
		}
	}

	public void wrongMenu() {
		System.out.println("잘못된 선택입니다");
	}
	
	public void exit() {
		System.out.println("프로그램을 종료합니다");
	}
}