/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users.recommendation;

/**
 *
 * @author ioannisxar
 */
public class ActionFindCommonMappingTasks {
    
    private String user, mappingName;
    
    public ActionFindCommonMappingTasks(String user, String mappingName){
        this.user = user;
        this.mappingName = mappingName;
    }
    
    public void findCommonScenarions(){
        System.out.println(user);
        System.out.println(mappingName);
    }
}
