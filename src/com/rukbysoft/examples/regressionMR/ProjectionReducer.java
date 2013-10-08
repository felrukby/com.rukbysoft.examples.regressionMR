package com.rukbysoft.examples.regressionMR;

import java.io.IOException;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class ProjectionReducer extends Reducer<IntWritable, Text, IntWritable, DoubleWritable> {

	@Override
	protected void reduce(final IntWritable key, final Iterable<Text> values, final Context context) throws IOException, InterruptedException {
		
		int targetPeriod = 10;
		
		Participant participant = new Participant(values,targetPeriod);
		double projectedScoreAtTargetPeriod = participant.getProjectedScoreAtTargetPeriod();
		double projectedCumulativeScoreAtTargetPeriod = participant.getProjectedCumulativeScoreAtTargetPeriod();
		
		System.out.println("For Participant: " + String.valueOf(key) + " the projected value at period " + String.valueOf(targetPeriod) + " is " + String.valueOf(projectedScoreAtTargetPeriod));
		
		System.out.println("For Participant: " + String.valueOf(key) + " the projected cumulative value at period " + String.valueOf(targetPeriod) + " is " + String.valueOf(projectedCumulativeScoreAtTargetPeriod));
			
		context.write(key, new DoubleWritable(projectedCumulativeScoreAtTargetPeriod));
	  }
	}
