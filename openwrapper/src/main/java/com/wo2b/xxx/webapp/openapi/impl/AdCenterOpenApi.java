package com.wo2b.xxx.webapp.openapi.impl;

import com.opencdk.common.util.http.RequestParams;
import com.wo2b.xxx.webapp.Wo2bResListHandler;
import com.wo2b.xxx.webapp.ad.Advertisement;
import com.wo2b.xxx.webapp.openapi.OpenApi;

/**
 * 广告中心
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 */
public class AdCenterOpenApi extends OpenApi
{

	/**
	 * 广告
	 * 
	 * @param adType
	 * @param wo2bResHandler
	 */
	public void findByAdType(int adType, Wo2bResListHandler<Advertisement> wo2bResHandler)
	{
		final RequestParams params = new RequestParams();
		params.put("adType", adType);
		post("/mobile/AdCenter_findByAdType", params, wo2bResHandler);
	}

}
