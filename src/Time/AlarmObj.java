/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Time;
/**
 *
 * @author peevto
 */
public class AlarmObj {
    
    private int hours;
    private int minutes;
    private final String text;
    
    
    public AlarmObj(Integer mins , String b, Integer h){
        if(h!=null)
        {
          hours = h;  
        }
        
        minutes = mins;
        text = b;
    }
    
    public int getMinutes() {
        return minutes;
    }
    public int getHours(){
        return hours;
    }
    
    public String getText() {
        return text;
    }
}
