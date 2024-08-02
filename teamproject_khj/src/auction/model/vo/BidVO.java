package auction.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BidVO {
	private int bi_num;
	private AuctionVO auction;
	private int bi_price;
	private String bi_me_id;
	
	public BidVO(AuctionVO auction, int bi_price, String bi_me_id) {
		this.auction = auction;
		this.bi_price = bi_price;
		this.bi_me_id = bi_me_id;
	}
	public BidVO(int bi_price, String bi_me_id) {
		this.bi_price = bi_price;
		this.bi_me_id = bi_me_id;
	}
}
