package com.rukbysoft.examples.regressionMR;

import org.apache.hadoop.io.Text;
import java.math.BigDecimal;



public class Participant {
	
	private int targetPeriod;
	private int initialNumberOfPeriods = 0;
	private double sumOfXValues = 0;
	private double sumOfYValues = 0;
	private double sumOfXYValues = 0;
	private double sumOfXXValues = 0;
	private double slope = 0;
	private double intercept = 0;
	private double projectedScoreAtTargetPeriod = 0;
	private double projectedCumulativeScoreAtTargetPeriod=0;
	
	public Participant(final Iterable<Text> values, int period)
	{
		this.targetPeriod = period;
		
		calculateSumsOfValues(values);
		
		slope = calculateSlope(initialNumberOfPeriods, sumOfXValues, sumOfYValues, sumOfXYValues, sumOfXXValues);
		
		intercept = calculateIntercept(initialNumberOfPeriods, sumOfXValues, sumOfYValues, slope);

		projectedScoreAtTargetPeriod = round(calculateProjectedScoreForTargetPeriod(slope, intercept, targetPeriod),2,BigDecimal.ROUND_HALF_UP);
		
		projectedCumulativeScoreAtTargetPeriod = round(calculateCumulativeScoreAtTargetPeriod(),2,BigDecimal.ROUND_HALF_UP);
	}
	
	public double getProjectedScoreAtTargetPeriod()
	{
		return projectedScoreAtTargetPeriod;
	}
	
	public double getProjectedCumulativeScoreAtTargetPeriod()
	{
		return projectedCumulativeScoreAtTargetPeriod;
	}
	
	private double calculateCumulativeScoreAtTargetPeriod()
	{
		
		double sumOfFutureYValues = 0;
		double interimProjectedScore = 0;
		double cumulativeScoreAtTargetPeriod = 0;
		
		for (int i = this.initialNumberOfPeriods+1 ; i < this.targetPeriod+1 ; ++i)
		{
			interimProjectedScore = round(calculateProjectedScoreForTargetPeriod(slope, intercept, i),2,BigDecimal.ROUND_HALF_UP);
			sumOfFutureYValues += interimProjectedScore;
		}
		
		cumulativeScoreAtTargetPeriod = sumOfFutureYValues + sumOfYValues;
		
		return cumulativeScoreAtTargetPeriod;
		
	}
	
	private void calculateSumsOfValues(Iterable<Text> values)
		{
			double sumX = 0;
			double sumY = 0;
			double sumXX = 0;
			double sumXY = 0;
			int i = 0;
			
			
			  for(Text value : values)
			  {
				  String[] v = value.toString().split("-");
				  double x = Double.valueOf(v[0]);
				  double y = Double.valueOf(v[1]);
				  sumX += x;
				  sumY += y;
				  sumXX += x*x;
				  sumXY += x*y;
				  i ++;
			  }
			  
			  initialNumberOfPeriods = i;
			  sumOfXValues = round(sumX,2,BigDecimal.ROUND_HALF_UP);
			  sumOfYValues = round(sumY,2,BigDecimal.ROUND_HALF_UP);
			  sumOfXYValues = round(sumXY,2,BigDecimal.ROUND_HALF_UP);
			  sumOfXXValues = round(sumXX,2,BigDecimal.ROUND_HALF_UP);
		}

		
		
		private double calculateSlope(int NumberOfPeriods, double SumOfXValues, double SumOfYValues, double SumOfXYValues, double SumOfXXValues)
		{
			double slope = ((NumberOfPeriods*SumOfXYValues)-(SumOfXValues*SumOfYValues))/((NumberOfPeriods*SumOfXXValues)-(Math.pow(SumOfXValues,2))) ;
			return slope;
		}
		
		private double calculateIntercept(int NumberOfPeriods, double SumOfXValues, double SumOfYValues, double Slope)
		{

			double intercept = (SumOfYValues - (Slope*SumOfXValues)) / NumberOfPeriods;
			return intercept;
		}
		
		private double calculateProjectedScoreForTargetPeriod(double Slope, double Intercept, double TargetPeriod)
		{
			double ProjectedScore = Intercept + (Slope*TargetPeriod);
			return ProjectedScore;
		}

		private double round(double unrounded, int precision, int roundingMode)
		{
		    BigDecimal bd = new BigDecimal(unrounded);
		    BigDecimal rounded = bd.setScale(precision, roundingMode);
		    return rounded.doubleValue();
		}		
}
