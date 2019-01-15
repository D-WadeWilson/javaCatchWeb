package start;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpClientLogin {
	private static StringBuffer tmpcookies = null;
	/**
	 * 
	 * @param loginUrl
	 * http://user-bems:8080/energy-1.0/login.do?action=LoginAction&event_submit_doLogin=method";  
	 * @param parameter
	 * username=admin&password=123456&remember=on
	 * @param requestType 
	 * 1=post 2=get
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public static void initLogin(String loginUrl,String parameter,int requestType) throws Exception{
		tmpcookies = new StringBuffer();
		HttpClient httpClient = new HttpClient();
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if(parameter!=null && parameter.length()>0){
			if(parameter.contains("&")){
				String []str1= parameter.split("&");
				if(str1!=null && str1.length>0){
					for(int i=0;i<str1.length;i++){
						String []str2 = str1[i].split("=");
						NameValuePair nvp = new NameValuePair(str2[0],str2[1]);
						nvps.add(nvp);
					}
				}
			}else{
				String []str2 = parameter.split("=");
				NameValuePair nvp = new NameValuePair(str2[0],str2[1]);
				nvps.add(nvp);
			}
		}
		if(nvps.size()==0){
			return ;
		}
		
		NameValuePair[] data = new NameValuePair[nvps.size()];
		for(int i=0;i<nvps.size();i++){
			data[i]= nvps.get(i);
		}
		httpClient.getParams().setCookiePolicy(  
                CookiePolicy.BROWSER_COMPATIBILITY);  
		if(requestType==1){
			 PostMethod postMethod = new PostMethod(loginUrl);  
			 postMethod.setRequestBody(data);
			 httpClient.executeMethod(postMethod);
			
		}else if(requestType==2){
			GetMethod getMethod = new GetMethod(loginUrl+"?"+parameter);
			httpClient.executeMethod(getMethod);
		}
		Cookie[] cookies = httpClient.getState().getCookies();  
        for (Cookie c : cookies) {  
            tmpcookies.append(c.toString() + ";");  
        }  
	}
	/**
	 * 
	 * @param dataUrl
	 * http://user-bems:8080/energy-1.0/admin/adminScreen.do?action=ItemDeptHourAction&event_submit_do_query_data=method
	 * @param parameter
	 * date=2017&deptId=34c6fd0d-04e5-4173-acc6-c8ffc51353f7&eiId=7af48e3b-d6b5-11e4-bba2-000c2969e1e1 
	 * @param requestType
	 * 1=post 2=get
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public static String getData(String dataUrl,String parameter,int requestType) throws Exception{
		String result="";
		HttpClient httpClient = new HttpClient();
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if(parameter!=null && parameter.length()>0){
			if(parameter.contains("&")){
				String []str1= parameter.split("&");
				if(str1!=null && str1.length>0){
					for(int i=0;i<str1.length;i++){
						String []str2 = str1[i].split("=");
						NameValuePair nvp = new NameValuePair(str2[0],str2[1]);
						nvps.add(nvp);
					}
				}
			}else{
				String []str2 = parameter.split("=");
				NameValuePair nvp = new NameValuePair(str2[0],str2[1]);
				nvps.add(nvp);
			}
		}
		if(nvps.size()==0){
			return "";
		}
		NameValuePair[] data = new NameValuePair[nvps.size()];
		for(int i=0;i<nvps.size();i++){
			data[i]= nvps.get(i);
		}
		//httpClient.getParams().setCookiePolicy(  
        //        CookiePolicy.BROWSER_COMPATIBILITY);  
		if(requestType==1){
			 PostMethod postMethod = new PostMethod(dataUrl);  
			 postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			 postMethod.setRequestBody(data);
			 postMethod.setRequestHeader("cookie", tmpcookies.toString());  
			 httpClient.executeMethod(postMethod);
			 result = postMethod.getResponseBodyAsString();  
			
		}else if(requestType==2){
			GetMethod getMethod = new GetMethod(dataUrl+"?"+parameter);
			getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			getMethod.setRequestHeader("cookie", tmpcookies.toString());
			httpClient.executeMethod(getMethod);
			result = getMethod.getResponseBodyAsString();  
		}
		return result;
	}
	
	public static void main(String[] args) {  
        // 登陆 Url  
        String loginUrl = "http://hunanpg.cspiii.com/User/LoginSubmit";
        // 需登陆后访问的 Url  
        String dataUrl = "http://115.29.244.91:6112/sendInjunction";
        String username = "hnxxjishoushi";
        String password = "123456";
//		String pwd = DigestUtils.md5Hex(oripwd);
//		String password = DigestUtils.md5Hex(username+"."+oripwd).toUpperCase();
//		System.out.println(pwd);
//		System.out.println(password);

        try {
			initLogin(loginUrl,"Name="+username+"&password="+password,1);
			System.out.println("tmpcookies："+tmpcookies);
			
			String result = getData(loginUrl,"Name="+username+"&password="+password,1);
			System.out.println("查询结果："+result);

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  
}
