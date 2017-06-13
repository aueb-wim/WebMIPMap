/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users;

import gr.aueb.context.ApplicationContextProvider;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author ioannisxar
 */
public class ActionDeleteTrustedUser {
    
    private JSONObject object = new JSONObject();
    private JdbcTemplate jdbcTemplate = (JdbcTemplate) ApplicationContextProvider.getApplicationContext().getBean("jdbcTemplate");
    
    public void performAction(String user, String id) {
        jdbcTemplate.query(
            "SELECT id, username "
            + "FROM mipmapuser "
            + "WHERE username = ? ", new Object[] { user },
            (rs, rowNum) -> new MipMapUser(rs.getInt("id"), rs.getString("username"))
        ).stream().forEach((mipMapUser) -> 
            {                
                int fromId = mipMapUser.getId();
                int toId = Integer.valueOf(id);
                
                //send to db
                int row = jdbcTemplate.update("DELETE FROM user_user WHERE  \"userA\" = ? AND \"userB\" = ? "
                        , new Object[] { toId, fromId });                
                if (row==1) {
                    object.put("msg", "The selected user deleted successfully!");
                } else {
                    object.put("msg", "An error occured!");
                }
            });
    }
    
    public JSONObject getMessage(){
        return this.object;
    }
    
}
