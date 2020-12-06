package hu.bme.mit.cps.smartuniversity;

import java.util.concurrent.TimeUnit;

import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

public class Database {
	private static String InfluxURL =  "http://10.96.0.100:8086"; //"http://192.168.99.102:8086"; 
	private static String InfluxUser = "cps-k8slab-user";
	private static String InfluxPass = "LaborImage";
	private static String InfluxDBname = "cps-k8slab-power";
	protected InfluxDB databaseConnection;
	
	public Database() {
		databaseConnection = InfluxDBFactory.connect(InfluxURL, InfluxUser, InfluxPass);
		databaseConnection.setDatabase(InfluxDBname);
		databaseConnection.enableBatch(BatchOptions.DEFAULTS);
	}
	
	public void addData(long timestamp, float value, int id){
		databaseConnection.write(Point.measurement("adjustments")
        	    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
        	    .addField("value", value)
        	    .addField("timestamp", timestamp)
        	    .addField("labID", id)
        	    .build());
	}

}
