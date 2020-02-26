package com.sgl.mywallet;

import androidx.annotation.NonNull;

/**
 * PreferredBankAccount is a class that represents a person's account with preferred interests.
 * The effective interest rates for all calculations of interests purposes are
 * the specific preferred interest rates and not the general ones.
 */
public class PreferredBankAccount extends BankAccount {

    // A string representation of the account's type
    private static final String TYPE_OF_ACCOUNT = "Preferred Account";

    // Initializing interests
    protected float preferredPosInterest = POS_INTEREST;
    protected float preferredNegInterest = NEG_INTEREST;

    /**
     * Constructs a bank account with a name, an amount for deposit, an overdraft limit,
     * a positive preferred interest rate, and a negative preferred interest rate.
     * @param accountOwner - The account owner's name.
     * @param initialDeposit - The initial deposit to the account.
     * @param overdraftLimit - The overdraft limit of the account.
     * @param negInterest - The negative interest of the account.
     * @param posInterest - The positive interest of the account.
     * @throws IllegalArgumentException is thrown if the owner's name is "" or null,
     * if the deposit is negative, or if the overdraft limit is positive.
     */
    public PreferredBankAccount(String accountOwner, float initialDeposit, float overdraftLimit,
                                float negInterest, float posInterest) throws IllegalArgumentException {
        super(accountOwner, initialDeposit, overdraftLimit);
        setPreferredNegInterest(negInterest);
        setPreferredPosInterest(posInterest);
    }

    /**
     * Constructs a bank account with a name, an amount for deposit, an overdraft limit,
     * a positive preferred interest rate, and a negative preferred interest rate.
     * @param accountOwner - The account owner's name.
     * @param initialDeposit - The initial deposit to the account.
     * @param negInterest - The negative interest of the account.
     * @param posInterest - The positive interest of the account.
     * @throws IllegalArgumentException is thrown if the owner's name is "" or null,
     * if the deposit is negative, or if the overdraft limit is positive.
     */
    public PreferredBankAccount(String accountOwner, float initialDeposit,
                                float negInterest, float posInterest) throws IllegalArgumentException {
        super(accountOwner, initialDeposit);
        setPreferredNegInterest(negInterest);
        setPreferredPosInterest(posInterest);
    }

    /**
     * Constructs a bank account with a name, an amount for deposit, an overdraft limit,
     * a positive preferred interest rate, and a negative preferred interest rate.
     * @param accountOwner - The account owner's name.
     * @param negInterest - The negative interest of the account.
     * @param posInterest - The positive interest of the account.
     * @throws IllegalArgumentException is thrown if the owner's name is "" or null,
     * if the deposit is negative, or if the overdraft limit is positive.
     */
    public PreferredBankAccount(String accountOwner, float negInterest, float posInterest)
                                                            throws IllegalArgumentException {
        super(accountOwner);
        setPreferredNegInterest(negInterest);
        setPreferredPosInterest(posInterest);
    }

    /**
     * Constructs a bank account with a name, an amount for deposit, an overdraft limit,
     * a positive preferred interest rate, and a negative preferred interest rate.
     * @param accountOwner - The account owner's name.
     * @throws IllegalArgumentException is thrown if the owner's name is "" or null,
     * if the deposit is negative, or if the overdraft limit is positive.
     */
    public PreferredBankAccount(String accountOwner) throws IllegalArgumentException {
        super(accountOwner);
        setPreferredNegInterest(getNegativeInterestRate());
        setPreferredPosInterest(getPositiveInterestRate());
    }

    /**
     * Sets the preferred positive interest rate.
     * @param posInterest - The positive interest rate.
     * @throws IllegalArgumentException is thrown if the given interest rate is negative.
     */
    private void setPreferredPosInterest(float posInterest) throws IllegalArgumentException {
        if(posInterest < 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
        this.preferredPosInterest = posInterest;
    }

    /**
     * Sets the preferred negative interest rate.
     * @param negInterest - The positive interest rate.
     * @throws IllegalArgumentException is thrown if the given interest rate is positive.
     */
    private void setPreferredNegInterest(float negInterest) throws IllegalArgumentException {
        if(negInterest > 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
        this.preferredNegInterest = negInterest;
    }

    /**
     * Returns the preferred positive interest rate.
     * @return the preferred positive interest rate.
     */
    public float getPreferredPosInterest() {
        return posInterest;
    }

    /**
     * Returns the preferred negative interest rate.
     * @return the preferred negative interest rate.
     */
    public float getPreferredNegInterest() {
        return negInterest;
    }

    /**
     * Calculates the monthly interest according to the preferred rates
     * and updates the account accordingly.
     * @throws OverdraftLimitException is thrown if the balance exceeds the overdraft limit.
     */
    public void monthElapsed() throws OverdraftLimitException {
        if(balance <= 0) {
            balance += interestExchange(balance * negInterest);
            if(balance < overdraftLimit) {
                throw new OverdraftLimitException("You have exceeded your overdraft limit!");
            }
        } else {
            balance += interestExchange(balance * posInterest);
        }
    }

    /**
     * Returns a string representation of the account's type.
     * @return a string representation of the account's type.
     */
    public String getAccountType() {
        return TYPE_OF_ACCOUNT;
    }

    @NonNull
    @Override
    /**
     * Returns a string representation of the account data.
     * @return a string representation of the account data.
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append("\tPreferred positive interest rate: " + getPreferredPosInterest());
        builder.append("\tPreferred negative interest rate: " + getPreferredNegInterest());
        return builder.toString();
    }
}
