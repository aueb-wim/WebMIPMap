/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users.recommendation.mappingmodel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author ioannisxar
 */
public class Table {
    
    private HashMap<String, ArrayList<Field>> table;
    
    public Table(){
        this.table = new HashMap<>();
    }
    
    public Table(String tableName, ArrayList<Field> fields){
        this.table = new HashMap<>();
        table.put(tableName, fields);
    }
    
    public void addTable(String tableName, ArrayList<Field> fields){
        table.put(tableName, fields);
    }
    
    public HashMap<String, ArrayList<Field>> getTableName(){
        return table;
    }
}
