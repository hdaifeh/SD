import java.util.Arrays;

/**
 * @author shuet
 */
public class CollectionTerritories {

    Territory myTerre;
    // Define the starting point of the simulation
    int timeBeforeInit_αcf_initial; // here we consider the evolution of behavioural intention of food composting before the initial observation
    int timeBeforeInit_αcg_initial; // here we consider the evolution of behavioural intention of green composting before the initial observation
    int timeBeforeInit_αsf_initial; // here we consider the evolution of behavioural intention of food sorting for dedicated collection before the initial observation (it's 2 for CAM)
    int timeBeforeInit_αsg_initial; // here we consider the evolution of behavioural intention of green sorting for dedicated collection before the initial observation

    double Kc_initial; // (parameter) initial capacity of home composting 
    double Ks_initial; // (parameter) initial dedicated collection capacity 
    double[] Kct; // (variable) linear evolution of home composter until planned capacity  
    double[] Kst; // (variable) linear evolution of dedicated collection until planned capacity 
    double αc_target; // (parameter) the planned maximum capacity of home composter 
    double αs_target; // (parameter) the planned maximum capacity of dedicated collection
    int yearRef; // year of departure for individual composting capacity non-limiting to departure

    double[] LinearHomeComposter; // linear function for planned capacity evolution of home composter 

    double[] sigmoide_mcf; // innovation diffusion function of food composting behavioural intention evolution 
    double[] sigmoide_mcg; // innovation diffusion function of green composting behavioural intention evolution 

    double[] LinearDedicatedCollection; // linear function for planned capacity evolution of dedicated collection

    double[] sigmoide_msf; // innovation diffusion function of evolution of food sorting for dedicated collection behavioural intention  
    double[] sigmoide_msg; // innovation diffusion function of evolution of green sorting for dedicated collection behavioural intention 

    double[] sigmoide_mpg; // innovation diffusion function of evolution of reduction behaviour of green waste
    double αcf_initial; // initial behavioural intention of food composting 
    double αcg_initial; // initial behavioural intention of green composting
    double αcf_max; // maximum evolution of food composting behavioural intention (here we consider it as one)
    double αcg_max; // maximum evolution of green composting behavioural intention (here we consider it as one)
    double αsf_initial; // initial behavioural intention of food sorting for dedicated collection
    double αsf_max; // maximum evolution of food sorting for dedicated collection behavioural intention (here we consider it as one)
    double αsg_initial; // initial behavioural intention of green sorting for dedicated collection
    double αsg_max; // maximum evolution of green sorting for dedicated collection behavioural intention (here we consider it as one)

    // TODO HYPOTHESIS: NO CAPACITY FOR RECYCLING CENTRE + ONLY ONE RECYCLING CENTRE PER TERRITORY
    double b_pf; // baseline food waste production per capita (which varies for each collection territory). per inhabitant/year in tonnes   
    double b_pg; // baseline green waste production per capita (which varies for each collection territory). per inhabitant/year in tonnes            

    double αv; // volume of green waste sent to the valorisation centre 

    double r; // the annual growth rate of the population for each collection territory
    int sizePop; // size of population for each collection territory

    double duraImplemCompo; // linear function for home composter capacity development 
    double mc; // inflexion point of the practical sigmoid curve of home composting
    double duraImplemCollect; // linear function for dedicated collection capacity development 
    double ms; // inflexion point of the practical sigmoid curve of sorting for dedicated collection
    double mpg; // inflexion point of the green waste reduction sigmoid curve
    double αpg_target; // the ABP's target reduction for green waste to be achieved by the target year

