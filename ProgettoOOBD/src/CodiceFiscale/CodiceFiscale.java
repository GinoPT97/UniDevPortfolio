package CodiceFiscale;

import GUI.Controller;
import java.sql.SQLException;

public class CodiceFiscale {

    private char sesso;
    private String nome;
    private String cognome;
    private int giorno;
    private String mese;
    private int anno;
    private String comune;
    private String cf = "";
    private Controller theController;


    public CodiceFiscale(char sesso, String nome, String cognome, int giorno, String mese, int anno, String comune, Controller controller) throws SQLException {
        this.sesso = sesso;
        this.nome = nome;
        this.cognome = cognome;
        this.giorno = giorno;
        this.mese = mese;
        this.anno = anno;
        this.comune = comune;
        theController = controller;
    }

    public String generateCF() throws SQLException {

        //1. Se il cognome ha meno di 3 caratteri
        if (cognome.length() < 3) {
            int i = 0;
            while (i < cognome.length()) {
                if (cognome.charAt(i) != '\0') {
                    cf = cf + cognome.charAt(i);
                    i++;
                }
            }
            while (i < 3) {
                cf = cf + "X";
                i++;
            }
        }
        //2. Se il cognome ha più di 3 caratteri
        else {
            int i = 0;
            int countCons = 0;
            while (i < cognome.length()) {
                if (cognome.charAt(i) != 'A' && cognome.charAt(i) != 'E' && cognome.charAt(i) != 'I' && cognome.charAt(i) != 'O' && cognome.charAt(i) != 'U')
                    countCons++;
                i++;
            }
            //3. Se il numero di consonanti è inferiore a 3
            if (countCons < 3) {
                int j = 0;
                int k = 0;
                while (k < countCons) {
                    if (cognome.charAt(j) != 'A' && cognome.charAt(j) != 'E' && cognome.charAt(j) != 'I' && cognome.charAt(j) != 'O' && cognome.charAt(j) != 'U' && cognome.charAt(j) != ' ') {
                        cf = cf + cognome.charAt(j);
                        k++;
                        j++;
                    } else
                        j++;
                }
                j = 0;
                while (k < 3) {
                    if (cognome.charAt(j) == 'A' || cognome.charAt(j) == 'E' || cognome.charAt(j) == 'I' || cognome.charAt(j) == 'O' || cognome.charAt(j) == 'U') {
                        cf = cf + cognome.charAt(j);
                        k++;
                        j++;
                    } else
                        j++;
                }
            }
            //4. Se il numero di consonanti è almeno pari a 3
            else {
                int j = 0;
                int k = 0;
                while (j < 3) {
                    if (cognome.charAt(k) != 'A' && cognome.charAt(k) != 'E' && cognome.charAt(k) != 'I' && cognome.charAt(k) != 'O' && cognome.charAt(k) != 'U' && cognome.charAt(j) != ' ') {
                        cf = cf + cognome.charAt(k);
                        k++;
                        j++;
                    } else
                        k++;
                }
            }

            //Caratteri del nome:
            //Si applica lo stesso algoritmo impiegato per il cognome

            if (nome.length() < 3) {
                int y = 0;
                while (y < nome.length()) {
                    if (nome.charAt(y) != '\0') {
                        cf = cf + nome.charAt(y);
                        y++;
                    }
                }
                while (y < 3) {
                    cf = cf + "X";
                    y++;
                }
            } else {
                int x = 0;
                int countConso = 0;
                while (x < nome.length()) {
                    if (nome.charAt(x) != 'A' && nome.charAt(x) != 'E' && nome.charAt(x) != 'I' && nome.charAt(x) != 'O' && nome.charAt(x) != 'U')
                        countConso++;
                    x++;
                }
                if (countConso < 3) {
                    int j = 0;
                    int k = 0;
                    while (k < countConso) {
                        if (nome.charAt(j) != 'A' && nome.charAt(j) != 'E' && nome.charAt(j) != 'I' && nome.charAt(j) != 'O' && nome.charAt(j) != 'U' && nome.charAt(j) != ' ') {
                            cf = cf + nome.charAt(j);
                            k++;
                            j++;
                        } else
                            j++;
                    }
                    j = 0;
                    while (k < 3) {
                        if (nome.charAt(j) == 'A' || nome.charAt(j) == 'E' || nome.charAt(j) == 'I' || nome.charAt(j) == 'O' || nome.charAt(j) == 'U') {
                            cf = cf + nome.charAt(j);
                            k++;
                            j++;
                        } else
                            j++;
                    }
                } else {
                    int j = 0;
                    int k = 0;
                    while (j < 3) {
                        if (nome.charAt(k) != 'A' && nome.charAt(k) != 'E' && nome.charAt(k) != 'I' && nome.charAt(k) != 'O' && nome.charAt(k) != 'U') {
                            cf = cf + nome.charAt(k);
                            k++;
                            j++;
                        } else
                            k++;
                    }
                }
            }
        }

        //Caratteri dell'anno di nascita:

        cf = cf + String.valueOf(anno).substring(2);

        //Carattere del mese:

        switch (mese)   {
            case "Gennaio": {cf = cf + "A"; break;}
            case "Febbraio" : {cf = cf + "B"; break;}
            case "Marzo" : {cf = cf + "C"; break;}
            case "Aprile" : {cf = cf + "D"; break;}
            case "Maggio" : {cf = cf + "E"; break;}
            case "Giugno" : {cf = cf + "H"; break;}
            case "Luglio" : {cf = cf + "L"; break;}
            case "Agosto" : {cf = cf + "M"; break;}
            case "Settembre" : {cf = cf + "P"; break;}
            case "Ottobre" : {cf = cf + "R"; break;}
            case "Novembre" : {cf = cf + "S"; break;}
            case "Dicembre" : {cf = cf + "T"; break;}
            default: {System.out.println("Mese non valido!"); break;}
        }

        //Caratteri del giorno di nascita:
        System.out.println(giorno+40);
        if (sesso == 'M')
            cf = cf + giorno;
        else if (sesso == 'F')
            cf = cf + String.valueOf(giorno + 40);

        //Caratteri per il comune di nascita:
        cf = cf + theController.getCodiceCatastaleByComune(comune);

        //Carattere di controllo:

        int sommaPari=0;
        for (int a=1;a<=13;a+=2) {
            switch (cf.charAt(a)) {
                case '0': {sommaPari+=0;break;}
                case '1': {sommaPari+=1;break;}
                case '2': {sommaPari+=2;break;}
                case '3': {sommaPari+=3;break;}
                case '4': {sommaPari+=4;break;}
                case '5': {sommaPari+=5;break;}
                case '6': {sommaPari+=6;break;}
                case '7': {sommaPari+=7;break;}
                case '8': {sommaPari+=8;break;}
                case '9': {sommaPari+=9;break;}
                case 'A': {sommaPari+=0;break;}
                case 'B': {sommaPari+=1;break;}
                case 'C': {sommaPari+=2;break;}
                case 'D': {sommaPari+=3;break;}
                case 'E': {sommaPari+=4;break;}
                case 'F': {sommaPari+=5;break;}
                case 'G': {sommaPari+=6;break;}
                case 'H': {sommaPari+=7;break;}
                case 'I': {sommaPari+=8;break;}
                case 'J': {sommaPari+=9;break;}
                case 'K': {sommaPari+=10;break;}
                case 'L': {sommaPari+=11;break;}
                case 'M': {sommaPari+=12;break;}
                case 'N': {sommaPari+=13;break;}
                case 'O': {sommaPari+=14;break;}
                case 'P': {sommaPari+=15;break;}
                case 'Q': {sommaPari+=16;break;}
                case 'R': {sommaPari+=17;break;}
                case 'S': {sommaPari+=18;break;}
                case 'T': {sommaPari+=19;break;}
                case 'U': {sommaPari+=20;break;}
                case 'V': {sommaPari+=21;break;}
                case 'W': {sommaPari+=22;break;}
                case 'X': {sommaPari+=23;break;}
                case 'Y': {sommaPari+=24;break;}
                case 'Z': {sommaPari+=25;break;}
            }
        }
        int sommaDispari=0;
        for (int a=0;a<14;a+=2) {
            switch (cf.charAt(a)) {
                case '0': {sommaDispari+=1;break;}
                case '1': {sommaDispari+=0;break;}
                case '2': {sommaDispari+=5;break;}
                case '3': {sommaDispari+=7;break;}
                case '4': {sommaDispari+=9;break;}
                case '5': {sommaDispari+=13;break;}
                case '6': {sommaDispari+=15;break;}
                case '7': {sommaDispari+=17;break;}
                case '8': {sommaDispari+=19;break;}
                case '9': {sommaDispari+=21;break;}
                case 'A': {sommaDispari+=1;break;}
                case 'B': {sommaDispari+=0;break;}
                case 'C': {sommaDispari+=5;break;}
                case 'D': {sommaDispari+=7;break;}
                case 'E': {sommaDispari+=9;break;}
                case 'F': {sommaDispari+=13;break;}
                case 'G': {sommaDispari+=15;break;}
                case 'H': {sommaDispari+=17;break;}
                case 'I': {sommaDispari+=19;break;}
                case 'J': {sommaDispari+=21;break;}
                case 'K': {sommaDispari+=2;break;}
                case 'L': {sommaDispari+=4;break;}
                case 'M': {sommaDispari+=18;break;}
                case 'N': {sommaDispari+=20;break;}
                case 'O': {sommaDispari+=11;break;}
                case 'P': {sommaDispari+=3;break;}
                case 'Q': {sommaDispari+=6;break;}
                case 'R': {sommaDispari+=8;break;}
                case 'S': {sommaDispari+=12;break;}
                case 'T': {sommaDispari+=14;break;}
                case 'U': {sommaDispari+=16;break;}
                case 'V': {sommaDispari+=10;break;}
                case 'W': {sommaDispari+=22;break;}
                case 'X': {sommaDispari+=25;break;}
                case 'Y': {sommaDispari+=24;break;}
                case 'Z': {sommaDispari+=23;break;}
            }
        }
        int interoControllo = (sommaPari+sommaDispari)%26;
        String carattereControllo="";
        switch (interoControllo) {
            case 0:{carattereControllo="A";break;}
            case 1:{carattereControllo="B";break;}
            case 2:{carattereControllo="C";break;}
            case 3:{carattereControllo="D";break;}
            case 4:{carattereControllo="E";break;}
            case 5:{carattereControllo="F";break;}
            case 6:{carattereControllo="G";break;}
            case 7:{carattereControllo="H";break;}
            case 8:{carattereControllo="I";break;}
            case 9:{carattereControllo="J";break;}
            case 10:{carattereControllo="K";break;}
            case 11:{carattereControllo="L";break;}
            case 12:{carattereControllo="M";break;}
            case 13:{carattereControllo="N";break;}
            case 14:{carattereControllo="O";break;}
            case 15:{carattereControllo="P";break;}
            case 16:{carattereControllo="Q";break;}
            case 17:{carattereControllo="R";break;}
            case 18:{carattereControllo="S";break;}
            case 19:{carattereControllo="T";break;}
            case 20:{carattereControllo="U";break;}
            case 21:{carattereControllo="V";break;}
            case 22:{carattereControllo="W";break;}
            case 23:{carattereControllo="X";break;}
            case 24:{carattereControllo="Y";break;}
            case 25:{carattereControllo="Z";break;}
        }
        cf = cf + carattereControllo;
        return cf;
    }
}
