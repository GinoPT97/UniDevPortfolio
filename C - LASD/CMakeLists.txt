cmake_minimum_required(VERSION 3.0)
project(C-LASD-Geany)

# Aggiungi tutti i file del progetto
add_executable(C-LASD-Ubuntu
    main.c
    funzioni.c
    libreria.h
    ../Librerie-C/Stack/stack.h
    ../Librerie-C/Input/inputReader.c
    ../Librerie-C/Input/inputReader.h
    ../Librerie-C/Grafi/graph.h
    ../Librerie-C/Grafi/list.c
    ../Librerie-C/Grafi/list.h
    ../Librerie-C/Stack/stack.c
    ../Librerie-C/MatrixGraph/matrixgraph.c
    ../Librerie-C/MatrixGraph/matrixgraph.h
    ../Librerie-C/List/listdopp.c
    ../Librerie-C/List/listdopp.h
    ../Librerie-C/Code/queue.c
    ../Librerie-C/Code/queue.h
    ../Librerie-C/Grafi/graph.c
    ../Librerie-C/BST/tree.c
    ../Librerie-C/BST/tree.h
    ../Librerie-C/Heap/heap.c
    ../Librerie-C/Heap/heap.h
)

# Aggiungi eventuali altre impostazioni o librerie necessarie

