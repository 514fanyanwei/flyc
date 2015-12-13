package cc.util.db;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Properties;
import java.util.UUID;

import javax.sql.DataSource;

import sun.util.logging.resources.logging;

import cc.gps.data.Recourse;

import com.alibaba.druid.pool.DruidDataSourceFactory;


/**
 * The Class DataSourceUtil.
 */
public class DataSourceUtil {

    /** 使用配置文件构建Druid数据源. */
    public static final int DRUID_MYSQL_SOURCE = 0;

    /** 使用配置文件构建Druid数据源. */
    public static final int DRUID_MYSQL_SOURCE2 = 1;

    /** 使用配置文件构建Dbcp数据源. */
    public static final int DBCP_SOURCE = 4;
    public static String confile = "druid.properties";
    public static Properties p = null;
    
    private static DataSource dataSource = null;
    private static DataSourceUtil dsu=null;
    
    static {
        p = new Properties();
        InputStream inputStream = null;
        try {
            //java应用
        	/*
        	System.out.println(DataSourceUtil.class.getClassLoader().getResource("").getPath()+"*******" );
            confile = DataSourceUtil.class.getClassLoader().getResource("").getPath() + confile;
            System.out.println(confile);
            File file = new File(confile);
            inputStream = new BufferedInputStream(new FileInputStream(file));*/
            inputStream=Recourse.class.getClassLoader().getResourceAsStream(confile);
            p.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据类型获取数据源
     * 
     * @param sourceType
     *            数据源类型
     * @return druid或者dbcp数据源
     * @throws Exception
     *             the exception
     */
    public static final DataSource initDataSource(int sourceType) throws Exception {
       
        switch (sourceType) {
        case DRUID_MYSQL_SOURCE:
            dataSource = DruidDataSourceFactory.createDataSource(p);
            break;
        case DRUID_MYSQL_SOURCE2:
            dataSource = DruidDataSourceFactory.createDataSource(p);
            break;
        case DBCP_SOURCE:
            // dataSource = BasicDataSourceFactory.createDataSource(
            // MySqlConfigProperty.getInstance().getProperties());
            break;
        }
        return dataSource;
    }
    public synchronized static Connection getConn(){
    	Connection conn=null;
    	if(dataSource==null){
    		try {
    			dataSource=DataSourceUtil.initDataSource(DRUID_MYSQL_SOURCE);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	try {
			conn=dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return conn;
    }
    
    public static int insertRow(String tablename, Hashtable fieldsvalue,String remove)
    {
    	ResultSet rs = null;
    	Statement stmt = null;
    	Connection conn=getConn();
    	if (conn == null) {
    		return -1;
    	}
    //	DBQuery myQuery=new DBQuery(cm, conn); 
    	//myQuery.charset = cm.getCharset();
        try
        {
        	stmt = conn.createStatement();
        	rs = stmt.executeQuery("select top 1 * from "+tablename+" where 1=2");			     
    		ResultSetMetaData  rsmt=rs.getMetaData();         
    		String names = null; //插入字段列表
    		String values = null;  //插入字段值列表
    		int ColumnCount=rsmt.getColumnCount();         
    		for (int i=0; i<ColumnCount; i++)
    		{
    			String fieldname = rsmt.getColumnName(i+1).toLowerCase();	 //当前字段名  
    			if (remove.indexOf(fieldname)!=-1)  
    				continue;    
    			String fvalue = (String)fieldsvalue.get(fieldname); //当前字段值
    			String ftype = rsmt.getColumnTypeName(i+1).toLowerCase();//				
    			if (names == null)
    			{
    				names = fieldname;
    				if (ftype.equals("varchar") || ftype.equals("text") || ftype.equals("char") || ftype.equals("varchar2"))
    					values = "'"+PubFunc.toSqlStr(fvalue)+"'";
    				else
    					values = ( (fvalue==null || fvalue.trim().equals(""))?"null":fvalue);
    			}
    			else
    			{
    				names += ", "+fieldname; 
    				if (ftype.equals("varchar") || ftype.equals("text") || ftype.equals("char")	|| ftype.equals("varchar2")||ftype.equals("datetime")||ftype.equals("image"))
    				//if (ftype.equals("varchar") || ftype.equals("text") || ftype.equals("char")	|| ftype.equals("varchar2")||ftype.equals("datetime"))
    					values += ", '"+PubFunc.toSqlStr(fvalue)+"'";
    				else
    					values += ", "+( (fvalue==null || fvalue.trim().equals(""))?"null":fvalue);
    			}
    		}
    		if (names != null)
    		{
    			String strSql = "insert into "+tablename+"("+names+") values ("+values+")";
    			//log.error(strSql);
    			PreparedStatement pst=conn.prepareStatement(strSql);
    			return pst.executeUpdate();
    		}
         }
        catch(Exception e)
        {
          System.out.println("执行表插入时出错:"+e.getMessage());
        }
        finally
        {
        	try
        	{
    			if (rs != null)
    			{
    				rs.close();
    				rs = null;
    			}
    			if (stmt != null)
    			{
    				stmt.close();
    				stmt = null;
    			}
    			conn.close();
    		}
    		catch(Exception e)	
    		{
    			System.out.println("关闭结果集出错:"+e.getMessage());	
    		}
        }	 
    	return -1;	  
    }
    
    public static void main(String args[]){
    	Connection con=DataSourceUtil.getConn();
    	String sql="select * from company";
		PreparedStatement pst=null;

		String uid=UUID.randomUUID().toString();
		try{
			pst=con.prepareStatement(sql);
			ResultSet rs=pst.executeQuery();
			while(rs.next()){
				System.out.println(rs.getString("cname"));
			}
		}catch(Exception e){e.printStackTrace();}
		finally{
			
			try{
				if(pst!=null) pst.close();
				con.close();
			}catch(Exception e2){e2.printStackTrace();}
			
		}
    }
}
