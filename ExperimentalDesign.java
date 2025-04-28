import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author : Sylvie Huet
 * @version : June 2005 This class launches the individual-centred model for
 * studying information transmission
 */
class ExperimentalDesign {

    boolean printTrajectory = false; // to write the details of a detailed trajectory
    PrintStream console = System.out;
    PrintStream ps;
    FileOutputStream fic;
    static String nomFichier;
    double[] paramsTerritory;
    int nameExpe;

    int nbYearsSimu;
    int nbSubterritories;
    double[][] paramsSubTerritories;

    /**
     * population having to decide on the object
     */
    public Territory myDyn;

    /*
     * public static void main(String[] args) { nomFichier = args[0]; new
     * ModeleInfoTrans(nomFichier); }
     */
    public ExperimentalDesign(String param, int ligne, int fs, boolean entete) {
        ecritureResultats("fichierResultat.txt");
        console.println("launching the simulation with file " + param);
        lectureEntree(param, ligne, fs, entete);
        System.err.println("The simulation is finished.");
        try {
            fic.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("problem closing the file");
        }
    }

    public ExperimentalDesign(String param, String nomFichR, int ligne, int fs, boolean entete) {
        ecritureResultats(nomFichR);
        console.println("launching the simulation with file " + param + " the result file is " + nomFichR);
        lectureEntree(param, ligne, fs, entete);
        try {
            fic.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("problem closing the file");
        }
    }

    String paramFileName;

