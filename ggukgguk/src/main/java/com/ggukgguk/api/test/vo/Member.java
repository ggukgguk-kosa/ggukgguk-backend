package com.ggukgguk.api.test.vo;

import java.util.Date;

import com.ggukgguk.api.common.enums.MemberRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Member {
	private String memberId;
	private String memberPw;
	private String memberName;
	private Date memberBirth;
	private String memberNickname;
	private String memberEmail;
	private char memberType;
	private String memberComment;
	private Date memberCreatedAt;
	
	public String getRole() {
		return MemberRole.valueOfCode(memberType).label();
	}
}