    double[] P; // the change in population size of each collection territory at year t
    double[] B; // Quantity of biowaste produced per households
    double[] Bpg; // The household's green waste production from each collection territory 
    double[] Bpf; // The household's food waste production from each collection territory 
    double[] ABP; // Food waste to be removed as a result of reducing food waste
    double[] R; // rate of reduction of food waste at t as a function of objGaspi at term (term = 2xtiantigaspi of the sigmoid) AFTER THAT CONSTANT RATE
    double[] G; // Quantity reduction in food waste 
    double[] αcf; // the sorting of food waste for home composting intentions 
    double[] αcg; // the sorting of green waste for home composting intentions 
    double[] αvg; // the volume of green waste sent to the valorisation centre in each collection territory as consequences of other intentions
    double[] αsf; // the intentions of practical sorting for dedicated collection behaviour for food waste at time 
    double[] αsg; // the intentions of practical sorting for dedicated collection behaviour for green waste at time 
    double[] C_log; // From sigmoid for evolution of individual composting logistics
    double[] C_pop; // From sigmoid giving speed of individual evolution for composting practice
    double[] Bcg; // The biomass of home compostable green waste 
    double[] Bcf; // The biomass of home compostable food waste 
    double[] Bcf_composted; // the biomass of home composted food waste
    double[] Bcg_composted; // the biomass of home composted green waste
    double[] Bc_composted; // the biomass of home composted biowaste
    double[] Uc; // home composting-part surplus 
    double[] Ucg; // Quantity of green biowaste removed from local composting due to surplus
    double[] Ucf; // Quantity of food biowaste removed from local composting due to surplus
    double[] sLbis; // Intermediate management of local composting surpluses (adjusted composted)
    double[] Bv; // Green waste directed to the green valorisation centres 
    double[] Bsg; // sortable green waste for dedicated collection
    double[] Bsf; // sortable food waste for dedicated collection
    double[] Bs_sorted; // biomass of sorted green and food waste in dedicated collection 
    double[] Bsf_sorted; // biomass of sorted food waste in dedicated collection 
    double[] Bsg_sorted; // biomass of sorted green waste in dedicated collection 
    double[] Usf; // sorting-part food waste surplus 
    double[] Usg; // sorting-part green waste surplus 
    double[] sAa_bis; // Quantity of food waste removed from collection due to surplus
    double[] sAv_bis; // Quantity of green biowaste removed from collection due to surplus
    double[] Us; // Surplus from collection #1
    double[] sAbis; // Surplus from collection #2
    double[] Br; // food waste directed to the residual household waste 
    double αpf_target; // the ABP's target reduction for food waste to be achieved by the target

    int subTerritoryName;

    double[] propPopDesserviCollDA; // proportion of the population served by food waste collection in a given year
    double[] nbKgCollectHabDesservi; // number of kilograms of food waste collected per inhabitant served by the collection in a given year
    double[] nbKgOMRHab; // number of kilograms of food waste collected per inhabitant served by the collection in a given year
    // EquipmentValorisation myOwnEquip; // FOR NOW WE CONSIDER THAT SUBTERRITORIES DO NOT HAVE THEIR OWN EQUIPMENT OR THEIR CAPACITIES ARE SUMMED TO MAKE A COMMON EQUIPMENT
    double[] tauxReductionDechetVert; // rate of reduction of green waste entering the recycling centre
    int ident; // sub-territory number

    public CollectionTerritories(Territory mt, int id) {
        myTerre = mt;
        ident = id;
    }

    // TODO I HAVE MADE THE HYPOTHESIS OF ONE RECYCLING CENTRE PER TERRITORY // calling the function
    public void iterate(int year) {
        LinearHomeComposter[year] = linear(year, duraImplemCompo);
        LinearDedicatedCollection[year] = linear(year, duraImplemCollect); // logistic sorting capacity: duration = 7 
        if (myTerre.useSocialDynamics) {
            // sigmoideLogCompostLocal[year] = sigmoide(year, duraImplemCompo); // logistic composting capacity
            // logistic composting capacity: duration = 7

            sigmoide_mcf[year] = sigmoide(year + timeBeforeInit_αcf_initial, mc); // Practical composting behaviour: should I add +timeBeforeInit_αcf_initial to mc?

            sigmoide_mcg[year] = sigmoide(year + timeBeforeInit_αcg_initial, mc); // Practical composting behaviour: Should I add timeBeforeInit_αcg_initial to mc?

            // sigmoideLogCollecte[year] = sigmoide(year, duraImplemCollect); // Logistic collection capacity

            sigmoide_msf[year] = sigmoide(year + timeBeforeInit_αsf_initial, ms); // Practical sorting behaviour
            sigmoide_msg[year] = sigmoide(year + timeBeforeInit_αsg_initial, ms); // Practical sorting behaviour
            // System.err.println(year+" "+ms+" timebefore "+timeBeforeInit_αsg_initial+" sig "+sigmoide(year + timeBeforeInit_αsg_initial, ms));
            sigmoide_mpg[year] = sigmoide(year, mpg); // reduction green waste adoption 
        }

        computeProducedBioWaste(year);
        // Step 1: Distribution of biowaste
        computeFluxRates(year); // alpha
        localCompost(year); // C
        collect(year); // S
        recyclingCentre(year); // DV
        residualHouseholdWaste(year); // OMR
        // myOwnEquip.iterate(year, this); // FOR NOW WE CONSIDER THAT SUBTERRITORIES DO NOT HAVE THEIR OWN EQUIPMENT OR THEIR CAPACITIES ARE SUMMED TO MAKE A COMMON EQUIPMENT
    }

