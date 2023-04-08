package com.ggukgguk.api.member.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ggukgguk.api.auth.enums.MemberRoleEnum;

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
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date memberBirth;
	private String memberNickname;
	private String memberEmail;
	private char memberType;
	private String memberComment;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date memberCreatedAt;
	
	public String getRole() {
		return MemberRoleEnum.valueOfCode(memberType).label();
	}
}
