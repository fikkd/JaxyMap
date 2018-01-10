package com.scott.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 信用地图专用企业信息表
 * 
 * 信用地图最后一层级数据
 *
 * @since  2017年11月1日
 * @author 李瑞辉
 *
 */
public class QyInfo_Map implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7355962086257463088L;
	
	private String id;
	/*
	 * 企业名称
	 */
	private String c_name;
	private String c_social_credit_code;
	private Date estart_time;
	private String legal_representative;
	private String res_address;
	private Number reg_capital;
  	private String m_lng;
  	private String m_lat;	
	
	public QyInfo_Map() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getC_name() {
		return c_name;
	}

	public void setC_name(String c_name) {
		this.c_name = c_name;
	}

	public String getC_social_credit_code() {
		return c_social_credit_code;
	}

	public void setC_social_credit_code(String c_social_credit_code) {
		this.c_social_credit_code = c_social_credit_code;
	}

	public Date getEstart_time() {
		return estart_time;
	}

	public void setEstart_time(Date estart_time) {
		this.estart_time = estart_time;
	}

	public String getLegal_representative() {
		return legal_representative;
	}

	public void setLegal_representative(String legal_representative) {
		this.legal_representative = legal_representative;
	}

	public String getRes_address() {
		return res_address;
	}

	public void setRes_address(String res_address) {
		this.res_address = res_address;
	}

	public Number getReg_capital() {
		return reg_capital;
	}

	public void setReg_capital(Number reg_capital) {
		this.reg_capital = reg_capital;
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

	

	
	
	
}
