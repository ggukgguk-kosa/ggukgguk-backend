package com.ggukgguk.api.common.vo;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageHandler {

	private SearchCondition sc;
	public final int NAV_SIZE = 10; 
	private int totalCnt; 
	private int totalPage; 
	private int beginPage; 
	private int endPage; 
	private boolean showNext = false; 
	private boolean showPrev = false; 

	public PageHandler(int totalCnt, Integer page) {
		this(totalCnt, new SearchCondition(page, 10));
	}

	public PageHandler(int totalCnt, Integer page, Integer pageSize) {
		this(totalCnt, new SearchCondition(page, pageSize));
	}

	public PageHandler(int totalCnt, SearchCondition sc) {
		this.totalCnt = totalCnt;
		this.sc = sc;

		doPaging(totalCnt, sc);
	}

	private void doPaging(int totalCnt, SearchCondition sc) {
		this.totalPage = totalCnt / sc.getPageSize() + (totalCnt % sc.getPageSize() == 0 ? 0 : 1);
		this.sc.setPage(Math.min(sc.getPage(), totalPage)); 
		this.beginPage = (this.sc.getPage() - 1) / NAV_SIZE * NAV_SIZE + 1; 
		this.endPage = Math.min(beginPage + NAV_SIZE - 1, totalPage);
		this.showPrev = beginPage != 1;
		this.showNext = endPage != totalPage;
	}

	public String getQueryString() {
		return getQueryString(this.sc.getPage());
	}

	public String getQueryString(Integer page) {
		return UriComponentsBuilder.newInstance().queryParam("page", page).queryParam("pageSize", sc.getPageSize())
				.queryParam("option", sc.getOption()).queryParam("keyword", sc.getKeyword()).build().toString();
	}

	void print() {
		System.out.println("page=" + sc.getPage());
		System.out.print(showPrev ? "PREV " : "");

		for (int i = beginPage; i <= endPage; i++) {
			System.out.print(i + " ");
		}
		System.out.println(showNext ? " NEXT" : "");
	}

}
