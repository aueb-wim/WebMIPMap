/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users.recommendation;

import gr.aueb.context.ApplicationContextProvider;
import gr.aueb.users.recommendation.mappingmodel.Correspondence;
import gr.aueb.users.recommendation.mappingmodel.Field;
import gr.aueb.users.recommendation.mappingmodel.Schema;
import gr.aueb.users.recommendation.mappingmodel.Table;
import gr.aueb.users.recommendation.mappingmodel.UserMappingCorrespondences;
import it.unibas.spicy.persistence.DAOException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author ioannisxar
 */
public class GetCorrespondenceScore {
    
    private ArrayList<UserMappingCorrespondences> umc;
    private JdbcTemplate jdbcTemplate = (JdbcTemplate) ApplicationContextProvider.getApplicationContext().getBean("jdbcTemplate");
    private OpenMappingScenario scenario;
    
    
    public GetCorrespondenceScore(ArrayList<UserMappingCorrespondences> umc, OpenMappingScenario scenario){
        this.umc = umc;
        this.scenario = scenario;
    }
    
    public void performAction() throws DAOException, IOException{
        //feature - users' Pagerank
        HashMap<String, Double> usersPRank = getUsersPagerank();
        //feature - users' Credibility
        HashMap<String, Double> usersCredibility = getUsersCredibility();
        //feature - users' Total Connection normalized
        HashMap<String, Double> usersTotalConnectionsNormalized = getUsersTotalConnectionsNormalized();
        //TODO: feature - users' Average Connections Per Mapping Scenario
        //HashMap<String, Double> usersAverageMappings = getUsersAverageMappings();
        
        // add empty correspondences
        ArrayList<UserMappingCorrespondences> umcWithEmpty = new ArrayList<>();
        for(UserMappingCorrespondences u: umc){
            u.addCorrespondences(addEmptyCorrespondences(u.getCorrespondences(), scenario.getScenarioSchema("target", "public")));
            umcWithEmpty.add(u);
        } 
    }
    
    private HashMap<String, Double> getUsersPagerank(){
        DirectedGraph<String, DefaultEdge> directedGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
        HashMap<String, String> userIds = new HashMap<>();
        jdbcTemplate.query(
            "SELECT \"id\", \"username\" "
            + "FROM mipmapuser;",
            (rs, rowNum) ->  new Object[] { rs.getString("id"), rs.getString("username") }
        ).stream().forEach((user) -> { 
            userIds.put(String.valueOf(user[0]), String.valueOf(user[1]));
        });
        userIds.forEach((id, user)->{
            directedGraph.addVertex(user);
        });
        jdbcTemplate.query(
            "SELECT \"userA\", \"userB\" "
            + "FROM user_user "
            + "WHERE status = 1;",
            (rs, rowNum) ->  new Object[] { rs.getString("userA"), rs.getString("userB") }
        ).stream().forEach((user) -> { 
            directedGraph.addEdge(userIds.get(user[1]), userIds.get(user[0])); 
        });
        
        PageRank a = new PageRank(directedGraph);
        HashMap<String, Double> usersPRank = new HashMap<>();
        Set<String> users = new HashSet<>();
        umc.forEach((umcObject)->{
            users.add(umcObject.getUser());
        });
        for(String user: users){
            usersPRank.put(user, a.getVertexScore(user));
        }
        return usersPRank;
    }
    
    //TODO
    private HashMap<String, Double> getUsersAverageMappings(){
        HashMap<String, Double> usersAverageMappings = new HashMap<>();
        umc.forEach((corrList) -> {
            System.out.println(corrList.getUser());
            System.out.println(corrList.getMappingName());
            System.out.println(corrList.getCorrespondences().size());
        });
        return usersAverageMappings;
    }
    
    private HashMap<String, Double> getUsersCredibility(){
        HashMap<String, Double> usersCredibility = new HashMap<>();
        jdbcTemplate.query(
            "SELECT \"username\", \"mappings_accepted\", \"mappings_total\" "
            + "FROM mipmapuser;",
            (rs, rowNum) ->  new Object[] { rs.getString("username"), rs.getString("mappings_accepted"), rs.getString("mappings_total") }
        ).stream().forEach((obj) -> { 
            usersCredibility.put(String.valueOf(obj[0]), (double)Integer.parseInt(String.valueOf(obj[1]))/Integer.parseInt(String.valueOf(obj[2])));
        });
        return usersCredibility;
    }
    
    private HashMap<String, Double> getUsersTotalConnectionsNormalized(){
        HashMap<String, Double> usersTotalConnectionsNormalized = new HashMap<>(); 
        String query = "SELECT min(\"mappings_total\") as min, max(\"mappings_total\") as max FROM mipmapuser;";
        Map<String, Object> row = jdbcTemplate.queryForMap(query);
        int min = Integer.parseInt(String.valueOf(row.get("min")));
        int max = Integer.parseInt(String.valueOf(row.get("max")));
        jdbcTemplate.query(
            "SELECT \"username\", \"mappings_total\" "
            + "FROM mipmapuser;",
            (rs, rowNum) ->  new Object[] { rs.getString("username"), rs.getString("mappings_total") }
        ).stream().forEach((obj) -> {
            double score = (double)(Integer.valueOf(String.valueOf(obj[1])) - min)/(double)(max-min);
            usersTotalConnectionsNormalized.put(String.valueOf(obj[0]), score);
        });
        return usersTotalConnectionsNormalized;
    }

    private ArrayList<Correspondence> addEmptyCorrespondences(ArrayList<Correspondence> correspondences, Schema schema) throws DAOException, IOException {
        ArrayList<Correspondence> newCorrespondences = new ArrayList<>();
        System.out.println("------- ATTRIBUTES -------");
        for(String s: schema.getTableAttributeNames()){
            System.out.println(s);
        }
        System.out.println("------- ATTRIBUTES -------");
        System.out.println("------- CORRESPONDENCES -------");
        for(Correspondence corr: correspondences){
            System.out.println(corr.getTarget());
        }
        System.out.println("------- CORRESPONDENCES -------");
        return newCorrespondences;
    }
}
