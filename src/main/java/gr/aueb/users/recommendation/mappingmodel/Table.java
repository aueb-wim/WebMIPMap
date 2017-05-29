/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users.recommendation.mappingmodel;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author ioannisxar
 */
public class Table {
    
    private ArrayList<Field> attributes;
    private String tableName;
    
    public Table(){
        this.attributes = new ArrayList<>();
    }
    
    public Table(String tableName, ArrayList<Field> attributes){
        this.attributes = new ArrayList<>();
        this.tableName = tableName;
        this.attributes.addAll(attributes);
    }
    
    public void addTable(String tableName, ArrayList<Field> attributes){
        this.attributes.addAll(attributes);
    }
    
    public ArrayList<Field> getAttributes(){
        return attributes;
    }
    
    public String getTableName(){
        return tableName;
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Table){
            if(tableName.equals(((Table) o).tableName)
                    && attributes.containsAll(((Table) o).getAttributes()) && ((Table) o).getAttributes().containsAll(attributes)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.attributes);
        hash = 11 * hash + Objects.hashCode(this.tableName);
        return hash;
    }
}
