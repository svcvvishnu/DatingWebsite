import java.applet.Applet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FirstSampleServletClass extends HttpServlet {

    public void init() {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        JSONObject result = new JSONObject();
        String action = req.getParameter(ApplicationConstants.ACTION);
        
        System.out.println("Action:" + action);
        switch (action){
            case ApplicationConstants.FETCH_ALL_USERS:
                result = getAllUsers(req);
                break;
            case ApplicationConstants.FETCH_PSS_REQ_USERS:
                result = getPassReqUsers();
                break;
            case ApplicationConstants.IS_REQUESTED:
                 try {
                        result.put(ApplicationConstants.IS_REQUESTED,isRequested(req));
                     } catch (JSONException ex) {
                        Logger.getLogger(FirstSampleServletClass.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("Status data:"+ result);
                break;
            case ApplicationConstants.RELATED_USERS:
                result = getRelatedUsers(req);
                break;
            case ApplicationConstants.FETCH_USER:
                result = getUser(req);
                break;
            case ApplicationConstants.GET_STATUS:
                {
                    try {
                        result.put(ApplicationConstants.HAS_REQUEST,hasRequest(req));
                        result.put(ApplicationConstants.IS_FRIEND,isFriend(req));
                        result.put(ApplicationConstants.ALREADY_REQUESTED,alreadyRequested(req));
                    } catch (JSONException ex) {
                        Logger.getLogger(FirstSampleServletClass.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("Status data:"+ result);
                }
                break;
        }

        System.out.println("Result:"+ result);
        PrintWriter writer = res.getWriter();
        writer.write(result.toString());
        writer.close();
    }



    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String action = req.getParameter(ApplicationConstants.ACTION);
        JSONObject result = new JSONObject();
        switch(action){
            case ApplicationConstants.CREATE:
                result = createMember(req);
                break;
            case ApplicationConstants.ACCEPT:
                result = acceptRequest(req);
                break;
            case ApplicationConstants.REJECT:
                result = rejectRequest(req);
                break;
            case ApplicationConstants.SEND_REQUEST:
                result = sendRequest(req);
                break;
            case ApplicationConstants.UPDATE_USER:
                result = updateUser(req);
                break;
            case ApplicationConstants.EMAIL_REQUEST:
                result = emailRequest(req);
                break;
        }
        PrintWriter writer = res.getWriter();
        writer.write(result.toString());
        writer.close();

    }

    private JSONObject getUser(HttpServletRequest req) {
        String currentUserName = req.getParameter(ApplicationConstants.USER_NAME);
        
        JSONObject result = new JSONObject();
        System.out.println("UserName:" + currentUserName);
        if(currentUserName.equals("Administrator")){
            System.out.println("INside: Admin");
            try {
                result.put(ApplicationConstants.USER_NAME,currentUserName);
                result.put(ApplicationConstants.PASSWORD,currentUserName);
                result.put("admin",true);
            } catch (JSONException ex) {
                System.out.println("Exception" + ex);
                Logger.getLogger(FirstSampleServletClass.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Returning");
            return result;
        }else{
        System.out.println("Continuing");
        String selectQuery = "SELECT  FIRST_NAME,LAST_NAME," +
                "USER_NAME,EMAIL_ID,PASSWORD,SEX,AGE,COUNTRY,ADDRESS FROM DATING_USERS WHERE USER_NAME = '"+currentUserName+"'";
        ResultSet resultSet = PostgresSQLConnection.executeQuery(selectQuery);
        try {
            assert resultSet != null;
            if(resultSet.next()){
                int columnIndex = 1;
                result.put(ApplicationConstants.FIRST_NAME,resultSet.getString(columnIndex++));
                result.put(ApplicationConstants.LAST_NAME,resultSet.getString(columnIndex++));
                result.put(ApplicationConstants.USER_NAME,resultSet.getString(columnIndex++));
                result.put(ApplicationConstants.EMAIL_ID,resultSet.getString(columnIndex++));
                result.put(ApplicationConstants.PASSWORD,resultSet.getString(columnIndex++));
                result.put(ApplicationConstants.SEX,resultSet.getString(columnIndex++));
                result.put(ApplicationConstants.AGE,resultSet.getString(columnIndex++));
                result.put(ApplicationConstants.COUNTRY,resultSet.getString(columnIndex++));
                result.put(ApplicationConstants.ADDRESS,resultSet.getString(columnIndex));
                System.out.println(result.toString());
            }
        } catch (SQLException | JSONException e) {
            e.printStackTrace();
        }}
        return result;
    }

    private JSONObject getAllUsers(HttpServletRequest req) {
        String currentUserName = req.getParameter(ApplicationConstants.USER_NAME);
        
        JSONObject result = new JSONObject();
        try {
            result.put("allUsers",getUsers(currentUserName));
            //result.put("acceptedUsers",getAcceptedUsers(currentUserName));
            //result.put("rejectedUsers",getRejectedUsers(currentUserName));
            //result.put("pendingUsers",getPendingUsers(currentUserName));
        } catch (JSONException | SQLException ex) {
            Logger.getLogger(FirstSampleServletClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private JSONObject rejectRequest(HttpServletRequest req) {
        String currentUserName = req.getParameter(ApplicationConstants.USER_NAME);
        String profileUserName = req.getParameter(ApplicationConstants.PROFILE_USER_NAME);
        JSONObject result = new JSONObject();
        try {
            result.put(ApplicationConstants.SUCCESS,false);
            String updateQuery = "UPDATE USER_PROFILE_REQUEST SET ACCEPTANCE = 2" +
                    "  WHERE PROF_USER_NAME = '"+currentUserName+"' AND USER_NAME = '"+profileUserName+"'";
            PostgresSQLConnection.executeUpdate(updateQuery);
            result.put(ApplicationConstants.SUCCESS,Boolean.TRUE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private JSONObject acceptRequest(HttpServletRequest req) {

        String currentUserName = req.getParameter(ApplicationConstants.USER_NAME);
        String profileUserName = req.getParameter(ApplicationConstants.PROFILE_USER_NAME);
        JSONObject result = new JSONObject();
        try {
            result.put(ApplicationConstants.SUCCESS,false);
            System.out.println("Inside Reject Request");
            String updateQuery = "UPDATE USER_PROFILE_REQUEST SET ACCEPTANCE = 1" +
                    "  WHERE PROF_USER_NAME = '"+currentUserName+"' AND USER_NAME = '"+profileUserName+"'";
            PostgresSQLConnection.executeUpdate(updateQuery);
            result.put(ApplicationConstants.SUCCESS,Boolean.TRUE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private JSONObject createMember(HttpServletRequest req) {
        JSONObject result = new JSONObject();
        try {
            JSONObject user = new JSONObject(req.getParameter(ApplicationConstants.USER));
            result.put(ApplicationConstants.SUCCESS,false);

            String firstName = user.getString(ApplicationConstants.FIRST_NAME);
            String lastName = user.getString(ApplicationConstants.LAST_NAME);
            String userName = user.getString(ApplicationConstants.USER_NAME);
            String emailId = user.getString(ApplicationConstants.EMAIL_ID);
            String password = user.getString(ApplicationConstants.PASSWORD);

            String selectQuery = "INSERT INTO DATING_USERS(FIRST_NAME,LAST_NAME,USER_NAME,EMAIL_ID,PASSWORD) \n" +
                    "VALUES('"+firstName+"','"+lastName+"','"+userName+"','"+emailId+"','"+password+"')";

            PostgresSQLConnection.executeUpdate(selectQuery);
            result.put(ApplicationConstants.SUCCESS,true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private JSONObject sendRequest(HttpServletRequest req) {
        String currentUserName = req.getParameter(ApplicationConstants.USER_NAME);
        String profileUserName = req.getParameter(ApplicationConstants.PROFILE_USER_NAME);

        JSONObject result = new JSONObject();
        try{
            result.put(ApplicationConstants.SUCCESS,Boolean.FALSE);
            String selectQuery = "INSERT INTO USER_PROFILE_REQUEST(USER_NAME,PROF_USER_NAME,ACCEPTANCE) \n" +
                    "VALUES('"+currentUserName+"','"+profileUserName+"',0)";

            PostgresSQLConnection.executeUpdate(selectQuery);
            result.put(ApplicationConstants.SUCCESS,Boolean.TRUE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;

    }

    private boolean isFriend(HttpServletRequest req) {
        String currentUserName = req.getParameter(ApplicationConstants.USER_NAME);
        String profileUserName = req.getParameter(ApplicationConstants.PROFILE_USER_NAME);

        String selectQuery = "SELECT COUNT(1) FROM USER_PROFILE_REQUEST WHERE ACCEPTANCE = 1" +
                " AND  USER_NAME = '"+currentUserName+"' AND PROF_USER_NAME = '"+profileUserName+"'";
        ResultSet resultSet = PostgresSQLConnection.executeQuery(selectQuery);
        String secondQuery = "SELECT COUNT(1) FROM USER_PROFILE_REQUEST WHERE ACCEPTANCE = 1" +
                " AND  USER_NAME = '"+profileUserName+"' AND PROF_USER_NAME = '"+currentUserName+"'";
        ResultSet secondResultSet = PostgresSQLConnection.executeQuery(secondQuery);
        
        try {
            assert resultSet != null;
            assert secondResultSet != null;
            if((resultSet.next() && resultSet.getInt(1) == 1) || (secondResultSet.next() && secondResultSet.getInt(1) == 1) )
                return true;
                
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean hasRequest(HttpServletRequest req) {
        String currentUserName = req.getParameter(ApplicationConstants.USER_NAME);
        String profileUserName = req.getParameter(ApplicationConstants.PROFILE_USER_NAME);
        String selectQuery = "SELECT COUNT(1) FROM USER_PROFILE_REQUEST WHERE ACCEPTANCE = 0" +
                " AND USER_NAME = '"+profileUserName+"' AND PROF_USER_NAME = '"+currentUserName+"'";
        ResultSet resultSet = PostgresSQLConnection.executeQuery(selectQuery);
        
        try {
            assert resultSet != null;
            if(resultSet.next() && resultSet.getInt(1) == 1)
                return true;
                
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void destroy() {
    }

    private JSONArray getUsers(String currentUserName) throws JSONException, SQLException {
        String selectQuery = "SELECT  FIRST_NAME,LAST_NAME, "
                + "USER_NAME,EMAIL_ID,PASSWORD,AGE,SEX,COUNTRY,ADDRESS FROM DATING_USERS WHERE USER_NAME != '"+currentUserName+"'";
        ResultSet resultSet = PostgresSQLConnection.executeQuery(selectQuery);
        JSONArray userArray = new JSONArray();
        assert resultSet != null;
            while(resultSet.next()){
                JSONObject user = new JSONObject();
                int columnIndex = 1;
                user.put(ApplicationConstants.FIRST_NAME,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.LAST_NAME,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.USER_NAME,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.EMAIL_ID,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.PASSWORD,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.AGE,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.SEX,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.COUNTRY,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.ADDRESS,resultSet.getString(columnIndex));
                userArray.put(user);
            }
        return userArray;
    }

    private JSONArray getAcceptedUsers(String currentUserName) throws SQLException, JSONException {
         String selectQuery = "select prof_user.FIRST_NAME,prof_user.LAST_NAME,prof_user.USER_NAME,prof_user.EMAIL_ID,prof_user.PASSWORD,\n" +
                "prof_user.AGE,prof_user.SEX,prof_user.COUNTRY,prof_user.ADDRESS from  DATING_USERS prof_user,USER_PROFILE_REQUEST request\n" +
                "where request.acceptance = 1 and  request.user_name = '"+currentUserName+"'\n" +
                "and prof_user.user_name = request.prof_user_name";
        ResultSet resultSet = PostgresSQLConnection.executeQuery(selectQuery);
        JSONArray userArray = new JSONArray();
        assert resultSet != null;
            while(resultSet.next()){
                JSONObject user = new JSONObject();
                int columnIndex = 1;
                user.put(ApplicationConstants.FIRST_NAME,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.LAST_NAME,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.USER_NAME,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.EMAIL_ID,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.PASSWORD,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.AGE,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.SEX,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.COUNTRY,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.ADDRESS,resultSet.getString(columnIndex));
                userArray.put(user);
            }
            System.out.println("Accepted Users:" + userArray);
        return userArray;
    }

    private JSONArray getRejectedUsers(String currentUserName) throws SQLException, JSONException {
        String selectQuery = "select prof_user.FIRST_NAME,prof_user.LAST_NAME,prof_user.USER_NAME,prof_user.EMAIL_ID,prof_user.PASSWORD,\n" +
                "prof_user.AGE,prof_user.SEX,prof_user.COUNTRY,prof_user.ADDRESS from  DATING_USERS prof_user,USER_PROFILE_REQUEST request\n" +
                "where request.acceptance = 2 and  request.user_name = '"+currentUserName+"'\n" +
                "and prof_user.user_name = request.prof_user_name";
        ResultSet resultSet = PostgresSQLConnection.executeQuery(selectQuery);
        JSONArray userArray = new JSONArray();
        assert resultSet != null;
            while(resultSet.next()){
                JSONObject user = new JSONObject();
                int columnIndex = 1;
                user.put(ApplicationConstants.FIRST_NAME,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.LAST_NAME,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.USER_NAME,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.EMAIL_ID,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.PASSWORD,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.AGE,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.SEX,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.COUNTRY,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.ADDRESS,resultSet.getString(columnIndex));
                userArray.put(user);
            }
        return userArray;
    }

    private JSONArray getPendingUsers(String currentUserName) throws SQLException, JSONException {
         String selectQuery = "select prof_user.FIRST_NAME,prof_user.LAST_NAME,prof_user.USER_NAME,prof_user.EMAIL_ID,prof_user.PASSWORD,\n" +
                "prof_user.AGE,prof_user.SEX,prof_user.COUNTRY,prof_user.ADDRESS from  DATING_USERS prof_user,USER_PROFILE_REQUEST request\n" +
                "where request.acceptance = 0 and  request.prof_user_name = '"+currentUserName+"'\n" +
                "and prof_user.user_name = request.user_name";
        ResultSet resultSet = PostgresSQLConnection.executeQuery(selectQuery);
        JSONArray userArray = new JSONArray();
        System.out.println("Start Pending Requests");
        assert resultSet != null;
            while(resultSet.next()){
                System.out.println("Got Pending Requests");
                JSONObject user = new JSONObject();
                int columnIndex = 1;
                user.put(ApplicationConstants.FIRST_NAME,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.LAST_NAME,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.USER_NAME,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.EMAIL_ID,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.PASSWORD,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.AGE,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.SEX,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.COUNTRY,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.ADDRESS,resultSet.getString(columnIndex));
                userArray.put(user);
            }
        return userArray;
    }

    private JSONObject updateUser(HttpServletRequest req) {
        JSONObject result = new JSONObject();
        
        try {
            JSONObject user =new JSONObject(req.getParameter("user"));
            String[] userParams = JSONObject.getNames(user);
            result.put(ApplicationConstants.SUCCESS,Boolean.FALSE);
            HashMap<String,String> updateValues = new HashMap<String, String>();
            updateValues.put(ApplicationConstants.FIRST_NAME, "");
            updateValues.put(ApplicationConstants.LAST_NAME, "");
            updateValues.put(ApplicationConstants.USER_NAME, "");
            updateValues.put(ApplicationConstants.EMAIL_ID, "");
            updateValues.put(ApplicationConstants.PASSWORD, "");
            updateValues.put(ApplicationConstants.AGE, "");
            updateValues.put(ApplicationConstants.SEX, "");
            updateValues.put(ApplicationConstants.COUNTRY, "");
            updateValues.put(ApplicationConstants.ADDRESS, "");
            for(int i=0; i<userParams.length ; i++){
                updateValues.put(userParams[i],user.getString(userParams[i]));
            }
            System.out.println("Inside Update User");
            String updateUserQuery = "UPDATE DATING_USERS SET FIRST_NAME = '"+ updateValues.get(ApplicationConstants.FIRST_NAME)+"' ," +
                    "LAST_NAME = '"+ updateValues.get(ApplicationConstants.LAST_NAME)+"' ," +
            "USER_NAME = '"+ updateValues.get(ApplicationConstants.USER_NAME)+"' ," +
            "EMAIL_ID = '"+ updateValues.get(ApplicationConstants.EMAIL_ID)+"' ," +
            "PASSWORD = '"+ updateValues.get(ApplicationConstants.PASSWORD)+"' ," +
            "AGE = '"+ updateValues.get(ApplicationConstants.AGE)+"' ," +
            "SEX = '"+ updateValues.get(ApplicationConstants.SEX)+"' ," +
            "COUNTRY = '"+ updateValues.get(ApplicationConstants.COUNTRY)+"' ," +
            "ADDRESS = '"+ updateValues.get(ApplicationConstants.ADDRESS)+"'" +
                    "WHERE USER_NAME = '"+ updateValues.get(ApplicationConstants.USER_NAME)+"'";
            PostgresSQLConnection.executeUpdate(updateUserQuery);
            result.put(ApplicationConstants.SUCCESS,Boolean.TRUE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
        }

    private boolean alreadyRequested(HttpServletRequest req) {
        String currentUserName = req.getParameter(ApplicationConstants.USER_NAME);
        String profileUserName = req.getParameter(ApplicationConstants.PROFILE_USER_NAME);
        String selectQuery = "SELECT COUNT(1) FROM USER_PROFILE_REQUEST WHERE ACCEPTANCE = 0" +
                " AND USER_NAME = '"+currentUserName+"' AND PROF_USER_NAME = '"+profileUserName+"'";
        ResultSet resultSet = PostgresSQLConnection.executeQuery(selectQuery);
        
        try {
            assert resultSet != null;
            if(resultSet.next() && resultSet.getInt(1) == 1)
                return true;
                
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private JSONObject getRelatedUsers(HttpServletRequest req) {
        String currentUserName = req.getParameter(ApplicationConstants.USER_NAME);
        
        JSONArray userArray = new JSONArray();
        JSONObject result = new JSONObject();
        try {
            result.put("connectedUsers",getConnectedUsers(currentUserName));
            result.put("acceptedUsers",getAcceptedUsers(currentUserName));
            result.put("rejectedUsers",getRejectedUsers(currentUserName));
            result.put("pendingUsers",getPendingUsers(currentUserName));
        } catch (JSONException | SQLException ex) {
            Logger.getLogger(FirstSampleServletClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private JSONArray getConnectedUsers(String currentUserName) throws JSONException, SQLException {
        String selectQuery = "select prof_user.FIRST_NAME,prof_user.LAST_NAME,prof_user.USER_NAME,prof_user.EMAIL_ID,prof_user.PASSWORD,\n" +
"                prof_user.AGE,prof_user.SEX,prof_user.COUNTRY,prof_user.ADDRESS from  DATING_USERS prof_user,USER_PROFILE_REQUEST request\n" +
"                where request.acceptance = 1 and  request.user_name = '"+currentUserName+"'\n" +
"                AND  request.prof_user_name = prof_user.user_name\n" +
"UNION\n" +
"                select prof_user.FIRST_NAME,prof_user.LAST_NAME,prof_user.USER_NAME,prof_user.EMAIL_ID,prof_user.PASSWORD,\n" +
"                prof_user.AGE,prof_user.SEX,prof_user.COUNTRY,prof_user.ADDRESS from  DATING_USERS prof_user,USER_PROFILE_REQUEST request\n" +
"                where request.acceptance = 1 and  request.prof_user_name = '"+currentUserName+"'\n" +
"                AND  request.user_name = prof_user.user_name";
        ResultSet resultSet = PostgresSQLConnection.executeQuery(selectQuery);
        JSONArray userArray = new JSONArray();
        assert resultSet != null;
            while(resultSet.next()){
                JSONObject user = new JSONObject();
                int columnIndex = 1;
                user.put(ApplicationConstants.FIRST_NAME,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.LAST_NAME,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.USER_NAME,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.EMAIL_ID,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.PASSWORD,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.AGE,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.SEX,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.COUNTRY,resultSet.getString(columnIndex++));
                user.put(ApplicationConstants.ADDRESS,resultSet.getString(columnIndex));
                userArray.put(user);
            }
            System.out.println("Related Users:" + userArray);
        return userArray;
    }

    private JSONObject emailRequest(HttpServletRequest req) {
        String emailId = req.getParameter(ApplicationConstants.EMAIL_ID);
        JSONObject result = new JSONObject();
        try{
            result.put(ApplicationConstants.SUCCESS,Boolean.FALSE);
            String selectQuery = "INSERT into  user_pass_request (email_id) values ('"+emailId+"')";
            if(req.getParameter("insert").equals("false")){
                selectQuery = "DELETE FROM  user_pass_request where email_id = '"+emailId+"'";
            }
            PostgresSQLConnection.executeUpdate(selectQuery);
            result.put(ApplicationConstants.SUCCESS,Boolean.TRUE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private JSONObject getPassReqUsers() {
        
        JSONObject result = new JSONObject();
        String selectQuery = "SELECT  FIRST_NAME,LAST_NAME, "
                + "USER_NAME,EMAIL_ID,PASSWORD,AGE,SEX,COUNTRY,ADDRESS FROM DATING_USERS WHERE EMAIL_ID IN"
                + "( SELECT EMAIL_ID FROM  user_pass_request) ";
        ResultSet resultSet = PostgresSQLConnection.executeQuery(selectQuery);
        JSONArray userArray = new JSONArray();
        assert resultSet != null;
        try {
            while(resultSet.next()){ 
                    JSONObject user = new JSONObject();
                    int columnIndex = 1;
                    user.put(ApplicationConstants.FIRST_NAME,resultSet.getString(columnIndex++));
                    user.put(ApplicationConstants.LAST_NAME,resultSet.getString(columnIndex++));
                    user.put(ApplicationConstants.USER_NAME,resultSet.getString(columnIndex++));
                    user.put(ApplicationConstants.EMAIL_ID,resultSet.getString(columnIndex++));
                    user.put(ApplicationConstants.PASSWORD,resultSet.getString(columnIndex++));
                    user.put(ApplicationConstants.AGE,resultSet.getString(columnIndex++));
                    user.put(ApplicationConstants.SEX,resultSet.getString(columnIndex++));
                    user.put(ApplicationConstants.COUNTRY,resultSet.getString(columnIndex++));
                    user.put(ApplicationConstants.ADDRESS,resultSet.getString(columnIndex));
                    userArray.put(user);
            }
            result.put("allUsers",userArray);
        } catch (JSONException |SQLException ex) {
            Logger.getLogger(FirstSampleServletClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Pass Rquest Users:" + userArray);
        return result;
    }

    private boolean isRequested(HttpServletRequest req) {
        String userName = req.getParameter(ApplicationConstants.USER_NAME);
        String selectQuery = "select count(1) from user_pass_request req , "
                + "DATING_USERS dat_user where req.email_id = dat_user.email_id and dat_user.user_name ='"+userName+"'";
        ResultSet resultSet = PostgresSQLConnection.executeQuery(selectQuery);
        
        try {
            assert resultSet != null;
            if(resultSet.next() && resultSet.getInt(1) == 1)
                return true;
                
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}