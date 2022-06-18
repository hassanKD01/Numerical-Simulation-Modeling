
package nrmse;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class NRMSE {
    
    public static double Moyenne(ArrayList<Double> values){
        float sum =0,moyenne;
        for (double value : values)sum+= value;
        moyenne = sum/values.size();
        return moyenne;
    }
    
    public static double EcartType(ArrayList<Double> values){
        double moyenne = Moyenne(values),sum = 0;
        for (double value : values)sum+= (value - moyenne)*(value - moyenne);
        return Math.sqrt(sum/values.size());
    }
    
    public static double Covariance(ArrayList<Double> x, ArrayList<Double> y){
        double moyenneX = Moyenne(x), moyenneY = Moyenne(y), sum = 0;
        for(int i =0; i < x.size(); i++) sum += (x.get(i) - moyenneX) * (y.get(i) - moyenneY);
        return sum/x.size();
    }
    
    public static double Correlation(ArrayList<Double> x, ArrayList<Double> y){
        return Covariance(x,y)/(EcartType(x)*EcartType(y));
    }
    
    public static double PredictionLineaire(double abscisse, double a , double b ){
        return a * abscisse + b;
    }
    
    public static double SSE(ArrayList<Double> yApproche, ArrayList<Double> y){
        double sse = 0;
        for(int i =0 ;i < y.size(); i++){
            sse += (y.get(i) - yApproche.get(i)) * (y.get(i) - yApproche.get(i));
        }
        return sse;
    }
    
    public static ArrayList<Double> EnsembleApprocheLineaire(ArrayList<Double> x, double a , double b){
        ArrayList<Double> yApproche = new ArrayList();
        for(int i =0 ;i < x.size(); i++)
            yApproche.add(a * x.get(i) + b);
        return yApproche;
    }
    
    public static double NRMSE(ArrayList<Double> yApproche,ArrayList<Double> x,ArrayList<Double> y){
        double SSE = SSE(yApproche,y);
        double RMSE = Math.sqrt(SSE/x.size());
        return RMSE/Moyenne(y);
    }
    
    public static ArrayList<Double> CalculerDistance(double newPoint, ArrayList<Double> x){
        ArrayList<Double> distances = new ArrayList();
        for (int i =0; i< x.size();i++)
            distances.add((newPoint - x.get(i))*(newPoint - x.get(i)));
        return distances;
    }
    
    public static double PredictionKPPV(double abscisse,ArrayList<Double> x,ArrayList<Double> y){
        ArrayList<Double> distances = CalculerDistance(abscisse,x);
        ArrayList<Double> copy = (ArrayList<Double>) distances.clone();
        int k = (int) Math.round(Math.sqrt(x.size()));
        Collections.sort(distances);
        List<Double> k_nearest = distances.subList(1, k+1);
        int [] indexes = new int[k];
        for(int i=0; i<k;i++)
            indexes[i] = copy.indexOf(k_nearest.get(i));
        double sum =0;
        for(int i=0; i<k;i++)
            sum += y.get(indexes[i]);
        return sum/k;
    }
    
    public static ArrayList<Double> EnsembleApprocheKPPV(ArrayList<Double> x,ArrayList<Double> y){
        ArrayList<Double> yApproche = new ArrayList();
        for(int i =0 ;i < x.size(); i++)
            yApproche.add(PredictionKPPV(x.get(i),x,y));
        return yApproche;
    }

    public static void LireFichier(String nomFichier,ArrayList<Double> x,ArrayList<Double> y) throws IOException{
        try {
            BufferedReader reader = new BufferedReader(new FileReader(nomFichier));
            reader.readLine();
            while (reader.ready()) {
                String[] line = reader.readLine().split("\\t");
                x.add(Double.parseDouble(line[0]));
                y.add(Double.parseDouble(line[1]));
            }
            reader.close();
        } catch (FileNotFoundException e) {}
    }
    
    public static void main(String[] args) throws IOException {
        ArrayList<Double> x = new ArrayList();
        ArrayList<Double> y = new ArrayList();
        LireFichier("DataSet1.txt",x,y);
        double a = Correlation(x, y)*(EcartType(x)/EcartType(y));
        double b = Moyenne(y) - a * Moyenne(x);
        ArrayList<Double> yApprocheLineaire = EnsembleApprocheLineaire(x,a,b);
        ArrayList<Double> yApprocheKPPV = EnsembleApprocheKPPV(x,y);

        System.out.format("NRMSE régression linéaire: %f\nNRMSE K-plus proches voisins: %f\n", NRMSE(yApprocheLineaire,x,y), NRMSE(yApprocheKPPV,x,y));
    }
    
}
