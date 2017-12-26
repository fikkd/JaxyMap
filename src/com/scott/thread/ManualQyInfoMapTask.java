package com.scott.thread;

import java.util.List;

import com.scott.dao.FiDAO;
import com.scott.model.QyInfo;

/**
 * 将原始企业信息表中数据拷贝到信用地图专用使用的企业信息表中
 *
 * @since 2017年11月2日
 * @author 李瑞辉
 *
 */
public class ManualQyInfoMapTask implements Runnable {

	private List<QyInfo> list;
	private FiDAO fiDAO;

	public ManualQyInfoMapTask(List<QyInfo> list, FiDAO fiDAO) {
		this.list = list;
		this.fiDAO = fiDAO;
	}

	@Override
	public void run() {
		fiDAO.saveQyInfoMap(list);
	}

}
