package com.taotao.strategy;

import java.lang.reflect.Field;

import com.taotao.factory.MaskBase;
import com.taotao.factory.MaskExecutor;
import com.taotao.factory.MaskFactory;

/**
 * 根据证件类型掩码
 * <p>Title: MaskCardLevel</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2019年7月24日下午8:52:46
 * @version:1.0
 * @param <T>
 */
public class MaskCardLevel<T> extends MaskBase<T> implements MaskStrategy {
	private String cardType;

	public MaskCardLevel(String str, String warning, Field field, T t) {
		super(str, warning, field, t);
	}

	public MaskCardLevel(String str, String warning, Field field, T t, String cardType) {
		super(str, warning, field, t);
		this.cardType = cardType;
	}

	@Override
	public void mask() throws Exception {
		throwing();
		MaskExecutor<T> executor = MaskFactory.getExecutor(cardType, str, field, t);
		executor.process();
	}
}
