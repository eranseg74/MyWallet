package com.sgl.mywallet;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A class BankAccount represents the accumulation of a person's funds in a bank account.
 */
public class BankAccount {

    // Initialize the negative and positive interests to 0
    protected static final float NEG_INTEREST = 0.0f;
    protected static final float POS_INTEREST = 0.0f;

    // Representation of the interest rate
    protected static float negInterest = NEG_INTEREST;
    protected static float posInterest = POS_INTEREST;

    // The overdraft limit of the bank account
    protected float overdraftLimit;

    // The account type
    private static final String TYPE_OF_ACCOUNT = "Regular Account";

    // The owner's name
    protected String accountOwner;

    // The account balance
    protected float balance = 0.0f;

    // The number of the accounts
    private static int numberOfAccounts = 0;

    // The account unique number
    protected int accountNumber;

    /**
     * Constructs a bank account with a name and an amount for initial deposit for the account.
     * @param accountOwner - The name of the account owner.
     * @throws IllegalArgumentException is thrown if the owner's name is "" or null.
     */
    public BankAccount(String accountOwner)
            throws IllegalArgumentException {
        this(accountOwner, 0.0f, 0.0f);
    }

    /**
     * Constructs a bank account with a name and an amount for initial deposit for the account.
     * @param accountOwner - The name of the account owner.
     * @param initialDeposit - The initial deposit for the account.
     * @throws IllegalArgumentException is thrown if the owner's name is "" or null,
     * or if the deposit is negative.
     */
    public BankAccount(String accountOwner, float initialDeposit) {
        this(accountOwner, initialDeposit, 0.0f);
    }

    /**
     * Constructs a bank account with a name, an amount for initial deposit,
     * and an overdraft limit for the account.
     * @param overdraftLimit - The overdraft limit of the account.
     * @param accountOwner - The name of the account owner.
     * @param initialDeposit - The initial deposit for the account.
     * @throws IllegalArgumentException is thrown if the owner's name is "" or null,
     * if the deposit is negative, or if the overdraft limit is positive.
     */
    public BankAccount(String accountOwner, float initialDeposit, float overdraftLimit)
            throws IllegalArgumentException {
        if(accountOwner == "" || accountOwner == null)
            throw new IllegalArgumentException("No owner name was found to the account");
        this.accountOwner = accountOwner;
        this.setOverdraftLimit(overdraftLimit);
        this.deposit(initialDeposit);
        this.accountNumber = ++numberOfAccounts;
    }

    /**
     * Deposit the given amount to the account. The method throws IllegalArgumentException if the
     * value of the deposit is negative.
     * @param amount - The amount to deposit to the account.
     * @throws IllegalArgumentException if the value of the deposit is negative.
     */
    public void deposit(float amount) throws IllegalArgumentException {
        if(amount < 0)
            throw new IllegalArgumentException("Must deposit only positive amount");
        this.balance += dollarExchange(amount);
    }

    /**
     * Withdraw a given amount of money from the account.
     * @param amount - The amount of money to be withdrawn from the account.
     * @throws IllegalArgumentException if the amount of money to withdraw is negative.
     * @throws OverdraftLimitException if the withdrawn exceeds the overdraft limit.
     */
    public void withdraw(float amount) throws IllegalArgumentException, OverdraftLimitException {
        if(amount < 0)
            throw new IllegalArgumentException("Must withdraw only positive amount!");
        if(balance - amount < overdraftLimit)
            throw new OverdraftLimitException("Your overdraft limit is: " + overdraftLimit +
                    ". You can withdraw only " + (balance + overdraftLimit) + " Dollars");
        this.balance -= dollarExchange(amount);
    }

