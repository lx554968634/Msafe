package org.com.lix_.db.entity;

import org.com.lix_.db.engine.Table;
import org.com.lix_.db.engine.Table.Column;

@Table(name = "virvus_datainfo")
public class VirvusEntity {

	public String getTimeSmt() {
		return timeSmt;
	}

	public void setTimeSmt(String timeSmt) {
		this.timeSmt = timeSmt;
	}

	@Column(name = "md5", type = Column.TYPE_STRING)
	private String md5;
	@Column(name = "name", type = Column.TYPE_STRING)
	private String name;
	@Column(name = "des", type = Column.TYPE_STRING)
	private String des;
	@Column(name = "type", type = Column.TYPE_INTEGER)
	private int type;
	@Column(name = "timeSmt", type = Column.TYPE_STRING)
	private String timeSmt;

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
