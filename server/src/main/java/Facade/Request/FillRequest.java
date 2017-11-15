package Facade.Request;

public class FillRequest {
// Constructors
    /**
     * PRIMARY CONSTRUCTOR:
     * Creates a FillRequest object.
     *
     * @param un, non-empty string
     */
    public FillRequest(String un) {
        final int DEFAULT_NUM_GENERATIONS = 4;

        username        = un;
        num_generations = DEFAULT_NUM_GENERATIONS;
    }

    /**
     * OPTIONAL CONSTRUCTOR:
     * Creates a FillRequest object specifying a .
     *
     * @param un, non-empty string
     * @param ng, an integer greater than zero
     */
    public FillRequest(String un, int ng) {
        username        = un;
        num_generations = ng;
    }


// Members
    /**
     * USERNAME:
     *
     */
    private String username;
    public String getUsername() { return username; }
    public void setUsername(String un) { username = un; }

    /**
     * NUMBER OF GENERATIONS:
     * A positive integer.
     */
    private int num_generations;
    public int getNumGenerations() { return num_generations; }
    public void setNumGenerations(int ng) { num_generations = ng; }
}
