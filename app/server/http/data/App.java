package com.dsv.appstore.http.data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * App entity.
 * @author 何喆
 */

@SuppressWarnings("serial")
public class App implements Serializable {

	// Fields
	private Integer appid;
	private Appversion appversion;
	private String appname;
	private String apptype;
	private String appicon;
	private String appdeveloper;
	private String appdesc;
	private Integer appdownnum;
	private String star;
	private String appimg;
	private String status;
	private String packagename;
	private String addtime;
	
	private String ptcode;
	private String pinyinszm;
	private Float orderby;

	public Integer getAppid() {
		return appid;
	}

	public void setAppid(Integer appid) {
		this.appid = appid;
	}

	public Appversion getAppversion() {
		return appversion;
	}

	public void setAppversion(Appversion appversion) {
		this.appversion = appversion;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getApptype() {
		return apptype;
	}

	public void setApptype(String apptype) {
		this.apptype = apptype;
	}

	public String getAppicon() {
		return appicon;
	}

	public void setAppicon(String appicon) {
		this.appicon = appicon;
	}

	public String getAppdeveloper() {
		return appdeveloper;
	}

	public void setAppdeveloper(String appdeveloper) {
		this.appdeveloper = appdeveloper;
	}

	public String getAppdesc() {
		return appdesc;
	}

	public void setAppdesc(String appdesc) {
		this.appdesc = appdesc;
	}

	public Integer getAppdownnum() {
		return appdownnum;
	}

	public void setAppdownnum(Integer appdownnum) {
		this.appdownnum = appdownnum;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public String getAppimg() {
		return appimg;
	}

	public void setAppimg(String appimg) {
		this.appimg = appimg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getPtcode() {
		return ptcode;
	}

	public void setPtcode(String ptcode) {
		this.ptcode = ptcode;
	}

	public String getPinyinszm() {
		return pinyinszm;
	}

	public void setPinyinszm(String pinyinszm) {
		this.pinyinszm = pinyinszm;
	}

	public Float getOrderby() {
		return orderby;
	}

	public void setOrderby(Float orderby) {
		this.orderby = orderby;
	}

	@Override
	public String toString() {
		return "App{" +
				"appid=" + appid +
				", appversion=" + appversion +
				", appname='" + appname + '\'' +
				", apptype='" + apptype + '\'' +
				", appicon='" + appicon + '\'' +
				", appdeveloper='" + appdeveloper + '\'' +
				", appdesc='" + appdesc + '\'' +
				", appdownnum=" + appdownnum +
				", star='" + star + '\'' +
				", appimg='" + appimg + '\'' +
				", status='" + status + '\'' +
				", packagename='" + packagename + '\'' +
				", addtime=" + addtime +
				", ptcode='" + ptcode + '\'' +
				", pinyinszm='" + pinyinszm + '\'' +
				", orderby=" + orderby +
				'}';
	}
}