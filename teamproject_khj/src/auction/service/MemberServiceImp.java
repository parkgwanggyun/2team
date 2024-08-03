package auction.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import auction.dao.MemberDAO;
import auction.model.vo.MemberVO;

public class MemberServiceImp implements MemberService{

	private MemberDAO memberDao;

	public MemberServiceImp() {
		String resource = "auction/config/mybatis-config.xml";
		InputStream inputStream;
		SqlSession session;
		try {
			inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			session = sessionFactory.openSession(true);
			memberDao = session.getMapper(MemberDAO.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// 회원 추가
	public boolean insertMember(MemberVO member) {
		if(member == null) {
			return false;
		}
		//이미 등록된 회원인지 확인		
		String memberId = memberDao.selectMemberId(member.getMe_id());
//		System.out.println(memberId); //확인용
		//등록된 회원이면
		if(memberId != null) {
			return false;
		}
		return memberDao.insertMember(member);
	}

	public boolean exists(String memberId) {
		if(memberId == null) {
			return false;
		}
		String dbMemberId = memberDao.selectMemberId(memberId);
//		System.out.println(dbMemberId); //확인용
		//등록되지 않은 아이디이면 false 반환
		return dbMemberId != null;
	}
	// 회원 수정
	public boolean updateMember(String memberId, MemberVO newMember) {
		if(memberId == null || newMember == null) {
			return false;
		}
		newMember.setMe_id(memberId);
		return memberDao.updateMember(newMember);
	}
	// 회원 삭제
	public boolean deleteMember(String memberId) {
		if(memberId == null) {
			return false;
		}
		return memberDao.deleteMember(memberId);
	}
	// 회원 검색
	public ArrayList<MemberVO> searchMemberList(String searchMemberInfo) {
		if(searchMemberInfo == null) {
			return null;
		}
		searchMemberInfo = "%"+searchMemberInfo+"%";
		return memberDao.selectMemberList(searchMemberInfo);
	}
	
	@Override
	public MemberVO logIn(String id, String password) {
		MemberVO user = memberDao.selectMemberById(id);
		if(user == null) {
			return null;
		}
		if(user.getMe_password().equals(password)) {
			return user;
		}
		return null;
	}

}
