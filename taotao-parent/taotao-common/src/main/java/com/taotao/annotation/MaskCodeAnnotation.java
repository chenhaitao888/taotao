package com.taotao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.taotao.enums.MaskLevelEnum;
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaskCodeAnnotation {
	/* 0：没有 1：中间 2：全掩码 */
	// public abstract int maskLevel() default 0;
	MaskLevelEnum maskLevel() default MaskLevelEnum.DEFAULTLEVEL;
	String key() default "";
}
