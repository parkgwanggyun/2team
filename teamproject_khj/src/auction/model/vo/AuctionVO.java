package auction.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuctionVO {
	private int au_num;
	private String au_date;
	private String au_name;
	private int au_start_price;
	private int au_winning_bid;
	private String au_me_id;
	
	public AuctionVO(String au_name, int au_start_price) {
		this.au_name = au_name;
		this.au_start_price = au_start_price;
	}

	@Override
	public String toString() {
		return "[" + au_num + "][" + au_date + "][경매품: " + au_name + "][시작가: "
				+ au_start_price + "][낙찰가: " + au_winning_bid + "][낙찰자ID: " + au_me_id + "]";
	}
		
	
	
}
