/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users.recommendation.mappingmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ioannisxar
 */
public class Schema {
    
    private Map<String, ArrayList<Field>> schema;
    
    public Schema(){
        this.schema = new HashMap<>();
    }
    
    public Schema(String tableName, ArrayList<Field> fields){
        this.schema = new HashMap<>();
        schema.put(tableName, fields);
    }
    
    public void addTable(String tableName, ArrayList<Field> fields){
        schema.put(tableName, fields);
    }
}
