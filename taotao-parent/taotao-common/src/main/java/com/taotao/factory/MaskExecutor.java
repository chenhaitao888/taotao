package com.taotao.factory;

import java.lang.reflect.Field;

import com.taotao.utils.MaskCodeUtil;

public class MaskExecutor<T> {
	public String str;
	public String warning;
	public Field field;
	public T t;

	public MaskExecutor(String str, String warning, Field field, T t) {
		this.str = str;
		this.warning = warning;
		this.field = field;
		this.t = t;
	}

	public void process() throws Exception {
	}

	public void maskCommon(String str, int beginIndex, int endIndex, char cover, Field field, T t) throws Exception {
		String maskSubWay = MaskCodeUtil.getMaskSubWay(str, beginIndex, endIndex, '*');
		field.setAccessible(true);
		field.set(t, maskSubWay);
	}
}

/** * 身份证 * * @author CHENHAITAO723 * @param <T> * */
class IdentityCardExecutor<T> extends MaskExecutor<T> {
	public IdentityCardExecutor(String str, String notice, Field field, T t) {
		super(str, notice, field, t);
	}

	@Override
	public void process() throws Exception {
		int l = str.length();
		if (l != 18) {
			throw new Exception(warning);
		}
		maskCommon(str, 2, 17, '*', field, t);
	}
}


/** * 护照 * * @author CHENHAITAO723 * @param <T> * */
class PassportExecutor<T> extends MaskExecutor<T> {
	public PassportExecutor(String str, String notice, Field field, T t) {
		super(str, notice, field, t);
	}

	@Override
	public void process() throws Exception {
		int l = str.length();
		if (l <= 2) {
			throw new Exception(warning);
		}
		maskCommon(str, 1, l - 2, '*', field, t);
	}
}

/** * 军官证 * * @author CHENHAITAO723 * @param <T> * */
class OfficerCardExecutor<T> extends MaskExecutor<T> {
	public OfficerCardExecutor(String str, String notice, Field field, T t) {
		super(str, notice, field, t);
	}

	@Override
	public void process() throws Exception {
		int l = str.length();
		if (l <= 2) {
			throw new Exception(warning);
		}
		maskCommon(str, 1, l - 2, '*', field, t);
	}
}

/** * 其他证件 * * @author CHENHAITAO723 * @param <T> * */
class OtherCardExecutor<T> extends MaskExecutor<T> {
	public OtherCardExecutor(String str, String warning, Field field, T t) {
		super(str, warning, field, t);
	}

	@Override
	public void process() throws Exception {
		int l = str.length();
		if (l < 3) {
			throw new Exception(warning);
		}
		maskCommon(str, 2, l - 1, '*', field, t);
	}
}


class DefaultExecutor<T> extends MaskExecutor<T> {
	public DefaultExecutor(String str, String warning, Field field, T t) {
		super(str, warning, field, t);
	}

	@Override
	public void process() throws Exception {
	}
}
