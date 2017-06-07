/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users.recommendation.mappingmodel;

import java.util.Objects;

/**
 *
 * @author ioannisxar
 */
public class Field {
    
    private String fieldName, fieldType;
    
    public Field(){}
    
    public Field(String fieldName, String fieldType){
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }
    
    public void addFieldName(String fieldName){
        this.fieldName = fieldName;
    }
    
    public void addFieldType(String fieldType){
        this.fieldType = fieldType;
    }
    
    public String getFieldName(){
        return fieldName;
    }
    
    public String getFieldType(){
        return fieldType;
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Field){
            return fieldName.equals(((Field) o).fieldName) && fieldType.equalsIgnoreCase(((Field) o).fieldType);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.fieldName);
        hash = 67 * hash + Objects.hashCode(this.fieldType);
        return hash;
    }
}
