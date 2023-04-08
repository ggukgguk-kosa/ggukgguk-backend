package com.ggukgguk.api.test.vo;

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
	private String memberBirth;
	private String memberNickname;
	private String memberEmail;
	private String memberType;
	private String memberComment;
	private String memberCreatedAt;
}
