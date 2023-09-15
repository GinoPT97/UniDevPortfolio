package Model;

public class Calc {
          private float operando1;
          private float operando2;
          private String operatore;
          private float risultato;
          
          public Calc(float operando1,float operando2, String operatore, float risultato) {
        	  this.operando1 = operando1;
        	  this.operando2 = operando2;
        	  this.operatore = operatore;
        	  this.risultato = risultato;
          }
          
          public float getOp1() {
         	 return operando1;
          } 
          
          public void setOp1(float operando1) {
         	 this.operando1 = operando1;
          }
          
          public float getOp2() {
          	 return operando2;
           } 
           
           public void setOp2(float operando2) {
          	 this.operando2 = operando2;
           }
           
           public String getOperator() {
            	 return operatore;
             } 
             
             public void setOperator(String operatore) {
            	 this.operatore = operatore;
             }
             
             public float getRis() {
              	 return risultato;
               } 
               
               public void setRis (float risultato) {
              	 this.risultato = risultato;
               }
}