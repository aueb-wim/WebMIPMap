/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.mipmapgui.controller.file;

import it.unibas.spicy.model.correspondence.GetIdFromDb;
import it.unibas.spicy.persistence.AccessConfiguration;
import it.unibas.spicy.persistence.DAOException;
import it.unibas.spicy.persistence.relational.IConnectionFactory;
import it.unibas.spicy.persistence.relational.SimpleDbConnectionFactory;
import it.unibas.spicy.utility.SpicyEngineConstants;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author zannis
 */
public class ActionGetDbOffset {
    
    private String dbProperties;
    public ActionGetDbOffset(String dbProperties) {
        this.dbProperties = dbProperties;
    }
    
    public String performAction() throws SQLException, DAOException{
        String offset = "0";
        if(dbProperties != null){
            updateDbProperties();
            Connection connection = connectToDb();
            Statement statement = connection.createStatement();
            if (SpicyEngineConstants.TEMP_DB_PROPERTIES.getFunction().equalsIgnoreCase("max")){
                statement.execute("SELECT MAX(\""+ SpicyEngineConstants.TEMP_DB_PROPERTIES.getColumn() 
                        +"\") FROM \"" + SpicyEngineConstants.TEMP_DB_PROPERTIES.getTable() + "\";");
            }
            ResultSet rs = statement.getResultSet();
            if (rs.next()) {
                offset = String.valueOf(rs.getInt(1));
            }
            return offset;
        }
        return null;
    }
    
    private void updateDbProperties(){
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(dbProperties);
            String driver = (String) jsonObject.get("driver");
            String uri = (String) jsonObject.get("uri");
            String schema = (String) jsonObject.get("schema");
            String username = (String) jsonObject.get("username");
            String password = (String) jsonObject.get("password");
            String table = (String) jsonObject.get("table");
            String column = (String) jsonObject.get("column");
            String function_value = (String) jsonObject.get("function_value");
            SpicyEngineConstants.TEMP_DB_PROPERTIES = new GetIdFromDb(driver, uri, schema, username, password, table, column, function_value);
        }catch(ParseException e){
            System.err.println(e.getMessage());
        }
    }
    
    private Connection connectToDb() throws DAOException {
        AccessConfiguration accessConfiguration = new AccessConfiguration();
        accessConfiguration.setDriver(SpicyEngineConstants.TEMP_DB_PROPERTIES.getDriver());
        accessConfiguration.setUri(SpicyEngineConstants.TEMP_DB_PROPERTIES.getUri());
        if (!SpicyEngineConstants.TEMP_DB_PROPERTIES.getSchema().equals("")) {
            accessConfiguration.setSchemaName(SpicyEngineConstants.TEMP_DB_PROPERTIES.getSchema());
        }
        accessConfiguration.setLogin(SpicyEngineConstants.TEMP_DB_PROPERTIES.getLogin());
        accessConfiguration.setPassword(SpicyEngineConstants.TEMP_DB_PROPERTIES.getPassword());
        IConnectionFactory connectionFactory = new SimpleDbConnectionFactory();
        return connectionFactory.getConnection(accessConfiguration);
    }
}
