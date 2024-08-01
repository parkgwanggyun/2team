package auction.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberVO {
	private String me_id;
	private String me_password;
	private String me_name;
	private String me_address;
	private String me_contact;
	
	public MemberVO(String me_id, String me_password, String me_name, String me_address, String me_contact) {
		this.me_id = me_id;
		this.me_password = me_password;
		this.me_name = me_name;
		this.me_address = me_address;
		this.me_contact = me_contact;
	}
	public MemberVO(String me_password, String me_name, String me_address, String me_contact) {
		this.me_password = me_password;
		this.me_name = me_name;
		this.me_address = me_address;
		this.me_contact = me_contact;
	}
	
	@Override
	public String toString() {
		return "[" + me_id + " : " + me_password + "]  [이름: " + me_name + "]  [주소: "
				+ me_address + "]  [연락처: " + me_contact + "]";
	}
	
	
}
