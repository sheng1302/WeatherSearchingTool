/**
 * Created by shengliu on 8/27/17.
 */
import org.json.JSONObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;


class IPHostInfo{
   private InetAddress       IpHost          = null;
   private String            resultMes       = null;
   private final String[]    REQUEST_MESSAGE = {"ip","country_name","region_code","region_name","city","zip_code"};

   IPHostInfo() throws UnknownHostException{
      boolean setStatus = false;

      try {
         setStatus = setIpHost();
         seekIpLoc();
      }
      catch(IOException e){
         JOptionPane.showMessageDialog(null,"Exception: " + e.getMessage());
      }


   }

   private boolean setIpHost() throws UnknownHostException {

      InetAddress hostAdd = null;
      try {
         hostAdd = InetAddress.getLocalHost();

         if(hostAdd != null) {
            IpHost = hostAdd;
            return true;
         }
         else{
            return false;
         }
      } catch (UnknownHostException e) {
         e.printStackTrace();
      }

      return false;
   }

   private void printMessage(String message){

      System.out.println(message);
   }

   private void seekIpLoc() throws IOException {
      BufferedReader reader = null;

      try {
         //String httpAdd = "http://tools.keycdn.com/geo.json?host={" + IpHost.getHostAddress() + "}";
         String httpAdd = "http://freegeoip.net/json/";// + IpHost.getHostAddress();
         URL url = new URL(httpAdd);
         HttpURLConnection connection = (HttpURLConnection) url.openConnection();

         connection.setRequestMethod("GET");
         reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         //System.out.println(reader.readLine());
         setResult(reader);

      }
      catch (IOException e) {
         throw new IOException("IP Loc can not be initiated : " + e.getMessage());
      }


   }

   private void setResult(BufferedReader finalMes) throws IOException {
      String str = null;

      try {
         resultMes  = new String();
         while ((str = finalMes.readLine()) != null) {
            resultMes = resultMes + str;
         }

      }catch (IOException e){
         throw new IOException("result can not be set : " + e.getMessage());
      }
   }

   String getResultMes(){

      return resultMes;
   }

   void displayResult() throws Exception{
      String[] fild_Request_Message = getREQUEST_MESSAGE();
      JSONObject json = null;

      try {
         json = new JSONObject(getResultMes());
         for(int counter=0; counter<fild_Request_Message.length;counter++){
            System.out.println("Host_" + fild_Request_Message[counter] +":"+ json.get(fild_Request_Message[counter]));
         }
      } catch (Exception e) {
        throw new Exception(e);
      }
   }

   String[] getREQUEST_MESSAGE(){

      return REQUEST_MESSAGE;
   }

   String getCity() throws Exception {
      JSONObject json = null;

      try {
         String[] fild_Request_Message = getREQUEST_MESSAGE();

         json = new JSONObject(getResultMes());
         return json.get(fild_Request_Message[4]).toString();           // 4 means the index of city
      } catch (Exception e){
         throw new Exception(e);
      }
   }

   String getState() throws Exception {
      JSONObject json = null;

      try {
         String[] fild_Request_Message = getREQUEST_MESSAGE();

         json = new JSONObject(getResultMes());
         return json.get(fild_Request_Message[4]).toString();           // 4 means the index of city
      }catch (Exception e){
         throw new Exception(e);
      }
   }
}




