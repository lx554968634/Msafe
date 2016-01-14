package org.com.lix_.db.entity;

import org.com.lix_.db.engine.Table;
import org.com.lix_.db.engine.Table.Column;
import org.com.lix_.util.Debug;

@Table(name = "t_use_log_wap")
public class WapuselogEntity {
	
	private String TAG = "WapuselogEntity" ;
	@Column(name = "uid", type = Column.TYPE_INTEGER)
	private Integer uid;
	
	@Column(name = "sPckname", type = Column.TYPE_STRING)
	private String sPckname;
	
	@Column(name = "status", type = Column.TYPE_INTEGER)
	private Integer status;
	
	@Column(name = "starttimesmt", type = Column.TYPE_DOUBLE)
	private Long starttimesmt;
	
	@Column(name = "endtimesmt", type = Column.TYPE_DOUBLE)
	private Long endtimesmt;
	
	@Column(name = "startwapdata", type = Column.TYPE_DOUBLE)
	private Long startwapdata;
	
	@Column(name = "endwapdata", type = Column.TYPE_DOUBLE)
	private Long endwapdata;
	
	
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getStarttimesmt() {
		return starttimesmt;
	}
	public void setStarttimesmt(Long starttimesmt) {
		this.starttimesmt = starttimesmt;
	}
	public Long getEndtimesmt() {
		return endtimesmt;
	}
	public void setEndtimesmt(Long endtimesmt) {
		this.endtimesmt = endtimesmt;
	}
	public Long getStartwapdata() {
		return startwapdata;
	}
	public void setStartwapdata(Long startwapdata) {
		this.startwapdata = startwapdata;
	}
	public Long getEndwapdata() {
		return endwapdata;
	}
	public void setEndwapdata(Long endwapdata) {
		this.endwapdata = endwapdata;
	}
	
	

}
