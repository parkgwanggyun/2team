package auction.client;

import java.net.Socket;

import auction.config.Config;

public class Bidder {
	public static void main(String[] args) {
		System.out.print("접속할 IP와 port번호 입력 : ");
		String ip = Config.scan.next();
		int port = Config.scan.nextInt();
		System.out.print("아이디 입력 : ");
		String id = Config.scan.next();
		try {
			Socket socket = new Socket(ip, port);
			System.out.println("[경매 서버에 연결]");
			Client client = new Client(socket, id);
			client.start();
		} catch (Exception e) {
			System.out.println("[연결에 실패하였습니다.]");
		}
	}
}
