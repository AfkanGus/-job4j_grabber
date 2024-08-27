package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * 1. Quartz [#175122].
 */
public class AlertRabbit {
    public static void main(String[] args) throws IOException {
        try (InputStream in = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            Properties properties = new Properties();
            properties.load(in);

            try (Connection connection = initConnection(properties)) {
                List<Long> store = new ArrayList<>();
                Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
                scheduler.start();

                JobDataMap data = new JobDataMap();
                data.put("store", store);
                data.put("connection", connection);

                JobDetail job = newJob(Rabbit.class)
                        .usingJobData(data)
                        .build();
                SimpleScheduleBuilder times = simpleSchedule()
                        .withIntervalInSeconds(5)
                        .repeatForever();
                Trigger trigger = newTrigger()
                        .startNow()
                        .withSchedule(times)
                        .build();
                scheduler.scheduleJob(job, trigger);
                /*Работает 10 секунд*/
                Thread.sleep(10000);
                scheduler.shutdown();

                System.out.println(store);
            }
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    /*Метод для создания соединения с базой данных*/
    private static Connection initConnection(Properties properties) throws Exception {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/schema_db";
        String login = "postgres";
        String password = "password";
        return DriverManager.getConnection(url, login, password);
    }


    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
            Connection connection = (Connection) context.getJobDetail().getJobDataMap().get("connection");

            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO rabbit (created_date) VALUES (?)")) {
                statement.setDate(1, new java.sql.Date(System.currentTimeMillis()));
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            @SuppressWarnings("unchecked")
            List<Long> store = (List<Long>) context.getJobDetail().getJobDataMap().get("store");
            store.add(System.currentTimeMillis());
        }
    }
}

