package com.spectre.payload.tools;


public class EarningsResponseDTO {
    private double totalDifference;
    private double perPerson;
    private boolean isProfit;
    private String summary;

    public double getTotalDifference() { return totalDifference; }
    public void setTotalDifference(double totalDifference) { this.totalDifference = totalDifference; }

    public double getPerPerson() { return perPerson; }
    public void setPerPerson(double perPerson) { this.perPerson = perPerson; }

    public boolean isProfit() { return isProfit; }
    public void setProfit(boolean profit) { isProfit = profit; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
}
