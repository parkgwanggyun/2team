package auction;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item implements Serializable{
	
	private static final long serialVersionUID = 276242421431949884L;
	private String name;
	private int price;
	private String bidder;
	
	public String getPriceWon() {
		DecimalFormat format = new DecimalFormat("\u00A4 ###,###,###");
		return format.format(price);
	}
	
	@Override
	public String toString() {
		return "진행중인 경매 [물품명: " + name + "] [최고입찰가: " + getPriceWon() + "] [입찰자: " + bidder + "]";
	}
	
//	public void updateBid(String id, int price) {
//		this.bidder = id;
//		this.price = price;
//	}
	
}
