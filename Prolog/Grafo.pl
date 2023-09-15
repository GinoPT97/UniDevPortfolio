arco(a,b).
arco(a,c).
arco(b,d).
arco(d,e).
arco(c,d).
arco(f,g).

connessi(X,X).
connessi(X,Y):- arco(X,Z), connessi(Z,Y).
