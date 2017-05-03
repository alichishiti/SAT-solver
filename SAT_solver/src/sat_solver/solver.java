package sat_solver;

import static java.lang.Math.pow;
import java.util.*;  
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class solver {
    
   char[] binaryOperators;
   String expression;
   ArrayList<Character> propositionList;
   HashMap<Character,Integer> propositionToValue;
   String updatedExpression="";
   ArrayList<Integer> resultList;
   public boolean tautology=true;
   public boolean contradiction=true;
   public boolean contigency=false;
   
    
    public solver(String expression){
        this.resultList=new ArrayList<>();
        this.propositionToValue= new HashMap<>();
        this.propositionList = new ArrayList<>();
        this.binaryOperators=new char[]{'&', '|', '>', '='};
        this.expression=expression;
    }
    
    public void extractPropositions(){
        char element;
        boolean addProposition = true;
        for(int i = 0 ; i < this.expression.length();i++){
            element = this.expression.charAt(i);
            
            if(Character.isLetter(element)){
                addProposition = true;
                
                for (int j = 0; j < this.propositionList.size(); j++){
                    if(this.propositionList.get(j) == element){
                        addProposition = false;
                        break;
                    }        
                }

                if(addProposition){
                    this.propositionList.add(element);
                    addProposition = true;
                }
            }
        }
        
        System.out.println(propositionList);
    }
    
    public ArrayList<Integer> ComputeExpression(){
        
        
        for(int i = 0 ; i< pow(2,propositionList.size());i++){
            String binary= Integer.toBinaryString(i);
            binary= append(binary,propositionList.size());
            for(int j =0 ; j< binary.length(); j++){
                propositionToValue.put(propositionList.get(j), 
                        Integer.parseInt(binary.substring(j,j+1)));
                }
            Calculate(propositionToValue);
        }
        return resultList;
    }
    
    private void Calculate(HashMap<Character,Integer> map){
       boolean negation=false;
       char element;
       
       for(int i = 0 ; i < expression.length();i++){
           element = expression.charAt(i); 
           if(element =='~'){
               negation= true;
           }
           else if(isOperand(element)){
                updatedExpression=updatedExpression+String.valueOf(element);
           }
           else if(Character.isLetter(element)){
               if(negation){
                   Integer value=map.get(element);
                   if(value==1){
                       updatedExpression=updatedExpression+"0";
                   }
                   else if(value==0){
                       updatedExpression=updatedExpression+"1";
                   }
                   negation=false;
               }
               else{
                   updatedExpression=updatedExpression+String.valueOf(
                   map.get(element));
               }
           }
       }
      calEngine(updatedExpression); 
      updatedExpression="";
    }
    
    private void calEngine(String calc){
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Object result=null;
        try{
             result = engine.eval(calc);
        }
        catch(Exception e){
            System.out.println(e.getStackTrace());
        }
        Integer value= (Integer)result;
        resultList.add(value);
        
    }
    private boolean isOperand(Character element){
        return element=='('||element==')'||element=='&'||element=='|';
    }
    
    private String append(String binary, int length){   
        while(binary.length()!=length){        
            binary="0"+binary;
        }
        return binary;
    }
     public void checkTautology(ArrayList<Integer> results){
        for(int i = 0; i< results.size(); i++){
            if(results.get(i)==0){
                this.tautology=false;
                break;
            }
        }
    }
    public void checkContradiction(ArrayList<Integer> results){
        for(int i = 0; i< results.size(); i++){
            if(results.get(i)==1){
                this.contradiction=false;
                break;
            }
        }
    }
    public void checkContigency(){
        if(!this.contradiction && !this.tautology){
            this.contigency=true;
        }
    }
    
}
