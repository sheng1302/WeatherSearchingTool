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
   private final String[] REQUEST_MESSAGE = {"date","day","high","low","text"};
   private JSONObject[] jSonList = null;
   private String retMes = null;

   WeatherInfo(){

   }

   WeatherInfo(String requestedLocationCity, String requestedLocationState){

      setCurrentGeoLocation(requestedLocationCity,requestedLocationState);

   }

   void setCurrentGeoLocation(String geoLocationCity,String geoLocationState){

      currentGeoLocationCity = geoLocationCity;
      currentGeoLocationState = geoLocationState;
   }

   String getCurrentGeoLocationCity(){

      return currentGeoLocationCity;
   }

   String getCurrentGeoLocationState(){

      return currentGeoLocationState;
   }

   String[] checkWeatherGeoLocation() throws Exception {
      String geoLocCity = getCurrentGeoLocationCity();
      String geoLocState = getCurrentGeoLocationState();
      String httpURL = "https://query.yahooapis.com/v1/public/yql?q=";
      String httpYQL = "select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22New%20York%2C%20NY%22)";
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

   String[] loadForcast() throws Exception {

      String retData        =   null;
      JSONObject jSon       =   null;
      JSONArray jArr        =   null;
      String[] requestMes   =   null;
      String[] returnForeCastData      =   null;
      int forecastSize      =   0;


      try {
         retData        =   getRetMes();
         jSon           =   new JSONObject(retData);
         returnForeCastData = new String[queryForcast(jSon).length()];
         forecastSize   =   queryForcast(jSon).length();
         requestMes     =   getREQUEST_MESSAGE();


         for(int i = 0; i < forecastSize; i++){
            returnForeCastData[i] = "";
            for(int counter = 0; counter < getREQUEST_MESSAGE().length; counter++){

                returnForeCastData[i] = (returnForeCastData[i] +
                                        getREQUEST_MESSAGE()[counter] +
                                        " : " +
                                        queryForcast(jSon).getJSONObject(i).get(getREQUEST_MESSAGE()[counter]) +
                                        "  " );

            }

         }

         return returnForeCastData;
      } catch (Exception e) {
         throw new Exception(e);
      }

   }

   String[] getREQUEST_MESSAGE(){

      return REQUEST_MESSAGE;
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

