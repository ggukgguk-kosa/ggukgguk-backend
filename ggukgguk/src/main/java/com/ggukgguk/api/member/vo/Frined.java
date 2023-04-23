package com.ggukgguk.api.member.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Frined {
	private String friendId;
	private String member1Id;
	private String member2Id;
	@JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss.SSSZ", timezone = "Asia/Seoul")
	private Date friendCreatedAt;
}
