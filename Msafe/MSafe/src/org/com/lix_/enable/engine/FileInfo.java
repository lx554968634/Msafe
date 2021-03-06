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
	public String m_sType = "未知类型文件";
	public FileInfo() {
	}
	public FileInfo(String absolutePath,String sType) {
		m_sAbFilePath = absolutePath ;
		m_sType = sType ;
	}
	public FileInfo(String absolutePath) {
		m_sAbFilePath = absolutePath ;
	}
	
	public long getSizeLong()
	{
		if (m_nFileSize == -1) {
			File pFile = new File(m_sAbFilePath);
			if (pFile.exists()) {
				m_nFileSize = UiUtils.getFileSize(pFile);
			}
		}
		return m_nFileSize;
	}

	public String getSize() {
		if (m_nFileSize == -1) {
			File pFile = new File(m_sAbFilePath);
			if (pFile.exists()) {
				m_nFileSize = UiUtils.getFileSize(pFile);
			}
		}
		return UiUtils.getCacheSize(m_nFileSize);
	}

	public boolean exists() {
		if (m_sAbFilePath == null || m_sAbFilePath.equals(""))
			return false;
		else {
			File pTmp = new File(m_sAbFilePath);
			boolean bTmp = pTmp.exists();
			pTmp = null;
			return bTmp;
		}
	}

	public File getFile() {
		return new File(m_sAbFilePath);
	}

	public static FileInfo init(File pFileTmp) {
		FileInfo pFile = new FileInfo() ;
		pFile.m_nFileSize = UiUtils.getFileSize(pFileTmp) ;
		pFile.m_sAbFilePath = pFileTmp.getAbsolutePath() ;
		pFile.m_sFileName = pFileTmp.getName() ;
		return pFile;
	}
	public String m_sModifyTime ;
	public void initModifyTime() {
		m_sModifyTime = "2015/10/31 17:37" ;
	}
}
