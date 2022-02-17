package com.tl.smbms.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author tl
 * 公共类
 * 数据库操作
 */
//操作数据库的公共类
public class BaseDao {
    private static String DRIVER;
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    //静态代码块同类初始化，在类加载的时候就初始化
    static {
        //创建Properties对象
        Properties properties = new Properties();
        //获取类加载器
        ClassLoader loader = BaseDao.class.getClassLoader();
        //通过类加载器加载资源文件为字节输入流
        InputStream in = loader.getResourceAsStream("db.properties");

        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取db.properties资源
        DRIVER = properties.getProperty("DRIVER");
        URL = properties.getProperty("URL");
        USERNAME = properties.getProperty("USERNAME");
        PASSWORD = properties.getProperty("PASSWORD");
    }
    //编写获取数据库的连接对象的公共方法
    public static Connection getConnection(){
        Connection connection = null;
        try {
            //DRIVER=com.mysql.jdbc.Driver
            Class.forName(DRIVER);//通过反射加载jdbc驱动
            //通过DriverManager获取连接数据库的对象
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
    //编写查询表的公共方法
    /**
     *
     * @param connection    连接数据库的对象
     * @param sql           编写MySQL语句
     * @param param         数组
     * @param preparedStatement     预编译对象  通过该对象执行MySQL语句
     * @param resultSet     结果集
     * @return              返回结果集
     */
    public static ResultSet execute(Connection connection, String sql, Object[] param,
                                    PreparedStatement preparedStatement, ResultSet resultSet){
        try {
            preparedStatement = connection.prepareStatement(sql);
            //setObject,占位符从1开始，参数下标从0开始
            for (int i = 0; i < param.length ; i++) {
                preparedStatement.setObject(i+1,param[i]);
            }
            //以结果集的形式返回
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }


    //编写修改(增添改)表的公共方法
    //重写execeute()方法
    /**
     *
     * @param connection    连接数据库的对象
     * @param sql           编写MySQL语句
     * @param param         数组
     * @param preparedStatement     预编译对象  通过该对象执行MySQL语句
     * @return              返回修改次数
     */
    public static int execute(Connection connection,String sql,Object[] param,
                               PreparedStatement preparedStatement){
        int resultNum = 0;
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < param.length ; i++) {
                preparedStatement.setObject(i+1,param[i]);
            }
            resultNum = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultNum;
    }
    //关闭资源的公共方法
    public static boolean close(Connection connection,PreparedStatement statement,ResultSet resultSet){
        boolean flag = true;
        if (resultSet != null){
            try {
                resultSet.close();
                //垃圾回收的操作
                resultSet = null;
            } catch (SQLException e) {
                e.printStackTrace();
                //如果没有释放成功
                flag = false;
            }
        }
        if (statement != null){
            try {
                statement.close();
                statement = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if (connection != null){
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }
}
