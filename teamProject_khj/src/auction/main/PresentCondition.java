package auction.main;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PresentCondition{
	//경매현황에는 경매품명, 시작가, 최고입찰가, 종료시간, 인상액이, 입찰id가 있다
	private String name;
	private int startPrice;
	private int highestBid;
	private LocalTime endTime;
	private int increment;
	private String id;
		
	public String getEndTimeToString() {
		String endTime = this.endTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		return endTime;
	}
	
	public void setHighestBidToInt(String bid) {
		this.highestBid = Integer.parseInt(bid);
	}
	
	@Override
	public String toString() {
		return "진행중인 경매 [경매품: " + name + "] [시작가: " + startPrice + "] [최고입찰가: " 
	+ highestBid + "] [종료시간: " + endTime + "] [최소 입찰 가능액: " + increment + "]";
	}

// // yyyy-MM-dd HH:mm:ss 형식으로 변경하기
//	String parsedLocalDateTimeNow = localDateTimeNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//	System.out.println("기존의 LocalDateTimeNow : " + localDateTimeNow);
//	System.out.println("변경된 LocalDateTimeNow : " + parsedLocalDateTimeNow);
// // String > LocalDate	
//	String parsedLocalDateTimeNow = localDateTimeNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//    LocalDateTime formatLocalDateTimeNow =
//	LocalDateTime.parse(parsedLocalDateTimeNow, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//  
 
	
}
