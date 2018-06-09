package com.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;


@SuppressWarnings({ })
public class GraphBar extends Application {
    private static Map<String, Map<String,Object>> mapXYSeries = new TreeMap<>();
    private static String tittleApp;
    private static String tittleBar;
    private static String xLabel;
    private static String yLabel;
    
    public void setMapXYSeries(Map<String, Map<String, Object>> mapXY) {
    		mapXYSeries = mapXY;
    }
    
    public void setTittleApp(String vTittleApp) {
    		tittleApp = vTittleApp;
    }
    
    public void setTittleBar(String vTittleBar) {
    		tittleBar = vTittleBar;
    }
    
    public void setXLabel(String vXLabel) {
    		xLabel = vXLabel;
    }
    
    public void setYLabel(String vYLabel) {
    		yLabel = vYLabel;
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override public void start(Stage stage) throws InterruptedException {
		stage.setTitle(tittleApp);
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle(tittleBar);
        xAxis.setLabel(xLabel);       
        yAxis.setLabel(yLabel);
        
        List<XYChart.Series> lstXYChartSeries = new ArrayList<>();
        XYChart.Series series;
        for (Map.Entry<String, Map<String,Object>> entry : mapXYSeries.entrySet()) {
        		series = new XYChart.Series();
        		series.setName(entry.getKey());
        		
        		Map<String, Object> orderMap = new TreeMap<>();
        		for (Map.Entry<String, Object> parValue : entry.getValue().entrySet()) {
        			orderMap.put(parValue.getKey(), parValue.getValue());
        		}
        		
        		for (Map.Entry<String, Object> parValue : orderMap.entrySet()) {
        			series.getData().add(new XYChart.Data(parValue.getKey(), parValue.getValue()));
        		}
        		
        		lstXYChartSeries.add(series);
        }
 
        Scene scene;
        scene  = new Scene(bc,1200,600);
        for (int i=0; i<lstXYChartSeries.size();i++) {
        		bc.getData().addAll(lstXYChartSeries.get(i));
        }
        stage.setScene(scene);
        stage.show();
	}
}
