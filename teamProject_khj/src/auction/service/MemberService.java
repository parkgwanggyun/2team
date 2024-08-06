package auction.service;

import java.util.ArrayList;

import auction.model.vo.MemberVO;

public interface MemberService {

	boolean insertMember(MemberVO member);

	boolean exists(String memberId);

	boolean updateMember(String memberId, MemberVO newMember);

	boolean deleteMember(String memberId);

	ArrayList<MemberVO> searchMemberList(String searchMemberInfo);

	MemberVO logIn(String id, String password);

}
