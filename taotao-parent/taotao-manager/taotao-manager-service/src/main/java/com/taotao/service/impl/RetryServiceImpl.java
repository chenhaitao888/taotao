package com.taotao.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.taotao.annotation.RetryAnnotation;
import com.taotao.exception.TaoTaoException;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.service.RetryService;

@Service
public class RetryServiceImpl implements RetryService {
	AtomicBoolean released = new AtomicBoolean(false);
	private static final ThreadLocal<Integer> loacl = new ThreadLocal<>();
	private String mm = new String("dd");
	@Autowired
	private RetryService retryService;
	@Autowired
	private TbUserMapper user;
	private static final ExecutorService exe = Executors.newFixedThreadPool(3);
	private static final ThreadLocal<Integer> countLoacl = new ThreadLocal<>();

	@Override
	@RetryAnnotation(retryAdapter = "com.taotao.retry.MapRetryAdapter", retryCount = 3, timeUnit = TimeUnit.SECONDS, waitTime = 3)
	public Map<String, Object> retryTest(List<String> list) throws Exception {
		Map<String, Object> map = new HashMap<>();
		if (loacl.get() == null) {
			loacl.set(0);
		}
		Integer integer = loacl.get();
		/*
		 * map.put("responseCode", "001"); map.put("responseMsg", "系统异常");
		 */
		System.out.println(Thread.currentThread().getName() + "--" + integer);
		if (integer == 3) {
			loacl.remove();

		} else {
			map.put("responseCode", "001");
			map.put("responseMsg", "系统异常");
			integer = integer + 1;
			loacl.set(integer);
		}
		list.add(integer + "");
		return map;
	}

