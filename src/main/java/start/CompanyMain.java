package start;



import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.Date;

/**
 * Created by duchengyang on 2018/9/11.
 */
public class CompanyMain {
    private static Logger log = Logger.getLogger(CompanyMain.class);
    public static void main(String args[]) throws Exception {
        log.info("-------------开始----------------------");
        CompanyMain main = new CompanyMain();
        main.init();

    }

    private void init() throws Exception{
        MybTimeTask.getInstance().start();          //抓取企业信息，审核评分

    }

    public static class MybTimeTask extends TimerTask implements Runnable{

        private Timer timer = null;
        private static boolean flag = false;

        private static MybTimeTask mybTimeTask = null;

        //时间间隔
        private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

        private MybTimeTask(){

        }


        //单例模式，保持这个对象
        public static MybTimeTask getInstance(){
            if (mybTimeTask == null || flag ) {
                //当flag == true时，为了解决，timer.cancel()后，重新创建一个timer
                mybTimeTask = new MybTimeTask();
                if (flag){
                    flag = false;
                }
            }

            return mybTimeTask;
        }

        public void start() {

            Calendar calendar = Calendar.getInstance();

            /*** 定制每日8:00执行方法 ***/
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Date date=calendar.getTime(); //第一次执行定时任务的时间

            //如果第一次执行定时任务的时间 小于 当前的时间
            //此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
//            if (date.before(new Date())) {
//                date = this.addDay(date, 1);
//            }

            Timer timer = new Timer();

            //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
            timer.scheduleAtFixedRate(this,date,PERIOD_DAY);
            System.out.println("定时任务开始...............");
        }


        public void destroyed(){
            System.out.println("定时任务销毁............................");
            //终止此计时器，丢弃所有当前已安排的任务。(不但结束当前schedule，连整个Timer的线程(即当前的定时任务)都会结束掉)
            timer.cancel();
            flag = true;
        }

        // 增加或减少天数
        public Date addDay(Date date, int num) {
            Calendar startDT = Calendar.getInstance();
            startDT.setTime(date);
            startDT.add(Calendar.DAY_OF_MONTH, num);
            return startDT.getTime();
        }

        @Override
        public void run() {
            SqlSession session = openSession();
            try {
                  CloseableHttpClient httpclient= HttpClients.createDefault();
                CompanyInfoClass.getCompanyInfo(session,httpclient); //定时获取企业信息，审核评分

            }catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeSession(session);
            }
        }
    }
    private static SqlSession openSession() {
        SqlSessionFactory sessionFactory = DbUtil.getSessionFactory();
        SqlSession session = sessionFactory.openSession();
        return session;
    }
    private static void closeSession(SqlSession session) {
        try {
            session.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
