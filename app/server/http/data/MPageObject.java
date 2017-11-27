package com.dsv.appstore.http.data;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class MPageObject<T> implements java.io.Serializable{ 
	
	private int pagesize;    // 每页显示多少条
	private int currentpage; // 当前页
	private int rowcount;    // 数据总数; 
	@SuppressWarnings("unused")
	private int pagecount;   // 总页数
	private List<T> result = new ArrayList<T>();//结果集

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public int getCurrentpage() {
		return currentpage;
	}

	public void setCurrentpage(int currentpage) {
		this.currentpage = currentpage;
	}

	public int getRowcount() {
		return rowcount;
	}

	public void setRowcount(int rowcount) {
		this.rowcount = rowcount;
	}

	public int getPagecount() {
		return pagecount;
	}

	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "MPageObject{" +
				"pagesize=" + pagesize +
				", currentpage=" + currentpage +
				", rowcount=" + rowcount +
				", pagecount=" + pagecount +
				", result=" + result +
				'}';
	}
}
