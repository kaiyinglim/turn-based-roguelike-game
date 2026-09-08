package game.systems;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompanyQuotaTest {

    @Test
    void startsAtRankOneWithBaseQuotaAndTimeLimit() {
        CompanyQuota quota = new CompanyQuota();

        assertEquals(true, quota.isSupercomputerUsable());
        assertEquals(200, quota.getTurnsRemaining());
        assertEquals(
                "Company Rank 1 | Company Credits: 0/100 | Turns remaining: 200",
                quota.statusSummary()
        );
    }

    @Test
    void handlesCreditsBelowExactlyAtAndAboveCurrentQuota() {
        CompanyQuota belowQuota = new CompanyQuota();
        belowQuota.addCompanyCredits(50);

        CompanyQuota exactQuota = new CompanyQuota();
        exactQuota.addCompanyCredits(100);

        CompanyQuota aboveQuota = new CompanyQuota();
        aboveQuota.addCompanyCredits(125);

        assertEquals(
                "Company Rank 1 | Company Credits: 50/100 | Turns remaining: 200",
                belowQuota.statusSummary()
        );
        assertEquals(
                "Company Rank 2 | Company Credits: 0/105 | Turns remaining: 220",
                exactQuota.statusSummary()
        );
        assertEquals(
                "Company Rank 2 | Company Credits: 0/105 | Turns remaining: 220",
                aboveQuota.statusSummary()
        );
    }

    @Test
    void roundsQuotaUpAcrossMultipleSuccessfulCycles() {
        CompanyQuota quota = new CompanyQuota();

        quota.addCompanyCredits(100);
        quota.addCompanyCredits(105);

        assertEquals(
                "Company Rank 3 | Company Credits: 0/111 | Turns remaining: 242",
                quota.statusSummary()
        );
    }

    @Test
    void resetsTimerWhenQuotaIsMetBeforeDeadline() {
        CompanyQuota quota = new CompanyQuota();

        for (int turn = 0; turn < 20; turn++) {
            assertEquals(false, quota.advanceTurnAndCheckFailure());
        }

        quota.addCompanyCredits(100);

        assertEquals(
                "Company Rank 2 | Company Credits: 0/105 | Turns remaining: 220",
                quota.statusSummary()
        );
    }

    @Test
    void failsAfterDeadlineWhenQuotaIsNotMetAndIgnoresLaterCredits() {
        CompanyQuota quota = new CompanyQuota();

        for (int turn = 0; turn < 200; turn++) {
            assertEquals(false, quota.advanceTurnAndCheckFailure());
        }

        assertEquals(0, quota.getTurnsRemaining());
        assertEquals(true, quota.advanceTurnAndCheckFailure());
        assertEquals(false, quota.isSupercomputerUsable());

        quota.addCompanyCredits(100);

        assertEquals(
                "Company Rank 1 | Company Credits: 0/100 | Turns remaining: 0",
                quota.statusSummary()
        );
    }

    @Test
    void negativeCreditsDoNothing() {

        CompanyQuota quota = new CompanyQuota();

        quota.addCompanyCredits(-50);

        assertEquals(
                "Company Rank 1 | Company Credits: 0/100 | Turns remaining: 200",
                quota.statusSummary()
        );
    }
}
