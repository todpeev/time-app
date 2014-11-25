/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Time;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
/**
 *
 * @author peevto
 */
public class App extends javax.swing.JFrame {
    /**
     * Creates new form Clock
     */
    public App() {
        super("Timer");
        try 
        {
            alarmImage = ImageIO.read((getClass().getResource("image/alarm.jpg")));         
        } catch (IOException ex) 
        {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        initComponents();
        setLocationRelativeTo(null);
        initClassVariables();
   
        while(true)
        {
            if(isClock)
            {
                clock();  
            }
            else
            {
                timer();
            }
        }       
    }
    public void initClassVariables()
    {
        this.Alarms = new HashMap<Integer, AlarmObj>(3);
        setStartingAlarms();  
        this.isClock = false;
        this.IsTimerReset = false;
        GregorianCalendar calendar = new GregorianCalendar();
        this.startMinutes = calendar.get(Calendar.MINUTE);
        this.startSeconds = calendar.get(Calendar.SECOND);
        this.defaultColor = rightPanel.getBackground();
        Font font = new Font("Jokerman", Font.PLAIN, 35);
this.label.setFont(font);
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
this.setVisible( true );
this.setResizable(false);
    }
    
    public void setStartingAlarms() 
    {
        // attempt to read starting alarm setting from properties file,
        // if the file doesnt exist, create it with hardcoded values:
        
        try 
        {
            // try to open config.properties:
            FileInputStream test = new FileInputStream("C:\\config.properties");
            test.close();
         
} 
        catch (FileNotFoundException ex) 
        {
            // if the file is not there create it:
            String[] states = {"On","Off","Off","Off"};
            String[] times = {"45","15","5","12:15"};
            String[] texts = {"Time for microbreak!","","",""};
            saveFile(states,times,texts);            
} 
        catch (IOException ex) 
        { 
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally 
        {
            // open the file config.properties:
            readFile(); 
}
 
    }
    
    public void saveFile(String[] states, String[] times, String[] texts)
    {        
        Properties initialAlarmSettings = new Properties();
FileOutputStream output = null;
        String timerAlarmTime;
        String timerState;
        String timerAlarmText;
        String timerNo;
        String timerTimeProp = "_Time";
        String timerStateProp = "_State";
        String timerTextProp = "_Text";
        
        
        try 
        { 
            output = new FileOutputStream("C:\\config.properties");
            for(int i = 1; i <= states.length ; i++)
            {
                timerNo = "Alarm" + i;
                timerState = timerNo + timerStateProp;
                timerAlarmTime = timerNo + timerTimeProp;
                timerAlarmText = timerNo + timerTextProp;
                if(i == 1)
                {    
                    initialAlarmSettings.setProperty(timerState, states[0]);
                    initialAlarmSettings.setProperty(timerAlarmTime, (times[0]));
                    initialAlarmSettings.setProperty(timerAlarmText, texts[0]);
                }
                else if (i == 2)
                {
                    initialAlarmSettings.setProperty(timerState, states[1]);
                    initialAlarmSettings.setProperty(timerAlarmTime, (times[1]));
                    initialAlarmSettings.setProperty(timerAlarmText, texts[1]);
                }
                else if (i == 3)
                {
                    initialAlarmSettings.setProperty(timerState, states[2]);
                    initialAlarmSettings.setProperty(timerAlarmTime, (times[2]));
                    initialAlarmSettings.setProperty(timerAlarmText, texts[2]);  
                }   
                else
                {
                    initialAlarmSettings.setProperty(timerState, states[3]);
                    initialAlarmSettings.setProperty(timerAlarmTime, (times[3]));
                    initialAlarmSettings.setProperty(timerAlarmText, texts[3]);
                }
            }
            initialAlarmSettings.store(output, null);
            if (output != null) 
            {                  
                output.close();                  
            }
        } 
        catch (IOException io) 
        {
            io.printStackTrace();
            System.exit(1);
        }
    }
    
    public void readFile()
    {
        Properties initialAlarmSettings = new Properties();
FileInputStream input = null;
        String alarmTime;
        String alarmState;
        String alarmText;
        String alarmNo;
        String alarmTimeProp = "_Time";
        String alarmStateProp = "_State";
        String alarmTextProp = "_Text";
        String clTime;
        
        try 
        {
            input = new FileInputStream("C:\\config.properties");
            initialAlarmSettings.load(input);
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        AlarmObj alarmInfo;
        int minute;
        Integer hour;
        HashMap<Integer, AlarmObj> map = new HashMap<Integer, AlarmObj>();
        // read starting alarm settings from properties file:
        for(int i = 1; i <= 4; i++)
        {
            alarmNo = "Alarm" + i;
            alarmState = alarmNo + alarmStateProp;
            if( initialAlarmSettings.getProperty(alarmState, "Off").equals("On") )
            {
                alarmTime = alarmNo + alarmTimeProp;
                if(i == 1)
                {    
                    minute = Integer.parseInt((initialAlarmSettings.getProperty(alarmTime, "45")));
                    hour = null;
                }
                else if( i == 2)
                {
                    minute = Integer.parseInt((initialAlarmSettings.getProperty(alarmTime, "15")));
                    hour = null;
                }
                else if (i == 3)
                {
                    minute = Integer.parseInt((initialAlarmSettings.getProperty(alarmTime, "5")));
                    hour = null;
                }             
                else
                {
                    clTime = (initialAlarmSettings.getProperty(alarmTime, "12:15"));
                    String[] split = clTime.split(":");
                    hour = Integer.parseInt(split[0]);
                    minute = Integer.parseInt(split[1]);
                }
                alarmText = alarmNo + alarmTextProp;
                alarmInfo = new AlarmObj(minute, initialAlarmSettings.getProperty(alarmText, ""), hour);
                System.out.println("!!!!!!!!!!!hour: " + hour+ " alarm"+i);
                map.put(i,alarmInfo);
            }                  
        }
        
        // load in the alarm the read values:
        setAlarms(null, null, map);           
        // close input file stream:
        try 
        {
            if (input != null) 
            {                  
                input.close();                  
            }
        } 
        catch (IOException ex1)
        {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex1);
        }     
    }
    
    
    @SuppressWarnings("SleepWhileInLoop")
    private void clock()
    {    
GregorianCalendar gcalendar = new GregorianCalendar();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        String currentTime; 
        while(true)
        {
            if(!isClock)
            {
                return;
            }
            
            currentTime = dateFormat.format(gcalendar.getTime());
            label.setText(currentTime);
            label.repaint();
            int currentMinute = gcalendar.get(Calendar.MINUTE);
            int currentHour;
            int currentSecond = gcalendar.get(Calendar.SECOND); 
            if(!hourFormat.format(gcalendar.getTime()).equals("00")){
                
                currentHour = Integer.parseInt(hourFormat.format(gcalendar.getTime())); 
            }
            else
            {
                currentHour = 0;
            }
            
            // displayAlarm(currentMinutes, currentSeconds, this.startSeconds,this.startMinutes);
            if( Alarms.get(4) != null )
            {
                clockAlarmHour = Alarms.get(4).getHours();
                clockAlarmMinute = Alarms.get(4).getMinutes();
                if(currentMinute == clockAlarmMinute && currentHour == clockAlarmHour && currentSecond == 0)
                {
                        AlarmBox box = new AlarmBox( Alarms.get(4).getText());
                        box.setVisible(true); 
                }
            }
            try 
            {
                Thread.sleep(980);
            } 
            catch (InterruptedException e) 
            {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, e);
            } 
            finally
            {
                gcalendar.set(Calendar.SECOND, gcalendar.get(Calendar.SECOND)+1);
            }
}
    }
    
    
    public void timer()
    {
        int second = 0;
        int minute = 0;
        String timerSecond = "";
        String timerMinute = "";
        String currentTimer;
                     
        while(true)
        {
            if(isClock)
            {
                return;
            }
            if(IsStartTimeChanged)
            {
                IsStartTimeChanged = false;
                IsTimerReset = true;
            }
            
            if(IsTimerReset)
            {
                second = 0;
                minute = 0;
                IsTimerReset = false;
            }
            
            lock.lock();
             
            try 
            {
                while(isPaused)
                {
                   clockPaused.await();
                }        
            } 
            catch (InterruptedException ex) 
            {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally
            {
                lock.unlock();
            }
            
            if(second < 10)
            {
                timerSecond = "0" + second;
            } 
            else
            {
                timerSecond = second + "";
            }
            
            if(minute < 10)
            {
                timerMinute = "0" + minute;
            } 
            else
            {
                timerMinute = minute + "";
            }
            currentTimer = timerMinute + ":" + timerSecond;
            label.setText(currentTimer);
            label.repaint();
            displayAlarm(minute, second, 0,0);
            
            try 
            {
                Thread.sleep(980);
            } 
            catch (InterruptedException ex) 
            {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(second == 59)
            {
                second = 0;
                minute++;
            }
            else 
            {
                second++;  
            }                
        }
    }
    
    
    public void displayAlarm(int currentMinutes, int currentSeconds, int startSeconds, int startMinutes)
    {
        
        
        for(int i = 1; i <= 3; i++)
        {
            int alarmMinutes;
            if(startBlinking)
            {
               Color currentColor = rightPanel.getBackground();
               if(currentColor.equals(defaultColor))
               {
                   rightPanel.setBackground(blinkColor);
               }
               else
               {
                   rightPanel.setBackground(defaultColor);
                   
               }
            }
            
            if( Alarms.get(i) == null)
            {
                continue;
            } else {
                alarmMinutes = Alarms.get(i).getMinutes();
            }
           
            if(currentMinutes >= startMinutes + alarmMinutes)
            {
                
                if(currentSeconds == startSeconds)
                {    
                    if( Math.abs(currentMinutes - startMinutes) % alarmMinutes == 0 )
                    {
                            //System.out.println("x2 "+currentSeconds +" "+ currentMinutes+" "+startSeconds+" "+startMinutes+" "+alarmMinutes);
                            AlarmBox box = new AlarmBox( Alarms.get(i).getText());
                            box.setVisible(true);
                             if(startBlinking  == false)
                            {
                                startBlinking = true;
                            }   
                            
                            if(i == 1 )
                            {
                                if(blinkColor == null || !blinkColor.equals(Color.red))
                                    blinkColor = new Color(255,0,0); // red                 
                            }
                            else if(i == 2)
                            {
                                if( blinkColor == null || !blinkColor.equals(Color.red))
                                    blinkColor = new Color(255,255,0); // yellow
                            }
                            else 
                            {
                                if(blinkColor == null)
                                    blinkColor = new Color(0,255,0); // green
                            }
                            
                            break;
                    }
                }    
            } 
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
        rightPanel = new javax.swing.JPanel();
        label = new javax.swing.JLabel();
        leftPanel = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(alarmImage);
        rightPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addComponent(label, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        leftPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton2.setText("Clock");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jButton1.setText("Alarms");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton3.setText("Reset");
        jButton3.setEnabled(true);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jButton4.setText("Pause");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(rightPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(leftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rightPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(leftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>                        
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        OptionFrame options = new OptionFrame(this);
    }                                        
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        if(isClock)
        {
            jButton2.setText("Clock");
            jButton4.setEnabled(true);
        }
        else
        {
            jButton2.setText("Timer");
            jButton4.setEnabled(false);
        }
        jButton3.setEnabled(isClock);
        isClock = !isClock;        
    }                                        
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        IsTimerReset = true;
        stopBlinking();
    }                                        
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        
        if(isPaused)
        {
           jButton4.setText("Pause");
           isPaused = false;
        }
        else
        {
            jButton4.setText("Resume");
            isPaused = true;
        }
   
        System.out.println(isPaused);
        lock.lock();
        try 
        {
            clockPaused.signal();
        } 
        finally 
        {
            lock.unlock();
        }      
    }                                        
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
            App a = new App();
       }
    public final void setAlarms(Integer number, AlarmObj alarmInfo, HashMap<Integer, AlarmObj> map ) {
        
        Alarms.clear();
        if (number == null && alarmInfo == null) {
            Alarms.putAll(map);
        } else if(map == null) {
            Alarms.put(number, alarmInfo);
        } else {
            System.out.println("wrong combination of method arguments");
            
        }
        
        for (Map.Entry<Integer, AlarmObj> entry : Alarms.entrySet()) {
System.out.println("Key : " + entry.getKey() + " Value : "
+entry.getValue().getHours()+ " "+ entry.getValue().getMinutes() +" "+ entry.getValue().getText());
}
     }
    
    public void setStart(int minutes, int seconds){
       
        this.startMinutes = minutes;
        this.startSeconds = seconds;
        System.out.println("start minutes: "+startMinutes);
        System.out.println("start seconds: "+startSeconds);
    }
    
    public void changeStart(boolean change)
    {
        IsStartTimeChanged = change;
    }
    
    public void stopBlinking()
    {
        startBlinking = false;
        rightPanel.setBackground(defaultColor);
        blinkColor = null;
    }
    
    final Lock lock = new ReentrantLock();
    final Condition clockPaused  = lock.newCondition();
    private boolean isPaused = false;
    private int clockAlarmHour;
    private int clockAlarmMinute;
    private BufferedImage alarmImage;
    private Color defaultColor;
    private boolean startBlinking = false;
    private Color blinkColor;
    private boolean IsStartTimeChanged;
    private boolean IsTimerReset;
    private boolean isClock;
    private int startSeconds;
    private int startMinutes;
    private HashMap<Integer, AlarmObj> Alarms;    
    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel label;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel rightPanel;
    // End of variables declaration                   
    
}
