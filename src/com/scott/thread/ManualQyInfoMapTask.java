package com.scott.thread;

import java.util.List;
import java.util.concurrent.CountDownLatch;

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

	private CountDownLatch latch;
	private List<QyInfo> list;
	private FiDAO fiDAO;

	public ManualQyInfoMapTask(CountDownLatch latch, List<QyInfo> list, FiDAO fiDAO) {
		this.latch = latch;
		this.list = list;
		this.fiDAO = fiDAO;
	}

	@Override
	public void run() {
		fiDAO.saveQyInfoMap(list);		
		latch.countDown();
	}

}
