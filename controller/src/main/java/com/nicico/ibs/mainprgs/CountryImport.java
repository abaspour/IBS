package com.nicico.ibs.mainprgs;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

public class CountryImport {

	public static void main(String[] args) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@xxx:1521:orcl1", "xx", "xx");
/*
			"jdbc:oracle:thin:@//xxxx:1521/pdb_dev01"
*/
			PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO tbl_country (id,country_calling_code,country_code,country_name_en," +
							"      country_name_local,currency_code,currency_name_en,flag,official_language_name_en,official_language_name_local,region,c_created_by,d_created_date,n_version) \n" +
							"      VALUES (SEQ_COUNTRY_ID.nextval,?,?,?,?,?,?,?,?,?,?,'admin',sysdate,0) ");

			InputStream stream = CountryImport.class.getResourceAsStream("/country.json");
			List<Map> list = mapper.readValue(stream, List.class);
			for (Map map : list) {
				System.out.println("map = " + map);
				statement.setObject(1, map.get("countryCallingCode"));
				statement.setObject(2, map.get("countryCode"));
				statement.setObject(3, map.get("countryNameEn"));
				statement.setObject(4, map.get("countryNameLocal"));
				statement.setObject(5, map.get("currencyCode"));
				statement.setObject(6, map.get("currencyNameEn"));
				statement.setObject(7, map.get("flag"));
				statement.setObject(8, map.get("officialLanguageNameEn"));
				statement.setObject(9, map.get("officialLanguageNameLocal"));
				statement.setObject(10, map.get("region"));
				statement.executeUpdate();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
