package com.dsv.appstore.http.data;

import java.sql.Timestamp;

/**
 * Appversion entity. 
 * @author 何喆
 */
@SuppressWarnings("serial")
public class Appversion implements java.io.Serializable {

	// Fields

	private Integer vid;
	private Integer appid;
	private String vsize;
	private String vname;
	private Integer vcode;
	private String downloadpath;
	private String status;
	private String updatelog;
	private String updatedate;
	private Integer ftpid;
	private String appflag;

	public Integer getVid() {
		return vid;
	}

	public void setVid(Integer vid) {
		this.vid = vid;
	}

	public Integer getAppid() {
		return appid;
	}

	public void setAppid(Integer appid) {
		this.appid = appid;
	}

	public String getVsize() {
		return vsize;
	}

	public void setVsize(String vsize) {
		this.vsize = vsize;
	}

	public String getVname() {
		return vname;
	}

	public void setVname(String vname) {
		this.vname = vname;
	}

	public Integer getVcode() {
		return vcode;
	}

	public void setVcode(Integer vcode) {
		this.vcode = vcode;
	}

	public String getDownloadpath() {
		return downloadpath;
	}

	public void setDownloadpath(String downloadpath) {
		this.downloadpath = downloadpath;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdatelog() {
		return updatelog;
	}

	public void setUpdatelog(String updatelog) {
		this.updatelog = updatelog;
	}

	public String getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(String updatedate) {
		this.updatedate = updatedate;
	}

	public Integer getFtpid() {
		return ftpid;
	}

	public void setFtpid(Integer ftpid) {
		this.ftpid = ftpid;
	}

	public String getAppflag() {
		return appflag;
	}

	public void setAppflag(String appflag) {
		this.appflag = appflag;
	}

	@Override
	public String toString() {
		return "Appversion{" +
				"vid=" + vid +
				", appid=" + appid +
				", vsize='" + vsize + '\'' +
				", vname='" + vname + '\'' +
				", vcode=" + vcode +
				", downloadpath='" + downloadpath + '\'' +
				", status='" + status + '\'' +
				", updatelog='" + updatelog + '\'' +
				", updatedate=" + updatedate +
				", ftpid=" + ftpid +
				", appflag='" + appflag + '\'' +
				'}';
	}
}