    /**
     * A utility method for validating the sum format.
     * @param sum - The amount of money to be validate. This value should include only
     *            two digits after the decimal point, otherwise an IllegalArgumentException is thrown.
     * @return the amount of money in dollars and cents.
     * @throws IllegalArgumentException if the given amount has more then
     * two digits after the decimal point.
     */
    protected float dollarExchange(float sum) throws IllegalArgumentException {
        // Check if there are more then two digits after the decimal point
        if(((sum * 100) % 1.0) != 0)
            throw new IllegalArgumentException("Amount must be in dollars and cents");
        return interestExchange(sum);
    }

    /**
     * Set the sum value to return only two digits after the decimal point.
     * This method does not throw an exception in order to allow the interest calculation.
     * @param sum - A float value, given by the user.
     * @return a trimmed value that contains only two digits after the decimal point.
     */
    protected float interestExchange(float sum) {
        if(((sum * 100) % 1.0) != 0)
            Log.i("PRECISION WARNING", "interestExchange: Possible lost of precision");
        float temp = sum * 100;
        sum = (int) temp;
        return sum / 100;
    }

    /**
     * Calculates the monthly interest and updates the account.
     * @throws OverdraftLimitException if after the month elapsed the balance is lower then
     * the overdraft limit.
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
     * Utility method that returns the overdraft limit of the account.
     * @return the overdraft limit of the account.
     */
    public float getOverdraftLimit() {
        return overdraftLimit;
    }

    /**
     * Utility method that returns the owner name of the account.
     * @return the owner name of the account.
     */
    public String getAccountOwner() {
        return accountOwner;
    }

    /**
     * Utility method that returns the balance of the account.
     * @return the balance of the account.
     */
    public float getBalance() {
        return dollarExchange(balance);
    }

    /**
     * Utility method that returns the account number.
     * @return the account number.
     */
    public int getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the overdraft limit in accordance with the given parameter.
     * @param overdraftLimit - The overdraft limit of the account.
     * @throws IllegalArgumentException if the overdraft limit value is greater then 0.
     */
    public void setOverdraftLimit(float overdraftLimit) throws IllegalArgumentException {
        if(overdraftLimit > 0)
            throw new IllegalArgumentException("Overdraft limit must be a negative number");
        this.overdraftLimit = overdraftLimit;
    }

    /**
     * Returns the positive interest rate of the account.
     * @return the positive interest rate of the account.
     */
    public float getPositiveInterestRate() {
        return posInterest;
    }

    /**
     * Returns the negative interest rate of the account.
     * @return the negative interest rate of the account.
     */
    public float getNegativeInterestRate() {
        return negInterest;
    }

    /**
     * Sets the negative interest rate.
     * @param negInter - The new negative interest rate value.
     * @throws IllegalArgumentException if the given negative interest value is smaller then 0.
     */
    public void setNegativeInterestRate(float negInter) throws IllegalArgumentException {
        if(negInterest >= 0)
            throw new IllegalArgumentException("number must be lower then 0!");
        negInterest = negInter;
    }

    /**
     * Sets the negative interest rate.
     * @param posInter - The new positive interest rate value.
     * @throws IllegalArgumentException if the given negative interest value is smaller then 0.
     */
    public void setPositiveInterestRate(float posInter) throws IllegalArgumentException {
        if(posInterest <= 0)
            throw new IllegalArgumentException("number must be higher then 0!");
        posInterest = posInter;
    }

    @NonNull
    @Override
    /**
     * Returns a string representation of the account data.
     * @return a string representation of the account data.
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("Account type: %s\t", TYPE_OF_ACCOUNT));
        builder.append(String.format("Owner name: %s\t", getAccountOwner()));
        builder.append(String.format("Balance: %s\t", getBalance()));
        builder.append(String.format("Account number: %s\t", getAccountNumber()));
        builder.append(String.format("Overdraft limit: %s\t", getOverdraftLimit()));
        return builder.toString();
    }

    @Override
    /**
     * Returns true if the given account is the current account
     * @return true if the given account is the current account
     */
    public boolean equals(@Nullable Object obj) {
        return (obj instanceof BankAccount) && (this.accountNumber == ((BankAccount)obj).accountNumber);
    }
}
