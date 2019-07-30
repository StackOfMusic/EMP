package com.emp.bpms.util.paging;

public class PageMaker {

	private int totalCount;     // 게시?�� ?���? ?��?��?�� 개수
	private int displayPageNum = 10;   // 게시?�� ?��면에?�� ?��번에 보여�? ?��?���? 번호?�� 개수 ( 1,2,3,4,5,6,7,9,10)
	
	private int startPage;      // ?��?�� ?��면에?�� 보이?�� startPage 번호
	private int endPage;        // ?��?�� ?��면에 보이?�� endPage 번호
	private boolean prev;       // ?��?���? ?��?�� 버튼 ?��?��?�� ?���?
	private boolean next;       // ?��?���? ?��?�� 버튼 ?��?��?�� ?���?
	
	private Criteria cri;       // ?��?�� ?��?��?�� Criteria�? 주입받는?��.

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		
		calcData();
	}
	
	private void calcData() {
		endPage = (int) (Math.ceil(cri.getPage() / (double) displayPageNum) * displayPageNum);
		
		startPage = (endPage - displayPageNum) + 1;
		
		int tempEndPage = (int) (Math.ceil(totalCount / (double) cri.getPerPageNum()));
		
		if(endPage > tempEndPage) {
			endPage = tempEndPage;
		}
		
		prev = startPage == 1 ? false : true;
		next = endPage * cri.getPerPageNum() >= totalCount ? false : true;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public boolean isPrev() {
		return prev;
	}

	public void setPrev(boolean prev) {
		this.prev = prev;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}

	public int getDisplayPageNum() {
		return displayPageNum;
	}

	public void setDisplayPageNum(int displayPageNum) {
		this.displayPageNum = displayPageNum;
	}

	public Criteria getCri() {
		return cri;
	}

	public void setCri(Criteria cri) {
		this.cri = cri;
	}

	@Override
	public String toString() {
		return "PageMaker [totalCount=" + totalCount + ", startPage=" + startPage + ", endPage=" + endPage + ", prev="
				+ prev + ", next=" + next + ", displayPageNum=" + displayPageNum + ", cri=" + cri + "]";
	}
		
}