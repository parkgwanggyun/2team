package auction.community;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import auction.Config;
import auction.Item;
import lombok.AllArgsConstructor;

//연결 소켓을 이용하여 데이터를 주고 받는(Scanner를 통해) 클래스
@AllArgsConstructor
public class Client extends ClientMain {
	private String id;
	private Socket socket;
	
	//소켓에서 보내온 문자열을 받아서 출력하는 스레드를 생성하고 실행
	public void recieve() {
		@SuppressWarnings("static-access")
		Thread t = new Thread(()->{
			try {
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				while(true) {
					String id = ois.readUTF();
					String chat = ois.readUTF();
					if (chat.equals(Config.EXIT)) {
						System.out.println("[" + id + "님이 나갔습니다]");
						break;
					}
					System.out.println(id + " : " + chat);
					if (chat.contains("입찰")) {
						nowItem.setFinalPrice(nowItem.getFinalPrice() + nowItem.getUnit());
						System.out.println(nowItem.getName() + "의 현재 가격 : " + nowItem.getFinalPrice() + "원");
					} else System.out.println("포함안됨");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		t.start();
	}
	
	//문자열을 입력해서 소켓으로 전송하는 스레드를 생성하고 실행하는 메소드
	public void send() {
		Thread t = new Thread(()->{
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
}
