package Model;

public class Tessera {
         private String codtessera;
         private int npunti;
         private Cliente proprietario;

         public Tessera(String codtessera, int npunti, Cliente proprietario) {
        	 this.codtessera = codtessera;
        	 this.npunti = npunti;
        	 this.proprietario = proprietario;
         }

         public String getCodT() {
         	 return codtessera;
          }

          public void setCodT(String codtessera) {
         	 this.codtessera = codtessera;
          }

          public int getNPunti() {
          	 return npunti;
           }

           public void setNPunti(int npunti) {
          	 this.npunti = npunti;
           }

           public Cliente getProp() {
           	 return proprietario;
            }

            public void setProp(Cliente proprietario) {
           	 this.proprietario = proprietario;
            }

            @Override
			public String toString() {
            	return (codtessera + " " + npunti + " ");
            }
}
