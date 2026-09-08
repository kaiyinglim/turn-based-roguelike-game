package game.systems;

/**
 * Tracks the shared company quota cycle separately from workers' personal credits.
 * Manages company rank progression, quota requirements, and failure conditions.
 * @author Heesu Kim
 */
public class CompanyQuota {

    /**
     * The starting company rank.
     */
    private static final int INITIAL_RANK = 1;

    /**
     * The starting quota requirement.
     */
    private static final int INITIAL_QUOTA = 100;

    /**
     * The starting number of turns available to meet the quota.
     */
    private static final int INITIAL_TIME_LIMIT = 200;

    /**
     * Current company rank.
     */
    private int companyRank;

    /**
     * Current quota target that must be achieved.
     */
    private int quota;

    /**
     * Credits accumulated towards the current quota.
     */
    private int companyCredits;

    /**
     * Maximum number of turns allowed to meet the current quota.
     */
    private int timeLimit;

    /**
     * Number of turns elapsed in the current quota cycle.
     */
    private int elapsedTurns;

    /**
     * Indicates whether the company has failed to meet the quota.
     */
    private boolean failed;

    /**
     * Constructor of the CompanyQuota class.
     * Initializes the company quota system with its starting values.
     */
    public CompanyQuota() {
        this.companyRank = INITIAL_RANK;
        this.quota = INITIAL_QUOTA;
        this.companyCredits = 0;
        this.timeLimit = INITIAL_TIME_LIMIT;
        this.elapsedTurns = 0;
        this.failed = false;
    }

    /**
     * Advances the quota timer by one turn and checks whether
     * the company has failed to meet the quota deadline.
     *
     * @return true if the quota deadline has been reached and the company failed,
     *         false otherwise
     */
    public boolean advanceTurnAndCheckFailure() {
        if (failed) {
            return false;
        }

        if (elapsedTurns >= timeLimit && companyCredits < quota) {
            failed = true;
            return true;
        }

        elapsedTurns++;
        return false;
    }

    /**
     * Adds company credits towards the current quota target.
     * If the quota is met or exceeded, a new quota cycle begins.
     *
     * @param amount the number of credits earned for the company
     */
    public void addCompanyCredits(int amount) {
        if (amount > 0 && !failed) {
            companyCredits += amount;
            if (companyCredits >= quota) {
                resetCycle();
            }
        }
    }

    /**
     * Resets the quota cycle after the current quota has been fulfilled.
     * Increases company rank, quota requirement, and time limit.
     */
    private void resetCycle() {
        companyRank++;
        quota = increaseAndRoundUp(quota, 5);
        timeLimit = increaseAndRoundUp(timeLimit, 10);
        companyCredits = 0;
        elapsedTurns = 0;
    }

    /**
     * Increases a value by a given percentage and rounds the result up.
     *
     * @param value the value to increase
     * @param percentage the percentage increase to apply
     * @return the increased value rounded up to the nearest integer
     */
    private int increaseAndRoundUp(int value, int percentage) {
        return (int) Math.ceil(value * (100 + percentage) / 100.0);
    }

    /**
     * Returns the number of turns remaining before the quota deadline.
     *
     * @return remaining turns in the current quota cycle
     */
    public int getTurnsRemaining() {
        return Math.max(0, timeLimit - elapsedTurns);
    }

    /**
     * Indicates whether the Supercomputer facilities can still be used.
     *
     * @return true if the company has not failed the quota cycle,
     *         false otherwise
     */
    public boolean isSupercomputerUsable() {
        return !failed;
    }

    /**
     * Returns a summary of the current company quota status.
     *
     * @return a formatted string containing company rank, credits,
     *         quota target, and turns remaining
     */
    public String statusSummary() {
        return "Company Rank " + companyRank
                + " | Company Credits: " + companyCredits + "/" + quota
                + " | Turns remaining: " + getTurnsRemaining();
    }
}