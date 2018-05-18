package com.scott.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.scott.model.QyInfo_Map;
import com.scott.model.QyInfo_Map_Level;



public class FiDAO extends NDAO {


	public void deleteMap() {
		Session session = null;
		Transaction ts = null;
		try {
		   session = this.getSessionFactory().openSession();
		   ts = session.beginTransaction();
		   session.createSQLQuery("delete from QYINFO_MAP").executeUpdate();
		   session.createSQLQuery("delete from QYINFO_MAP_LEVEL").executeUpdate();
		   ts.commit();
		} catch (Exception ignore) {
		} finally {
		   session.close();
		}		
	}
	
	/**
	 * 
	 * 删除QYINFO_MAP_LEVEL表所有数据
	 * 为了重新构造各个层级数据做准备
	 * 
	 *
	 * @变更记录 2018年1月10日 上午9:53:03 李瑞辉 创建
	 *
	 */
	public void deleteMapLevel() {
		Session session = null;
		Transaction ts = null;
		try {
		   session = this.getSessionFactory().openSession();
		   ts = session.beginTransaction();
		   session.createSQLQuery("delete from QYINFO_MAP_LEVEL").executeUpdate();
		   ts.commit();
		} catch (Exception ignore) {
		} finally {
		   session.close();
		}		
	}
	
