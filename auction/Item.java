package auction;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Item implements Serializable {
	private static final long serialVersionUID = -5627462342306498311L;
	
	private String name;
	private int start;
	private int time;
	private int unit;
	private int finalPrice;
	
	public Item(String name, int start, int time, int unit) {
		this.name = name;
		this.start = start;
		this.time = time;
		this.unit = unit;
	}
}