package com.scott.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.scott.model.QyInfo;
import com.scott.model.QyInfo_Map;
import com.scott.model.QyInfo_Map_Level;



public class FiDAO extends NDAO {

	
	
	public void testFun() {
		Session session = null;
		Transaction ts = null;
		try {
			session = this.getSessionFactory().openSession();
			ts = session.beginTransaction();
			List<?> list = session.createSQLQuery("select * from ANIMAL t ").list();
			System.out.println(list.size());
			ts.commit();
		} catch (Exception e) {
			ts.rollback();
		} finally {
			session.close();
		}
		
	}
	
	
	public int findCountOfENTER_BASIC_INFO() {
		List<Object> params = new ArrayList<>();
		return this.findCountBySQLQuery("select count(*) from ENTER_BASIC_INFO", params.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<QyInfo> findQyInfoByPage(int i) {
		List<QyInfo> list = new ArrayList<>();
		QyInfo qyinfo;
		List<Object[]> lt = (List<Object[]>) this.findByNamedQuery("findPageENTER_BASIC_INFO", new Object[] { (i + 1) * 10, i * 10 });
		if (lt == null)
			return null;
		for (Object[] object : lt) {
			try {
				qyinfo = new QyInfo();
				qyinfo.setId(object[0].toString());
				qyinfo.setQymc(object[1].toString());
				list.add(qyinfo);
			} catch (Exception e) {

			}
		}
		return list;
	}
	
	
	/**
	 * ���浽��ͼר�õ���ҵ��Ϣ����
	 *
	 *
	 * @�����¼ 2017��10��31�� ����8:36:05 ����� ����
	 *
	 */
	public void saveQyInfoMap(List<QyInfo> list) {
		Session session = null;
		Transaction ts;
		try {
			session = this.getSessionFactory().openSession();
			ts = session.beginTransaction();
			/**
			 *  ����д��<tt>insert all</tt>��<tt>Oracle</tt>�п���������������
			 *
			 */
			StringBuffer sql = new StringBuffer("insert all ");

			for (QyInfo data : list) {
				sql.append(" into qyinfo_map(id, qymc) values('" + data.getId() + "','" + data.getQymc() + "')");
			}
			sql.append(" select 1 from dual");

			session.createSQLQuery(sql.toString()).executeUpdate();
			ts.commit();
		} catch (Exception e) {
			/**
			 * A�����ݿ�����B����
			 * ���㸴�ƹ��̳����쳣Ҳ����Ҫ�ع�
			 */
			//ts.rollback();
		} finally {
			session.close();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<QyInfo_Map> findQyInfoMapByPage(int i) {
		return this.findPageByHql("from QyInfo_Map where m_lng is null", new Object[] {}, i * 10, 10);
	}
	
	/** ��󾭶� */
	@SuppressWarnings("unchecked")
	public Double getMaxLng(String value) {
		List<String> list;
		if (value.equals("s"))
			list = (List<String>) this.findByHql("select to_char(max(to_number(m_lng))) from QyInfo_Map");
		else 
			list = (List<String>) this.findByHql("select to_char(max(to_number(m_lng))) from QyInfo_Map_Level where m_zoom_level_" + value + "='1'");
		return Double.valueOf(list.get(0));
		
	}

	/** ��С���� */
	@SuppressWarnings("unchecked")
	public Double getMinLng(String value) {
		List<String> list;
		if (value.equals("s"))
			list = (List<String>) this.findByHql("select to_char(min(to_number(m_lng))) from QyInfo_Map");
		else 
			list = (List<String>) this.findByHql("select to_char(min(to_number(m_lng))) from QyInfo_Map_Level where m_zoom_level_" + value + "='1'");
		return Double.valueOf(list.get(0));
	}

	/** ���γ�� */
	@SuppressWarnings("unchecked")
	public Double getMaxLat(String value) {
		List<String> list;
		if (value.equals("s"))
			list = (List<String>) this.findByHql("select to_char(max(to_number(m_lat))) from QyInfo_Map");
		else 
			list = (List<String>) this.findByHql("select to_char(max(to_number(m_lat))) from QyInfo_Map_Level where m_zoom_level_" + value + "='1'");
		return Double.valueOf(list.get(0));
	}

	/** ��Сγ�� */
	@SuppressWarnings("unchecked")
	public Double getMinLat(String value) {
		List<String> list;
		if (value.equals("s"))
			list = (List<String>) this.findByHql("select to_char(min(to_number(m_lat))) from QyInfo_Map");
		else 
			list = (List<String>) this.findByHql("select to_char(min(to_number(m_lat))) from QyInfo_Map_Level where m_zoom_level_" + value + "='1'");
		return Double.valueOf(list.get(0));
	}
	
	
	@SuppressWarnings("unchecked")
	public List<QyInfo_Map> findListCell(Double LNG_F, Double LNG_B, Double LAT_F, Double LAT_B) {
		
		Session session = null;
		List<QyInfo_Map> list = null;
		try {
			session = this.getSessionFactory().openSession();
			list = session.createSQLQuery("select * from QyInfo_Map map where " + LNG_F + " <= to_number(map.m_lng) and to_number(map.m_lng) < "+LNG_B+" and "+LAT_F+" <= to_number(map.m_lat) and to_number(map.m_lat) < "+LAT_B).addEntity(QyInfo_Map.class).list();
//			list = session.createSQLQuery("select * from QyInfo_Map map where 114.960436 <= to_number(map.m_lng) and to_number(map.m_lng) < 114.960438 and 27.160925 <= to_number(map.m_lat) and to_number(map.m_lat) < 27.160926").addEntity(QyInfo_Map.class).list();
		} catch (Throwable e) {
		} finally {
			session.close();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<QyInfo_Map_Level> findListCell(Double LNG_F, Double LNG_B, Double LAT_F, Double LAT_B, String value) {
		Session session = null;
		List<QyInfo_Map_Level> list = null;
		try {
			session = this.getSessionFactory().openSession();
			list = session.createSQLQuery("select * from QyInfo_Map_Level map where " + LNG_F + " <= to_number(map.m_lng) and to_number(map.m_lng) < "+LNG_B+" and "+LAT_F+" <= to_number(map.m_lat) and to_number(map.m_lat) < "+LAT_B + " and map.m_zoom_level_" + value + "='1' ").addEntity(QyInfo_Map_Level.class).list();
		} catch (Throwable e) {
		} finally {
			session.close();
		}
		return list;
	}
	
	
	/**
	 * �������õ�ͼ�㼶����
	 * 
	 * @param m_lng ����
	 * @param m_lat γ��
	 * @param m_count_qy ��ҵ����
	 * @param m_range ��Χ
	 * @param value
	 *
	 * @�����¼ 2017��11��1�� ����1:51:42 ����� ����
	 *
	 */
	public void saveMap(String m_lng, String m_lat, int m_count_qy, String m_range, String value) {
		Session session = null;
		Transaction ts = null;
		try {
			session = this.getSessionFactory().openSession();
			ts = session.beginTransaction();
			session.createSQLQuery("insert into qyinfo_map_level(id,m_lng,m_lat,m_count_qy,m_range,m_zoom_level_" + value + ") values( " + "sys_guid(), '" + m_lng + "', '" + m_lat + "', '" + m_count_qy + "','" + m_range + "', '1')").executeUpdate();
			ts.commit();
		} catch (Exception e) {
			ts.rollback();
		} finally {
			session.close();
		}
	}
	
	
	
}
