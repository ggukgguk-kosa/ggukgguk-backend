package com.ggukgguk.api.member.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequest {

	private String friendRequestId;
	private String fromMemberId;
	private String toMemberId;
	@JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss.SSSZ", timezone = "Asia/Seoul")
	private Date friendRequestTime;
}
