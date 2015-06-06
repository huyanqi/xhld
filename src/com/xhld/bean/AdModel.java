package com.xhld.bean;

import java.io.Serializable;

/**
 * @author Frankie
 *
 * 广告
 */
public class AdModel implements Serializable{

	public long id;
	
	public String url;	//图片的url
	
	public String text;//文本内容
	
	public Long adColumnCode;//所属广告栏code
	
	public Long adOrder;//顺序
	
	public String mark;//备注
	
}
