import static java.lang.Float.max;
import static java.lang.Float.min;
import java.util.Arrays;

/*
 * To change this licence header, choose Licence Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author shuet
 */
public class Territory {

    boolean printTrajectory;
    boolean useSocialDynamics = true; // allows to cancel the evolution of the behavioural intention (i.e. the alpha) when it is false
    double einit; // edible part of the initial food waste production = 0.032
    //double objGaspi; // anti-waste objective if applicable to the entire territory
    double theta1; // Ptri[y] Sigmoid: Social dynamics modelled by the sigmoid that consists of implementing an incentive for sorting from Î±sf_initial = 70% to Î±sf_max = 95%
    double theta2; // Ptri[y] Sigmoid: Social dynamics modelled by the sigmoid that consists of implementing an incentive for sorting from Î±sf_initial = 70% to Î±sf_max = 95%

    double mpf; // inflexion point of the food waste reduction sigmoid curve

    double[] Pall; // population size of all collection territory
    double[] Ball; // total biowaste production
    double[] Bfall; // total food waste production
    double[] Bgall; // total green waste production
    double[] ABPall; // anti-biowaste plan 
    double[] Bcall; // Composted biowaste
    double[] Bsall; // sorted biowaste
    double[] Brall; // total food waste directed to the residual household waste 
    double[] Bvgall; // total Green waste directed to the green valorisation centres
    
    double[] Ucfall; // total home composting-part food surplus 
    double[] Ucgall; // total home composting-part green surplus 
    
    double[] Usgall; // total sorting-part green waste surplus
    double[] Usfall; // total sorting-part food waste surplus

    int refYear; // reference year between 0 and nbYears simulation for the evaluation of objectives such as increase or decrease
    double firstYearValorisationCenterAll;
    double firstYearQuantityOfResidualWasteALL;

    double[] sigmoideABP; // stores the evolution of practices according to mpf // CAUTION THE SPEED IS THE SAME FOR ALL PROCESSES BECAUSE TI IS THE SAME FOR ALL!!!!!!

    boolean[] checkFluxStage1;
    boolean[] checkFluxStage2;

    int territoryName;
    int nbSubterritories;
    int nbEquipments;
    CollectionTerritories[] myTerrit;
    LargeScaleInfrastructure myCommonEquip;

    double increasedObjectiveOfMethanisedFoodWaste;

    public Territory(int nbYears, int nbSubterritories, double[] paramsTerritory, double[][] paramsSubTerritories, boolean printTraj) {

        refYear = 0; //
        printTrajectory = printTraj;
        myTerrit = new CollectionTerritories[nbSubterritories];
        int sizeData = nbYears + 1;
        for (int i = 0; i < nbSubterritories; i++) {
            myTerrit[i] = new CollectionTerritories(this, i);
            myTerrit[i].init(sizeData, paramsSubTerritories[i], refYear);
        }
        // initialisation of territory (after sub-territories as required to indicate which contribute to common valorisation equipment)
        myCommonEquip = new LargeScaleInfrastructure();
        init(sizeData, paramsTerritory);
        // iterate over the time
        for (int i = 1; i <= nbYears; i++) {
            computeSubterritories(i);
            // calculation of the sum of fluxes from territories contributing to equipment
            computeFluxesForCommonEquipment(i);
            // calculation of common valorisation or incinerator
            myCommonEquip.iterate(i, totCollecteVert, totCollecteFood, totDechetterie, totOMR);
            computeTotalFluxSubTerritories(i);
            // Bug control
            checkConservationFlux(i);
        }

    }

    public void computeSubterritories(int year) {
        sigmoideABP[year] = sigmoide(year, mpf);
        for (int i = 0; i < nbSubterritories; i++) {
            // Production of biowaste and Distribution of biowaste in local compost, recycling centre and collection
            myTerrit[i].iterate(year);
        }
    }

    public double sigmoide(double x, double ti) {
        double t = Math.pow(x, 5);
        double z = t / (t + Math.pow(ti, 5)); // ti is the inflexion point of the sigmoid (the value 0.5 is returned in the ti-th year)
        return z;
    }

