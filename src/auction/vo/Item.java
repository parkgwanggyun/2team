package auction.vo;

import java.text.DecimalFormat;

import lombok.Data;

@Data
public class Item {
	private int it_num;
	private String it_name;
	private int it_start_price;
	private String it_winning_bid;
	
	public Item(String it_name, int it_start_price, String it_winning_bid) {
		this.it_name = it_name;
		this.it_start_price = it_start_price;
		this.it_winning_bid = it_winning_bid;
	}
	
	public String getPriceWon() {
		DecimalFormat format = new DecimalFormat("\u00A4 ###,###,###");
		return format.format(it_start_price);
	}
	
	@Override
	public String toString() {
		return "진행중인 경매 [물품명: " + it_name + "] [최고입찰가: " + getPriceWon() + "] [입찰자: " + it_winning_bid + "]";
	}
	
//	public void updateBid(String id, int price) {
//		this.bidder = id;
//		this.price = price;
//	}
	
}
