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
	 * ɾ��QYINFO_MAP_LEVEL����������
	 * Ϊ�����¹�������㼶������׼��
	 * 
	 *
	 * @�����¼ 2018��1��10�� ����9:53:03 ����� ����
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
	 * ��ҳ��ѯԴ��ҵ������
	 * 
	 * @param i ҳ��
	 * @param tName ����
	 * @param tColumns �ֶ���
	 * @return
	 *
	 * @�����¼ 2018��1��8�� ����11:48:32 ����� ����
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
	 * ��ҳ��ѯ���õ�ͼר����ҵ��Ϣ������
	 * 
	 * @param i
	 * @return
	 *
	 * @�����¼ 2018��1��9�� ����12:26:30 ����� ����
	 *
	 */
	@SuppressWarnings("unchecked")
	public List<QyInfo_Map> findQyInfoMapByPage(int i) {
		
		return this.findPageByHql("from QyInfo_Map ", new Object[] {}, i * 10, 10);
	}
	
	/**
	 * ������column1,column2,column3ת��t.column1,t.column2,t.column3 
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
	 * ��ֹ������ֶ�ֵ��null 
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
	
	/** ���γ�� */
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
	
	/** ��󾭶� */
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
	
	
	/** ��Сγ�� */
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

	/** ��С���� */
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
	 * ��ѯԴ��ҵ��������
	 * 
	 * @param tName Դ��ҵ����
	 * @return
	 *
	 * @�����¼ 2018��1��8�� ����11:47:23 ����� ����
	 *
	 */
	public int getPageCount(String tName) {
		List<Object> params = new ArrayList<>();
		return this.findCountBySQLQuery("select count(*) from " + tName, params.toArray());
	}

	/**
	 * 
	 * ��ѯ���õ�ͼר�ñ�������
	 * 
	 * @return
	 *
	 * @�����¼ 2018��1��9�� ����12:02:20 ����� ����
	 *
	 */
	public int getPageCountOfMap() {
		List<Object> params = new ArrayList<>();
		return this.findCountBySQLQuery("select count(*) from qyinfo_map ", params.toArray());
	}
	
	/**
	 * 
	 * ��ѯ���õ�ͼר�ñ�γ��Ϊ�յ���ҵ����
	 */
	public int getLngisnull() {
		List<Object> params = new ArrayList<>();
		return this.findCountBySQLQuery("select count(*) from qyinfo_map where m_lng is null", params.toArray());
	}
	
	/**
	 * ��ѯ10�����õ�ͼר�ñ������( �ų���γ��Ϊ�յ����� )
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<QyInfo_Map> getListOfMapOn() {
		return this.findPageByHql("from QyInfo_Map m where (m.m_lng is null or m.m_lat is null) and m.m_on is null", new Object[] {}, 0, 10);
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
	
	/**
	 * 
	 * ���浽��ͼר�õ���ҵ��Ϣ����
	 * 
	 * @param list Դ����
	 * @param table Ŀ���
	 * @param columns Ŀ���ֶ�
	 *
	 * @�����¼ 2018��1��8�� ����11:49:26 LRH ����
	 *
	 */
	public void saveQyInfoMap(List<Object[]> list, String table, String columns) {
		Session session = null;
		Transaction ts;
		try {
			session = this.getSessionFactory().openSession();
			ts = session.beginTransaction();
			
			/**
			 * �˴��������Ƿ������ͬ��ͳһ������ô���
			 * ��ͬ��ֻ����һ������ 
			 */
			Set<String> set = new HashSet<String>();
			Iterator<Object[]> it = list.iterator();
			while (it.hasNext()) {
				Object[] o = it.next();
				if (o == null)  it.remove();
				if (o[0] == null || o[0].equals(""))  it.remove(); // ����
				if (o[2] == null || o[0].equals(""))  it.remove(); // ͳһ������ô���
				if (!set.add(o[2].toString())) it.remove();  // �������Set����ʧ��˵���ظ�
			}
			
			/**
			 *  ����д��<tt>insert all</tt>��<tt>Oracle</tt>�п���������������
			 *
			 */
			StringBuffer sql = new StringBuffer("insert all ");

			for (Object[] data : list) {				
				/**
				 * �ж����ݿ����Ƿ������ͬ��ͳһ������ô��������
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
			 * Դ�����ݿ�����Ŀ�����
			 * ���㸴�ƹ��̳����쳣Ҳ����Ҫ�ع�
			 */
			//ts.rollback();
		} finally {
			session.close();
		}
	}
	
	
	/**
	 * 
	 * ����Qyinfo_Map���еľ�γ��
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
