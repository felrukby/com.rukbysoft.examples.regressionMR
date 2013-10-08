package com.rukbysoft.examples.regressionMR;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;



public class ProjectionMapper extends Mapper<LongWritable, Text, IntWritable, Text> {

	  @Override
	  public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		  //Should add code to check to handle issues with the data
		  String line = value.toString();
		  String[] ScoreData = line.split("\\t");
	      IntWritable userId = new IntWritable(Integer.parseInt(ScoreData[1]));
	      Text intervalscore = new Text(ScoreData[0].toString() + "-" + ScoreData[2].toString());
	      context.write(userId, intervalscore);
	  }

	}
