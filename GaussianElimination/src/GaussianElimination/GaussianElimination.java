package GaussianElimination;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;


public class GaussianElimination {
    
    public static void Annule(float [][]A, int k , int i){
        float coeff = A[i][k]/A[k][k];
        int size = A[0].length;
        for(int j=0; j<size ; j++){
            A[i][j] = A[i][j] - (coeff * A[k][j]);
        }
    }
    
    public static void Permute(float[][] A, int k){
        int index =0;
        float []temp;
        for(int i=k+1; i<A.length; i++){
            if( A[i][k] != 0) {index =i; break;}
        }
        temp = A[k];
        A[k] = A[index];
        A[index] = temp;
    } 
    
    public static float[] EliminationDeGausse(float[][]A){
        int lines = A.length;
        int cols = A[0].length;
        float []X = new float[lines];
        
        for(int k=0; k<lines-1; k++){
            for(int i =k+1; i<lines; i++){
                if(A[k][k] == 0) Permute(A, k);
                Annule(A, k , i);
            }
        }
        
        for(int i=lines-1 ; i>=0 ; i--){
            float var = A[i][cols-1];
            int xindex = i+1;
            for(int k = i+1; k< cols-1; k++){
                var -= (A[i][k] * X[xindex]);
                xindex++;
            }
            X[i] = var/A[i][i];
        }
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
        dim = FileSize("DataSet2.txt");
        float[][] A = new float[dim[0]][dim[1]];
        LireFichier("DataSet2.txt",A);
        float []X = EliminationDeGausse(A);
        int size = X.length;
        for(int i=0; i<size; i++){
            System.out.println("x"+(i+1)+" : "+X[i]);
        }
    }
    
}
