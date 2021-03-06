package com.emp.bpms.util.paging;

public class PageMaker {

	private int totalCount;     // κ²μ? ? μ²? ?°?΄?° κ°μ
	private int displayPageNum = 10;   // κ²μ? ?λ©΄μ? ?λ²μ λ³΄μ¬μ§? ??΄μ§? λ²νΈ? κ°μ ( 1,2,3,4,5,6,7,9,10)
	
	private int startPage;      // ??¬ ?λ©΄μ? λ³΄μ΄? startPage λ²νΈ
	private int endPage;        // ??¬ ?λ©΄μ λ³΄μ΄? endPage λ²νΈ
	private boolean prev;       // ??΄μ§? ?΄?  λ²νΌ ??±? ?¬λΆ?
	private boolean next;       // ??΄μ§? ?€? λ²νΌ ??? ?¬λΆ?
	
	private Criteria cri;       // ?? ??±? Criteriaλ₯? μ£Όμλ°λ?€.

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