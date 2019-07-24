package com.taotao.strategy;

import java.lang.reflect.Field;

import com.taotao.factory.MaskBase;
/**
 * 姓名掩码
 * <p>Title: MaskNameLevel</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2019年7月24日下午8:48:00
 * @version:1.0
 * @param <T>
 */
public class MaskNameLevel<T> extends MaskBase<T> implements MaskStrategy{
	public MaskNameLevel(String str, String warning, Field field, T t) {
        super(str, warning, field, t);
    }

    @Override
    public void mask() throws Exception {
        throwing();
        /**
         * 1.如果名字长度<=3:保留第一位，其余掩盖
         * 2.如果名字>=4:保留总字符个数的前一半，其余掩盖
         */
        if(str.length() == 1){
            throw new Exception("姓名长度不可为一位");
        }
        int length = str.length();
        int noMaskLength = length/2;
        maskCommon(str, noMaskLength + 1, str.length(), '*', field, t);
    }
}
