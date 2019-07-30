package com.emp.bpms.util.paging;

public class PageMaker {

	private int totalCount;     // ê²Œì‹œ?Œ ? „ì²? ?°?´?„° ê°œìˆ˜
	private int displayPageNum = 10;   // ê²Œì‹œ?Œ ?™”ë©´ì—?„œ ?•œë²ˆì— ë³´ì—¬ì§? ?Ž˜?´ì§? ë²ˆí˜¸?˜ ê°œìˆ˜ ( 1,2,3,4,5,6,7,9,10)
	
	private int startPage;      // ?˜„?ž¬ ?™”ë©´ì—?„œ ë³´ì´?Š” startPage ë²ˆí˜¸
	private int endPage;        // ?˜„?ž¬ ?™”ë©´ì— ë³´ì´?Š” endPage ë²ˆí˜¸
	private boolean prev;       // ?Ž˜?´ì§? ?´? „ ë²„íŠ¼ ?™œ?„±?™” ?—¬ë¶?
	private boolean next;       // ?Ž˜?´ì§? ?‹¤?Œ ë²„íŠ¼ ?™œ?„œ?™” ?—¬ë¶?
	
	private Criteria cri;       // ?•ž?„œ ?ƒ?„±?•œ Criteriaë¥? ì£¼ìž…ë°›ëŠ”?‹¤.

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