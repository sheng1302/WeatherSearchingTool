import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by shengliu on 8/27/17.
 */

class JFrameListener implements WindowListener{

   @Override
   public void windowOpened(WindowEvent e) {
   }

   @Override
   public void windowClosing(WindowEvent e) {

      System.exit(0);
   }

   @Override
   public void windowClosed(WindowEvent e) {

      System.exit(0);
   }

   @Override
   public void windowIconified(WindowEvent e) {

   }

   @Override
   public void windowDeiconified(WindowEvent e) {

   }

   @Override
   public void windowActivated(WindowEvent e) {

   }

   @Override
   public void windowDeactivated(WindowEvent e) {

   }
}

class JPanelFactory extends JPanel {

   JPanelFactory(){}


}

class JLabelFactory extends JLabel{

   JLabelFactory(String labelContent){

      super(labelContent);
   }

   JLabelFactory(Icon icon){

      super(icon);
   }

   JLabelFactory(String text,String fileAddress, String description, int horizontalAlignment){

      super(text,(new ImageIcon(fileAddress,description)),horizontalAlignment);
      super.setOpaque(true);
   }

}

class WeatherInterface extends JFrame{

   WeatherInterface() throws Exception {
      try {
         createDefaultDisplayUI();
      } catch (Exception e ){
         throw new Exception(e);
      }

   }

   public String[] createWeather() throws Exception{
   IPHostInfo ipPack = null;
   WeatherInfo weather = null;
   String []   retResult = null;

      try {
         ipPack = new IPHostInfo();
         weather = new WeatherInfo(ipPack.getCity(),ipPack.getState());
         retResult = weather.checkWeatherGeoLocation();

         return retResult;
      }
        catch (Exception e){
        throw new Exception(e);
      }

   }

   public void createDefaultDisplayUI() throws Exception {
   String[] result = null;

      try {
         setTitle("Weather Tool");
         setFrameSize(400, 500);
         setDefaultCloseOperation();
         super.addWindowListener(new JFrameListener());

         JPanel aPanel = new JPanel();
         aPanel.add(generateLabel("Here are the next 10 days of forecast: "));
         add(aPanel);
         result = createWeather();
         for (int i = 0; i < result.length; i++) {
            aPanel.add(generateLabel(result[i]));
            add(aPanel);
         }
         setVisible(true);
      }
      catch (Exception e){
         throw new Exception(e);
      }
   }

   @Override
   public void setTitle(String title){

      if(!(title.isEmpty()) && title.length() < 25){
         super.setTitle(title);
      }

   }

   public void setFrameSize(int height, int width){

      if((height > 0) && (width>0)){
         super.setSize(new Dimension(width,height));
      }
   }

   public void setDefaultCloseOperation(){

      super.setDefaultCloseOperation(WeatherInterface.EXIT_ON_CLOSE);
   }

   public void setVisible(boolean condition){

      super.setVisible(condition);
   }

   public JLabelFactory generateLabel(String labelContent){
   JLabelFactory aLabel = null;

      //aLabel = new JLabelFactory( "Testing","images/sunny.png", "sunny icon", JLabelFactory.CENTER);
      aLabel = new JLabelFactory(labelContent);
      return aLabel;
   }
}
