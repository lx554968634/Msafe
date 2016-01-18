package org.com.lix_.db.entity;

import org.com.lix_.db.engine.Table;
import org.com.lix_.db.engine.Table.Column;

@Table(name = "t_wap_record_table")
public class WapRecordEntity {

	public String getSappname() {
		return sappname;
	}

	public void setSappname(String sappname) {
		this.sappname = sappname;
	}

	@Column(name = "uid", type = Column.TYPE_INTEGER)
	private Integer uid;

	@Column(name = "sPckname", type = Column.TYPE_STRING)
	private String sPckname;
	@Column(name = "sappname", type = Column.TYPE_STRING)
	private String sappname;

	@Column(name = "nwapdata", type = Column.TYPE_DOUBLE)
	private Integer nwapdata;

	@Column(name = "status", type = Column.TYPE_INTEGER)
	private Integer status;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getsPckname() {
		return sPckname;
	}

	public void setsPckname(String sPckname) {
		this.sPckname = sPckname;
	}

	public Integer getNwapdata() {
		return nwapdata;
	}

	public void setNwapdata(Integer nwapdata) {
		this.nwapdata = nwapdata;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public static String[] getAllComlumn() {
		// TODO Auto-generated method stub
		return new String[] { "uid", "sPckname", "nwapdata", "status" };
	}
}
