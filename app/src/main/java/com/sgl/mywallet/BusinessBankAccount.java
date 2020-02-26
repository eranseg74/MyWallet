package com.sgl.mywallet;

/**
 * BusinessBankAccount is a class that represents a business account.
 * A business account allows its owner to withdraw as much money as he likes without overdraft limits.
 */
public class BusinessBankAccount extends BankAccount {

    // A string representation of the account's type
    private static final String TYPE_OF_ACCOUNT = "Business Account";
    // The overdraft limit of the business account
    private static final float INTEREST = 0.0f;

    /**
     * Constructs a bank account with a name, an amount for deposit, an overdraft limit,
     * a positive preferred interest rate, and a negative preferred interest rate.
     * @param companyName - The account owner's name.
     * @throws IllegalArgumentException is thrown if the owner's name is "" or null,
     * if the deposit is negative, or if the overdraft limit is positive.
     */
    public BusinessBankAccount(String companyName) throws IllegalArgumentException {
        super(companyName);
    }

    /**
     * Constructs a bank account with a name, an amount for deposit, an overdraft limit,
     * a positive preferred interest rate, and a negative preferred interest rate.
     * @param companyName - The account owner's name.
     * @param initialDeposit - The initial deposit to the account.
     * @throws IllegalArgumentException is thrown if the owner's name is "" or null,
     * if the deposit is negative, or if the overdraft limit is positive.
     */
    public BusinessBankAccount(String companyName, float initialDeposit) {
        super(companyName, initialDeposit);
    }

    /**
     * Constructs a bank account with a name, an amount for deposit, an overdraft limit,
     * a positive preferred interest rate, and a negative preferred interest rate.
     * @param companyName - The account owner's name.
     * @param initialDeposit - The initial deposit to the account.
     * @param overdraftLimit - The overdraft limit of the account.
     * @throws IllegalArgumentException is thrown if the owner's name is "" or null,
     * if the deposit is negative, or if the overdraft limit is positive.
     */
    public BusinessBankAccount(String companyName, float initialDeposit, float overdraftLimit) throws IllegalArgumentException {
        super(companyName, initialDeposit, overdraftLimit);
    }

    /**
     * The setOverdraftLimit method throws an IllegalArgumentException
     * @param overdraftLimit - The overdraft limit of the account.
     * @throws IllegalArgumentException whenever this method is invoked.
     */
    public void setOverdraftLimit(float overdraftLimit) throws IllegalArgumentException {
        throw new IllegalArgumentException("There is mo overdraft limit to this account!");
    }

    /**
     * The setOverdraftLimit method throws an IllegalArgumentException
     * @throws IllegalArgumentException
     */
    public float getOverdraftLimit(float overdraftLimit) throws IllegalArgumentException {
        throw new IllegalArgumentException("There is mo overdraft limit to this account!");
    }

    /**
     * Withdraw the given amount of money from the account.
     * @param amount - The amount of money to be withdrawn from the account.
     */
    public void withdraw(float amount) {
        super.dollarExchange(balance -= amount);
    }

    /**
     * Calculates the balance in accordance with the appropriate interest.
     */
    public void monthElapsed() {
        balance += super.interestExchange((balance < 0) ? balance * negInterest : balance * posInterest);
    }

    /**
     * Returns a string representation of the account's type.
     * @return a string representation of the account's type.
     */
    protected String getAccountType() {
        return TYPE_OF_ACCOUNT;
    }

    /**
     * Returns a string representation of the overdraft.
     * @return a string representation of the overdraft.
     */
    public float getOverdraftLimit() {
        return INTEREST;
    }
}
