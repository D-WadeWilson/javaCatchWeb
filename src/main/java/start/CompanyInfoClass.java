package start;

import dao.CheckGradeMapper;
import dao.CompanyInfoMapper;
import dataobject.CheckGrade;
import dataobject.CompanyInfo;
import dataobject.Criteria;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by duchengyang on 2019/1/10.
 */
public class CompanyInfoClass {
    private static Logger log = Logger.getLogger(CompanyInfoClass.class);
    private static String userName;
    private static String password;
    private static String sCharSet = "utf-8";

    public static void getCompanyInfo(SqlSession session, CloseableHttpClient httpclient) throws IOException, ParseException {

        System.out.println("开始测试企业信息");
        Boolean flag = true;
        userName = getParamFromProp("userName");
        password = getParamFromProp("password");

        while (flag){
//            String code = getVerifyCode(httpclient);        //获取验证码

            HashMap<String,String> map = new HashMap<String,String>();
            map.put("Name",userName);
            map.put("Password",password);
//            map.put("VerifyCode","88888");
            String loginSubmitUrl = getParamFromProp("loginSubmitUrl");
            List strAndCookie = getResponseStr(httpclient,new BasicCookieStore() ,map,loginSubmitUrl);      //第一步，登陆

            HashMap<String,String> paramsMap = getUrlParamsMap((String) strAndCookie.get(0));        //获取返回的body里的参数值
            String authenticationUrl = getParamFromProp("authenticationUrl");
            List strAndCookie1 = getResponseStr(httpclient, new BasicCookieStore(), paramsMap,authenticationUrl); //登陆后，验证第一步:访问接口，获取返回的str和cookie


            HashMap<String,String> paramsMapGLR = getUrlParamsMap((String) strAndCookie1.get(0)); //获取验证第一步返回的str

            String getLoginResultUrl = getParamFromProp("getLoginResultUrl");
            List strAndCookie2 = getResponseStr(httpclient, new BasicCookieStore(), paramsMapGLR,getLoginResultUrl); //登陆后，验证第二步:访问接口，获取返回的str和cookie

            String loginRes =  strAndCookie2.get(0).toString();
            if(!loginRes.contains("/Home/Index/")){             //如果登陆失败，循环终止
                continue;
            }
            List cookies = (List) (strAndCookie2.get(1)!=null?strAndCookie2.get(1):"");  //验证第二部完成后，登陆成功，获取此时的cookie，存入cookieStore
            CookieStore cookieStore = new BasicCookieStore();
            for(Object item:cookies){
                cookieStore.addCookie((Cookie) item);

            }

            HashMap<String,String> mapPageCompany = new HashMap<String,String>();      //访问接口的参数
            mapPageCompany.put("pageIndex","1");
            mapPageCompany.put("pageSize","1000");
            String businessInfoListUrl = getParamFromProp("businessInfoListUrl");
            List strAndCookie3 = getResponseStr(httpclient, cookieStore, mapPageCompany,businessInfoListUrl);       //访问企业信息接口
            String companyData = strAndCookie3.get(0)!=null? (String) strAndCookie3.get(0) :"";

            HashMap<String,String> mapPageGrade = new HashMap<String,String>();      //访问接口的参数
            mapPageGrade.put("pageIndex","1");
            String bindSearchDataUrl = getParamFromProp("bindSearchDataUrl");
            List strAndCookie4 = getResponseStr(httpclient, cookieStore, mapPageGrade,bindSearchDataUrl);     //访问审核评分接口
            String scoreData = strAndCookie4.get(0)!=null? (String) strAndCookie4.get(0) :"";


            CompanyInfoMapper companyInfoMapper = session.getMapper(CompanyInfoMapper.class);       //解析接口返回数据
            JSONObject companyInfo =  JSONObject.fromObject(companyData);
            List<JSONObject> companyInfoList = (List<JSONObject>) companyInfo.get("Rows");
            int iFlag = 1;
            int gFlag = 1;
            iFlag = insertCompanyInfo(companyInfoMapper, companyInfoList, iFlag);           //插入企业信息数据
            if(iFlag==1){
                System.out.println("企业信息保存成功");
            }else{
                continue;
            }
            CheckGradeMapper checkGradeMapper = session.getMapper(CheckGradeMapper.class);
            JSONObject gradeInfo =  JSONObject.fromObject(scoreData);
            List<JSONObject>  gradeInfoList = (List<JSONObject>) gradeInfo.get("Rows");

            gFlag = insertCheckGrade(gFlag, checkGradeMapper, gradeInfoList);               //插入审核评分数据
            if(gFlag==1){
                System.out.println("审核评分保存成功");
            }else{
                continue;
            }
            if(iFlag==1&&gFlag==1){             //当所有数据都保存成功，终止循环。
                flag = false;
            }
        }

        System.out.println("------------------本次抓取完毕-------------------");
        /*      //登陆页面接口,暂不需使用
        HttpGet httpGet = new HttpGet("http://hunanpg.cspiii.com/Home/Index/");
        httpGet.addHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
        HttpResponse responseLogin = httpclient.execute(httpget);
        String strLogin = EntityUtils.toString(responseLogin.getEntity(),"UTF-8");
        HttpEntity entity = responseLogin.getEntity();
        if (entity != null) {
            InputStream instreams = entity.getContent();
            String str = convertStreamToString(instreams);
            httpGet.abort();
            System.out.println("++strLogin++"+str);
        }*/
    /*退出接口,暂不需使用*/
/*        HttpGet httpget = new HttpGet("http://hunanpg.cspiii.com/User/LogOut");
        // 执行get请求
        HttpResponse response = httpclient.execute(httpget);
        String ste = EntityUtils.toString(response.getEntity());
        System.out.println("=================ste=======================" + ste);


        HashMap<String, String> paramsMapOut = getUrlParamsMap(ste); //获取验证第一步返回的str
        System.out.println("===============paramsMapOut" + paramsMapOut);
        List strAndCookie3 = getResponseStr(httpclient, new BasicCookieStore(), paramsMapOut, "http://passport.cspiii.com/Authentication.aspx");       //
        System.out.println(strAndCookie3.get(0));*/
    }

