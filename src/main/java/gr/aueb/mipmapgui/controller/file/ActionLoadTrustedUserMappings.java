/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.mipmapgui.controller.file;

import java.io.File;
import java.util.Arrays;
import org.json.simple.JSONObject;

/**
 *
 * @author ioannisxar
 */
public class ActionLoadTrustedUserMappings {
    
    private JSONObject json;
    public ActionLoadTrustedUserMappings(){}
    public void performAction(String user){
        
        String[] publicSchemas = new File(MipmapDirectories.getUserPublicPath(user)).list();
        json = new JSONObject();
        json.put("public_schemas",Arrays.asList(publicSchemas));
    }
    
    public JSONObject getMappings(){
        return json;
    }
}
