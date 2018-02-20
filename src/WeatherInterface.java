
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

class JLabelFactory extends JLabel{

   JLabelFactory(String labelContent){

      super(labelContent);
   }

   JLabelFactory(String labelContent, Icon icon, int alighnment){

      super(labelContent,icon,alighnment);
   }

   JLabelFactory(String text,String fileAddress, String description, int horizontalAlignment){

      super(text,(new ImageIcon(fileAddress,description)),horizontalAlignment);
      super.setOpaque(true);
   }

}

class WeatherInterface extends JFrame implements ActionListener{
private int MAX_LABELS = 11;
private int iterator = 0;
private JLabel[] labels = new JLabel[MAX_LABELS];
private JSONObject condition_code= null;

   WeatherInterface() throws Exception {
      try {
         createDefaultDisplayUI();
      } catch (Exception e ){
         throw new Exception(e);
      }

   }

   private JSONArray queryLocalWeather() throws Exception {
   IPHostInfo ipPack = null;
   WeatherInfo weather = null;
   JSONArray retResult = null;

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

   private String readConditionalCodeFile(){
      StringBuilder builder = new StringBuilder();
      String line = null;
      FileReader conFileReader = null;

      try {
         conFileReader = new FileReader("src/resources/conditionalCode.json");
         BufferedReader contxt = new BufferedReader(conFileReader);

         while((line = contxt.readLine()) != null){
            builder.append(line);

         }
      } catch (IOException e) {
         e.printStackTrace();
      }

      return builder.toString();
   }

   private void setCondition_Code(String content) throws Exception{


      if(content != null && !(content.equals(""))){
         condition_code = new JSONObject(content);
      } else {
         throw new Exception("Content Exception; Condition load failed");
      }
   }

   private JSONObject getCondition_Code(){

      return condition_code;
   }

   private String translateConditionCode(int code){
      JSONObject ao = getCondition_Code();
      String result = null;
      try {

         JSONArray newJSON = ao.getJSONObject("weather_codes").optJSONArray("code");
         if(code == 3200) {

            return newJSON.getJSONObject(48).get("description").toString();
         }

         result = newJSON.getJSONObject(code).get("description").toString();

      } catch (Exception e){
         JOptionPane.showMessageDialog(null,"Exception: " + e.getMessage());
      }

      return result;
   }

   private int getIterator(){

      return iterator;
   }

   private JLabel[] getLabels(){

      return labels;
   }

   private void increaseIterator(){

      iterator++;
   }

   private void decreaseIterator(){

      iterator--;
   }

   private void addWeatherLabels(JLabel label)throws Exception {

      JLabel[] labels = getLabels();
      try {
         if (getIterator() < MAX_LABELS) {

            labels[getIterator()] = label;
            increaseIterator();
         }
      } catch (Exception e){
         throw new Exception("Weather Label load failed");
      }
   }

   private JLabel addIndividualWeathLabel(JSONObject weatherJSON)throws Exception{
      ImageIcon weatherImg = null;
      String iconMSG = null;
      String date, high, low, txt;
      JLabel label = null;
      int conCode = 0;

      try {
         conCode = Integer.parseInt(weatherJSON.getString("code"));

         if ((iconMSG = translateConditionCode(conCode)) != null) {
            //System.out.println(iconMSG);
            weatherImg = new ImageIcon("src/resources/widget_" + iconMSG + ".png");

         } else {
            new Exception("Error! Unable to load icon! ");
         }


         date = weatherJSON.getString("date");
         high = weatherJSON.getString("high");
         low = weatherJSON.getString("low");
         txt = weatherJSON.getString("text");

         label = generateLabel(date + " | High:" + high + " | low: " + low + " | " + txt,weatherImg,JLabel.LEFT);

         return label;
      }catch (JSONException e) {
         JOptionPane.showMessageDialog(null, " Parsing failure. Try again later!" );
      }

      return null;
   }

   private void changeIndivWeathLabel(JSONObject weatherJSON,JLabel label) throws Exception{
      ImageIcon weatherImg = null;
      String iconMSG = null;
      String date, high, low, txt;
      int conCode = 0;

      try {
         conCode = Integer.parseInt(weatherJSON.getString("code"));

         if ((iconMSG = translateConditionCode(conCode)) != null) {
            weatherImg = new ImageIcon("src/resources/widget_" + iconMSG + ".png");

         } else {
            new Exception("Error! Unable to load icon! x140");
         }


         date = weatherJSON.getString("date");
         high = weatherJSON.getString("high");
         low = weatherJSON.getString("low");
         txt = weatherJSON.getString("text");

         label.setText(date + " | High:" + high + " | low: " + low + " | " + txt);

      }catch (JSONException e) {
         JOptionPane.showMessageDialog(null,"Parse failure. Try again later! x00000001" );
      }
   }

   private void refreshWeatherPanel(JSONArray arr) throws Exception{
      ImageIcon weatherImg = null;
      String iconMSG = null;
      JSONObject weatherJSON = null;
      String date, high, low, txt;
      JLabel label = null;
      try {
         for (int i = 0; i < arr.length(); i++) {
            weatherJSON = arr.getJSONObject(i);
            changeIndivWeathLabel(weatherJSON,getLabels()[i]);

            }

      } catch (JSONException e) {
         JOptionPane.showMessageDialog(null,"Parse failure. Try again later!");
      }
      
   }

   private void addWeather(JSONArray arr, JPanel toPanel) throws Exception {
      JSONObject weatherJSON = null;
      JLabel label = null;

      try {
         for (int i = 0; i < arr.length(); i++) {
            weatherJSON = arr.getJSONObject(i);
            if((label = addIndividualWeathLabel(weatherJSON)) != null){
               toPanel.add(label);
               addWeatherLabels(label);
            } else{
               JOptionPane.showMessageDialog(null,"Unable to load the program!");
               System.exit(0);
            }

         }
      } catch (JSONException e) {
         JOptionPane.showMessageDialog(null,"Parse failure. Try again later!");
      }
   }

   private void createDefaultDisplayUI() throws Exception {
   String[] result = null;

      try {
         this.setTitle("Weather Tool");
         this.setFrameSize(340, 450);
         this.setDefaultCloseOperation();
         this.setResizable(false);
         this.setLocationRelativeTo(null);
         super.addWindowListener(new JFrameListener());

         JPanel outterLayer = new JPanel();
         JPanel weatherPanel = new JPanel();

         weatherPanel.setLayout(new BoxLayout(weatherPanel,BoxLayout.PAGE_AXIS));
         weatherPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 10));
         setCondition_Code(readConditionalCodeFile());
         addWeather(queryLocalWeather(),weatherPanel);
         outterLayer.add(weatherPanel);

         //------------------------------------->
         JPanel buttonRefreshPanel = new JPanel();
         weatherPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 10));

         JLabel buttonStatus = generateLabel("Have a wonderful day :)");
         addWeatherLabels(buttonStatus);

         buttonRefreshPanel.add(buttonStatus);
         JButton refreshButton = new JButton("Refresh");
         refreshButton.addActionListener(this);

         buttonRefreshPanel.add(refreshButton);

         outterLayer.add(buttonRefreshPanel);

         this.add(outterLayer);

         this.setVisible(true);
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

      try {
         if ((height > 0) && (width > 0)) {
            super.setSize(new Dimension(width, height));
         }
      } catch(Exception e){
         JOptionPane.showMessageDialog(null,"Unable to create frame. ");
         System.exit(0);
      }
   }

   public void setDefaultCloseOperation(){

      super.setDefaultCloseOperation(WeatherInterface.EXIT_ON_CLOSE);
   }

   public void setVisible(boolean condition){

      super.setVisible(condition);
   }

   private JLabelFactory generateLabel(String labelContent){
   JLabelFactory aLabel = null;

      aLabel = new JLabelFactory(labelContent);
      return aLabel;
   }

   private JLabelFactory generateLabel(String labelContent, ImageIcon img, int ali){
      JLabelFactory aLabel = null;

      aLabel = new JLabelFactory(labelContent,img,ali);
      return aLabel;
   }

   @Override
   public void actionPerformed(ActionEvent e) {

      try {
         refreshWeatherPanel(queryLocalWeather());
         labels[10].setText("Refreshed. Have a wonderful day:)");
      } catch (Exception e1) {
         JOptionPane.showMessageDialog(null,e1);
      }
   }
}
