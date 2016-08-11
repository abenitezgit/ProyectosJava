/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvabtviewer;

import abtGlobals.globalVARArea;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import mypkg.hbaseDB;
import mypkg.sqlDB;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import static srvabtviewer.thExecETL.cETL_ACTIVE;

/**
 *
 * @author ABT
 */
public class SrvABTViewer {
    //Conectores a Bases de Datos
    //

    static hbaseDB connHB;

    private sqlDB vSqlConnDB;
    private ResultSet glb_rsSQL;

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        globalVARArea globalArea = new globalVARArea("/Users/andresbenitez/Documents/app/ABTViewer3/srvConf.properties");
        connHB = new hbaseDB("/Users/andresbenitez/Documents/app/ABTViewer3/srvConf.properties","HBConf2");
        
        //Procedimiento para Ejecutar Consultas y Retornar ResultSet de Datos
        //
        //Crear Lista para Filtros
        //
        List<Filter> filters = new ArrayList<>();

        
        //Filtro de columnas con valores
        

            SingleColumnValueFilter colValFilter = new SingleColumnValueFilter(Bytes.toBytes("time"), Bytes.toBytes("ano")
                , CompareFilter.CompareOp.GREATER_OR_EQUAL, new BinaryComparator(Bytes.toBytes("2015")));
            
            //SingleColumnValueFilter colValFilter2 = new SingleColumnValueFilter(Bytes.toBytes("grab"), Bytes.toBytes("url")
            //    , CompareFilter.CompareOp.GREATER_OR_EQUAL, new BinaryComparator(Bytes.toBytes("http://srv-gui-g.e-contact.cl/e-recorder/audio/20160310/09/01_20160310_095259")));
            
        
            colValFilter.setFilterIfMissing(true);
            colValFilter.filterRow();
            colValFilter.getLatestVersionOnly();
            
            filters.add(colValFilter);
            //filters.add(colValFilter2);
            //filters.add(filter2);
            
            //FilterList fl = new FilterList( FilterList.Operator.MUST_PASS_ALL, filters);
            
            //scan.setFilter(fl);
        
        
        
        //Definir los Filtros que se desean
        //
        //Para Agregar Filtro por Prefijos
        //byte[] prefix = Bytes.toBytes("srv");

        //
        //Filter filter1 = new RowFilter(CompareFilter.CompareOp.EQUAL,new BinaryComparator(Bytes.toBytes("C00002|")));
        //Filter filter2 = new ColumnPrefixFilter((Bytes.toBytes("grab")));
        //Filter filter1 = new PrefixFilter(prefix);
        //Filter filter1 = new RowFilter(CompareFilter.CompareOp.EQUAL,new BinaryComparator(Bytes.toBytes(vProc)));

        //Se agrupan los filtros
        //filters.add(filter1);
        //filters.add(filter2);

        //Se crea la matriz de columnas a devolver
        List<String> qualifiers = new ArrayList<>();

        //Agrega las Columnas y Qualifiers
        qualifiers.add("cli:name");
        qualifiers.add("grab:ani");
        qualifiers.add("grab:dnis");
        qualifiers.add("grab:name");
        qualifiers.add("grab:url");
        qualifiers.add("time:ano");
        qualifiers.add("time:dia");
        qualifiers.add("time:fgrab");
        qualifiers.add("time:hora");
        qualifiers.add("time:mes");
        qualifiers.add("time:min");
        qualifiers.add("time:seg");
        qualifiers.add("time:semana");

        //Se ejecuta el Metodo connHB.getRsQuery
        //
        DefaultTableModel dtmData = connHB.getRsQuery("hgrab", qualifiers, filters);
        
        for (int i=0; i<dtmData.getRowCount(); i++) {
            System.out.println(dtmData.getValueAt(i,0).toString());
        }
        /*
        if (dtmData.getRowCount()!=0) {
            for (int i=0; i<dtmData.getColumnCount();i++) {
                System.out.println(dtmData.getValueAt(0, i).toString());
//                switch (dtmData.getColumnName(i)) {
//                    case "etlActive":
//                        cETL_ACTIVE = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlCliID":
//                        cETL_CLIID = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlCliName":
//                        cETL_CLINAME = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlDBDestName":
//                        cETL_DBDESTNAME = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlDBDestPass":
//                        cETL_DBDESTPASS = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlDBDestPort":
//                        cETL_DBDESTPORT = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlDBDestTableName":
//                        cETL_DBDESTTABLENAME = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlDBDestType":
//                        cETL_DBDESTTYPE = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlDBDestUser":
//                        cETL_DBDESTUSER = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlDBSourceName":
//                        cETL_DBSOURCENAME = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlDBSourcePass":
//                        cETL_DBSOURCEPASS = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlDBSourcePort":
//                        cETL_DBSOURCEPORT = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlDBSourceTableName":
//                        cETL_DBSOURCETABLENAME = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlDBSourceType":
//                        cETL_DBSOURCETYPE = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlDBSourceUser":
//                        cETL_DBSOURCEUSER = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlDesc":
//                        cETL_DESC = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlFieldKeyExtract":
//                        cETL_FIELDKEYEXTRACT = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlIntervalExtract":
//                        cETL_INTERVALEXTRACT = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlTimeGap":
//                        cETL_TIMEGAP = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlIntervalUnidad":
//                        cETL_INTERVALUNIDAD = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlTimeIniGen":
//                        cETL_TIMEINIGEN = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlDBSourceServerIP":
//                        cETL_DBSOURCESERVERIP = dtmData.getValueAt(0, i).toString();
//                        break;
//                    case "etlDBDestServerIP":
//                        cETL_DBDESTSERVERIP = dtmData.getValueAt(0, i).toString();
//                        break;
//                    default:
//                        break;
//                }
            }

        }
*/        
        
        
//        
//        Thread th1 = new thExecETL(globalArea);
//        th1.start();
//        System.out.println("Se ha lanzado el Thread TH1-ETL");
        
    }
    
}
