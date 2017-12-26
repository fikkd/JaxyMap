package com.scott.thread;

import java.util.List;

import com.scott.dao.FiDAO;
import com.scott.model.QyInfo;

/**
 * ��ԭʼ��ҵ��Ϣ�������ݿ��������õ�ͼר��ʹ�õ���ҵ��Ϣ����
 *
 * @since 2017��11��2��
 * @author �����
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
