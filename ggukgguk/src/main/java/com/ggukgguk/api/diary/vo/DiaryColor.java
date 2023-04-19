package com.ggukgguk.api.diary.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiaryColor {

	private int diaryColorId;
	private int diaryId;
	private String diaryColor;
}
