package auction.community;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import auction.Config;

public class Server  {
	private static Server server;
	List<ObjectOutputStream> list = new ArrayList<ObjectOutputStream>();
	private Socket socket;
	
	
	public Server(List<ObjectOutputStream> list, Socket socket) {
		this.list = list;
		this.socket = socket;
	}
	
	/*public static synchronized Server getInstance(List<ObjectOutputStream> list, Socket socket) {
		if (getServer() == null) {
			System.out.println("서버 없음");
			setServer(new Server(list, socket));
			System.out.println("서버 있음" + getServer());
		}
		return getServer();
 	}*/

	public void receive() {
		Thread t = new Thread(()->{
			String id = "";
			try(ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());) {
				list.add(oos);
				
				while(true) {
					id = ois.readUTF();
					String chat = ois.readUTF();
					
					if(chat.equals(Config.EXIT)) {
						quit(id);
						break;
					}
					
					System.out.println(id + " : " + chat);
					
					for(ObjectOutputStream tmp : list) {
						if (tmp != oos)
							send(tmp, id, chat);
					}
				}
			} catch (IOException e) {
				quit(id);
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println("[예외 발생]");
			}
		});
		t.start();
	}
	
	public void send(ObjectOutputStream oos, String id, String message) {
		if (oos == null) return;
		
		System.out.println(message);
		Thread t = new Thread(()->{
			try {
				synchronized(oos) {
					oos.writeUTF(id);
					oos.writeUTF(message);
					oos.flush();
				}
			} catch (IOException e) {
				list.remove(oos);
			}
		});
		t.start();
	}
	
	public void send() {
		Thread t = new Thread(()->{
			String id = "adm";
			try {
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				while(true) {
					String str = Config.sc.nextLine();
					oos.writeUTF(id);
					oos.writeUTF(str);
					oos.flush();
					if(str.equals(Config.EXIT)) {
						quit(id);
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		t.start();
	}
	
	public void quit(String id) {
		System.out.println("[" + id + "님이 나갔습니다]");
	}
	
	public static Server getServer() {
		return server;
	}
	public static void setServer(Server server) {
		Server.server = server;
	}
}