    /**
     * Reading the characteristics of the population
     */
    public void lectureEntree(String fileName, int ligne, int frequenceSauvegarde, boolean entete) {
        try {
            paramFileName = fileName;
            // opening the file.
            FileReader file = new FileReader(fileName);
            StreamTokenizer st = new StreamTokenizer(file);
            while (st.lineno() < ligne) {
                st.nextToken();
            }
            int nbParamTerritory = 8;
            paramsTerritory = new double[nbParamTerritory];
            Arrays.fill(paramsTerritory, 0.0);
            nameExpe = (int) st.nval;
            paramsTerritory[0] = (double) nameExpe;//
            st.nextToken();
            paramsTerritory[1] = (double) st.nval;// g ti anti-waste
            st.nextToken();
            paramsTerritory[2] = (double) st.nval; // g
            st.nextToken();
            // paramsTerritory[3] = (double) st.nval; // objGaspi
            // st.nextToken();
            nbYearsSimu = (int) st.nval; // nbYears of simulation
            paramsTerritory[3] = (double) nbYearsSimu;
            st.nextToken();
            nbSubterritories = (int) st.nval; // nbYears of simulation
            paramsTerritory[4] = (double) nbSubterritories;
            st.nextToken();
            paramsTerritory[5] = (double) st.nval; // methane digester capacity common for the whole territory
            st.nextToken();
            paramsTerritory[6] = (double) st.nval; // incinerator capacity common for the whole territory
            st.nextToken();
            paramsTerritory[7] = (double) st.nval; // professional compost platform capacity common for the whole territory
            st.nextToken();
            int nbParamToDescribeSubTerritory = 24;
            paramsSubTerritories = new double[nbSubterritories][nbParamToDescribeSubTerritory];
            double[] paramSubT;
            for (int a = 0; a < nbSubterritories; a++) {
                paramSubT = new double[nbParamToDescribeSubTerritory];
                Arrays.fill(paramSubT, 0.0);
                paramSubT[0] = (double) st.nval; // numeric name of the sub-territory
                st.nextToken();
                paramSubT[1] = (double) st.nval; // tiLogistLocalCompost
                st.nextToken();
                paramSubT[2] = (double) st.nval; // tiLogisticCollect
                st.nextToken();
                paramSubT[3] = (double) st.nval; // tiPracticCompostLocal
                st.nextToken();
                paramSubT[4] = (double) st.nval; // tiPracticTriOrdure
                st.nextToken();
                paramSubT[5] = (double) st.nval; // Ba
                st.nextToken();
                paramSubT[6] = (double) st.nval; // Bv
                st.nextToken();
                paramSubT[7] = (double) st.nval; // alpha_1base food waste
                st.nextToken();
                paramSubT[8] = (double) st.nval; // alpha_1base green waste
                st.nextToken();
                paramSubT[9] = (double) st.nval; // alpha2base food waste // sorting practice rate for door-to-door collection of food waste
                st.nextToken();
                paramSubT[10] = (double) st.nval; // alpha2objectives food waste
                // System.err.println(paramSubT[10]);
                st.nextToken();
                paramSubT[11] = (double) st.nval; // increase alpha1 food waste
                st.nextToken();
                paramSubT[12] = (double) st.nval; // increase alpha1 green waste
                st.nextToken();
                paramSubT[13] = (double) st.nval; // alpha2base green waste // sorting practice rate for door-to-door collection of green waste
                st.nextToken();
                paramSubT[14] = (double) st.nval; // alpha2base green waste // sorting practice rate for door-to-door collection of green waste
                st.nextToken();
                paramSubT[15] = (double) st.nval; // K1 local composting capacity init
                st.nextToken();
                paramSubT[16] = (double) st.nval; // K1 local composting capacity target 2025
                st.nextToken();
                paramSubT[17] = (double) st.nval; // KA door-to-door collection capacity init
                st.nextToken();
                paramSubT[18] = (double) st.nval; // KA door-to-door collection capacity 2025
                st.nextToken();
                paramSubT[19] = (double) st.nval; // size of the sub-population
                st.nextToken();
                paramSubT[20] = (double) st.nval; // annual population growth
                st.nextToken();
                paramSubT[21] = (double) st.nval; // tiActionsEvitementDechetsVerts
                st.nextToken();
                paramSubT[22] = (double) st.nval; // tauxEvitementDechetsVertsHorizon2024
                st.nextToken();
                paramSubT[23] = (double) st.nval; // anti-waste of the sub-territory
                st.nextToken();
                // 23
                
                // System.err.println(paramSubT[0] +" "+paramSubT[1]+" "+paramSubT[9] +" "+paramSubT[10]+" "+paramSubT[11] +" "+paramSubT[12]+" "+paramSubT[13] +" "+paramSubT[14]+" "+paramSubT[15] +" "+paramSubT[16]) ;
                for (int b = 0; b < nbParamToDescribeSubTerritory; b++) {
                    paramsSubTerritories[a][b] = paramSubT[b];
                }
                paramSubT = null;
            }
            // Writing the header of the result file in the experiement design
            if (entete & !printTrajectory) {

                System.out.print("nameOftheParametersFile" + ";");
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("NameSubTerritory" + ";");
                    System.out.print("alpha1ObjDA" + i + ";");
                    System.out.print("alpha1ObjDV" + i + ";");
                    System.out.print("mc" + i + ";");
                    System.out.print("alpha2ObjDA" + i + ";");
                    System.out.print("alpha2ObjDV" + i + ";"); //
                    System.out.print("ms" + i + ";");
                }
                System.out.print("t" + ";"); //
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("nbKgCollectHabDesserviSubTerrit" + i + ";"); // print nbKgCollectHabDesserviSubTerrit
                }
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("partPopDesserviCollDAlim" + i + ";");
                }
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("nbKgDechetAlimOMRByHabitant" + i + ";");
                }
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("alpha_cf" + i + ";");
                }
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("alpha_cg" + i + ";");
                }
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("alpha_sf" + i + ";");
                }
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("alpha_sg" + i + ";");
                }
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("Kc_courant" + i + ";");
                }
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("Ks_courant" + i + ";");
                }
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("TauxEvolDechetsVertsDansDechetterie" + i + ";");
                }
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("Bg" + i + ";");
                }
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("Bsg" + i + ";");
                }
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("Usg" + i + ";");
                }
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("Bvg" + i + ";");
                }
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("Bcg" + i + ";");
                }
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("Ucg" + i + ";");
                }
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("Bf" + i + ";");
                }
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("Bsf" + i + ";");
                }
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("Usf" + i + ";");
                }
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("Bcf" + i + ";");
                }
                for (int i = 0; i < nbSubterritories; i++) {
                    System.out.print("Ucf" + i + ";");
                }

                System.out.print("nbKgDechetAlimOMRByHabitantGlobal" + "" + ";");
                System.out.print("sizePop" + ";");
                System.out.print("DiminutionGaspillageAlimentaire" + ";"); // the reduction of food waste 
                System.out.print("ProducedBioWasteTotale" + ";");
                System.out.print("ProducedBioWasteAlimentaire" + ";");
                System.out.print("ProducedBioWasteVerts" + ";");
                System.out.print("OMR" + ";");
                System.out.print("compostage local" + ";"); // quantity in local composting
                System.out.print("déchetterie" + ";"); // quantity in local composting ??? no in waste collection centre
                System.out.print("qteEntrantMethaniseur" + ";"); // quantity entering methane digester
                System.out.print("qteTraiteesParMethaniseur" + ";"); // quantity processed by methane digester
                System.out.print("compostage professionnel" + ";"); // quantity in professional composting
                System.out.print("incinerateur" + ";"); // quantity in the incinerator
                System.out.print("tauxValorisationbiowaste" + ";");
                System.out.print("nbSolutionsForFoodPerHab" + ";");
                System.out.print("nbSolutionsForGreenPerHab" + ";");
                System.out.print("tauxReductionDechetsVerts" + ";");
                System.out.print("increaseOfMethaniseFoodwaste" + ";");// "objective" increasing food waste in methanisation unit
                System.out.print("multiplicateurVolumeBiodechetOMR" + ";");
                System.out.print("totalIntentionForFoodWaste" + ";");
                System.out.print("totalIntentionForGreenWasteWithoutDechetterie" + ";");
                System.out.print("valorizationRateSufficientAt12" + ";");
                System.out.print("oneSolutionAtLeastPerHabForFoodAt6" + ";");
                System.out.print("oneSolutionAtLeastPerHabForGreen" + ";");
                System.out.print("tauxDiminutionDechetsVertsCorrect" + ";");
                System.out.print("correctIncreaseMethaniseFoodwasteCorrect" + ";");
                System.out.print("diminutionVolumeBiodechetOMR" + ";");
                System.out.print("checkCoherenceFluxStage1" + ";"); //
                System.out.print("checkCoherenceFluxStage2" + ";"); //
                System.out.print("year"+ ";");
                System.out.print("EvolVolumeDADansOMR" + ";");
                System.out.print("NombreObjectifsRespectes" + ";");
                System.out.print("4ObjectifsAtteints" + ";");
                System.out.println();
                entete = false;
            }
            if (entete & printTrajectory ) {
                
                for (int i = 1; i <= nbSubterritories; i++) { // the title of the results output file when printtrajectory is true
                    System.out.print("ident" + i + ";"); // it gave ident 0 , ident 1, ident 2,....ident 8 ( the subterritories number)
                    System.out.print("population" + i + ";");
                    System.out.print("Bf" + i + ";");
                    System.out.print("Bg" + i + ";");
                    System.out.print("alpha_cf" + i + ";");
                    System.out.print("alpha_cg" + i + ";");
                    System.out.print("Bcf" + i + ";");
                    System.out.print("Ucf" + i + ";");
                    System.out.print("Bcg" + i + ";");
                    System.out.print("Ucg" + i + ";");
                    System.out.print("Kc_courant" + i + ";");
                    System.out.print("alpha_sf" + i + ";");
                    System.out.print("alpha_sg" + i + ";");
                    System.out.print("Bsf" + i + ";");
                    System.out.print("Usf" + i + ";");
                    System.out.print("Bsg" + i + ";");
                    System.out.print("Usg" + i + ";");
                    System.out.print("Ks_courant" + i + ";");
                    System.out.print("Brf" + i + ";");
                    System.out.print("alpha_vg" + i + ";");
                    System.out.print("Bvg" + i + ";");
                }
                
                System.out.print("year" + ";");
                System.out.print("Bc+" + ";");
                System.out.print("Bmf" + ";");
                System.out.print("Umf" + ";");
                System.out.print("Bmg" + ";");
                System.out.print("Umg" + ";");
                System.out.print("Bif" + ";");
                System.out.println();
                entete = false;
            }
            // vary b and qa and qv to define different territories
            // int posAlpha3 = 0 ;
            // for (double z = 0.0; z < 0.45; z = z + 0.05) {// variation of alpha1base, base value 0.347
            // params[4] = z;

            // /* Calibration Bpf (and implicitly alpha1Base, assuming anti food waste = 0 [at least initially in 2018])
            // /*
            if (!printTrajectory) { // that is to make a large exploration:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                double stepToExplore = 0.002;
                
                
                double[] testObjectifCompostLocalDa = {1.0};// Br ... ofc , Br einit c 
                
                
                double[] testObjectifCompostLocalDv = { 1.0};
                
                // double[] testObjectifTriCollecteDa = {0, 0.05, 0.1, 0.15, 0.2, 0.3, 0.5, 0.7, 0.9, 1.0};
                
                double[] testObjectifTriCollecteDa = { 1.0};
                
                // double[] testObjectifTriCollecteDv = {0, 0.05, 0.1, 0.15, 0.2, 0.3, 0.5, 0.7, 0.9, 1.0}; // initial value 0.3600 for CAM only
                
                double[] testObjectifTriCollecteDv = { 1.0};; // initial value 0.3600 for CAM only
                
               
                // double[] vitesseAdoptPraticCompost = {2, 4, 6, 8}; // reference ti evolution practice 5, , 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14
                double[] vitesseAdoptPraticCompost = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}; // reference ti evolution practice 5  m^c
                
                // double[] vitesseAdoptPracticCollecte = {2, 4, 6, 8}; // reference ti evolution practice 5
                double[] vitesseAdoptPracticCollecte = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}; // reference ti evolution practice 5  m^s
                
                 

