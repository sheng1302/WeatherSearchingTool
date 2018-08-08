import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by shengliu on 8/27/17.
 */

class WeatherInfo {
   private String currentGeoLocationCity = null;
   private String currentGeoLocationState = null;
   private String retMes = null;

   WeatherInfo(){

   }

   WeatherInfo(String requestedLocationCity, String requestedLocationState){

      setCurrentGeoLocation(requestedLocationCity,requestedLocationState);

   }

   void setCurrentGeoLocation(String geoLocationCity,String geoLocationState){
      currentGeoLocationCity = geoLocationCity;
      System.out.print(geoLocationCity);
      currentGeoLocationState = geoLocationState;
   }

   String getCurrentGeoLocationCity(){

      return currentGeoLocationCity;
   }

   String getCurrentGeoLocationState(){

      return currentGeoLocationState;
   }

   JSONArray checkWeatherGeoLocation() throws Exception {
      String geoLocCity = getCurrentGeoLocationCity();
      String geoLocState = getCurrentGeoLocationState();
      String httpURL = "https://query.yahooapis.com/v1/public/yql?q=";
      String httpYQL = "select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20"+
                       "where%20text%3D%22"+ geoLocCity.replace(" ", "%20") +"%2C%20"+getCurrentGeoLocationState()+"%22)";
      String httpFormat = "&format=json";
      String httpAdd = httpURL + httpYQL + httpFormat;
      HttpURLConnection geoLocConnect = null;
      URL newURL  = null;
      BufferedReader reader = null;
      try {
         newURL = new URL(httpAdd);
         geoLocConnect = (HttpURLConnection) newURL.openConnection();
         reader = new BufferedReader(new InputStreamReader(geoLocConnect.getInputStream()));
         setRetMes(reader.readLine());
         return(loadForcast());

      }

      catch (MalformedURLException e){
         throw new Exception("Unexpected exception occurred!" + e);
      }

   }

   void setRetMes(String retOutput){

      retMes = retOutput;
   }

   String getRetMes(){

      return retMes;
   }

   JSONArray loadForcast() throws Exception {

      String retData        =   null;
      JSONObject jSon       =   null;
      JSONArray returnForeCastData = null;

      try {

         retData        =   getRetMes();
         jSon           =   new JSONObject(retData);
         System.out.print("WeatherInfo checked 0x1");
         returnForeCastData = queryForcast(jSon);  // <- this will parse or cut all useless informtaion, return 10 days of broadcast
         System.out.print("WeatherInfo checked 0x2");
         return returnForeCastData;
      } catch (Exception e) {
         throw new Exception(e);
      }

   }

   JSONArray queryForcast(JSONObject jSon) throws Exception {

      try {
         return jSon.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast");
      }
      catch (Exception e){
         throw new Exception(e);
      }
   }
}

