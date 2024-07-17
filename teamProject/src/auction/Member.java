package auction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Data;

@Data
public class Member implements Serializable {
	private static final long serialVersionUID = -7070308425216151272L;
	private String id;
	private String pw;
	private List<Item> list = new ArrayList<Item>();
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Member other = (Member) obj;
		return Objects.equals(id, other.id) && Objects.equals(pw, other.pw);
	}
	@Override
	public int hashCode() {
		return Objects.hash(id, pw);
	}
	
	public Member(String id, String pw) {
		this.id = id;
		this.pw = pw;
	}
}