    public void computeProducedBioWaste(int y) {
        P[y] = P[y - 1] * (1 + r); // Population size at time t
       
        R[y] = αpf_target * myTerre.sigmoideABP[y]; // rate of reduction of food waste at t as a function of objGaspi at term
        // G[y] = myTerre.einit * P[y]; // einit=volume in tons of food waste wasted in 2018 per year and per inhabitant, so total wasted by the population
        // G[y] = myTerre.einit * P[y - 1];
        ABP[y] = R[y] * G[y]; // Amount of food waste to be removed due to reduction of food waste
        
        // Bv[y] = (b_pg - (b_pg * sigmoide_mpg[y] * αpg_target)) * P[y]; // Quantity of green biowaste produced by inhabitants
        Bpg[y] = b_pg * (1 - αpg_target * myTerre.sigmoideABP[y]) * P[y];
        // Bv[y] = (b_pg - (b_pg * sigmoide_mpg[y] * αpg_target)) * P[y - 1];        
        // System.err.println(αpg_target+" "+sigmoide_mpg[y]);
        // double e = myTerre.einit/BaInit; // the percentage of edible part
        Bpf[y] = b_pf * (1 - αpf_target * myTerre.sigmoideABP[y] * myTerre.einit) * P[y]; // 𝒃_𝒇^𝒑 (𝟏−𝒐_𝒇^𝒑 𝒁(𝒕,𝒎_𝒇^𝒑) 𝒆) 𝑷(𝒕) it was (y-1) I have changed the parameterisation of the model // Quantity of food biowaste produced by inhabitants taking into account food waste
        // System.err.println(Bpf[y]+" "+b_pf+" "+P[y]+" "+ABP[y]+" "+R[y]+" "+G[y]+" "+myTerre.objGaspi+" "+myTerre.einit+" "+myTerre.sigmoideABP[y]);
        B[y] = Bpg[y] + Bpf[y]; // Quantity of biowaste produced 
    }

    // computation of the intention to act (realised if the capacity is sufficient)
    public void computeFluxRates(int y) {// social behaviour
        double trucDa;
        double trucDv;
        αcf[y] = Math.min((αcf_initial + ((1 - αcf_initial) * sigmoide_mcf[y - 1])), 1.0);
        αcg[y] = Math.min((αcg_initial + ((1 - αcg_initial) * sigmoide_mcg[y - 1])), 1.0); // Proportion of biowaste going to local composting taking into account actors   
        αsf[y] = αsf_initial + ((1 - αsf_initial) * sigmoide_msf[y]); // we prioritise the desire to compost and assume that people compost or participate in collection
        trucDa = αcf[y] + αsf[y];
        if (trucDa > 1.0) {
            αsf[y] = (1 - αcf[y]);
        }
        αsg[y] = αsg_initial + ((αsg_max - αsg_initial) * sigmoide_msg[y]);
        // System.err.println(y+" "+αsg_initial+" "+αsg[y]+" "+sigmoide_msg[y]);
        trucDv = αcg[y] + αsg[y];
        if (trucDv > 1.0) {
            αsg[y] = 1.0 - αcg[y];
        }
        αvg[y] = 1 - αcg[y] - αsg[y]; // concerns only green waste which goes to the recycling centre
        
        // System.err.println("αsf_initial: " + αsf_initial);
        // System.err.println("αsg_initial: " + αsg_initial);
        // System.err.println("αsf_max: " + αsf_max);
        // System.err.println("αsg_max: " + αsg_max);
    }

