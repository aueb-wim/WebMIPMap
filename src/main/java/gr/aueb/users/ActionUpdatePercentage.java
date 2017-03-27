package gr.aueb.users;

import gr.aueb.context.ApplicationContextProvider;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;


public class ActionUpdatePercentage {
    private JdbcTemplate jdbcTemplate = (JdbcTemplate) ApplicationContextProvider.getApplicationContext().getBean("jdbcTemplate");

    public ActionUpdatePercentage() {}
    
    public void performAction(String trustedUser, int acceptedConnections, int totalConnections){
        try {
            //get user and his/her score
            MipMapUser mipMapUser = jdbcTemplate.queryForObject(
                "SELECT id, username, score, mappings_accepted, mappings_total "
                + "FROM mipmapuser "
                + "WHERE username = ? ", new Object[] { trustedUser },
                (rs, rowNum) -> new MipMapUser(rs.getInt("id"), rs.getString("username"), rs.getDouble("score"), rs.getInt("mappings_accepted"), rs.getInt("mappings_total"))
            );
            
            //calculate new score plus its parameters
            int mappingsAcceptedNew = mipMapUser.getMappingsAccepted() + acceptedConnections;
            int mappingsTotalNew = mipMapUser.getMappingsTotal() + totalConnections;
            double scoreNew = 100 *( (double)mappingsAcceptedNew / (double)mappingsTotalNew);
            
            System.out.println("Previous score: " + mipMapUser.getScore() + "% (" + mipMapUser.getMappingsAccepted() + " out of " + mipMapUser.getMappingsTotal() + ")");            
            System.out.println("New score: " + scoreNew + "% (" + mappingsAcceptedNew + " out of " + mappingsTotalNew + ")");
            //update database entry
            jdbcTemplate.update(
                "UPDATE mipmapuser "
                + "SET mappings_accepted = ?,  mappings_total = ?, score = ? "
                + "where username = ?",
                mappingsAcceptedNew, mappingsTotalNew, scoreNew, trustedUser
            );
        } catch (EmptyResultDataAccessException e) {
            
        }
    }
}
