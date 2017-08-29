/**
 * Created by shengliu on 8/27/17.
 */

public class Weather {
private IPHostInfo ipPack = null;
private WeatherInfo weather = null;

   Weather() throws Exception {
      try {
         ipPack = new IPHostInfo();
         ipPack.displayResult();
         weather = new WeatherInfo(ipPack.getCity(),ipPack.getState());
      } catch (Exception e) {
         throw new Exception(e);
      }
   }
}
