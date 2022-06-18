
package PiEstimation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class MonteCarlo {
    
    public static double Estimation(List<Double> x, List<Double> y, double n, double b, double a, double d) throws IOException{
        int m =0;
        double p;
        try (FileWriter outCircle = new FileWriter("Fout.txt");FileWriter inCircle = new FileWriter("Fin.txt")) {
            for(int i=0; i<x.size(); i++){
                if(y.get(i) <= Math.sqrt(1-(x.get(i)*x.get(i)))){ //f(x) = sqrt(1-x^2)
                    m++;
                    inCircle.write(x.get(i)+" "+y.get(i)+"\n");
                }
                else outCircle.write(x.get(i)+" "+y.get(i)+"\n");
            }
            p = m/n;
        }
        return 4*( p * (b-a) *d);//car cette surface et celle d'une demi cercle on le multiplie par 2 pour obtenir l'estimation de pi
    }
    
    public static void main(String[] args) throws IOException {
        List<Double> x = null, y = null;
        Random random = new Random();
        //on travaille sur une demi cercle de rayon 1, -1(a)<=x<=1(b) 0<=y<=1(d) 
        double a=0, b=1, d=1;
        
        /*generation de 100,000 points aleatoires p(x,y) telque -1<x<1 0<y<1*/
        int n = 100000;
        x = random.doubles(a,b).distinct().limit(n).boxed().collect(Collectors.toList());
        y = random.doubles(0,d).distinct().limit(n).boxed().collect(Collectors.toList());
        
        System.out.println(Estimation(x,y,n,b,a,d));
    }
}
