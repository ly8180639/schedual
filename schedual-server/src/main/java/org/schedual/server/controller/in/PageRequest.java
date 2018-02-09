package org.schedual.server.controller.in;

public class PageRequest {
	/**
	 * 每页多少条记录，默认20条
	 */
	private int pagePerCount=20;
	
	/**
	 * 当前第几页
	 */
	private int currPage=1;
	
	/**
	 * 排序字段
	 */
	private String sortKey;
	
	/**
	 * 字段方向
	 */
	private String sortDirect;
	
	
	
	/**
	 * 获取查询的起点
	 * @return
	 */
	public int getStartIndex() {
		if(currPage<1)currPage=1;
		return (currPage-1)*pagePerCount;
	}


	public int getPagePerCount() {
		return pagePerCount;
	}

	public void setPagePerCount(int pagePerCount) {
		this.pagePerCount = pagePerCount;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}


	public String getSortKey() {
		return sortKey;
	}


	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}


	public String getSortDirect() {
		return sortDirect;
	}


	public void setSortDirect(String sortDirect) {
		this.sortDirect = sortDirect;
	}
	
	public enum SqlDirection {
		DESC("DESC"),ASC("ASC");
		private String direction;

		private SqlDirection(String direction) {
			this.direction = direction;
		}

		public String getDirection() {
			return direction;
		}

		public void setDirection(String direction) {
			this.direction = direction;
		}
	}
}