    public void init(int sizeData, double[] params) { // initialisation of global parameters and common valorisation equipment

// TODO TO BE ENTERED IN THE PARAMETERS, especially for the CAM
        increasedObjectiveOfMethanisedFoodWaste = 5700.0; // the CAM must collect 5700 t of additional biowaste from residents (starting from 0 or almost) methanised food waste ref SGTDO on all producers (objectives multiplication by three)

        territoryName = (int) params[0];
        mpf = params[1]; // inflexion point of the sigmoid curve 
        einit = params[2]; // 
        //objGaspi = params[3]; // waste reduction objective
        sigmoideABP = new double[sizeData];
        Arrays.fill(sigmoideABP, 0.0);
        int KMethaniseur = (int) params[5]; // priority 1 if surplus capacity Î±m_max
        int KIncinerator = (int) params[6]; // priority 2 if surplus 
        int KnbCompostPro = (int) params[7]; // priority 3 if surplus capacity Î±cc_max
        nbSubterritories = (int) params[4];

        myCommonEquip.init(sizeData, KMethaniseur, KIncinerator, KnbCompostPro);

        Pall = new double[sizeData]; //sizeData = nbyears of simulation + 1 (for the initial state)
        Arrays.fill(Pall, 0.0);
        Ball = new double[sizeData];
        Arrays.fill(Ball, 0.0);
        Bfall = new double[sizeData];
        Arrays.fill(Bfall, 0.0);
        Bgall = new double[sizeData];
        Arrays.fill(Bgall, 0.0);
        ABPall = new double[sizeData];
        Arrays.fill(ABPall, 0.0);
        Bcall = new double[sizeData];
        Arrays.fill(Bcall, 0.0);
        Brall = new double[sizeData];
        Arrays.fill(Brall, 0.0);
        Bsall = new double[sizeData];
        Arrays.fill(Bsall, 0.0);
        Bvgall = new double[sizeData];
        Arrays.fill(Bvgall, 0.0);

        //computeTotalFluxSubTerritories(0);
        checkFluxStage1 = new boolean[sizeData];
        Arrays.fill(checkFluxStage1, true);
        checkFluxStage2 = new boolean[sizeData];
        Arrays.fill(checkFluxStage2, true);
        computeFluxesForCommonEquipment(0);
        computeTotalFluxSubTerritories(0);
        this.myCommonEquip.Bi[0] = this.totOMR;
        this.myCommonEquip.Bcc[0] = this.totDechetterie;
        this.myCommonEquip.Bmf[0] = this.totCollecteFood;
        this.myCommonEquip.Bmg[0] = this.totCollecteVert;
    }

    public void printVector(double[] edit) {
        for (int i = 0; i < edit.length; i++) {
            System.err.print(edit[i] + "\t");
        }
        System.err.println();
    }

    public void computeTotalFluxSubTerritories(int y) {
        for (int i = 0; i < nbSubterritories; i++) {
            Pall[y] = Pall[y] + myTerrit[i].P[y];
            //System.err.println("i: "+i+" y: "+y+" pop "+Pall[y]+" "+myTerrit[i].P[y]) ;
            Ball[y] = Ball[y] + myTerrit[i].B[y];
            Bfall[y] = Bfall[y] + myTerrit[i].Bpf[y];
            Bgall[y] = Bgall[y] + myTerrit[i].Bpg[y];
            ABPall[y] = ABPall[y] + myTerrit[i].ABP[y];
            Bcall[y] = Bcall[y] + myTerrit[i].Bc_composted[y]; // personal compost
            Brall[y] = Brall[y] + myTerrit[i].Br[y]; // biowaste in household waste
            Bsall[y] = Bsall[y] + myTerrit[i].Bs_sorted[y]; // biowaste collection
            Bvgall[y] = Bvgall[y] + myTerrit[i].Bv[y]; // green waste in recycling centre
            if (y == refYear) {
                firstYearValorisationCenterAll = firstYearValorisationCenterAll + myTerrit[i].Bv[0];
                firstYearQuantityOfResidualWasteALL = firstYearQuantityOfResidualWasteALL + myTerrit[i].Br[0];
            }
        }
    }

    double totDechetterie;
    double totCollecteVert;
    double totCollecteFood;
    double totOMR;

    public void computeFluxesForCommonEquipment(int y) {
        totDechetterie = 0.0;
        totCollecteVert = 0.0;
        totCollecteFood = 0.0;
        totOMR = 0.0;
        for (int i = 0; i < nbSubterritories; i++) {
            totDechetterie = totDechetterie + myTerrit[i].Bv[y];
            totCollecteVert = totCollecteVert + myTerrit[i].Bsg[y];
            totCollecteFood = totCollecteFood + myTerrit[i].Bsf[y];
            totOMR = totOMR + myTerrit[i].Br[y];
        }
    }

    public void checkConservationFlux(int y) {
        int i = 0;
        double time1 = 0.0;
        double time2 = 0.0;
        time1 = time1 + Bcall[y] + Brall[y] + Bsall[y] + Bvgall[y]; // sub-territory
        time2 = time2 + myCommonEquip.Bm[y] + myCommonEquip.Bi[y] + Bcall[y] + myCommonEquip.Bcc[y]; // Valorisation equipment
        double t1 = time1 - Ball[y];
        double t2 = time2 - Ball[y];
        if (Math.abs(t1) > 0.00000001) {
            System.err.println("there is a bug in the computation of flux in stage 1, the error is " + t1 + " at time " + y);
            checkFluxStage1[y] = false;
        }
        if (Math.abs(t2) > 0.00000001) {
            System.err.println("there is a bug in the computation of flux in stage 2, the error is " + t2 + " at time " + y);
            checkFluxStage2[y] = false;
        }
    }

}
