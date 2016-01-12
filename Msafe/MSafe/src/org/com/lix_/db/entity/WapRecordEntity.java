package org.com.lix_.db.entity;

import org.com.lix_.db.engine.Table;
import org.com.lix_.db.engine.Table.Column;

@Table(name = "t_wap_record_table")
public class WapRecordEntity {
	@Column(name = "nid", type = Column.TYPE_BOOLEAN, isPrimaryKey = true)
	private Integer nid;

	@Column(name = "uid", type = Column.TYPE_INTEGER)
	private Integer uid;

	@Column(name = "sPckname", type = Column.TYPE_STRING)
	private String sPckname;

	@Column(name = "nwapdata", type = Column.TYPE_DOUBLE)
	private Integer nwapdata;

	@Column(name = "status", type = Column.TYPE_INTEGER)
	private Integer status;

	public Integer getNid() {
		return nid;
	}

	public void setNid(Integer nid) {
		this.nid = nid;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
