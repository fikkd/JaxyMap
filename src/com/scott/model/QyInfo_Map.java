package com.scott.model;

/**
 * ���õ�ͼ���һ�㼶����
 *
 * @since  2017��11��1��
 * @author �����
 *
 */
public class QyInfo_Map {

	// ����
	private String id;
	// ��ҵ����
	private String qymc;
	// ����
	private String m_lng;
	// γ��
	private String m_lat;
	// ʧ������ֵ
	private String m_count;

	public QyInfo_Map() {
	}

	public QyInfo_Map(String id, String qymc, String m_lng, String m_lat, String m_count) {
		super();
		this.id = id;
		this.qymc = qymc;
		this.m_lng = m_lng;
		this.m_lat = m_lat;
		this.m_count = m_count;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQymc() {
		return qymc;
	}

	public void setQymc(String qymc) {
		this.qymc = qymc;
	}

	public String getM_lng() {
		return m_lng;
	}

	public void setM_lng(String m_lng) {
		this.m_lng = m_lng;
	}

	public String getM_lat() {
		return m_lat;
	}

	public void setM_lat(String m_lat) {
		this.m_lat = m_lat;
	}

	public String getM_count() {
		return m_count;
	}

	public void setM_count(String m_count) {
		this.m_count = m_count;
	}

}
