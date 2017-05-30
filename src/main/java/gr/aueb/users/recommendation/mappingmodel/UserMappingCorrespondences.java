/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users.recommendation.mappingmodel;

import java.util.ArrayList;

/**
 *
 * @author ioannisxar
 */
public class UserMappingCorrespondences {
    
    private String user, mappingName;
    private ArrayList<Correspondence> correspondences;

    public UserMappingCorrespondences(String user, String mappingName, ArrayList<Correspondence> correspondences) {
        this.user = user;
        this.mappingName = mappingName;
        this.correspondences = correspondences;
    }
    
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMappingName() {
        return mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }

    public ArrayList<Correspondence> getCorrespondences() {
        return correspondences;
    }

    public void setCorrespondences(ArrayList<Correspondence> correspondences) {
        this.correspondences = correspondences;
    }
    
}
