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
   private String            ACESS_KEY       = "95ccd20c1539944ef846f6b2dce0cd55";
   private InetAddress       IpHost          = null;
   private String            resultMes       = null;
   private final String[]    REQUEST_MESSAGE = {"ip","country_name","region_code","region_name","city","zip_code"};

   IPHostInfo() throws Exception {
      boolean status = false;

      try {
         status = setIpHost();
         if(status == true){
            doGetRequest();
         } else{
            throw new Exception("Failed to set Ip Host");
         }

      }
      catch(IOException e){
         JOptionPane.showMessageDialog(null,"Loading Failed: please check your network connection. ");
         System.exit(0);
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

   private String getACESS_KEY(){

      return this.ACESS_KEY;
   }

   private void doGetRequest() throws IOException {
      BufferedReader reader = null;

      try {
         //String httpAdd = "http://tools.keycdn.com/geo.json?host={" + IpHost.getHostAddress() + "}";
         //http://api.ipstack.com/95ccd20c1539944ef846f6b2dce0cd55
         String param = "check?access_key=" + getACESS_KEY();
         String httpAdd = "http://api.ipstack.com/" + param;// + IpHost.getHostAddress();

         URL url = new URL(httpAdd);
         HttpURLConnection connection = (HttpURLConnection) url.openConnection();

         connection.setRequestMethod("GET");

         reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         setResult(reader.readLine());

      }
      catch (IOException e) {
         throw new IOException("IP Loc can not be initiated : " + e.getMessage());
      }


   }

   private boolean setResult(String finalMes) throws IOException {
      //String str = null;

      //str  = new String();
         /*while ((str = finalMes.readLine()) != null) {
            this.resultMes = this.resultMes + str;
         }*/

      System.out.println(finalMes );
      if(!finalMes.isEmpty()){
         this.resultMes = finalMes;
         return true;
      }else{
         return false;
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

      System.out.print(getREQUEST_MESSAGE()[4]);
      try {
         String[] fild_Request_Message = getREQUEST_MESSAGE();

         json = new JSONObject(getResultMes());

         System.out.print(json.get(fild_Request_Message[4]) .toString());
         return json.get(fild_Request_Message[4]) .toString();          // 4 means the index of city
      } catch (Exception e){
         throw new Exception(e);
      }
   }

   String getState() throws Exception {
      JSONObject json = null;

      System.out.print(getREQUEST_MESSAGE()[2]);
      try {
         String[] fild_Request_Message = getREQUEST_MESSAGE();

         json = new JSONObject(getResultMes());
         System.out.print(json.get(fild_Request_Message[2]).toString());
         return json.get(fild_Request_Message[2]).toString();           // 4 means the index of state
      }catch (Exception e){
         throw new Exception(e);
      }
   }


   Integer findMax(int[] list){
   int smallest = 0, total=0;

      for(int i = 0; i < list.length; i++){
         if(list[smallest] >= list[i]){
            smallest = i;     // will not add this lowest one
         }
      }

      for(int i = 0; i < list.length; i++){
         if(smallest != i){
            
            total = total + list[i];
         }
      }

      return total;
   }
}




