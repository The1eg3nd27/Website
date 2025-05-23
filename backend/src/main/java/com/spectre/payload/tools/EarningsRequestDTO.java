package com.spectre.payload.tools;

public class EarningsRequestDTO {
    private double balanceBefore;
    private double balanceAfter;
    private int playerCount;     
    private String note;        

    public double getBalanceBefore() { return balanceBefore; }
    public void setBalanceBefore(double balanceBefore) { this.balanceBefore = balanceBefore; }

    public double getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(double balanceAfter) { this.balanceAfter = balanceAfter; }

    public int getPlayerCount() { return playerCount; }
    public void setPlayerCount(int playerCount) { this.playerCount = playerCount; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
