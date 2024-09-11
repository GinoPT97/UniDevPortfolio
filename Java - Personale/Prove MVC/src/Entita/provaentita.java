package Entita;

public class provaentita {
	private String id;
	private String nome;
    private String contatto;
    private String corso;


    public provaentita(String id, String nome, String contatto, String corso)
    {
        this.id = id;
        this.nome = nome;
        this.contatto =  contatto;
        this.corso = corso;
    }

    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getnome() {
        return nome;
    }

    public void setnome(String nome) {
        this.nome = nome;
    }

    public String getcontatto() {
        return contatto;
    }

    public void setcontatto(String contatto) {
        this.contatto = contatto;
    }

    public String getcorso() {
        return corso;
    }

    public void setcorso(String corso) {
        this.corso = corso;
    }
    
    public String toString() {
    	String pe = getid() + " " + getnome() + " " + getcontatto() + " " + getcorso() + "";
		return pe;
    }
}
