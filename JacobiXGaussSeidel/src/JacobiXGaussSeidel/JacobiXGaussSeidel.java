
package JacobiXGaussSeidel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class JacobiXGaussSeidel {
    
    public static float[] Jacobi(float[][]A){
        float []X = new float[A.length];
        int col = A[0].length, count, iterrations;
        float sum;
        float []temp;
        boolean arret = false;
        
        for( iterrations =0 ;iterrations<100; iterrations ++){
            if(!arret){
                count =0;
                temp = X.clone();
                for(int i=0; i<A.length; i++){
                    sum=0;
                    for(int j=0; j<col-1 ; j++){
                        if(j==i) {
                        } else sum+=A[i][j]*temp[j];
                    }
                    X[i] = (1/A[i][i])*(A[i][col-1] - sum);
                }
                for(int k=0; k<X.length; k++){
                    if( Math.abs( (X[k]- temp[k]) / temp[k] ) *100 < 5){
                        count++;
                    }
                }
                if(count == X.length)
                    arret = true;
            }
            else{
                break;
            }
        }
        System.out.println("\n---------- Jacobi iterrations: "+iterrations+"\n");

        return X;
    }
    
    public static float[] Gauss_Seidel(float[][]A){
        float []X = new float[A.length];
        int col = A[0].length, count, iterrations;
        float sumCurrent, sum;
        float []temp = new float[X.length];
        boolean arret = false;
        
        for( iterrations =0 ;iterrations<100; iterrations ++){
            if(!arret){
                count = 0;
                temp = X.clone();
                for(int i=0; i<A.length; i++){
                    sum = sumCurrent=0;
                    
                    for(int j=0; j<i; j++)
                        sumCurrent += (A[i][j]*X[j]); 
                    
                    for(int j=i+1; j<col-1 ; j++)
                        sum += (A[i][j]*temp[j]);
                    
                    X[i] = (1/A[i][i])*(A[i][col-1] - sumCurrent - sum);
                }
                
                for(int k=0; k<X.length; k++){
                    if(Math.abs((X[k]- temp[k])/temp[k])*100 < 5){
                        count++;
                    }
                }
                if(count == X.length){
                    arret = true;
                }
            }
            else{
                break;
            }
        }
        System.out.println("\n----------Gauss-Seidel iterrations: "+iterrations+"\n");
        
        return X;
    }
    
    public static void LireFichier(String nomFichier,float [][]A) throws IOException{
        int i=0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(nomFichier));
            while (reader.ready()) {
                int j;
                String[] line = reader.readLine().split("\\t");
                for(j=0 ; j<line.length; j++) A[i][j] = Integer.parseInt(line[j]);
                i++;
            }
            reader.close();
        } catch (FileNotFoundException e) {}
    }
    public static int[] FileSize(String fileName) throws FileNotFoundException, IOException{
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        int lines = 0;
        int []dim = new int [2];
        if(reader.ready()){
            lines++;
            String[] line = reader.readLine().split("\\t");
            dim[1] = line.length;
            while (reader.readLine() != null) lines++;
        }
        dim[0] = lines;
        reader.close();
        return dim;
    }
    
    public static void main(String[] args) throws IOException {
        int []dim ;
        dim = FileSize("DataSet.txt");
        float[][] A = new float[dim[0]][dim[1]];
        LireFichier("DataSet.txt",A);
        float[] XJacobi = Jacobi(A);
        float[] XGaussSeidel = Gauss_Seidel(A);
        
        System.out.println("Jacobi          Gauss-Seidel");
        for(int i =0 ; i<XJacobi.length; i++)
            System.out.println(XJacobi[i]+"         "+XGaussSeidel[i]);
    }
    
}
