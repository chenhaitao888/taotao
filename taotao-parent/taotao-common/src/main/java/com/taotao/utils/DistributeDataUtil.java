package com.taotao.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 分配数据公共类
 * <p>Title: DistributeDataUtil</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2019年12月13日下午6:05:04
 * @version:1.0
 */
public class DistributeDataUtil {
	private int segments;
	private int batchData;
	private int totalData;
	private int batches;
	public DistributeDataUtil(int segments, int batchData, int totalData) {
		this.segments = segments;
		this.batchData = batchData;
		this.totalData = totalData;
		this.batches = (isOperation(batchData) && isOperation(totalData) && (totalData > batchData)) ? totalData >> count() : ((totalData % batchData == 0) ? 
				totalData / batchData : totalData / batchData + 1);
	}
	
	
	private List assiginSegmentData(int segment){
		List<Integer> res = new ArrayList<>();
		for (int i = 1; i <= batches; i++){
			int index;
			if (isOperation(segments)){
				index = indexFor(i, segments);
			}else {
				index = i % segments;
			}
			if (segment == index){
				res.add(i);
			}
		}
		return res;
	}
	
	private int indexFor(int batch, int segments) {
		return batch & (segments - 1); // 此处借用hashmap取模的技巧(前提是segments为2的幂),位运算提升运算效率
	}


	private int count() {
		int i = 0;
		while(true){
			if(batchData >> i == 1) break;
			i++;
		}
		return i;
	}
	private boolean isOperation(int v) {
		return (v & (v - 1)) == 0;  //判断v是不是2的幂
	}
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		int segments = 8;
		int batchData = 1 << 10;
		int totalData = 2231039;
		DistributeDataUtil util = new DistributeDataUtil(segments, batchData, totalData);
		for(int i = 0; i < segments; i++){
			List assiginSegmentData = util.assiginSegmentData(i);
			System.out.println(Arrays.toString(assiginSegmentData.toArray()));
		}
		System.out.println("耗时: " + (System.currentTimeMillis() - start));
	}
	
}
