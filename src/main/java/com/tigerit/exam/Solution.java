package com.tigerit.exam;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.tigerit.exam.IO.*;

/**
 * All of your application logic should be placed inside this class.
 * Remember we will load your application from our custom container.
 * You may add private method inside this class but, make sure your
 * application's execution points start from inside run method.
 */
public class Solution implements Runnable {
    @Override
    public void run() {
        // your application entry point

        // your application entry point
            
        int numberOfTestCase = readLineAsInteger();
        for(int testVar=1; testVar<=numberOfTestCase; testVar++){
            
            //input database
            int numberOfTable = readLineAsInteger();

            ArrayList<MyTable> tables = new ArrayList<MyTable>();

            for(int i=0; i<numberOfTable; i++){
                //input table info
                String tableName = readLine();

                String tempLine = readLine();
                String[] lineWords = tempLine.split(" ");            

                int numberOfCol = Integer.parseInt(lineWords[0]);
                int numberOfRow = Integer.parseInt(lineWords[1]);

                tables.add(i, new MyTable(tableName, numberOfCol, numberOfRow));

                //input column names
                String columnNameLine = readLine();
                String[] columnNameWords = columnNameLine.split(" ");
                for(int j=0; j<numberOfCol; j++){                              
                    tables.get(i).setColumn(columnNameWords[j], j);
                }

                for(int j=0;j<numberOfRow; j++){
                    //input row data
                    String dataLine = readLine();     //check for validation
                    String[] dataLineWords = dataLine.split(" ");
                    for(int k=0; k<numberOfCol; k++){
                        int data = Integer.parseInt(dataLineWords[k]);  //check for validation
                        tables.get(i).setRowData(data, k, j);
                    }                
                }
            }
            
            //output
            printLine("Test: "+testVar);
            
            int numberOfQuery = readLineAsInteger();
            for(int queryVar=0; queryVar<numberOfQuery; queryVar++){
                //input query
                String line1 = readLine();
                String line2 = readLine();
                String line3 = readLine();
                String line4 = readLine();
                readLine();
                
                String[] line1Words = line1.split(" ");
                String[] line2Words = line2.split(" ");
                String[] line3Words = line3.split(" ");
                String[] line4Words = line4.split(" ");

                
                String table1 = line2Words[1];
                String table2 = line3Words[1];

                String[] temp = line4Words[1].split("\\.");               
                String pk1 = temp[1];


                temp = line4Words[3].split("\\.");
                String pk2 = temp[1];

                //get table index number
                int table1Index=0, table2Index=0, pk1Index=0, pk2Index=0;
                for(int i=0; i<tables.size(); i++){
                    if(table1.equals(tables.get(i).tableName)){
                        table1Index = i;
                    }else if(table2.equals(tables.get(i).tableName)){
                        table2Index =  i;
                    }
                }
                    
                //get primary key index
                for(int i=0; i<tables.get(table1Index).numberOfCol; i++){

                    if(pk1.equals(tables.get(table1Index).columns.get(i).columnName)){
                        pk1Index=i;
                    }
                }
                for(int i=0; i<tables.get(table2Index).numberOfCol; i++){
                    if(pk2.equals(tables.get(table2Index).columns.get(i).columnName)){
                        pk2Index=i;
                    }
                }                
                    
                String output="";
                if(line1Words[1].equals("*")){
                    //select all columns   

                    //add all columns names to output
                    for(int i=0; i<tables.get(table1Index).numberOfCol; i++){
                        output +=tables.get(table1Index).columns.get(i).columnName+" ";
                    }
                    for(int i=0; i<tables.get(table2Index).numberOfCol; i++){
                        output +=tables.get(table2Index).columns.get(i).columnName+" ";
                    }                              
                    output +="\n";

                    //match output
                    for(int i=0; i<tables.get(table1Index).numberOfRow; i++){
                        for(int j=0; j<tables.get(table2Index).numberOfRow; j++){
                            if(tables.get(table1Index).columns.get(pk1Index).rows.get(i).data ==  tables.get(table2Index).columns.get(pk2Index).rows.get(j).data ){
                                //matched
                                for(int x=0; x<tables.get(table1Index).numberOfCol; x++){
                                    output +=tables.get(table1Index).columns.get(x).rows.get(i).data+" ";
                                }
                                for(int x=0; x<tables.get(table2Index).numberOfCol; x++){                                    
                                    output +=tables.get(table2Index).columns.get(x).rows.get(j).data+" ";                                    
                                }
                                output +="\n";                                
                            }
                        }
                    }

                    printLine(output);
                }else{
                    ArrayList<OutputColumns> outputColumns = new ArrayList<OutputColumns>();
                    
                    //select specific columns
                    for(int i=1; i<line1Words.length; i++){
                        String[] tableTemp = line1Words[i].split("\\.");
                        String[] columnTemp = tableTemp[1].split(",");
                                                
                        String tableName = tableTemp[0];
                        String columnName = columnTemp[0];
                            
                        output += columnName+" ";
                        
                        
                        int columnIndex = -1;
                        if(line2Words.length==2){
                            if(tableName.equals(line2Words[1])){
                                //get column index of original table                                
                                for(int j=0;j<tables.get(table1Index).numberOfCol; j++){
                                    if( columnName.equals( tables.get(table1Index).columns.get(j).columnName ) ){
                                        columnIndex = j;
                                        break;
                                    }
                                }  
                                
                                outputColumns.add(i-1,new OutputColumns(columnName,table1Index, columnIndex));
                            }else if(tableName.equals(line3Words[1])){
                                //get column index of original table                                
                                for(int j=0;j<tables.get(table2Index).numberOfCol; j++){
                                    if( columnName.equals( tables.get(table2Index).columns.get(j).columnName ) ){
                                        columnIndex = j;
                                        break;
                                    }
                                } 
                                
                                outputColumns.add(i-1,new OutputColumns(columnName,table2Index, columnIndex));
                            }
                        }else{
                            if(tableName.equals(line2Words[2])){
                                //get column index of original table                                
                                for(int j=0;j<tables.get(table1Index).numberOfCol; j++){
                                    if( columnName.equals( tables.get(table1Index).columns.get(j).columnName ) ){
                                        columnIndex = j;
                                        break;
                                    }
                                } 
                                outputColumns.add(i-1,new OutputColumns(columnName,table1Index, columnIndex));
                            }else if(tableName.equals(line3Words[2])){
                                for(int j=0;j<tables.get(table2Index).numberOfCol; j++){
                                    if( columnName.equals( tables.get(table2Index).columns.get(j).columnName ) ){
                                        columnIndex = j;
                                        break;
                                    }
                                }
                                
                                outputColumns.add(i-1,new OutputColumns(columnName,table2Index, columnIndex));
                            }
                        }
                    }
                    output +="\n";
                    
                    //match output
                    for(int i=0; i<tables.get(table1Index).numberOfRow; i++){
                        for(int j=0; j<tables.get(table2Index).numberOfRow; j++){
                            if(tables.get(table1Index).columns.get(pk1Index).rows.get(i).data ==  tables.get(table2Index).columns.get(pk2Index).rows.get(j).data ){
                                //matched
                                for(int x=0; x<outputColumns.size(); x++){
                                    output +=tables.get(outputColumns.get(x).tableIndex).columns.get(outputColumns.get(x).columnIndex).rows.get(i).data+" ";
                                }                                
                                output +="\n";
                            }
                        }
                    }
                    
                    printLine(output);
                }                                
            }                       
        }
    }
    
