package com.ggukgguk.api.admin.vo;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class NoticeList {
	private int articleId; // 寃뚯떆湲� �븘�씠�뵒
	private String memberNickname; //�쉶�썝 �땳�꽕�엫
	private String articleTitle; // 寃뚯떆湲� �젣紐�
	private String articleContent; // 寃뚯떆湲� �궡�슜
	private String memberId; // �쉶�썝 �븘�씠�뵒
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date articleCreatedAt; // 寃뚯떆湲� �옉�꽦�씪�떆
	private String commentCnt; // �뙎湲� �닔
	private int likeCnt;// 醫뗭븘�슂 媛쒖닔
	
}
