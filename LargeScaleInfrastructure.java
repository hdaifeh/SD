import java.util.Arrays;

/**
 * @author shuet
 * 
 * This class represents the common infrastructure (large scale) for biowaste valorisation.
 * 
 * It models large-scale facilities used in biowaste management, including:
 * - Methanisation units with configurable capacity constraints
 * - Professional composting platforms
 * - Incineration units
 * 
 * The class handles the flow calculations and distribution of different biowaste types
 * (food waste, green waste, etc.) through these treatment facilities, following
 * specific prioritisation rules when processing capacity constraints are reached.
 * 
 * The processing hierarchy for surplus waste follows this order:
 * 1. Methanisation
 * 2. Professional composting
 * 3. Incineration
 * 
 * The model tracks waste flows, calculates surpluses, and redistributes waste
 * streams to optimise facility utilisation whilst respecting capacity limitations.
 */
public class LargeScaleInfrastructure {

    double αm_max; // Maximum capacity of methanisation units (in our case study the capacity of methanisation unit is fixed at 20000)
    double αcc_max; // Maximum capacity of professional composters (in our case study we consider infinite capacity of professional composter unit)
    double αi_max; // Maximum capacity of the incineration units (in our case study we consider infinite capacity of incinerator unit)

    double[] Bi; // Flow of biowaste that goes to incinerator 
    double[] Bmg; // Flow of green waste that goes to methanisation
    double[] Bmf; // Flow of food waste that goes to methanisation
    double[] Bm; // Total flow in methanisation unit
    double[] Um; // Methanisation surplus 
    double[] sMbis; // Second calculation of surplus to check if any remains after removing green biowaste
    double[] Bmf_methanised; // Methanised food waste
    double[] Bmg_methanised; // Methanised green waste
    double[] Umf; // Quantity of food biowaste removed due to surplus
    double[] Umg; // Quantity of green biowaste removed due to surplus
    double[] sF; // Surplus from step 2
    double[] sFv_meth; // Surplus directed to methaniser
    double[] sFv_inci; // Surplus directed to incinerator
    //double[] Fv_bis; // Quantity of green biowaste after applying surplus
    double[] Bcc; // Quantity of green biowaste at the professional composting platform
    double[] sFv; // Surplus quantity of green biowaste 

    /**
     * Default constructor.
     * A set of equipment (methaniser, professional composter, incinerator) is supplied by
     * a global territory or a sub-territory.
     * The priority for surplus treatment is always:
     * 1. Methaniser
     * 2. Professional composter
     * 3. Incinerator
     */
    public LargeScaleInfrastructure() {
        // A set of equipment (methaniser, professional composter, incinerator) is supplied by
        // a global territory or a sub-territory.
        // The priority for surplus treatment is always:
        // 1. Methaniser
        // 2. Professional composter
        // 3. Incinerator
    }

    /**
     * Initialises the infrastructure with specific capacities.
     * 
     * @param sizeData Size of data arrays for time series
     * @param KMethaniseur Capacity of methanisation units
     * @param KIncinerator Capacity of incineration units
     * @param KnbCompostPro Capacity of professional composting platforms
     */
    public void init(int sizeData, int KMethaniseur, int KIncinerator, int KnbCompostPro) {

        αm_max = KMethaniseur;
        αcc_max = KnbCompostPro;
        αi_max = KIncinerator;

        Bi = new double[sizeData];
        Arrays.fill(Bi, 0.0);
        Bmf = new double[sizeData];
        Arrays.fill(Bmf, 0.0);
        Bmg = new double[sizeData];
        Arrays.fill(Bmg, 0.0);
        Bm = new double[sizeData];
        Arrays.fill(Bm, 0.0);
        Um = new double[sizeData];
        Arrays.fill(Um, 0.0);
        sMbis = new double[sizeData];
        Arrays.fill(sMbis, 0.0);
        Bmf_methanised = new double[sizeData];
        Arrays.fill(Bmf_methanised, 0.0);
        Bmg_methanised = new double[sizeData];
        Arrays.fill(Bmg_methanised, 0.0);
        Um = new double[sizeData];
        Arrays.fill(Um, 0.0);
        sMbis = new double[sizeData];
        Arrays.fill(sMbis, 0.0);
        Umf = new double[sizeData];
        Arrays.fill(Umf, 0.0);
        Umg = new double[sizeData];
        Arrays.fill(Umg, 0.0);
        sF = new double[sizeData];
        Arrays.fill(sF, 0.0);
        sFv_meth = new double[sizeData];
        Arrays.fill(sFv_meth, 0.0);
        sFv_inci = new double[sizeData];
        Arrays.fill(sFv_inci, 0.0);
        Bcc = new double[sizeData];
        Arrays.fill(Bcc, 0.0);
        //sFv = new double[sizeData];
        //Arrays.fill(sFv, 0.0);
    }

