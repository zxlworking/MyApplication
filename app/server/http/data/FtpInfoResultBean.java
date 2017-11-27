package com.dsv.appstore.http.data;

/**
 * Ftpinfo entity. 
 * @author 何喆
 */
@SuppressWarnings("serial")
public class FtpInfoResultBean implements java.io.Serializable {

	// Fields

	private Integer ftpid;
	private String ftpip;
	private Integer ftpport;
	private String ftpusername;
	private String ftppassword;
	private String ftptype;
	private String downloadpath;
	public Integer getFtpid() {
		return ftpid;
	}
	public void setFtpid(Integer ftpid) {
		this.ftpid = ftpid;
	}
	public String getFtpip() {
		return ftpip;
	}
	public void setFtpip(String ftpip) {
		this.ftpip = ftpip;
	}
	public Integer getFtpport() {
		return ftpport;
	}
	public void setFtpport(Integer ftpport) {
		this.ftpport = ftpport;
	}
	public String getFtpusername() {
		return ftpusername;
	}
	public void setFtpusername(String ftpusername) {
		this.ftpusername = ftpusername;
	}
	public String getFtppassword() {
		return ftppassword;
	}
	public void setFtppassword(String ftppassword) {
		this.ftppassword = ftppassword;
	}
	public String getFtptype() {
		return ftptype;
	}
	public void setFtptype(String ftptype) {
		this.ftptype = ftptype;
	}
	public String getDownloadpath() {
		return downloadpath;
	}
	public void setDownloadpath(String downloadpath) {
		this.downloadpath = downloadpath;
	}
	public FtpInfoResultBean() {
		super();
	}
	public FtpInfoResultBean(Integer ftpid, String ftpip, Integer ftpport,
			String ftpusername, String ftppassword, String ftptype,
			String downloadpath) {
		super();
		this.ftpid = ftpid;
		this.ftpip = ftpip;
		this.ftpport = ftpport;
		this.ftpusername = ftpusername;
		this.ftppassword = ftppassword;
		this.ftptype = ftptype;
		this.downloadpath = downloadpath;
	}

	@Override
	public String toString() {
		return "FtpInfoResultBean{" +
				"ftpid=" + ftpid +
				", ftpip='" + ftpip + '\'' +
				", ftpport=" + ftpport +
				", ftpusername='" + ftpusername + '\'' +
				", ftppassword='" + ftppassword + '\'' +
				", ftptype='" + ftptype + '\'' +
				", downloadpath='" + downloadpath + '\'' +
				'}';
	}
}