    /**
     * Step 1: Distribution of biowaste
     */
    public void localCompost(int y) {
        Bcg[y] = αcg[y] * Bpg[y]; // Quantity of green waste going towards local composting
        Bcf[y] = αcf[y] * Bpf[y]; // Quantity of food waste going towards local composting
        // → HYPOTHESIS: If L[y] > K1: We have a surplus, then we will: First put green biowaste Bcg[y] in the recycling centre then if Bcg[y] is empty and there is still a surplus and L[y] is still greater than K1 then we put food biowaste Bcf[y] in the collection.
        if (y == yearRef) { // Calibration for the SBA case
            Kc_initial = Bcg[y] + Bcf[y];
        }
        Kct[y] = Kc_initial + ((αc_target - Kc_initial) * LinearHomeComposter[y]); // here sigmoid becomes linear see iteration
        // System.err.println("sigmoid " + LinearHomeComposter[y] + " Kct " + Kct + " Bcg " + Bcg[y] + " Bcf " + Bcf[y]+" Py "+P[y-1]+" "+αcg[y]+" Bpg "+Bpg[y]+" αcg "+αcg[y]+" αvg "+αvg[y]);
        if ((Bcg[y] + Bcf[y]) > Kct[y]) {
            Uc[y] = Bcg[y] + Bcf[y] - Kct[y]; // First calculation of surplus
            Bcg_composted[y] = Math.max(Bcg[y] - Uc[y], 0.0); // Quantity of green biowaste after applying the surplus
            sLbis[y] = Math.max(0.0, (Bcg_composted[y] + Bcf[y] - Kct[y])); // Second calculation of surplus to see if there is still surplus after removing green biowaste
            Bcf_composted[y] = Math.max(Bcf[y] - sLbis[y], 0.0); // Quantity of food biowaste after applying the surplus
            Ucf[y] = Math.min(sLbis[y], Bcf[y]); // Quantity of food biowaste removed due to surplus
            Ucg[y] = Math.min(Uc[y], Bcg[y]); // Quantity of green biowaste removed due to surplus
            Bcg[y] = Bcg_composted[y];
            Bcf[y] = Bcf_composted[y];
        }
        Bc_composted[y] = Bcf[y] + Bcg[y]; // Values of L after removing the surplus 
    }

    public void collect(int y) {
        Bsg[y] = αsg[y] * Bpg[y]; // Quantity of green waste going towards collection
        Bsf[y] = (αsf[y] * Bpf[y]) + Ucf[y]; // → Quantity of food biowaste going towards collection
        // if (y == 1) {
        // System.err.println(αsg[y] + " ka " + Ks_initial + " Bsg " + Bsg[y] + " Bsf " + Bsf[y] + " a3dv " + αvg[y] + " a1dv " + αcg[y]);
        // }
        // → HYPOTHESIS: if A[y] > KA: We have a surplus, then we will: First put green biowaste Bsg[y] in the recycling centre then if Bsg[y] is empty and there is still a surplus and A[y] is still greater than KA then we put food biowaste Bsf[y] in the household residual waste.
        Kst[y] = Ks_initial + ((αs_target - Ks_initial) * LinearDedicatedCollection[y]);
        // if (ident==1) System.err.println("year "+y+" ident terr "+ident+" Kacourant "+Kst);
        if ((Bsg[y] + Bsf[y]) > Kst[y]) {
            Us[y] = Bsf[y] + Bsg[y] - Kst[y]; // → First calculation of surplus
            Bsg_sorted[y] = Math.max(Bsg[y] - Us[y], 0.0); // Quantity of green waste after applying the surplus
            sAbis[y] = Math.max(0.0, (Bsf[y] + Bsg_sorted[y] - Kst[y])); // Second calculation of surplus to see if there is still surplus after removing green biowaste
            // Av_bis[y] = Math.max(Bsg[y] - Us[y], 0.0); // Quantity of green biowaste after applying the surplus
            Bsf_sorted[y] = Math.max(Bsf[y] - sAbis[y], 0.0); // → Quantity of food biowaste after applying the surplus
            Usg[y] = Math.min(Us[y], Bsg[y]); // Quantity of green biowaste removed due to surplus GOES TO THE RECYCLING CENTRE!!!!
            // if (Usg[y]<0.0) { System.err.println(" jfjqkdksdj "+Us[y]+" "+Bsg[y]); }
            // Dv[y]=Bv[y]+Usg[y]; // putting surplus back to the recycling centre
            Usf[y] = Math.min(sAbis[y], Bsf[y]); // → Quantity of food biowaste removed due to surplus
            Bsg[y] = Bsg_sorted[y];
            Bsf[y] = Bsf_sorted[y];
        }
        Bs_sorted[y] = Bsg[y] + Bsf[y]; // → Value of A[y] after removing the surplus
    }

