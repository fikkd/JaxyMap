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
	 * 保存到地图专用的企业信息表中
	 *
	 *
	 * @变更记录 2017年10月31日 下午8:36:05 李瑞辉 创建
	 *
	 */
	public void saveQyInfoMap(List<QyInfo> list) {
		Session session = null;
		Transaction ts;
		try {
			session = this.getSessionFactory().openSession();
			ts = session.beginTransaction();
			/**
			 *  此种写法<tt>insert all</tt>在<tt>Oracle</tt>中可以批量插入数据
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
			 * A表数据拷贝到B表中
			 * 即便复制过程出现异常也不需要回滚
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
	
	/** 最大经度 */
	@SuppressWarnings("unchecked")
	public Double getMaxLng(String value) {
		List<String> list;
		if (value.equals("s"))
			list = (List<String>) this.findByHql("select to_char(max(to_number(m_lng))) from QyInfo_Map");
		else 
			list = (List<String>) this.findByHql("select to_char(max(to_number(m_lng))) from QyInfo_Map_Level where m_zoom_level_" + value + "='1'");
		return Double.valueOf(list.get(0));
		
	}

	/** 最小经度 */
	@SuppressWarnings("unchecked")
	public Double getMinLng(String value) {
		List<String> list;
		if (value.equals("s"))
			list = (List<String>) this.findByHql("select to_char(min(to_number(m_lng))) from QyInfo_Map");
		else 
			list = (List<String>) this.findByHql("select to_char(min(to_number(m_lng))) from QyInfo_Map_Level where m_zoom_level_" + value + "='1'");
		return Double.valueOf(list.get(0));
	}

	/** 最大纬度 */
	@SuppressWarnings("unchecked")
	public Double getMaxLat(String value) {
		List<String> list;
		if (value.equals("s"))
			list = (List<String>) this.findByHql("select to_char(max(to_number(m_lat))) from QyInfo_Map");
		else 
			list = (List<String>) this.findByHql("select to_char(max(to_number(m_lat))) from QyInfo_Map_Level where m_zoom_level_" + value + "='1'");
		return Double.valueOf(list.get(0));
	}

	/** 最小纬度 */
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
	 * 新增信用地图层级数据
	 * 
	 * @param m_lng 经度
	 * @param m_lat 纬度
	 * @param m_count_qy 企业数量
	 * @param m_range 范围
	 * @param value
	 *
	 * @变更记录 2017年11月1日 下午1:51:42 李瑞辉 创建
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
