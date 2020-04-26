package cn.zilanxuan.util;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import sun.misc.BASE64Encoder;

/**
 * 百度AI 接口类
 * @author zilanxuan
 * @website http://www.zilanxuan.cn
 * @version 1.0
 * @date 2020年4月11日 下午3:12:51
 */
public class BaiduAiUtil {
	
	
	
	//文字转语音
	public static String textToSpeech(String text,HashMap<String,Object> options) {
		 // 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(Constants.BAIDU_Voice_APP_ID, Constants.BAIDU_Voice_API_KEY, Constants.BAIDU_Voice_SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        

        // 调用接口
        TtsResponse res = client.synthesis(text, "zh", 1, options);
        byte[] data = res.getData();
        JSONObject res1 = res.getResult();
        String result = null;
        if (data != null) {
            try {
                result = BaiduAiUtil.synthesisToBase64(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (res1 != null) {
            System.out.println(res1.toString(2));
        }
        return result;
	}
	
	
	public static String synthesisToBase64(byte[] data) throws JSONException{
		return new BASE64Encoder().encode(data);
	}
}
