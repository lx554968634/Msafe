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
	@Column(name = "spckname", type = Column.TYPE_STRING)
	private String spckname;
	@Column(name = "des", type = Column.TYPE_STRING)
	private String des;
	@Column(name = "type", type = Column.TYPE_INTEGER)
	private int type;
	@Column(name = "timeSmt", type = Column.TYPE_STRING)
	private String timeSmt;

	public static String[] getColumns() {
		return new String[] { "md5", "spckname", "des", "type", "timeSmt" };
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getSpckname() {
		return spckname;
	}

	public void setSpckname(String spckname) {
		this.spckname = spckname;
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
