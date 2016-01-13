package org.com.lix_.db.entity;

import org.com.lix_.db.engine.Table;
import org.com.lix_.db.engine.Table.Column;

@Table(name = "t_tmp_status_wap")
public class TmpStatusEntity {

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public static final int WIFI_UNABLE = 0;
	public static final int WIFI_ENABLE = 1;

	public static final int GPRS_UNABLE = 2;
	public static final int GPRS_ENABLE = 3;
	@Column(name = "type", type = Column.TYPE_INTEGER)
	private Integer type;
	// 0 1 wifi ,2,3gprs
	@Column(name = "status", type = Column.TYPE_INTEGER)
	private Integer status;
	@Column(name = "timesmt", type = Column.TYPE_DOUBLE)
	private Long timesmt;

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

	public static String[] getAllComlumn() {
		// TODO Auto-generated method stub
		return new String[] { "status", "timesmt" ,"type"};
	}

}
