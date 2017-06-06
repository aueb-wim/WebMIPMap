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
public class MappingScenario {
    
    private String userName, mappingTaskName;
    private Schema source, target;
    
    public MappingScenario(String userName, String mappingTaskName, Schema source, Schema target) {
        this.userName = userName;
        this.mappingTaskName = mappingTaskName;
        this.source = source;
        this.target = target;
    }
    
    public String getUserName() {
        return userName;
    }

    public String getMappingTaskName() {
        return mappingTaskName;
    }

    public Schema getSource() {
        return source;
    }

    public Schema getTarget() {
        return target;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMappingTaskName(String mappingTaskName) {
        this.mappingTaskName = mappingTaskName;
    }

    public void setSource(Schema source) {
        this.source = source;
    }

    public void setTarget(Schema target) {
        this.target = target;
    }
      
    @Override
    public boolean equals(Object o){
        if(o instanceof MappingScenario){
            if(userName.equals(((MappingScenario) o).getUserName()) && mappingTaskName.equals(((MappingScenario) o).getMappingTaskName())){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.userName);
        hash = 89 * hash + Objects.hashCode(this.mappingTaskName);
        hash = 89 * hash + Objects.hashCode(this.source);
        hash = 89 * hash + Objects.hashCode(this.target);
        return hash;
    }
    
    
    public void printMappingScenario(){
        System.out.println("--------------------------------");
        System.out.println(getUserName());
        System.out.println(getMappingTaskName());
        getSource().printSchema();
        getTarget().printSchema();
        System.out.println("--------------------------------");
    }
    
}