    public void recyclingCentre(int y) {
        Bv[y] = αvg[y] * Bpg[y] + Ucg[y] + Usg[y]; // Quantity of green biowaste going towards the recycling centre
        // System.err.println(Bv[y]+" αvg "+αvg[y]+" Ucg "+Ucg[y]+" Bpg "+Bpg[y]+" Usg "+Usg[y]);
        // System.err.println(Bv[y]+" "+αvg[y]+" "+Bpg[y]+" "+Ucg[y]+" "+Usg[y]);
        // if(Bv[y]<0.0) System.err.println(αvg[y]+" "+Bpg[y]+" "+Ucg[y]+" "+Usg[y]); //System.err.println(Ucg[y]+" "+Usg[y]+);
    }

    public void residualHouseholdWaste(int y) {
        Br[y] = (1 - αcf[y] - αsf[y]) * Bpf[y] + Usf[y]; // Quantity of food biowaste going towards residual household waste
        if (Br[y] < 0) {
            System.err.println(αsf[y] + " alpha1 " + αcf[y] + " Ba " + Bpf[y] + " sAa " + Usf[y]);
        }
    }

    public double sigmoide(double x, double ti) {
        double t = Math.pow(x, 5);
        double z = t / (t + Math.pow(ti, 5)); // ti is the inflexion point of the sigmoid (the value 0.5 is returned in the ti-th year)
        return z;
    }

    public double linear(double t, double duration) {
        return Math.min(t / duration, 1.0);
    }

    public int calculateTimeBeforeInit(double alpha_base, double ti) { // time process
        int timeBeforeInit = 0;
        // Continuously calculate the sigmoid value at increasing time steps until it meets or exceeds alpha_base.
        if (alpha_base > 0) {

            double sigmoideValue = sigmoide(timeBeforeInit, ti);
            while (sigmoideValue < alpha_base) { // (the while-loop will not be entered if alpha_base is less than or equal to zero).
                timeBeforeInit++;
                sigmoideValue = sigmoide(timeBeforeInit, ti);
                // System.err.println(alpha_base + " " + ti + " " + timeBeforeInit + " " + sigmoideValue);
            }
        }
        return timeBeforeInit;
    }

