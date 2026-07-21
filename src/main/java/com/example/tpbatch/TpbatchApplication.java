package com.example.tpbatch;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TpbatchApplication implements CommandLineRunner {

	@Autowired
	JobOperator jobOperator;
	@Autowired
	Job job;
	public static void main(String[] args) {
		SpringApplication.run(TpbatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String typeCriteria = "";
		String criteria = "";
		if(args.length != 0 && args.length != 2)
		{
			throw new IllegalArgumentException("Le nombre d'arguments de la commande n'est pas bon");

		}if(args.length == 2)
		{
			typeCriteria = args[0];
			criteria = args[1];
		}
		JobParameters jobParameters= new JobParametersBuilder()
				.addString("typeCriteria", typeCriteria)
				.addString("criteria", criteria)
				.addLong("timestamp", System.currentTimeMillis())
				.toJobParameters();

		jobOperator.start(job,jobParameters);
	}
}