    private static int insertCheckGrade(int gFlag, CheckGradeMapper checkGradeMapper, List<JSONObject> gradeInfoList) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Criteria criteria = new Criteria();
        checkGradeMapper.deleteByExample(criteria);
        for (JSONObject grade : gradeInfoList) {
            CheckGrade checkGrade = new CheckGrade();
            String companyName = (String) grade.get("BusinessName");
            String tradeName1 = (String) grade.get("ParentIndustrtName");
            String tradeName2 = (String) grade.get("IndustryNames");
            String trade = tradeName1 + tradeName2;
            String questionnaire = (String) grade.get("QuestionnaireName");
            if (!"".equals(grade.get("TotalScore")) && grade.get("TotalScore") != null) {
                BigDecimal score = BigDecimal.valueOf(Double.valueOf(grade.get("TotalScore").toString()));
                checkGrade.setScore(score);
            }
            if (grade.get("ApprovalStatus") != null) {
                int status = Integer.valueOf(grade.get("ApprovalStatus").toString());
                checkGrade.setStatus(status);
            }
            if (!"".equals(grade.get("PhaseID")) && grade.get("PhaseID") != null) {
                int step = Integer.valueOf(grade.get("PhaseID").toString());
                checkGrade.setStep(step);
            }
            String evaluation_time = (String) grade.get("CommitDate");
            if (!"".equals(evaluation_time) && evaluation_time != null) {
                Date evalDate = sdf.parse(evaluation_time);
                checkGrade.setEvaluationTime(evalDate);
            }
            String scale = (String) grade.get("Description");
            checkGrade.setId(UUID.randomUUID().toString());
            checkGrade.setCompanyName(companyName);
            checkGrade.setTrade(trade);
            checkGrade.setQuestionnaire(questionnaire);
            checkGrade.setCreateTime(new Date());
            if (scale.equals("300人以下")) {
                checkGrade.setScale(1);
            } else if (scale.equals("300-1000人")) {
                checkGrade.setScale(2);
            } else if (scale.equals("1000-5000人")) {
                checkGrade.setScale(3);
            } else if (scale.equals("5000-10000人")) {
                checkGrade.setScale(4);
            } else if (scale.equals("10000人以上")) {
                checkGrade.setScale(5);
            } else {
                checkGrade.setScale(0);
            }
            int insertNum = checkGradeMapper.insert(checkGrade);
            if (insertNum != 1) {
                gFlag = 0;
            }
        }
        return gFlag;
    }

    private static int insertCompanyInfo(CompanyInfoMapper companyInfoMapper, List<JSONObject> companyInfoList, int iFlag) {
        Criteria criteria = new Criteria();             //删除旧数据
        companyInfoMapper.deleteByExample(criteria);
        for (JSONObject company : companyInfoList) {
            CompanyInfo companyInfoObj = new CompanyInfo();
            String companyName = (String) company.get("Name");
            String userName = (String) company.get("UserName");
            String linkman = (String) company.get("ContactName");
            String email = (String) company.get("Email");
            String telephone = (String) company.get("Phone");
            String mobilephone = (String) company.get("Mobile");
            String status = (String) company.get("StateName");
            String area = (String) company.get("Area");
            String trade = (String) company.get("Industry");
            String scale = (String) company.get("ScaleName");
            companyInfoObj.setId(UUID.randomUUID().toString());
            companyInfoObj.setCompanyName(companyName);
            companyInfoObj.setUserName(userName);
            companyInfoObj.setLinkman(linkman);
            companyInfoObj.setEmail(email);
            companyInfoObj.setTelephone(telephone);
            companyInfoObj.setMobilephone(mobilephone);
            companyInfoObj.setCreateTime(new Date());
            if (status.equals("激活")) {
                companyInfoObj.setStatus(1);
            } else {
                companyInfoObj.setStatus(0);
            }
            companyInfoObj.setArea(area);
            companyInfoObj.setTrade(trade);
            if (scale.equals("300人以下")) {
                companyInfoObj.setScale(1);
            } else if (scale.equals("300-1000人")) {
                companyInfoObj.setScale(2);
            } else if (scale.equals("1000-5000人")) {
                companyInfoObj.setScale(3);
            } else if (scale.equals("5000-10000人")) {
                companyInfoObj.setScale(4);
            } else if (scale.equals("10000人以上")) {
                companyInfoObj.setScale(5);
            } else {
                companyInfoObj.setScale(0);
            }
            int insertNum = companyInfoMapper.insert(companyInfoObj);
            if (insertNum != 1) {
                iFlag = 0;
            }
        }
        return iFlag;
    }

    private static String getVerifyCode(CloseableHttpClient httpclient) throws IOException {
        String url = getParamFromProp("verifyUrl");         //获取验证码接口
        // 通过get方式访问验证码连接，得到验证码图片
        HttpGet httpget = new HttpGet(url);
        // 执行get请求
        HttpResponse response = httpclient.execute(httpget);
        // 我们把返回的信息，保存在本地的一张图片里
        File file = new File("regist.jpg");         //将验证码转换成图片
        OutputStream ops = new FileOutputStream(file);
        response.getEntity().writeTo(ops);
        ops.close();
        return getImgContent("regist.jpg");
    }


    private static String getParamFromProp(String key) {
        InputStream is = CompanyMain.class.getClassLoader().getResourceAsStream("interface.properties");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        Properties props = new Properties();
        try {
            props.load(br);
            return (String) props.get(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String getStringFromJson(JSONObject adata) {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        for (Object key : adata.keySet()) {
            sb.append("\"" + key + "\":\"" + adata.get(key) + "\",");
        }
        String rtn = sb.toString().substring(0, sb.toString().length() - 1) + "}";
        return rtn;
    }

    protected static String getImgContent(String imgUrl) {
        String content = "";
        File imageFile = new File(imgUrl);
        //读取图片数字
        ITesseract instance = new Tesseract();


        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        instance.setLanguage("eng");//英文库识别数字比较准确
        instance.setDatapath(tessDataFolder.getAbsolutePath());


        try {
            content = instance.doOCR(imageFile).replace("\n", "");
            System.out.println(content);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
        return content;
    }

    private static HashMap<String, String> getUrlParamsMap(String str) {
        int start = str.indexOf("<input");
        int end = str.lastIndexOf("</form");
        String str1 = str.substring(start, end);
        String str2 = str1.replace("input", "").replace("<", "{").replace(">", "},").replace("=", ":")
                .replace("type:\"hidden\"", ",");
        String str3 = str2.substring(0, str2.length() - 1);
        String strEnd = "[" + str3 + "]";
//        System.out.println(strEnd);


        JSONArray jsonArray = JSONArray.fromObject(strEnd);
        HashMap<String, String> paramsMap = new HashMap<String, String>();
        for (Object jsonObject : jsonArray) {
            JSONObject jo = (JSONObject) jsonObject;
            String key = (String) jo.get("name");
            String value = (String) jo.get("value");
            paramsMap.put(key, value);
        }
        return paramsMap;
    }

    private static List getResponseStr(CloseableHttpClient httpclient, CookieStore cookieStore, HashMap<String, String> paramsMap, String url) throws IOException {
//        CookieStore cookieStore = new BasicCookieStore();
        httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();   //设置cookie
        HttpPost httpost = new HttpPost(url); // 登录url
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        Iterator<Map.Entry<String, String>> it = paramsMap.entrySet().iterator();     //获取参数，遍历map
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (entry.getKey().equals("Authenticator")) {
                String auth = entry.getValue().replace(":", "=");
                paramsList.add(new BasicNameValuePair(entry.getKey(), auth));
            } else {
                paramsList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        httpost.addHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
        httpost.setEntity(new UrlEncodedFormEntity(paramsList, sCharSet));
        HttpResponse response = httpclient.execute(httpost);

        List<Cookie> cookies = cookieStore.getCookies();        //得到cookie
//        StringBuilder cookieInfo  = new StringBuilder();
//        for (int i = 0; i < cookies.size(); i++) {
//            if (cookies.get(i).getName().equals("ASP.NET_SessionId")) {
//                cookieInfo = cookieInfo.append("ASP.NET_SessionId="+cookies.get(i).getValue());
//            }
//            if (cookies.get(i).getName().equals("Admin")) {
//                cookieInfo = cookieInfo.append(";Admin="+cookies.get(i).getValue());
//            }
//        }
        List strAndCookie = new ArrayList<String>();
        strAndCookie.add(EntityUtils.toString(response.getEntity()));
        strAndCookie.add(cookies);
        return strAndCookie;
    }

    public static String convertStreamToString(InputStream is) {
        StringBuilder sb1 = new StringBuilder();
        byte[] bytes = new byte[4096];
        int size = 0;

        try {
            while ((size = is.read(bytes)) > 0) {
                String str = new String(bytes, 0, size, "UTF-8");
                sb1.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb1.toString();
    }

}
