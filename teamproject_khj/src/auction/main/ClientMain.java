package auction.main;

import java.io.IOException;

public class ClientMain {

	public static void main(String[] args) {
		Bidder bidder = new Bidder();
		try {
			bidder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