    public void init(int sizeData, double[] params, int refYear) {
        yearRef = refYear;
        subTerritoryName = (int) params[0]; // numerical identifier of the sub-territory
        duraImplemCompo = params[1]; // inflexion point of the sigmoid curve
        duraImplemCollect = params[2]; // inflexion point of the sigmoid curve
        mc = params[3]; // inflexion point of the sigmoid curve
        ms = params[4]; // inflexion point of the sigmoid curve
        b_pf = params[5]; // Quantity of biowaste produced per inhabitant
        b_pg = params[6]; // proportion of green waste in b
        αcf_initial = params[7];
        αcg_initial = params[8];
        αsf_initial = params[9]; // practice of sorting for door-to-door collection init (from 70% to 95%)
        αsf_max = params[10]; // practice of sorting for door-to-door collection target (from 70% to 95%)
        αcf_max = params[11]; // desire to increase local composting practice for food waste
        αcg_max = params[12]; // same as above but for green waste
        αsg_initial = params[13]; // initial sorting of green waste
        αsg_max = params[14];
        Kc_initial = params[15]; // annual population growth according to national statistics
        αc_target = params[16]; // K(expected)^c
        Ks_initial = params[17]; // annual population growth according to national statistics
        αs_target = params[18];
        sizePop = (int) params[19]; // population size of sub-territory
        r = params[20]; // population size of sub-territory
        mpg = params[21]; // tiActionsAvoidanceGreenWaste
        αpg_target = params[22]; // rateAvoidanceGreenWasteHorizon2024        
        αpf_target = params[23];
        // Calculating time before the simulation starts for each alpha value (calculating time process)
        timeBeforeInit_αcf_initial = calculateTimeBeforeInit(αcf_initial, mc);
        timeBeforeInit_αcg_initial = calculateTimeBeforeInit(αcg_initial, mc);
        timeBeforeInit_αsf_initial = calculateTimeBeforeInit(αsf_initial, ms);
        timeBeforeInit_αsg_initial = calculateTimeBeforeInit(αsg_initial, ms);
        // System.err.println("ms "+ms);

        P = new double[sizeData]; // sizeData = number of years of simulation + 1 (for the initial state)
        Arrays.fill(P, 0.0);
        P[0] = sizePop;
        R = new double[sizeData];
        Arrays.fill(R, 0.0);
        ABP = new double[sizeData];
        Arrays.fill(ABP, 0.0);
        G = new double[sizeData];
        Arrays.fill(G, 0.0);
        B = new double[sizeData];
        Arrays.fill(B, 0.0);
        Bpg = new double[sizeData];
        Arrays.fill(Bpg, 0.0);
        Bpf = new double[sizeData];
        Arrays.fill(Bpf, 0.0);
        αcf = new double[sizeData];
        Arrays.fill(αcf, 0.0);
        αcg = new double[sizeData];
        Arrays.fill(αcg, 0.0);
        αvg = new double[sizeData];
        Arrays.fill(αvg, 0.0);
        C_log = new double[sizeData];
        Arrays.fill(C_log, 0.0);
        C_pop = new double[sizeData];
        Arrays.fill(C_pop, 0.0);
        Bc_composted = new double[sizeData];
        Arrays.fill(Bc_composted, 0.0);
        Bcg = new double[sizeData];
        Arrays.fill(Bcg, 0.0);
        Bcf = new double[sizeData];
        Arrays.fill(Bcf, 0.0);
        Uc = new double[sizeData];
        Arrays.fill(Uc, 0.0);
        Ucf = new double[sizeData];
        Arrays.fill(Ucf, 0.0);
        Ucg = new double[sizeData];
        Arrays.fill(Ucg, 0.0);
        Bcg_composted = new double[sizeData];
        Arrays.fill(Bcg_composted, 0.0);
        Bcf_composted = new double[sizeData];
        Arrays.fill(Bcf_composted, 0.0);
        sLbis = new double[sizeData];
        Arrays.fill(sLbis, 0.0);
        Bv = new double[sizeData];
        Arrays.fill(Bv, 0.0);
        Usg = new double[sizeData];
        Arrays.fill(Usg, 0.0);
        Br = new double[sizeData];
        Arrays.fill(Br, 0.0);
        Kst = new double[sizeData];
        Arrays.fill(Kst, 0.0);
        Kct = new double[sizeData];
        Arrays.fill(Kct, 0.0);
        // Fv_bis = new double[sizeData];
        // Arrays.fill(Fv_bis, 0.0);

        LinearHomeComposter = new double[sizeData];
        Arrays.fill(LinearHomeComposter, 0.0);
        sigmoide_mcf = new double[sizeData];
        Arrays.fill(sigmoide_mcf, 0.0);
        sigmoide_mcg = new double[sizeData];
        Arrays.fill(sigmoide_mcg, 0.0);
        LinearDedicatedCollection = new double[sizeData];
        Arrays.fill(LinearDedicatedCollection, 0.0);
        sigmoide_msf = new double[sizeData];
        Arrays.fill(sigmoide_msf, 0.0);
        sigmoide_msg = new double[sizeData];
        Arrays.fill(sigmoide_msg, 0.0);
        sigmoide_mpg = new double[sizeData];
        Arrays.fill(sigmoide_mpg, 0.0);
        Bsg = new double[sizeData];
        Arrays.fill(Bsg, 0.0);
        Bsf = new double[sizeData];
        Arrays.fill(Bsf, 0.0);
        Bsf_sorted = new double[sizeData];
        Arrays.fill(Bsf_sorted, 0.0);
        Bsg_sorted = new double[sizeData];
        Arrays.fill(Bsg_sorted, 0.0);
        Bs_sorted = new double[sizeData];
        Arrays.fill(Bs_sorted, 0.0);
        Us = new double[sizeData];
        Arrays.fill(Us, 0.0);
        sAbis = new double[sizeData];
        Arrays.fill(sAbis, 0.0);
        Usf = new double[sizeData];
        Arrays.fill(Usf, 0.0);

        propPopDesserviCollDA = new double[sizeData];
        Arrays.fill(propPopDesserviCollDA, 0.0);
        nbKgCollectHabDesservi = new double[sizeData];
        Arrays.fill(nbKgCollectHabDesservi, 0.0);
        nbKgOMRHab = new double[sizeData];
        Arrays.fill(nbKgOMRHab, 0.0);
        tauxReductionDechetVert = new double[sizeData];
        Arrays.fill(tauxReductionDechetVert, 0.0);
        αsg = new double[sizeData];
        Arrays.fill(αsg, 0.0);
        αsf = new double[sizeData];
        Arrays.fill(αsf, 0.0);
        Bpf[0] = b_pf * P[0];
        Bpg[0] = b_pg * P[0];
        Bcg[0] = Bpg[0] * αcg_initial;
        Bcf[0] = Bpf[0] * αcf_initial;
        Bsf[0] = Bpf[0] * αsf_initial;
        Bsg[0] = Bpg[0] * αsg_initial;
        Bv[0] = Bpg[0] - Bcg[0] - Bsg[0];
        Br[0] = Bpf[0] - Bcf[0] - Bsf[0];

    }