	@SuppressWarnings("unchecked")
	public List<QyInfo_Map> findListCell(Double LNG_F, Double LNG_B, Double LAT_F, Double LAT_B) {
		
		Session session = null;
		List<QyInfo_Map> list = null;
		try {
			session = this.getSessionFactory().openSession();
			list = session.createSQLQuery("select * from QyInfo_Map map where " + LNG_F + " <= to_number(map.m_lng) and to_number(map.m_lng) < "+LNG_B+" and "+LAT_F+" <= to_number(map.m_lat) and to_number(map.m_lat) < "+LAT_B).addEntity(QyInfo_Map.class).list();
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
	 * 
	 * 分页查询源企业表数据
	 * 
	 * @param i 页数
	 * @param tName 表名
	 * @param tColumns 字段名
	 * @return
	 *
	 * @变更记录 2018年1月8日 下午11:48:32 李瑞辉 创建
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Object[]> findQyInfoByPage(int i, String tName, String tColumns) {

		String mColumns = geneColumns(tColumns, "m");
		String kColumns = geneColumns(tColumns, "k");
		
		final String sql = "select " + mColumns + " from (select ROWNUM as rowno, u.* from ( select " + kColumns + " from " + tName + " k ) u where rownum <= " + (i + 1) * 10 + ") m where rowno > " + (i * 10);
		
		return getHibernateTemplate().execute(new HibernateCallback() {
		     public Object doInHibernate(Session session) throws HibernateException {
		          Query query = session.createSQLQuery(sql);
		          return query.list();
		     }
		});
	}
	
	
	/**
	 * 
	 * 分页查询信用地图专用企业信息表数据
	 * 
	 * @param i
	 * @return
	 *
	 * @变更记录 2018年1月9日 上午12:26:30 李瑞辉 创建
	 *
	 */
	@SuppressWarnings("unchecked")
	public List<QyInfo_Map> findQyInfoMapByPage(int i) {
		
		return this.findPageByHql("from QyInfo_Map ", new Object[] {}, i * 10, 10);
	}
	
	/**
	 * 将形如column1,column2,column3转成t.column1,t.column2,t.column3 
	 *
	 */
	private String geneColumns(String columns, String dot) {
		String[] arr = columns.split(",");
		StringBuffer buf = new StringBuffer();
		int size = arr.length;
		for (int i = 0; i < size -1; i++) {			
			buf.append(dot + "." + arr[i] + ",");			
		}
		buf.append(dot + "." + arr[size - 1]);
		return buf.toString();
		
	}
	
	
	/*
	 * 禁止插入的字段值是null 
	 *
	 */
	private Object[] geneValues(Object[] columns) {
		if (columns[1] == null) columns[1] = "";
		if (columns[2] == null) columns[2] = "";
		if (columns[3] == null) columns[3] = "";		
		if (columns[4] == null) columns[4] = "";
		if (columns[5] == null) columns[5] = "";			
		
		return columns;
	}
	
	/** 最大纬度 */
	@SuppressWarnings("unchecked")
	public Double getMaxLat(String value) {
		List<String> list;
		if (value.equals("s")) {
			Session session = null;
			session = this.getSessionFactory().openSession();
			list = session.createSQLQuery("select to_char(max(to_number(m_lat))) from QyInfo_Map").list();		
		}
		else 
			list = (List<String>) this.findByHql("select to_char(max(to_number(m_lat))) from QyInfo_Map_Level where m_zoom_level_" + value + "='1'");
		return Double.valueOf(list.get(0));
	}
	
	/** 最大经度 */
	@SuppressWarnings("unchecked")
	public Double getMaxLng(String value) {
		List<String> list;
		if (value.equals("s")) {
			Session session = null;
			session = this.getSessionFactory().openSession();
			list = session.createSQLQuery("select to_char(max(to_number(m_lng))) from QyInfo_Map").list();		
		}		
		else 
			list = (List<String>) this.findByHql("select to_char(max(to_number(m_lng))) from QyInfo_Map_Level where m_zoom_level_" + value + "='1'");
		return Double.valueOf(list.get(0));
		
	}
	
	
	/** 最小纬度 */
	@SuppressWarnings("unchecked")
	public Double getMinLat(String value) {
		List<String> list;
		if (value.equals("s")) {
			Session session = null;
			session = this.getSessionFactory().openSession();
			list = session.createSQLQuery("select to_char(min(to_number(m_lat))) from QyInfo_Map").list();		
		}
		else 
			list = (List<String>) this.findByHql("select to_char(min(to_number(m_lat))) from QyInfo_Map_Level where m_zoom_level_" + value + "='1'");
		return Double.valueOf(list.get(0));
	}

	/** 最小经度 */
	@SuppressWarnings("unchecked")
	public Double getMinLng(String value) {
		List<String> list;
		if (value.equals("s")) {
			Session session = null;
			session = this.getSessionFactory().openSession();
			list = session.createSQLQuery("select to_char(min(to_number(m_lng))) from QyInfo_Map").list();
		}		
		else 
			list = (List<String>) this.findByHql("select to_char(min(to_number(m_lng))) from QyInfo_Map_Level where m_zoom_level_" + value + "='1'");
		return Double.valueOf(list.get(0));
	}

	/**
	 * 
	 * 查询源企业表总数量
	 * 
	 * @param tName 源企业表名
	 * @return
	 *
	 * @变更记录 2018年1月8日 下午11:47:23 李瑞辉 创建
	 *
	 */
	public int getPageCount(String tName) {
		List<Object> params = new ArrayList<>();
		return this.findCountBySQLQuery("select count(*) from " + tName, params.toArray());
	}

	/**
	 * 
	 * 查询信用地图专用表总数量
	 * 
	 * @return
	 *
	 * @变更记录 2018年1月9日 上午12:02:20 李瑞辉 创建
	 *
	 */
	public int getPageCountOfMap() {
		List<Object> params = new ArrayList<>();
		return this.findCountBySQLQuery("select count(*) from qyinfo_map ", params.toArray());
	}
	
	/**
	 * 
	 * 查询信用地图专用表经纬度为空的企业数量
	 */
	public int getLngisnull() {
		List<Object> params = new ArrayList<>();
		return this.findCountBySQLQuery("select count(*) from qyinfo_map where m_lng is null", params.toArray());
	}
	
	/**
	 * 查询10条信用地图专用表的数据( 排除经纬度为空的数据 )
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<QyInfo_Map> getListOfMapOn() {
		return this.findPageByHql("from QyInfo_Map m where (m.m_lng is null or m.m_lat is null) and m.m_on is null", new Object[] {}, 0, 10);
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
	
	/**
	 * 
	 * 保存到地图专用的企业信息表中
	 * 
	 * @param list 源数据
	 * @param table 目标表
	 * @param columns 目标字段
	 *
	 * @变更记录 2018年1月8日 下午11:49:26 LRH 创建
	 *
	 */
	public void saveQyInfoMap(List<Object[]> list, String table, String columns) {
		Session session = null;
		Transaction ts;
		try {
			session = this.getSessionFactory().openSession();
			ts = session.beginTransaction();
			
			/**
			 * 此次数据中是否存在相同的统一社会信用代码
			 * 相同则只保留一个即可 
			 */
			Set<String> set = new HashSet<String>();
			Iterator<Object[]> it = list.iterator();
			while (it.hasNext()) {
				Object[] o = it.next();
				if (o == null)  it.remove();
				if (o[0] == null || o[0].equals(""))  it.remove(); // 主键
				if (o[2] == null || o[0].equals(""))  it.remove(); // 统一社会信用代码
				if (!set.add(o[2].toString())) it.remove();  // 如果存入Set集合失败说明重复
			}
			
			/**
			 *  此种写法<tt>insert all</tt>在<tt>Oracle</tt>中可以批量插入数据
			 *
			 */
			StringBuffer sql = new StringBuffer("insert all ");

			for (Object[] data : list) {				
				/**
				 * 判断数据库中是否存在相同的统一社会信用代码的数据
				 */
				String C_SOCIAL_CREDIT_CODE = data[2].toString();
				int exist = this.findCountBySQLQuery("select count(*) from "+ table +" t where t.C_SOCIAL_CREDIT_CODE=?", new Object[] {C_SOCIAL_CREDIT_CODE});
				if (exist > 0) {
					continue;
				}
				
				data = geneValues(data);
				sql.append(" into " + table + "(" + columns + ") values('" + data[0] + "','" + data[1] + "','" + data[2] + "', to_date('" + data[3] + "','yyyy-MM-dd'),'" + data[4] + "','" + data[5] + "'," + data[6] + ")");				
			}
			sql.append(" select 1 from dual");

			session.createSQLQuery(sql.toString()).executeUpdate();
			ts.commit();
		} catch (Exception ignore) {
			/**
			 * 源表数据拷贝到目标表中
			 * 即便复制过程出现异常也不需要回滚
			 */
			//ts.rollback();
		} finally {
			session.close();
		}
	}
	
	
	/**
	 * 
	 * 更新Qyinfo_Map表中的经纬度
	 */
	public void updateMap(final QyInfo_Map info) {

		this.update(info);
		
/*
		Session session = null;
		Transaction ts = null;
		try {
			session = this.getSessionFactory().openSession();
			ts = session.beginTransaction();
			session.createSQLQuery("update qyinfo_map set m_lng = '" + info.getM_lng() + "', m_lat='" + info.getM_lat() + "', m_on='" + info.getM_on() + "' where id = '" + info.getId() + "'").executeUpdate();
			ts.commit();
		} catch (Throwable e) {			
			e.printStackTrace();
		} finally {
			session.close();
		}	
		
*/
	}
	
	public void saveQyinfo(String qyname) {
				
		QyInfo_Map info = new QyInfo_Map();
		String id = UUID.randomUUID().toString().replaceAll("-", "");
		info.setId(id);
		info.setC_name(qyname);
		this.save(info);
		
	}
	
	
	
}
