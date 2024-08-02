package auction.model.vo;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuctionVO {
	private int au_num;
	private Date au_date;
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
		return au_num + ". 경매품 : " + au_name
				+ " | 시작가 : " + getFormatPrice(au_start_price)
				+ " | 낙찰가 : " + getFormatPrice(au_winning_bid)
				+ " | 낙찰일 : " + getFormatDate();
	}
	
	public String getFormatPrice(int price) {
		DecimalFormat format = new DecimalFormat("###,###,###,###");
		return format.format(price);
	}

	private String getFormatDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(au_date);
	}
}