    /**
     * Processes one iteration/year of the model.
     * Follows the priority sequence: methanisation first, composting second, incineration last.
     * 
     * @param year Current year in simulation
     * @param fluxBg Green biowaste flow
     * @param fluxBf Food biowaste flow
     * @param fluxDv Additional green waste flow
     * @param fluxBr Ordinary mixed residual waste flow
     */
    public void iterate(int year, double fluxBg, double fluxBf, double fluxDv, double fluxBr) {
        // Step 2: Biowaste treatment → HYPOTHESIS
        computeMethanisation(year, fluxBg, fluxBf); // First as priority 1
        computeCompostPlatform(year, fluxDv); // Second as priority 2
        computeIncinerator(year, fluxBr);
    }

    /**
     * Calculates flow through the incinerator.
     * 
     * @param y Year index
     * @param fluxOMR Ordinary mixed residual waste flow
     */
    public void computeIncinerator(int y, double fluxOMR) {
        // No SMA because surplus from MA goes to professional composting platform
        Bi[y] = fluxOMR + sFv_inci[y]; // Quantity of biowaste going to incinerator (takes OMR and surplus from composting platform)
    }

    /**
     * Step 2: Biowaste treatment (HYPOTHESIS)
     * Calculates flows through methanisation units.
     * 
     * @param y Year index
     * @param fluxBg Green biowaste flow
     * @param fluxBf Food biowaste flow
     */
    public void computeMethanisation(int y, double fluxBg, double fluxBf) {
        // If A > 0 then proceed (means there is biowaste collection)
        // If A > 0 and αm_max > 0 then process locally, otherwise A will be processed at MyTerritory level (by common equipment)
        if (αm_max > 0) { // There is a local methaniser (or multiple)
            Bmg[y] = fluxBg; // Quantity of green biowaste going to methaniser
            Bmf[y] = fluxBf; // Quantity of food biowaste going to methaniser
            Bm[y] = Bmg[y] + Bmf[y]; // Value of M[y] after removing surplus if any
            
            // If M[y] > αm_max: We have surplus, then:
            // First put green biowaste Bmg[y] into local composting, then if Bmg[y] is empty and surplus remains
            // and M[y] is still greater than αm_max, then put food biowaste Bmf[y] into the incinerator. → HYPOTHESIS
            if ((Bmg[y] + Bmf[y]) > αm_max) {
                Um[y] = Bmf[y] + Bmg[y] - αm_max; // First calculation of surplus
                Bmg_methanised[y] = Math.max(Bmg[y] - Um[y], 0.0); // Quantity of green biowaste after applying surplus
                sMbis[y] = Math.max(0.0, Bmf[y] + Bmg_methanised[y] - αm_max); // Second calculation of surplus to check if any remains after removing green biowaste
                Bmf_methanised[y] = Math.max(Bmf[y] - sMbis[y], 0.0); // Quantity of food biowaste after applying second surplus
                
                Umg[y] = Math.min(Um[y], Bmg[y]); // Quantity of green biowaste removed due to surplus
                Umf[y] = Math.min(sMbis[y], Bmf[y]); // Quantity of food biowaste removed due to surplus, directed to composting platform
                Bm[y] = Bmg_methanised[y] + Bmf_methanised[y]; // Value of M[y] after removing surplus if any
            }
        }
    }

    /**
     * Calculates flows through professional composting platforms.
     * Assumes one composting or grinding platform per territory.
     * 
     * @param y Year index
     * @param fluxDv Green waste flow
     */
    public void computeCompostPlatform(int y, double fluxDv) {
        Bcc[y] = fluxDv + Umg[y] + Umf[y]; // Quantity of biowaste going to composting
        // HYPOTHESIS: No Fa[y] because we don't have food biowaste in a composting platform → HYPOTHESIS
        // HYPOTHESIS: If F[y] > αcc_max: We have surplus, then:
        // First put green biowaste Bcc[y] into the methaniser if space remains, then if M[y] is full
        // and surplus remains, put remaining green biowaste into the incinerator.
        if (Bcc[y] > αcc_max) {
            sF[y] = Bcc[y] - αcc_max; // Calculate surplus
            sFv_meth[y] = Math.min(sF[y], αm_max - Bm[y]); // Calculate surplus going to methaniser
            Bm[y] = Bm[y] + sFv_meth[y];
            sFv_inci[y] = Math.max(0.0, (sF[y] - sFv_meth[y])); // Calculate surplus going to incinerator
            Bcc[y] = Math.max((Bcc[y] - sFv_inci[y] - sFv_meth[y]), 0.0); // Quantity of green biowaste after applying surplus
        }
        // By systematically following these steps, the model ensures that any surplus green biowaste is either
        // processed in the methanisation unit for energy recovery or managed through incineration, with the aim
        // of maintaining operational efficiency and capacity balance within the professional composting platform.
    }
    
    /**
     * Prints the trajectory data for a specific year.
     * 
     * @param year Year index
     */
    public void printTrajectory(int year) {
        System.out.print((year+2017)+";");
        System.out.print(Bcc[year]+";");
        System.out.print(Bmf[year]+";");
        System.out.print(Bmf_methanised[year]+";");
        System.out.print(Bmg[year]+";");
        System.out.print(Bmg_methanised[year]+";");
        System.out.print(Bi[year]+";");
        System.out.println();
    }
}

