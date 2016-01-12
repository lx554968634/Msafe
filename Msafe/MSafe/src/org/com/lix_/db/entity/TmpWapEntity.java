package org.com.lix_.db.entity;

import org.com.lix_.db.engine.Table;
import org.com.lix_.db.engine.Table.Column;

@Table(name = "t_tmp_wap")
public class TmpWapEntity {

	@Column(name = "nid", type = Column.TYPE_BOOLEAN, isPrimaryKey = true)
	private Integer nid;
	@Column(name = "status", type = Column.TYPE_INTEGER)
	private Integer status;
	@Column(name = "timesmt", type = Column.TYPE_DOUBLE)
	private Long timesmt;

	public Integer getNid() {
		return nid;
	}

	public void setNid(Integer nid) {
		this.nid = nid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getTimesmt() {
		return timesmt;
	}

	public void setTimesmt(Long timesmt) {
		this.timesmt = timesmt;
	}

}
