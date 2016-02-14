package org.com.lix_.plugin;

public class Rect {
	public float m_nX;
	public float m_nY;
	public int m_nWidth;
	public int m_nHeight;

	public Rect(float nX, float nY, int width, int height) {
		this.m_nHeight = height;
		this.m_nWidth = width;
		this.m_nX = nX;
		m_nY = nY;
	}

	@Override
	public String toString() {
		return m_nX + ":" + m_nY + ":" + m_nWidth + ":" + m_nHeight;
	}

}