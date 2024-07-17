package auction.community;

import java.io.IOException;
import java.net.Socket;

import auction.Config;
import auction.Item;

public class ClientMain {
	public static Item nowItem = new Item("사과", 100, 20, 10);
	//정상적으로 pm을 통한 접근이 가능해지면 지우기
	
	public static void main(String[] args) {
		try {
			System.out.print("아이디 입력 : ");
			String id = Config.sc.nextLine();
			Socket socket = new Socket(Config.ip, Config.port);
			
			System.out.println("[연결 성공]");
			System.out.println("이번 경매품");
			System.out.println("품목 : " + nowItem.getName());
			System.out.println("시작가 : " + nowItem.getStart() + "원");
			System.out.println("진행 시간 : " + nowItem.getTime() + "초");
			
			Client client = new Client(id, socket);
			
			client.recieve();
			client.send();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}