    public class MyTable{
        String tableName;
        int numberOfCol;
        int numberOfRow;
        ArrayList<MyColumn> columns = new ArrayList<MyColumn>();
        
        public MyTable(String tableName, int numberOfCol, int numberOfRow){
            this.tableName = tableName;
            this.numberOfCol = numberOfCol;
            this.numberOfRow = numberOfRow;
        }
        
        public void setColumn(String columnName, int i){
            columns.add(i, new MyColumn(columnName));
        }
        
        public void setRowData(int data, int colNum, int rowNum){
            columns.get(colNum).setRow(data, rowNum);
        }
    }
    
    public class MyColumn{
        String columnName;
        ArrayList<MyRow> rows = new ArrayList<MyRow>();
        public MyColumn(String columnName){
            this.columnName = columnName;
        }
        
        public void setRow(int data, int i){
            rows.add(i, new MyRow(data));
        }
    }
    
    public class MyRow{
        int data;
        public MyRow(int data){
            this.data = data;
        }                
    }
    public class OutputColumns{
        String columnName;
        int tableIndex, columnIndex;
        
        public OutputColumns(String columnName, int tableIndex, int columnIndex){
            this. columnName = columnName;
            this.tableIndex = tableIndex;
            this.columnIndex = columnIndex;
        }
    }
    
}