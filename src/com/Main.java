package com;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import net.sf.json.JSONObject;


public class Main {

	private static BigDecimal goldaz = new BigDecimal(31.1035);   //美元盎司
	
	public static void main(String args[]) {
		BigDecimal currentDollarPrice = getPrice();      //黄金当前的价格

		BigDecimal currentDollarPrice1 = currentDollarPrice.subtract(new BigDecimal(1.30)).setScale(2, RoundingMode.HALF_UP);
		
		RateVO rateVO = getExchangeRate();
		float currency = rateVO.getRetData().getCurrency();
		String dateTime = rateVO.getRetData().getDate() + " " + rateVO.getRetData().getTime();
		System.out.println("美元金价:" + currentDollarPrice);
		
		BigDecimal bigCurrency = new BigDecimal(currency).setScale(4, RoundingMode.HALF_UP);;
		
		BigDecimal total1 = currentDollarPrice1.multiply(bigCurrency).setScale(4, RoundingMode.HALF_UP);
		BigDecimal buyPrice = total1.divide(goldaz,2,BigDecimal.ROUND_DOWN);
		BigDecimal sellPrice = buyPrice.add(new BigDecimal(0.7)).setScale(2, RoundingMode.HALF_UP);
		System.out.println("汇率:" + bigCurrency);
		System.out.println("时间:" + dateTime);
		System.out.println("工商银行人民币买入价:" + buyPrice);
		System.out.println("工商银行人民币卖出价:" + sellPrice);
		
		
	}

	public static RateVO getExchangeRate() {
		String httpUrl = "http://apis.baidu.com/apistore/currencyservice/currency";
		String httpArg = "fromCurrency=USD&toCurrency=CNY&amount=2";
		String jsonResult = request(httpUrl, httpArg);
		System.out.println(jsonResult);
		
		JSONObject jsonObject = JSONObject.fromObject(jsonResult);
		RateVO rateVO  = (RateVO) JSONObject.toBean(jsonObject,RateVO.class);
		return rateVO;
	}
	
	
	/**
	 * @param urlAll  :请求接口
	 * @param httpArg :参数
	 * @return 返回结果
	 */
	public static String request(String httpUrl, String httpArg) {
	    BufferedReader reader = null;
	    String result = null;
	    StringBuffer sbf = new StringBuffer();
	    httpUrl = httpUrl + "?" + httpArg;

	    try {
	        URL url = new URL(httpUrl);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setRequestMethod("GET");
	        // 填入apikey到HTTP header
	        connection.setRequestProperty("apikey",  "7c40d808fc41fde4739b92b10e951824");
	        connection.connect();
	        InputStream is = connection.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        String strRead = null;
	        while ((strRead = reader.readLine()) != null) {
	            sbf.append(strRead);
	            sbf.append("\r\n");
	        }
	        reader.close();
	        result = sbf.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}

	
	/**
	 * 获得用户输入的值
	 * @return
	 */
	public static BigDecimal getPrice() {
		Scanner scan = new Scanner(System.in);
		String read = scan.nextLine();
		System.out.println("输入数据："+read);
		BigDecimal result = new BigDecimal(read).setScale(2, RoundingMode.HALF_UP);
		System.out.println("完成");
		return result;
	}
}
