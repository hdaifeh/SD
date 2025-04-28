/**
 * classe permettant de lancer des simulations pour plusieurs jeux de param�tres
 */

import java.util.*;
import java.io.*;

class MultiSimParam2 {

    Vector jeuxParam = new Vector();
    Vector dossierResult = new Vector();

    // on suppose que les fichiers de param�tres ont l'extension .txt

 MultiSimParam2(String fileName, int nLDebut, int nLFin, int fs) {
        boolean entete = true ;
        for (int i = nLDebut; i <= nLFin; i++) {
            new ExperimentalDesign(fileName, i, fs, entete) ;
            entete=false ;
            System.err.println("jeux de parametres n� " + (i - nLDebut + 1)
                    + " fini");
        }
    }

    MultiSimParam2(String fileName, String nomFichierSortie, int nLDebut, int nLFin, int fs) {
        boolean entete =true ;
        for (int i = nLDebut; i <= nLFin; i++) {
            new ExperimentalDesign (fileName, nomFichierSortie, i, fs, entete) ;
            entete=false ;
            System.err.println("jeux de parametres n� " + (i - nLDebut + 1)
                    + " fini");
        }
    }

    MultiSimParam2(String fileName, int nLDebut, int fs) {
        boolean entete = true ;
        int nLFin = nombreLignes(fileName);
        for (int i = nLDebut; i <= nLFin; i++) {
            //new Dynamics(fileName, i, fs, entete) ;
            //truc();
            new ExperimentalDesign(fileName, i, fs, entete) ;
            entete = false ;
            System.out.println("jeux de parametres n� " + (i - nLDebut + 1)
                    + " fini");
        }
    }

    static int nombreLignes(String fileName) {
        int nLignes = 0;
        int i = 0;
        try {
            FileReader file = new FileReader(fileName);
            StreamTokenizer st = new StreamTokenizer(file);
            while (st.nextToken() != st.TT_EOF)
                ;
            nLignes = st.lineno();
            file.close();
        } catch (Exception e) {
            System.out.println("erreur lecture : " + e.toString());
        }

        return nLignes - 1;
    }

    /*
     * nouvelle version : -i : nom du fichier de param
     * 
     */
    static boolean marqueur(String s) {
        return ((s.substring(0, 1)).equals("-"));
    }

    static void messageParam() {
        System.out.println("#### Rappel : les parametres sont :");
        System.out
                .println("\t nom du fichier de parametres (+ 1ere (+ derniere lignes))");
        System.out.println("\t -o : nom du fichier r�sultats (optionnel)");
        System.out.println("\t -f : fr�quence de sauvegarde (optionnel)");
    }

        /**
     * @param args the command line arguments
     */
    public static void truc() {

    }
    
    public static void main(String[] args) {
        // lecture des param�tres
        int icour = 0;
        String fichierParam = new String("");
        int nLDeb = 2; // lignes de d�but et de fin
        int nLFin = 2;
        int freqSvg = 1 ; // fr�quence des sauvegardes
        String fichierResult = new String("");
        int nargs = args.length;
        boolean problem = false;
        // on lit le nom du fichier de param et �ventuellement les premi�re et
        // derni�re lignes

        if (icour < nargs && !marqueur(args[icour])) {
            fichierParam = args[icour];
            if (++icour < nargs && !marqueur(args[icour])) {
                nLDeb = (Integer.valueOf(args[icour])).intValue();
                if (++icour < nargs && !marqueur(args[icour])) {
                    nLFin = (Integer.valueOf(args[icour++])).intValue();
                } else
                    nLFin = nombreLignes(fichierParam);
            } else
                nLFin = nombreLignes(fichierParam);

            while (icour < nargs) {
                if (args[icour].equals("-o")) {
                    if (++icour < nargs && !marqueur(args[icour])) {
                        fichierResult = args[icour++];
                    } else {
                        problem = true;
                    }
                } else {
                    if (args[icour].equals("-f")) {
                        if (++icour < nargs && !marqueur(args[icour])) {
                            freqSvg = (Integer.valueOf(args[icour++]))
                                    .intValue();
                        } else {
                            problem = true;
                        }
                    }
                }
            }
        } else {
            problem = true;
        }
        if (fichierParam.length() == 0 || problem) {
            messageParam();
        } else {
            if (fichierResult.length() != 0) {
                new MultiSimParam2(fichierParam, fichierResult, nLDeb, nLFin, freqSvg);
            } else {
                new MultiSimParam2(fichierParam, nLDeb, nLFin, freqSvg);

            }
        }
        //*/
    }

} // Fin de la classe

