
package CrossValidation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CrossValidation {
    
    public static double Moyenne(List<Double> values){
        float sum =0,moyenne;
        for (double value : values)sum+= value;
        moyenne = sum/values.size();
        return moyenne;
    }
    
    public static double EcartType(List<Double> values){
        double moyenne = Moyenne(values),sum = 0;
        for (double value : values)sum+= (value - moyenne)*(value - moyenne);
        return Math.sqrt(sum/values.size());
    }
    
    public static double Covariance(List<Double> x, List<Double> y){
        double moyenneX = Moyenne(x), moyenneY = Moyenne(y), sum = 0;
        for(int i =0; i < x.size(); i++) { sum += (x.get(i) - moyenneX) * (y.get(i) - moyenneY);/*System.out.println(x.size());*/}
        return sum/x.size();
    }
    
    public static double Correlation(List<Double> x, List<Double> y){
        return Covariance(x,y)/(EcartType(x)*EcartType(y));
    }
    
    public static double PredictionLineaire(double abscisse, double a , double b ){
        return a * abscisse + b;
    }
    
    public static ArrayList<Double> EnsembleApprocheLineaire(List<Double> x, double a , double b){
        ArrayList<Double> yApproche = new ArrayList();
        for(int i =0 ;i < x.size(); i++){
            yApproche.add(a * x.get(i) + b);
        }
        return yApproche;
    }
    
    public static ArrayList<Double> CalculerDistance(double newPoint, List<Double> x){
        ArrayList<Double> distances = new ArrayList();
        for (int i =0; i< x.size();i++)
            distances.add((newPoint - x.get(i))*(newPoint - x.get(i)));
        return distances;
    }
    
    public static double PredictionKPPV(double abscisse,List<Double> x,List<Double> y){
        ArrayList<Double> distances = CalculerDistance(abscisse,x);
        ArrayList<Double> copy = CalculerDistance(abscisse,x);
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
    
    public static ArrayList<Double> EnsembleApprocheKPPV(List<Double> testX, List<Double> x,List<Double> y){
        ArrayList<Double> yApproche = new ArrayList();
        for(int i =0 ;i < testX.size(); i++){
//            System.out.println(x.size()+i);
            yApproche.add(PredictionKPPV(testX.get(i),x,y));
        }
        return yApproche;
    }
    
    public static double NRMSE(List<Double> y, ArrayList<Double> yChap, int k){
        int n = y.size();
        double sum =0;
        for(int i=0; i<n; i++){
            sum+= (y.get(i) - yChap.get(i)) * (y.get(i) - yChap.get(i));
        }
        return (Math.sqrt(sum/n))/Moyenne(y);
    }
    
    public static double Validation_Croisee_RL(ArrayList<Double> x,ArrayList<Double> y){
        double a, b, sum = 0;
        int k =10;
        ArrayList<Double> yChap;
        int size = x.size()/k;
        
        for(int i=0; i<k; i++){
            
            List<Double> testX = new ArrayList<>(x.subList(i*size, i*size+size));
            List<Double> testY = new ArrayList<>(y.subList(i*size, i*size+size));
            ArrayList <Double>ApprentissageX = new ArrayList<>(x);
            ArrayList<Double> ApprentissageY = new ArrayList<>(y);
        
            for(int j =0; j<testX.size();j++){ApprentissageX.remove(testX.get(j)); ApprentissageY.remove(testY.get(j));}
            
            a = Correlation(ApprentissageX, ApprentissageY)*(EcartType(ApprentissageX)/EcartType(ApprentissageY));
            b = Moyenne(ApprentissageY) - a * Moyenne(ApprentissageX);
            yChap = EnsembleApprocheLineaire(testX,a,b);
                        
            sum += NRMSE(testY, yChap,k);
        }
        return sum/k;
    }
    
    public static double Validation_Croisee_KNN(ArrayList<Double> x,ArrayList<Double> y){
        double sum = 0;
        int k =10;
        ArrayList<Double> yChap;
        int size = x.size()/k;
        
        for(int i=0; i<k; i++){
            List<Double> testX = new ArrayList<>(x.subList(i*size, i*size+size));
            List<Double> testY = new ArrayList<>(y.subList(i*size, i*size+size));
            ArrayList <Double>ApprentissageX = new ArrayList<>(x);
            ArrayList<Double> ApprentissageY = new ArrayList<>(y);
        
            for(int j =0; j<testX.size();j++){ApprentissageX.remove(testX.get(j)); ApprentissageY.remove(testY.get(j));}
            
            yChap = EnsembleApprocheKPPV(testX, ApprentissageX, ApprentissageY);
                        
            sum += NRMSE(testY, yChap,k);
        }
        return sum/k;
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
       
        System.out.format("Validation croisee regression lineaire: %f\nValidation croisee k-nearest-neighbours: %f\n",
                Validation_Croisee_RL(x,y),Validation_Croisee_KNN(x,y));
    }
    
}