    public void printVector(double[] edit) {
        for (int i = 0; i < edit.length; i++) {
            System.err.print(edit[i] + "\t");
        }
        System.err.println();
    }

    public void indicSubTerritories(int year) {
        //System.err.print("year "+year+" "+" Kst "+Kst+" KA "+KA+" nb hab desservi ") ;
        double nbHabDesservi = Math.min(P[year], (double) Kst[year] / (39.0 / 1000.0)); // 39 kg [converti en tonnes] correspond à quantité article 5 arrêté du 7 juillet 2021 (base calcul pour qté détournée par habitant desservi par la collecte de dechets alimentaires)  
        //System.err.print(nbHabDesservi) ;
        propPopDesserviCollDA[year] = nbHabDesservi / P[year];
        if (nbHabDesservi > 0) {
            nbKgCollectHabDesservi[year] = (Bsf[year] * 1000.0) / nbHabDesservi;
        }
        nbKgOMRHab[year] = (Br[year] * 1000.0) / P[year];
        tauxReductionDechetVert[year] = (Bv[year] - Bv[0]) / Bv[0]; // this evolution perecentage of green waste in dechetre, negative value means reduction

    }

    public void printTrajectory(int year) {
        System.out.print(ident + ";");
        System.out.print(P[year] + ";");
        System.out.print(Bpf[year] + ";");
        System.out.print(Bpg[year] + ";");
        System.out.print(αcf[year] + ";");
        System.out.print(αcg[year] + ";");
        System.out.print(Bcf[year] + ";");
        System.out.print(Ucf[year] + ";");
        //System.err.println("je viens d'écrire SLA") ;
        System.out.print(Bcg[year] + ";");
        System.out.print(Ucg[year] + ";");
        System.out.print(Kct[year] + ";");
        System.out.print(αsf[year] + ";");
        System.out.print(αsg[year] + ";");
        System.out.print(Bsf[year] + ";");
        System.out.print(Usf[year] + ";");
        System.out.print(Bsg[year] + ";");
        System.out.print(Usg[year] + ";");
        System.out.print(Kst[year] + ";");
        System.out.print(Br[year] + ";");
        System.out.print(αvg[year] + ";");
        System.out.print(Bv[year] + ";");
    }
}