	@Override
	public void Test1() {
		if (released.compareAndSet(false, true)) {
			System.out.println("test1");
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		released.compareAndSet(true, false);
	}

	@Override
	public void Test2() {
		if (released.compareAndSet(false, true)) {
			mm = "aa";
			System.out.println(mm);
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		released.compareAndSet(true, false);
	}

	@Override
	public void test3(int begin, int retryCount, int evNum) {
		// int qryCount = user.qryCount();
		/*
		 * if((qryCount % 2) == 0){ qryCount = qryCount / 2; }else{ qryCount =
		 * qryCount / 2 + 1; }
		 */
		/*
		 * int[] evNum = new int[3]; evNum[0] = qryCount / 3; evNum[1] =
		 * qryCount / 3; if((qryCount % 3) == 0){ evNum[2] = qryCount / 3;
		 * }else{ evNum[2] = qryCount / 3 + qryCount % 3; } int[] qcount = new
		 * int[3]; for(int i = 0; i <= 2; i++){ if((evNum[i] % 2) == 0){
		 * qcount[i] = evNum[i] / 2; }else{ qcount[i] = evNum[i] / 2 + 1; } }
		 * 
		 * for(int i = 0; i <= 2; i++){ int j = i; exe.submit(() -> {
		 * ((RetryServiceImpl)AopContext.currentProxy()).saveRe(evNum[j] * j +
		 * 1,qcount[j]); //saveRe(evNum[j] * j + 1,qcount[j]); });
		 * 
		 * }
		 */

		// ((RetryServiceImpl)AopContext.currentProxy()).saveRe(qryCount);
		/*
		 * Integer i = 1; TbUser record = new TbUser();
		 * record.setUsername("chenhaitao" + i); record.setId(i.longValue());
		 * record.setPassword("ada"); record.setCreated(new Date());
		 * record.setUpdated(new Date()); user.insert(record); int j = i / 0;
		 */
		// ((RetryServiceImpl)AopContext.currentProxy()).insertData();
		((RetryServiceImpl) AopContext.currentProxy()).saveRe(begin, retryCount, evNum);

	}

	public void insertData(Integer i) {
		TbUser record = new TbUser();
		record.setUsername("chenhaitao" + i);
		record.setId(i.longValue());
		record.setPassword("ada");
		record.setCreated(new Date());
		record.setUpdated(new Date());
		user.insert(record);
		int j = i / (3 - i);
	}

	/**
	 * 对加了事务的方法,只有当前循环失败会回滚,不会影响其他
	 * <p>
	 * Title: saveRe
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param count
	 * @return: Boolean
	 */
	@RetryAnnotation(argNum = 2)
	public Boolean saveRe(int begin, Integer count, int evNum) {
		System.out.println(Thread.currentThread().getName() + "----" + "起始页: " + begin + "---" + "重试次数: " + count);
		Integer page = loacl.get();
		System.out.println(Thread.currentThread().getName() + "----" + "页数: " + page);
		try {
			if (page == null) {
				loacl.set(begin);
				page = loacl.get();
			}
			// page = loacl.get();
			PageHelper.startPage(page, 2);
			Boolean b = testCount(loacl.get(), countLoacl, evNum);
			if (b) {
				loacl.remove();
				countLoacl.remove();
				System.out.println(Thread.currentThread().getName() + "---" + "移除成功");
				return b;
			}
			/*
			 * TbUser record = new TbUser(); record.setUsername("chenhaitao" +
			 * i); record.setId(i.longValue()); record.setPassword("ada");
			 * record.setCreated(new Date()); record.setUpdated(new Date());
			 * user.insert(record);
			 */
			// ((RetryServiceImpl)AopContext.currentProxy()).insertData(i);
			System.out.println(Thread.currentThread().getName() + "---" + "重试次数: " + count);
			loacl.set(page + 1);
			return b;
		} catch (TaoTaoException e) {
			System.out.println(e.getMessage());
			loacl.set(page + 1);
			return false;
		} catch (Exception e) {
			loacl.remove();
			countLoacl.remove();
			System.out.println("heheh");
			return true;
		} finally {
			// System.out.println("分页清除");
			PageHelper.clearPage();
		}
	}

	private Boolean testCount(int i, ThreadLocal<Integer> countloacl, int evNum) throws TaoTaoException {
		try {
			// System.out.println("page值: " + i);
			List<String> qryUsr = user.qryUsr();
			if (CollectionUtils.isEmpty(qryUsr)) {
				return true;
			}
			// int j = 1 / (i - 4);
			/*
			 * System.out.println(Thread.currentThread().getName() + "----" +
			 * qryUsr); String test = ""; for (String string : qryUsr) { test =
			 * test + "," + string; }
			 * user.insertk(Thread.currentThread().getName(), test);
			 */
			/*
			 * if(qryUsr.size() < 2){ return true; }
			 */
			// 数据入库
			operatorData(qryUsr);
			countloacl.set((countloacl.get() == null ? 0 : countloacl.get()) + qryUsr.size());
			// System.out.println(Thread.currentThread().getName() + "---" +
			// countloacl.get() + "---" + evNum);
			if (countloacl.get() >= evNum || qryUsr.size() < 2) {

				System.out.println(Thread.currentThread().getName() + "执行完了..");
				return true;
			}
			System.out.println(Thread.currentThread().getName() + "--查数: " + qryUsr.size());
			return false;
		} catch (Exception e) {
			throw new TaoTaoException("yichang");
		}

	}

	/**
	 * 数据库操作
	 */
	public void operatorData(List<String> qryUsr) {
		System.out.println(Thread.currentThread().getName() + "----" + qryUsr);
		String test = "";
		for (String string : qryUsr) {
			test = test + "," + string;
		}
		user.insertk(Thread.currentThread().getName(), test);
		//user.updateUsr(qryUsr);
	}

	@Override
	public void test5() {
		TbUser usrs = user.selectByPrimaryKey(22L);
		PageHelper.startPage(2, 4);
		// List<TbUser> usrs = user.qryUsr();
		System.out.println(usrs.getUsername());
		int i = 1 / 0;
	}

	public static void main(String[] args) {
		for (int i = 1; i <= 10; i++) {
			int m = i;
			new Thread(new Runnable() {

				@Override
				public void run() {
					if (m == 5) {
						return;
					}
					int j = 4 / (m - 4);
					System.out.println(m);

				}
			}).start();

		}
	}

	@Override
	public void test6(int page) {
		List<String> qryUsr = user.qryUsr();
		if (CollectionUtils.isEmpty(qryUsr)) {
			return;
		}
		operatorData(qryUsr);

	}

}
