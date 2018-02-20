/**
 * Created by shengliu on 8/27/17.
 */

import javax.swing.*;

public class WeatherCheckingTool {

   public static void main(String[] args){

      try {
         WeatherInterface weatherUI = new WeatherInterface();
      } catch (Exception e) {
         JOptionPane.showMessageDialog(null,"Unexpected Exception Occurred.");
      }
   }
}
