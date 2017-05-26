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
      
}
