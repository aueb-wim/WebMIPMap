/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users.recommendation.mappingmodel;

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
}
