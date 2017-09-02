package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

public class DataBase {
	
	public static Connection connection;
	public static int lastUserTime;
	
	public static void connection() {
		 if ((Options.DBdriver != null) && (Options.DBURL != null) && (Options.DBUsername != null) && (Options.DBPassword != null)) {
			 try {
				Class.forName(Options.DBdriver);
				connection = DriverManager.getConnection(Options.DBURL, Options.DBUsername, Options.DBPassword);
				lastUserTime = (int) (System.currentTimeMillis()/1000);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			 
		 }
	}
	
	
	public static void cursor() {
		if(lastUserTime + Options.DBIdleTime < System.currentTimeMillis()) {
			connection();
		}
		else {
			lastUserTime = (int) (System.currentTimeMillis()/1000);
		}
	}
	
	
	public static ResultSet executeQuery(String sql, String... params) {
		cursor();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
			for(int i=0; i<params.length; i++) {
				preparedStatement.setString(i+1, params[i]);
			}
			ResultSet resultSet = preparedStatement.executeQuery();
			return resultSet;
		} catch (SQLException e) {
		}
		return null;
	}
	
	public static int executeUpdate(String sql, String... params) {
		cursor();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
			for(int i=0; i<params.length; i++) {
				preparedStatement.setString(i+1, params[i]);
			}
			int update = preparedStatement.executeUpdate();
			return update;
		} catch (SQLException e) {
		}
		
		return 0;
	}
	
	public static Object[][] get(String sql, String... params){
		cursor();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
			
			for(int i=0; i<params.length; i++) {
				preparedStatement.setString(i+1, params[i]);
			}
			
			ResultSet resultSet = preparedStatement.executeQuery();
			ResultSetMetaData columns = resultSet.getMetaData();
			int columnCount = columns.getColumnCount();
			String[] columnName = new String[columnCount];
			int[] columnTypeName = new int[columnCount];
			for(int i=1; i<=columnCount; i++) {
				columnName[i-1] = columns.getColumnName(i);
				columnTypeName[i-1] = getColumnTypeName(columns.getColumnType(i));
			}
			
			resultSet.last();
			
			int rowCount = resultSet.getRow();
			if(rowCount < 1)
				return new Object[rowCount][columnCount];
			
			resultSet.beforeFirst();
			
			Object[][] data = new Object[rowCount][columnCount];
			int row = 0;
			while(resultSet.next()) {
				Object[] temp = new Object[columnCount];
				for(int i=0; i<columnCount; i++) {
					switch(columnTypeName[i]) {
					case 0:
						temp[i] = resultSet.getBoolean(i+1);
						break;
					case 1:
						temp[i] = resultSet.getInt(i+1);
						break;
					case 2:
						temp[i] = resultSet.getString(i+1);
						break;
					}
				}
				data[row++] = temp;
			}
			resultSet.close();
			return data;
		} catch (SQLException e) {
		}
		
		return null;
	}
	
	private static int getColumnTypeName(int typeID) {
		switch (typeID) {
		case Types.NUMERIC:
			return 1;
		case Types.BOOLEAN:
			return 0;
		default:
			return 2;
		}
	}
	
	public static Object[] find(String sql, String... params) {
        Object[][] r = get(sql, params);
        if (r.length < 1) {
            return new Object[0];
        }
        return r[0];
    }
	
	
	 public static Object getField(String sql, String... params) {
	        Object[][] r = get(sql, params);
	        if (r.length < 1) {
	            return null;
	        } else {
	            return r[0][0];
	        }

	 }
	 
	 
	 public static int update(String sql, String... params) {
		 try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
			 for(int i=0; i<params.length; i++)
				 preparedStatement.setString(i+1, params[i]);
			 int update = preparedStatement.executeUpdate();
			 return update;
		} catch (SQLException e) {
			
		}
		 return 0;
	 }
	 
	 public static int insert(String sql, String...params) {
		 return update(sql, params);
	 }
	 
	 public static int delete(String sql, String... params) {
		 return delete(sql, params);
	 }
	 
	 public static String getSql(String sql, String... params) {
		 for(int i=0; i<params.length; i++)
			 sql = sql.replaceFirst("\\?", params[i]);
		 return sql;
	 }
	 
	 
	 
	 
	 
	
	
	
	

}
