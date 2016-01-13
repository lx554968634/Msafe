package org.com.lix_.db.entity;

import org.com.lix_.db.engine.Table;
import org.com.lix_.db.engine.Table.Column;

@Table(name = "t_tmp_record_wap")
public class TmpRecordWapEntity {

	@Column(name = "uid", type = Column.TYPE_INTEGER)
	private Integer uid;

	@Column(name = "sPckname", type = Column.TYPE_STRING)
	private String sPckname;

	@Column(name = "nwapdata", type = Column.TYPE_DOUBLE)
	private Integer nwapdata;

	@Column(name = "timesmt", type = Column.TYPE_DOUBLE)
	private Long timesmt;
	@Column(name = "status", type = Column.TYPE_INTEGER)
	private Integer status;

	public TmpRecordWapEntity() {
	}

	public TmpRecordWapEntity(int uid2, String packageName) {
		setUid(uid2);
		setsPckname(packageName);
	}

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

	public Long getTimesmt() {
		return timesmt;
	}

	public void setTimesmt(Long timesmt) {
		this.timesmt = timesmt;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public static String[] getAllComlumn() {
		// TODO Auto-generated method stub
		return new String[] { "uid", "sPckname", "nwapdata", "timesmt",
				"status" };
	}

}
