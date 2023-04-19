package com.ggukgguk.api.diary.dao;

import java.util.List;

import com.ggukgguk.api.diary.vo.Diary;
import com.ggukgguk.api.diary.vo.DiaryColor;
import com.ggukgguk.api.diary.vo.DiaryMonth;
import com.ggukgguk.api.diary.vo.DiarySearch;

public interface DiaryDao {

	public List<Diary> selectDairyList(DiarySearch diarySearch);

	public DiaryMonth selectDiary(DiarySearch diarySearch);

	public List<DiaryColor> selectColor(int diaryId);
	
	public void updateDiary(Diary diary) throws Exception;

}