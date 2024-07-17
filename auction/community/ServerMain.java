package auction.community;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import auction.Config;

public class ServerMain {
	public static void main(String[] args) {
		List<ObjectOutputStream> list = new ArrayList<ObjectOutputStream>();
		try(ServerSocket serverSocket = new ServerSocket(Config.port)){
			System.out.println("[참가자 연결 대기중]");
			while(true) {
				Socket socket = serverSocket.accept();
				System.out.println("[연결 성공]");
				Server server = new Server(list, socket);
				server.receive();
				//server.send();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}