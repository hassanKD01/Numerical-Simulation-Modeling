#C:\Users\USER\Documents\NetBeansProjects\Lab9

% Dessiner un cercle de rayon 1:
x=-2:2
y=1-x.^2
plot(x,y)
hold on
% Dessiner les points des fichiers Fin et Fout
[xi,yi]=textread('path:\to\FIn\File\Fin.txt'); 
scatter(xi,yi,'g') ; % en vert

[xo,yo]=textread('path\to\FOut\File\Fout.txt'); 
scatter(xo,yo,'r') ; % en rouge
