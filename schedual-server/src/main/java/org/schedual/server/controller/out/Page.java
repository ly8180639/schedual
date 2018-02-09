package org.schedual.server.controller.out;

import java.util.List;

import org.schedual.server.controller.in.PageRequest;

public class Page<T> {
	
	
	//总共多少条记录
	private int totalCount;
	
	//当前第几页
	private int currPage;
	
	//每页多少条
	private int pagePerCount;
	
	//一共多少页
	private int pageCount;
	
	//当前页的记录
	private List<T> content;
	
	public Page(int allCount,PageRequest pageReq) {
		// TODO Auto-generated constructor stub
		this.totalCount=allCount;
		this.currPage=pageReq.getCurrPage();
		this.pagePerCount=pageReq.getPagePerCount();
		this.pageCount=(this.totalCount%pagePerCount==0)?totalCount/pagePerCount:(totalCount/pagePerCount+1);
	}
	
	public Page(int allCount,PageRequest pageReq,List<T> content) {
		this.totalCount=allCount;
		this.currPage=pageReq.getCurrPage();
		this.pagePerCount=pageReq.getPagePerCount();
		this.pageCount=(this.totalCount%pagePerCount==0)?totalCount/pagePerCount:(totalCount/pagePerCount+1);
		this.content=content;
	}
	
	
	
	

	public Page() {
		super();
	}

	public int getPageCount() {
		return pageCount;
	}



	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}



	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPagePerCount() {
		return pagePerCount;
	}

	public void setPagePerCount(int pagePerCount) {
		this.pagePerCount = pagePerCount;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}
	
	
	
	

}