// objective parameters local composting behaviour 11 and 12, sorting for door-to-door collection 10 and 14
                // /*
                for (int i = 0; i < testObjectifCompostLocalDa.length; i = i + 1) { // territory 0 SBA
                    for (int z = 0; z < nbSubterritories; z++) {
                        paramsSubTerritories[z][11] = testObjectifCompostLocalDa[i]; // food waste local composting base increase of 0.3
                    }
                    for (int x = 0; x < testObjectifCompostLocalDv.length; x = x + 1) {
                        for (int z = 0; z < nbSubterritories; z++) {
                            paramsSubTerritories[z][12] = testObjectifCompostLocalDv[x];
                        }
                        for (int k = 0; k < vitesseAdoptPraticCompost.length; k = k + 1) { // variations in the speed of implementation of food waste logistics for SBA
                            for (int z = 0; z < nbSubterritories; z++) {
                                paramsSubTerritories[z][3] = vitesseAdoptPraticCompost[k]; // 
                            }
                            for (int y = 0; y < testObjectifTriCollecteDa.length; y = y + 1) {
                                // for regions with door-to-door collection only (SBA, CAM) 
                                paramsSubTerritories[0][10] = testObjectifTriCollecteDa[y];
                                paramsSubTerritories[1][10] = testObjectifTriCollecteDa[y];
                                for (int j = 0; j < testObjectifTriCollecteDv.length; j = j + 1) {
                                    // Only CAM, territory 1 practices door-to-door collection of green waste
                                    paramsSubTerritories[1][14] = testObjectifTriCollecteDv[j]; // green waste value at time 0 0.36
                                    for (int l = 0; l < vitesseAdoptPracticCollecte.length; l++) {
                                        // for regions with door-to-door collection only (SBA, CAM) 
                                        paramsSubTerritories[0][4] = vitesseAdoptPracticCollecte[l]; //
                                        paramsSubTerritories[1][4] = vitesseAdoptPracticCollecte[l];
                                        // }
                                        myDyn = null;
                                        myDyn = new Territory(nbYearsSimu, nbSubterritories, paramsTerritory, paramsSubTerritories, printTrajectory);
                                        indicatorsObjectives(nbYearsSimu);
                                        ecritureResultatsComputed(nbYearsSimu);
                                    }
                                    

                                }
                            }
                        }
                    }
                }
            } else { // this is just to print a trajectory (ie printTrajectory = true ;
                myDyn = null;
                myDyn = new Territory(nbYearsSimu, nbSubterritories, paramsTerritory, paramsSubTerritories, printTrajectory);
                indicatorsObjectives(nbYearsSimu);
                ecritureTrajectoire(nbYearsSimu);
            }
        } catch (Exception e) {
            System.err.println("reading error : " + e.toString());
            e.printStackTrace();
            System.out.println("reading error : " + e.toString());
        }
    }

   
    
    public void ecritureResultats(String nameResult) {
        try {
            fic = new FileOutputStream(nameResult, true);
            ps = new PrintStream(fic);
            System.setOut(ps);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
            System.out.println("I have a problem writing the results");
        }

    }

    /**
     * Writing the results
     */
    public void ecritureResultatsComputed(int nbYears) {
        try {
            double fq = 0.0;
            int bound = nbYears + 1;
            //for (int i = 1; i < bound; i++) {
            int a ;
            int b ;
            if (this.printTrajectory) { a=1; b=bound ; }
            else { a=7 ; b =8 ; } // année 2024:::{ a=7 ; b =8 ; }:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::2024!!!!!!!
            for (int i = a; i < b; i++) { // annee 2030 est 13, 23 est 2040
                
                System.out.print(paramFileName + ";");
                for (int v = 0; v < nbSubterritories; v++) {
                    System.out.print(v + ";");
                    System.out.print(Double.toString(paramsSubTerritories[v][11]).replace(".", ",") + ";");
                    System.out.print(Double.toString(paramsSubTerritories[v][12]).replace(".", ",") + ";");
                    System.out.print(Double.toString(paramsSubTerritories[v][3]).replace(".", ",") + ";");
                    System.out.print(Double.toString(paramsSubTerritories[v][10]).replace(".", ",") + ";");
                    System.out.print(Double.toString(paramsSubTerritories[v][14]).replace(".", ",") + ";");
                    System.out.print(Double.toString(paramsSubTerritories[v][4]).replace(".", ",") + ";");
                }
                System.out.print(i + ";");
                int v = 0;
                for (v = 0; v < nbSubterritories; v++) {
                    System.out.print(Double.toString(myDyn.myTerrit[v].nbKgCollectHabDesservi[i]).replace(".", ",") + ";");
                }
                for (v = 0; v < nbSubterritories; v++) {
                    System.out.print(Double.toString(myDyn.myTerrit[v].propPopDesserviCollDA[i]).replace(".", ",") + ";");
                }
                for (v = 0; v < nbSubterritories; v++) {
                    System.out.print(Double.toString(myDyn.myTerrit[v].nbKgOMRHab[i]).replace(".", ",") + ";");
                }
                for (v = 0; v < nbSubterritories; v++) {
                    System.out.print(Double.toString(myDyn.myTerrit[v].αcf[i]).replace(".", ",") + ";");
                }
                for (v = 0; v < nbSubterritories; v++) {
                    System.out.print(Double.toString(myDyn.myTerrit[v].αcg[i]).replace(".", ",") + ";");
                }
                for (v = 0; v < nbSubterritories; v++) {
                    System.out.print(Double.toString(myDyn.myTerrit[v].αsf[i]).replace(".", ",") + ";");
                }
                for (v = 0; v < nbSubterritories; v++) {
                    System.out.print(Double.toString(myDyn.myTerrit[v].αsg[i]).replace(".", ",") + ";");
                }
                for (v = 0; v < nbSubterritories; v++) {
                    System.out.print(Double.toString(myDyn.myTerrit[v].Kct[i]).replace(".", ",") + ";");
                }
                for (v = 0; v < nbSubterritories; v++) {
                    System.out.print(Double.toString(myDyn.myTerrit[v].Kst[i]).replace(".", ",") + ";");
                }
                for (v = 0; v < nbSubterritories; v++) {
                    System.out.print(Double.toString(myDyn.myTerrit[v].tauxReductionDechetVert[i]).replace(".", ",") + ";");
                }
                ///*
                for (v = 0; v < nbSubterritories; v++) {
                    System.out.print(Double.toString(myDyn.myTerrit[v].Bpg[i]).replace(".", ",") + ";");
                }
                for (v = 0; v < nbSubterritories; v++) {
                    System.out.print(Double.toString(myDyn.myTerrit[v].Bsg[i]).replace(".", ",") + ";");
                }
                for (v = 0; v < nbSubterritories; v++) {
                    System.out.print(Double.toString(myDyn.myTerrit[v].Usg[i]).replace(".", ",") + ";");
                }
                for (v = 0; v < nbSubterritories; v++) {
                    System.out.print(Double.toString(myDyn.myTerrit[v].Bv[i]).replace(".", ",") + ";");
                }
                for (v = 0; v < nbSubterritories; v++) {
                    System.out.print(Double.toString(myDyn.myTerrit[v].Bcg[i]).replace(".", ",") + ";");
                }
                for (v = 0; v < nbSubterritories; v++) {
                    System.out.print(Double.toString(myDyn.myTerrit[v].Ucg[i]).replace(".", ",") + ";");
                }
                for (v = 0; v < nbSubterritories; v++) {
                    System.out.print(Double.toString(myDyn.myTerrit[v].Bpf[i]).replace(".", ",") + ";");
                }
                for (v = 0; v < nbSubterritories; v++) {
                    System.out.print(Double.toString(myDyn.myTerrit[v].Bsf[i]).replace(".", ",") + ";");
                }
                for (v = 0; v < nbSubterritories; v++) {
                    System.out.print(Double.toString(myDyn.myTerrit[v].Usf[i]).replace(".", ",") + ";");
                }
                for (v = 0; v < nbSubterritories; v++) {
                    System.out.print(Double.toString(myDyn.myTerrit[v].Bcf[i]).replace(".", ",") + ";");
                }
                for (v = 0; v < nbSubterritories; v++) {
                    System.out.print(Double.toString(myDyn.myTerrit[v].Ucf[i]).replace(".", ",") + ";");
                }
                //*/
                System.out.print(Double.toString(kgDechetAlimDansOMRByHab[i]).replace(".", ",") + ";");
                System.out.print(Double.toString(myDyn.Pall[i]).replace(".", ",") + ";");
                System.out.print(Double.toString(myDyn.ABPall[i]).replace(".", ",") + ";");
                System.out.print(Double.toString(myDyn.Ball[i]).replace(".", ",") + ";");
                System.out.print(Double.toString(myDyn.Bfall[i]).replace(".", ",") + ";");
                System.out.print(Double.toString(myDyn.Bgall[i]).replace(".", ",") + ";");
                System.out.print(Double.toString(myDyn.Brall[i]).replace(".", ",") + ";");
                System.out.print(Double.toString(myDyn.Bcall[i]).replace(".", ",") + ";");
                System.out.print(Double.toString(myDyn.Bvgall[i]).replace(".", ",") + ";");
                double qteArrivantMethaniseur = myDyn.myCommonEquip.Bmf[i] + myDyn.myCommonEquip.Bmg[i]; ////////////////////Coooooooooool
                System.out.print(Double.toString(qteArrivantMethaniseur).replace(".", ",") + ";");
                System.out.print(Double.toString(myDyn.myCommonEquip.Bm[i]).replace(".", ",") + ";");
                System.out.print(Double.toString(myDyn.myCommonEquip.Bcc[i]).replace(".", ",") + ";");
                System.out.print(Double.toString(myDyn.myCommonEquip.Bi[i]).replace(".", ",") + ";");
                System.out.print(Double.toString(valorizationRate[i]).replace(".", ",") + ";");
                System.out.print(Double.toString(nbSolutionsForAllForFood[i]).replace(".", ",") + ";");
                System.out.print(Double.toString(nbSolutionsForAllForGreen[i]).replace(".", ",") + ";");
                System.out.print(Double.toString(tauxEvolutionGreenWaste[i]).replace(".", ",") + ";");
                System.out.print(Double.toString(increaseOfMethanisedFoodWaste[i]).replace(".", ",") + ";");
                System.out.print(Double.toString(multiplicateurVolumeBiodechetOMR[i]).replace(".", ",") + ";");//////////////////////////////////////////////////////////////////////////////
                System.out.print(Double.toString(totalIntentionForFoodWaste[i]).replace(".", ",") + ";");
                System.out.print(Double.toString(totalIntentionForGreenWasteWithoutDechetterie[i]).replace(".", ",") + ";");
                System.out.print(goodValorRate[i] + ";");
                System.out.print(solutionForAllForFood[i] + ";");
                System.out.print(solutionForAllForGreen[i] + ";");
                System.out.print(diminutionGreenWaste[i] + ";");
                System.out.print(correctIncreaseOfMethanisedFoodWaste[i] + ";");
                System.out.print(diminutionVolBiodechetOMR[i] + ";");
                System.out.print(myDyn.checkFluxStage1[i] + ";");
                System.out.print(myDyn.checkFluxStage2[i] + ";");
                System.out.print((2017 + i) + ";");
                System.out.print(volMethanised[i] + ";");
                System.out.print(sommeRespectedObjectifs[i] + ";");
                System.out.print(correct[i] + ";");
                System.out.println();
                //}
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
            System.out.println("j'ai un probl�me d'�criture des r�sultats");
        }

    }

    public void ecritureTrajectoire(int nbYears) {
        for (int i = 0 ; i < nbYears; i++) {
        //for (int i = 1; i < nbYears; i++) {
            for (int j = 0; j < nbSubterritories; j++) {
                myDyn.myTerrit[j].printTrajectory(i);
            }
            myDyn.myCommonEquip.printTrajectory(i);
        }
    }

    double[] valorizationRate;
    int[] goodValorRate;
    double[] nbSolutionsForAllForGreen;
    double[] nbSolutionsForAllForFood;
    int[] solutionForAllForGreen;
    int[] solutionForAllForFood; // obligations réglementaires
    double[] tauxEvolutionGreenWaste; // objectifs environnementaux
    double[] increaseOfMethanisedFoodWaste; // objectifs environnementaux
    int[] diminutionGreenWaste;
    int[] correctIncreaseOfMethanisedFoodWaste;
    int[] volMethanised;
    double[] multiplicateurVolumeBiodechetOMR; // objectifs environnementaux
    int[] diminutionVolBiodechetOMR;
    double[] kgDechetAlimDansOMRByHab;
    double[] totalIntentionForFoodWaste;
    double[] totalIntentionForGreenWasteWithoutDechetterie;
    int[] correct;
    int[] sommeRespectedObjectifs ;

    public void indicatorsObjectives(int nbYears) { // pour évaluer atteinte des objectifs données par gouvernement français
        int bound = nbYears + 1;
        // Volume of hsh waste = Ball - compostage local (réduction de 15 % par rapport à 2018 attendue pour 2030
        totalIntentionForFoodWaste = new double[bound];
        Arrays.fill(totalIntentionForFoodWaste, 0.0);
        totalIntentionForGreenWasteWithoutDechetterie = new double[bound];
        Arrays.fill(totalIntentionForGreenWasteWithoutDechetterie, 0.0);
        valorizationRate = new double[bound];
        Arrays.fill(valorizationRate, 0.0);
        goodValorRate = new int[bound];
        Arrays.fill(goodValorRate, 1);
        nbSolutionsForAllForGreen = new double[bound];
        nbSolutionsForAllForFood = new double[bound];
        Arrays.fill(nbSolutionsForAllForFood, 0.0);
        Arrays.fill(nbSolutionsForAllForGreen, 0.0);
        solutionForAllForGreen = new int[bound];
        solutionForAllForFood = new int[bound];
        Arrays.fill(solutionForAllForFood, 1);
        Arrays.fill(solutionForAllForGreen, 1);
        tauxEvolutionGreenWaste = new double[bound];
        increaseOfMethanisedFoodWaste = new double[bound];
        Arrays.fill(tauxEvolutionGreenWaste, 0.0);
        Arrays.fill(increaseOfMethanisedFoodWaste, 0.0);
        diminutionGreenWaste = new int[bound];
        correctIncreaseOfMethanisedFoodWaste = new int[bound];
        Arrays.fill(diminutionGreenWaste, 1);
        Arrays.fill(correctIncreaseOfMethanisedFoodWaste, 1);
        multiplicateurVolumeBiodechetOMR = new double[bound]; // objectifs environnementaux
        Arrays.fill(multiplicateurVolumeBiodechetOMR, 0.0);
        diminutionVolBiodechetOMR = new int[bound];;
        Arrays.fill(diminutionVolBiodechetOMR, 1);
        volMethanised = new int[bound];;
        Arrays.fill(volMethanised, 1);
        kgDechetAlimDansOMRByHab = new double[bound];;
        Arrays.fill(kgDechetAlimDansOMRByHab, 0.0);
        correct = new int[bound];
        Arrays.fill(correct, 0);
        sommeRespectedObjectifs = new int[bound];
        Arrays.fill(sommeRespectedObjectifs, 0);
        for (int i = 1; i < bound; i++) {
            valorizationRate[i] = (myDyn.myCommonEquip.Bm[i] + myDyn.myCommonEquip.Bcc[i]) / (myDyn.Ball[i] - myDyn.Bcall[i]);
            double totalCapacityForFoodWaste = 0.0;
            double totalProducedFoodWaste = 0.0;
            for (int j = 0; j < nbSubterritories; j++) {
                totalCapacityForFoodWaste = totalCapacityForFoodWaste + myDyn.myTerrit[j].Kct[i] + myDyn.myTerrit[j].Kst[i];
                totalProducedFoodWaste = totalProducedFoodWaste + myDyn.myTerrit[j].Bpf[i]; // Bpf est pour une année donnée !!!!!!!!T
                myDyn.myTerrit[j].indicSubTerritories(i); // calcul des indicateurs de l'état du sous-territoire j pour l'année i
                // Calcul de intention de solution de tri > 0.95
                totalIntentionForFoodWaste[i] = totalIntentionForFoodWaste[i] + myDyn.myTerrit[j].αcf[i] + myDyn.myTerrit[j].αsf[i];
                totalIntentionForGreenWasteWithoutDechetterie[i] = totalIntentionForGreenWasteWithoutDechetterie[i] + myDyn.myTerrit[j].αcg[i] + myDyn.myTerrit[j].αsg[i];
            }
            nbSolutionsForAllForFood[i] = totalCapacityForFoodWaste / totalProducedFoodWaste; // pas de problème pour Green tant que Dechetterie a capacité infinie
            // A CALCULER POUR CHAQUE SOUS-TERRITOIRE

            tauxEvolutionGreenWaste[i] = (myDyn.Bvgall[i] - myDyn.firstYearValorisationCenterAll) / myDyn.firstYearValorisationCenterAll;
            increaseOfMethanisedFoodWaste[i] = (myDyn.myCommonEquip.Bmf[i] - myDyn.myCommonEquip.Umf[i]) - myDyn.increasedObjectiveOfMethanisedFoodWaste; // traite des déchets alimentaires méthanisés, doivent augmenter de 5700
            multiplicateurVolumeBiodechetOMR[i] = myDyn.Brall[i] / myDyn.firstYearQuantityOfResidualWasteALL;
            kgDechetAlimDansOMRByHab[i] = (myDyn.Brall[i] * 1000.0) / myDyn.Pall[i];
            
        }
        if (nbYears > 5) {
            for (int i = 1; i < bound; i++) {
                if (nbSolutionsForAllForFood[6] < 1.0) { // doit être vrai en iter 6, fin 2023
                    solutionForAllForFood[i] = 0;
                }
                
                if (tauxEvolutionGreenWaste[7] > -0.12) { // 8 = 2025
                    diminutionGreenWaste[i] = 0;
                }
                if (increaseOfMethanisedFoodWaste[7] < 0) { // 8 = 2025 (la CAM doit récolter 5700 tonnes de plus de déchets alimentaires pour méthanisation)
                    correctIncreaseOfMethanisedFoodWaste[i] = 0;
                }
                if (multiplicateurVolumeBiodechetOMR[7] > 0.5) { // 8 = 2025 (objectifs du SBA)
                    diminutionVolBiodechetOMR[i] = 0;
                }
                if (myDyn.myCommonEquip.Bm[7] < 12000) {
                    volMethanised[i] = 0;
                }
                sommeRespectedObjectifs[i] = diminutionGreenWaste[i] + correctIncreaseOfMethanisedFoodWaste[i] + diminutionVolBiodechetOMR[i] + volMethanised[i];
                if (sommeRespectedObjectifs[i] == 4) {
                    correct[i] = 1;
                }
            }
        }
    }
}


