package org.com.lix_.enable.engine;

import java.io.File;
import java.io.Serializable;

import org.com.lix_.util.UiUtils;

public class FileInfo implements Serializable {
	private static final long serialVersionUID = 4328255902034485127L;
	public String m_sFileName;
	public String m_sAbFilePath;
	public long m_nFileSize = -1;
	public int m_nFileType = -1;

	public String getSize() {
		if (m_nFileSize == -1) {
			File pFile = new File(m_sAbFilePath);
			if (pFile.exists() && pFile.isDirectory()) {
				m_nFileSize = UiUtils.getFileSize(pFile);
			}
		}
		return UiUtils.getCacheSize(m_nFileSize);
	}
}
