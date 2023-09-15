#include "libreria.h"

//ESERCIZI SULL' HEAP 

//main traccia 09/02/2009

void main090209(){
	int heapsize = 0, H[MAX]={0}, i=0, k=0;
	printf("ACQUISIZIONE MIN HEAP\n");
	heapsize = acquireHeap(H);
	printHeap(H,0,heapsize,0);
	printf("\nCONTROLLO SE E' UNO HEAP..\n\n");
	if(checkHeap(H, 0, heapsize))
		printf("H e' uno heap\n");
	else
		printf("H non e' uno heap");
	printf("\nMODIFICA HEAP\n\n");
	modificaHeap(H,heapsize);
}

void spostaPari(Queue Q, Stack S, int flag){
	int err, holdQ;
	if(!emptyQueue(Q)){
		holdQ = dequeue(Q);
		if(holdQ%2 == 0){ //se pari
			if(flag){ //copiare in S in ordine di priorità di Q
				spostaPari(Q,S,flag);
				push(S,holdQ); //tempo costante
			}
			else{ //inserimento in S in ordine di priorita di Q inverso
				push(S,holdQ);
				spostaPari(Q,S,flag);
			}
		}
		else{
			spostaPari(Q,S,flag);
			enqueue(Q,holdQ);
		}
	}
}

int copiaDispari(Stack S, Queue Q){
	int holdS = 0, err, partialSum = 0;
	if(!emptyStack(S)){
		holdS = pop(S);
		partialSum = copiaDispari(S,Q);
		if(holdS%2){ //se dispari
			//reverseQueue(Q,&err);
			enqueue(Q,holdS); //tempo costante
			//reverseQueue(Q,&err);
		}
		push(S,holdS);
	}
	return holdS + partialSum;
}

int sommaQueue(Queue Q){
	int err, holdQ = 0, sum = 0;
	if(!emptyQueue(Q)){
		holdQ = dequeue(Q);
		sum = sommaQueue(Q);
		enqueue(Q,holdQ);
		//reverseQueue(Q,&err);
	}
	return sum + holdQ;
}

void modificaStrutture(Queue Q, Stack S){
	//nq = numero elem. coda, ns = numero elem. stack
	int sommaS = 0, sommaQ = 0, err;
	reverseQueue(Q); // lineare su nq
	sommaS = copiaDispari(S,Q); // lineare su ns
	reverseQueue(Q); // linreare su nq
	sommaQ = sommaQueue(Q); //lineare su nq
	reverseQueue(Q); //lineare su nq
	printf("SOMMAS = %d\n",sommaS);
	printf("SOMMAQ = %d\n",sommaQ);
	if(sommaS > sommaQ) //inserimento in S con stesso ordine di precedenza in Q
		spostaPari(Q,S,1); // lineare su nq
	else
		spostaPari(Q,S,0); // lineare su nq
	reverseQueue(Q); // lineare su nq
	return;
}

int sommaStack(Stack S){
	int err, holdS = 0, sum = 0;
	if(!emptyStack(S)){
		holdS = pop(S);
		sum = sommaStack(S);
		push(S, holdS);
	}
	return holdS + sum;
}

void insertInStack(Stack S){
	int n = 0, err;
	while(scanf("%d",&n)>0)
		push(S,n);
	scanf("%*[^\n]");
}

void insertInQueue(Queue Q){
		int n = 0, err;
	while(scanf("%d",&n)>0)
		enqueue(Q,n);
	scanf("%*[^\n]");
}

void main090714() {
	int err;
	Stack S = NULL;
	Queue Q = NULL;
	S = initStack();
	Q = initQueue();
	printf("Inserisci lista di interi da inserire nello stack (terminata da un char):\n");
	insertInStack(S);
	printf("Inserisci lista di interi da inserire nella coda  (terminata da un char):\n");
	insertInQueue(Q);
	printf("\nStack acquisito:\n");
	printStack(S);
	printf("\nCoda acquisita:\n");
	printQueue(Q);
	putchar('\n');
	modificaStrutture(Q,S);
	printf("\nStack modificato:\n");
	printStack(S);
	printf("\nCoda modificata:\n");
	printQueue(Q);
	putchar('\n');
}

// ESERCIZI SUGLI STACK

void moltiplicaric(Stack s, int m){
	int n = 0;
	
	if(!emptyStack(s)){
		n = pop(s);
		m *= n;
		moltiplicaric(s,m);
		push(s,m);
	}
}

void moltiplica(Stack S){
	
	Stack tmp;
	
    moltiplicaric(S,1);
    printStack(S);
    printf("\n");
}

void main181208(){
	Stack s = stackCreationMenu(0), tmp = initStack();
	
	printStack(s);
	
	reverseStack(s,tmp);
    moltiplicaric(tmp,1);
    printStack(tmp);
    printf("\n");
    
}

// ESERCIZI SULLE CODE

//Traccia 12-06-2019 

Queue eliminaPositivi(Queue Q){
	int value;
	if(!emptyQueue(Q))
     					{
     						value=dequeue(Q);
   				   	        Q=eliminaPositivi(Q);
   				    	        if(value<=0)
   				    		    enqueue(Q,value);
   				    	} 		
           return Q;
}

Queue eliminaNegativi(Queue Q){
	int value;	
	if(!emptyQueue(Q))
     					{
     				                    value=dequeue(Q);
   				    		    Q=eliminaNegativi(Q);
   				    	             if(value>0)
   				    		    enqueue(Q,value);
                                         }
	 return Q;
							
}

void eliminaPositiviNegativi(Queue Q){
 	int scelta;
 	
 	printf("ELIMINARE:\n[1]POSITIVI\n[2]NEGATIVI\n");
 	scanf("%d",&scelta);
 	
 	if(scelta==1)
 	Q=eliminaPositivi(Q);
 	if(scelta==2)
 	Q=eliminaNegativi(Q);
 	reverseQueue(Q);
 	
 }

// main N°1

void main10619(){
 	// Creo una coda
    Queue Q = queueCreationMenu(0);
    printQueue(Q);
    eliminaPositiviNegativi(Q);
    printf("\n\nCoda Q dopo le opportune modifiche:\n\n");
	printQueue(Q);
    emptyQueue(Q);
}

void sostitusciTripla_ric(Queue Q){
	int val1=0, val2=0, val3=0, err=0;

	if(!emptyQueue(Q)){
		val1=dequeue(Q);
		if(!emptyQueue(Q)){
			val2=dequeue(Q);
			if(!emptyQueue(Q)){
				val3=dequeue(Q);	
				sostitusciTripla_ric(Q);
				enqueue(Q,val1*val2*val3);
			}
			else{
				enqueue(Q,val2);
				enqueue(Q,val1);
			}
		}
	}
}

void sotituisciTripla(Queue Q){
    sostitusciTripla_ric(Q);
    reverseQueue(Q);
}

void main1090209(){
	Queue Q = queueCreationMenu(0);
    printQueue(Q);
    sotituisciTripla(Q);
    printf("\n\nCoda Q dopo le opportune modifiche:\n\n");
	printQueue(Q);
    emptyQueue(Q);
}

//traccia  10/2018

void Aux(Queue Q,int sommapari,int sommadispari){
    int ins=0;
    if(!emptyQueue(Q)){
        ins=dequeue(Q);
        if(ins%2==0) sommapari+=ins;
        else sommadispari+=ins;

        Aux(Q,sommapari,sommadispari);

        if(ins%2==0){ 
		enqueue(Q,sommapari);
		enqueue(Q,ins); 
		}else{ 
		enqueue(Q,sommadispari); 
		enqueue(Q,ins); 
		}
    }
}

void Somma(Queue Q){
    Aux(Q,0,0);
    reverseQueue(Q);
}

// main

void main11018(){
	Queue Q = queueCreationMenu(0);
    printQueue(Q);
    Somma(Q);
    printf("\n\nCoda Q dopo le opportune modifiche:\n\n");
	printQueue(Q);
    emptyQueue(Q);
}

void contaQueue(Queue q, int n){
	int tmp;
	if(!emptyQueue(q)){
		tmp = dequeue(q);
		contaQueue(q,n);
		enqueue(q,tmp);
		n++;
	}
}

void gioco1(Queue q1, Queue q2, int turno){
	int x,y;
	x=y=0;
	if(!emptyQueue(q1) && !emptyQueue(q2)){
		 x = dequeue(q1);
		 y = dequeue(q2);
		 gioco1(q1,q2,turno);
	}
	if(x != 0 && y != 0){
		if((x+y)%2 == 0) enqueue(q1,x);
		else enqueue(q2,y);
	} else {
		if(turno%2 == 0) enqueue(q2,y);
		else enqueue(q1,x);
	}
	turno++;
}

void gioco(Queue q1, Queue q2){
	int turno = 1,x = 0, y = 0;
	gioco1(q1,q2,turno);
	contaQueue(q1,x);
	contaQueue(q2,y);
	if(x > y) printf("Q1 vince!\n");
	else if(x < y) printf("Q2 vince!\n");
	else printf("Patta!\n");
}

void main130616(){
	Queue q1,q2;
	q1 = initQueue();
	q2 = initQueue();
	
	q1 = queueCreationMenu(0);
	q2 = queueCreationMenu(0);
	
	printQueue(q1);
	printQueue(q2);
	
	gioco(q1,q2);
	
	printf("\nCode modificate\n");
	printf("Q1: ");
	printQueue(q1);
	printf("\nQ2: ");
	printQueue(q2);
	printf("\n");
	
	free(q1);
	free(q2);
}

// ESERCIZI SULLE LISTE DOPPIE

// main esercizio interleaving

ListDopp elimina_neg(ListDopp l) {
    if (l) {
        l->next = elimina_neg(l->next);
        if (l->info < 0) {
            if (l->next)
                l->next->prev = l->prev;
            if (l->prev)
                l->prev->next = l->next;
            ListDopp tmp = l->next;
            free(l);
            l = tmp;
        }
    }
    return l;
}

void trova_elimina(ListDopp* l1, ListDopp* l2) {
    *l1 = elimina_neg(*l1);
    *l2 = elimina_neg(*l2);
}

ListDopp interleaving(ListDopp l1, ListDopp l2, ListDopp l3) {
    if (l1 != NULL && l2 != NULL) {
        l3 = inCoda(l3, l1->info);
        l3 = inCoda(l3, l2->info);
        return interleaving(l1->next, l2->next, l3);
    } else {
        return l3;
    }
}

void mainesameinterleaving() {
    ListDopp l1, l2;
    l1 = l2 = NULL;
    PopolaLista(&l1, &l2);
    trova_elimina(&l1, &l2);
    printf("\n\n\nStampa Lista L1 doppiamente concatenata finale:");
    printListD(l1);
    printf("\nStampa Lista L2 doppiamente concatenata finale:");
    printListD(l2);
    if (CalcolaDim(l1) == CalcolaDim(l2)) {
        ListDopp l3 = NULL;
        l3 = interleaving(l1, l2, l3);
        printf("\nStampa Lista L3 doppiamente concatenata dopo Interleaving:");
        printListD(l3);
        freeListD(l3);
    } else {
        printf("\nLa lista L1 e la lista L2 hanno dimensioni differenti, non e' possibile fare un Interleaving\n");
    }
    freeListD(l1);
    freeListD(l2);
}

// esercizio 2 della traccia di giugno 2019

ListDopp SpostaPositivi(ListDopp l1, ListDopp l2) {
    if (l1 == NULL) {
        return l2;
    }

    if (l1->info >= 0) {
        l2 = inTesta(l2, l1->info);
        l1 = l1->next;
    } else {
        l1 = l1->next;
    }

    return SpostaPositivi(l1, l2);
}

ListDopp SpostaNegativi(ListDopp l1, ListDopp l2) {
    if (l1 == NULL) {
        return l2;
    }

    if (l1->info < 0) {
        l2 = inTesta(l2, l1->info);
        l1 = l1->next;
    } else {
        l1 = l1->next;
    }

    return SpostaNegativi(l1, l2);
}

void main062019() {
    ListDopp l1 = NULL;
    ListDopp l2 = NULL;

    PopolaLista(&l1, &l2);

    SpostaPositivi(l1, l2);
    SpostaNegativi(l2, l1);

    printf("\n\nEcco la prima lista dopo le modifiche, L:");
    printListD(l1);

    printf("\n\nEcco la seconda lista dopo le modifiche, L:");
    printListD(l2);

    freeListD(l1);
    freeListD(l2);
}

int  sommaLista(ListDopp L){
  if (L == NULL)  return 0;
  else return L->info + sommaLista(L->next);
}

void togli_somma(ListDopp* L1, ListDopp* L2) {
    if (*L1 && *L2) {
        *L2 = EliminaDoppia(*L2, sommaLista(*L1));
        togli_somma(L1, L2);
    }
}

void main200606(){
	ListDopp l1, l2;
	
	l1=l2=NULL;

    PopolaLista(&l1, &l2);

    togli_somma(&l1, &l2);

    printf("\n\nEcco la prima lista dopo le modifiche, L:");
    printListD(l1);

    printf("\n\nEcco la seconda lista dopo le modifiche, L:");
    printListD(l2);

    freeListD(l1);
    freeListD(l2);
}

void elimina_ricorsiva(ListDopp top1, ListDopp top2) {
    if (top1 == NULL || top2 == NULL) {
        return;
    }

    if (top1->info == top2->info) {
        ListDopp *tmp = top1;
        if (top1->next != NULL) {
            top1->next->prev = tmp->prev;
        }
        if (top1->prev != NULL) {
            top1->prev->next = tmp->next;
        }
        free(tmp);
    }

    elimina_ricorsiva(top1->next, top2);
}

void main1181208(){
	ListDopp l1, l2;
	
	l1=l2=NULL;

    PopolaLista(&l1, &l2);
    
    elimina_ricorsiva(l1,l2);

    printf("\n\nEcco la prima lista dopo le modifiche, L:");
    printListD(l1);

    printf("\n\nEcco la seconda lista dopo le modifiche, L:");
    printListD(l2);

    freeListD(l1);
    freeListD(l2);
}

// ESERCIZI SUGLI ALBERI

// traccia 18/02/2017 esercizio 2

int drop_minimo (Tree* t){
	int min;
	if (*t) {
		if((*t)->sx==NULL){ 
			min=(*t)->info;
			Tree temp=(*t)->dx;
			free(*t);
			*t=temp;
		}
		else min= drop_minimo(&(*t)->sx);
	}
	return min;
}


Tree delete_node_abr (Tree t, int el){
	Tree temp;
	int min;
	if(t) {
		if(t->info > el){ /* el va cercato nel sottoalbero sinistro */
			t->sx= delete_node_abr(t->sx,el);
		}
		else if(t->info< el){ /* el va cercato nel sottoalb. destro */
			t->dx= delete_node_abr(t->dx,el);
		}
		else{ /* Trovato elemento da cancellare */
			if(!t->sx){ // se t->sx è NULL, entra anche quando sia t->sx che t->dx sono NULL
				temp=t->dx;
				free(t);
				return temp;
			}
			else if(!t->dx){ // se t->dx è NULL
				temp=t->sx;
				free(t);
				return temp;
			}
			if (t->dx && t->sx){ // trovo il minimo lo metto al posto di t->info, ed elimino il minimo
				min=drop_minimo(&(t)->dx); // funzione che restituisce il minimo ed elimina il nodo con info==minimo
				t->info=min;
			}
		}
		return t;
	} 
}

Tree delete_min_t1 (Tree t){
	if (t){
		if (t->sx && t-> dx){
			t->dx = delete_min_t1 (t->dx);
			t->sx = delete_min_t1 (t->sx);
			if(t->info < ( ((t->sx->info)+(t->dx->info)) /2 ) ){
				t=delete_node_abr(t,t->info);
			}
		}
		else if (t->sx && !(t->dx) ){
			t->sx = delete_min_t1 (t->sx);
			if(t->info <  ((t->sx->info) /2 ) ){
				t=delete_node_abr(t,t->info);
			}
		}
		else if (!(t->sx) && t->dx ){
			t->dx = delete_min_t1 (t->dx);
			if(t->info <  ((t->dx->info) /2 ) ){
				t=delete_node_abr(t,t->info);
			}
		}
	}
	return t;
}

Tree delete_max_t2 (Tree t){
	if (t){
		if (t->sx && t-> dx){
			t->dx = delete_max_t2(t->dx);
			t->sx = delete_max_t2(t->sx);
			if(t->info > ( ((t->sx->info)+(t->dx->info)) /2 ) ){
				t=delete_node_abr(t,t->info);
			}
		}
		else if (t->sx && !(t->dx) ){
			t->sx = delete_max_t2(t->sx);
			if(t->info > ((t->sx->info)/2 ) ){
				t=delete_node_abr(t,t->info);
			}
		}
		else if (!(t->sx) && t->dx ){
			t->dx = delete_max_t2(t->dx);
			if(t->info >  ((t->dx->info) /2 ) ){
				t=delete_node_abr(t,t->info);
			}
		}
	}
	return t;
}


void main180217(){
	printf ("CREAZIONE ABR T1:\n");Tree t1=treeCreationMenu(0); inOrderPrint(t1);
	printf ("CREAZIONE ABR T2:\n");Tree t2=treeCreationMenu(0); inOrderPrint(t2);
	
	t1=delete_node_abr (t1,1);
	t2=delete_node_abr (t2,2);
	
	printf ("\n\nABR T1 DOPO MODIFICHE:\n");inOrderPrint(t1);
	printf ("\n\nABR T2 DOPO MODIFICHE:\n");inOrderPrint(t2);
	
	freeTree(t1);
	freeTree(t2);
	
}

//Traccia 15 giugno 2015 esercizio 2

Tree UnionTree(Tree t1, Tree t2,Tree t3){
	
	if(t1!=NULL && t2!=NULL){
		t3 = insertNodeTree(t3, t1->info+t2->info);
		t3->sx = UnionTree(t1->sx,t2->sx,t3->sx);
		t3->dx = UnionTree(t1->dx,t2->dx,t3->dx);
	}
	
	return t3;
}

tern iniNode(int info){
	tern T = malloc(sizeof(struct Pree));
	
    T->info = info;
    T->sx = NULL;
    T->dx = NULL;
    T->cx = NULL;
    
    return T;
}

void freePree(tern T) {
	if(T) {
		freePree(T->sx);
		freePree(T->cx);
		freePree(T->dx);
		free(T);
	}
}

tern trasformazione(Tree T, tern p){
	
	p = iniNode(T->info);
	if(T->sx != NULL && T->dx != NULL){
		
		p->cx = iniNode((T->sx->info + T->dx->info)/2);
		p->dx = trasformazione(T->dx, p->dx);
		p->sx = trasformazione(T->sx, p->sx);
		return p;
		
	} else {
		
		if(T->sx != NULL){
			p->cx = iniNode(T->sx->info/2);
		    p->sx = trasformazione(T->sx, p->sx);
		    return p;
		    
		} else {
			if(T->sx != NULL){
				
				p->cx=iniNode((T->dx->info)/2);
                p->dx=trasformazione(T->dx,p->dx);
                return p;
                
			} else return p;
		} 
	}
}

void print_Ternario(tern T, int indent) {
  if (T) {
    int i;
    for (i = 0; i < indent; i++)
      printf("    ");
    printf("-(%d)\n", T->info);
    print_Ternario(T->sx, indent + 1);
    print_Ternario(T->cx, indent + 1);
    print_Ternario(T->dx, indent + 1);
  }
}

void main215615() {
  printf("CREAZIONE ABR T1:\n");
  Tree t1 = treeCreationMenu(0);
  inOrderPrint(t1);

  printf("CREAZIONE ABR T2:\n");
  Tree t2 = treeCreationMenu(0);
  inOrderPrint(t2);

  Tree t3 = UnionTree(t1, t2, t3);
  inOrderPrint(t3);

  tern p = NULL;
  p = trasformazione(t3, p);

  print_Ternario(p, 0);

  freeTree(t1);
  freeTree(t2);
  freeTree(t3);
  freePree(p);
}

int check_ABR(Tree T, int min, int max){
  int res = 1;
  if(T){
    if( ! (T->info >= min && T->info <= max && check_ABR(T->sx, min, T->info ) && check_ABR(T->dx, T->info, max )) )
      res = 0;
  }
  return res;
}

tern creaTernario(Tree T){
  tern TER = NULL;
  if(T){
    TER = (tern)calloc(1,sizeof(tern));
    TER->info = T->info;
    TER->sx = creaTernario(T->sx);
    TER->dx = creaTernario(T->dx);
    if(TER->sx && TER->dx){
      TER->cx = (tern)malloc(sizeof(tern));
      TER->cx->info = (TER->sx->info + TER->dx->info)/2;
      TER->cx->sx = TER->cx->dx = TER->cx->cx = NULL;
    }
  }
  return TER;
}

void print_ABR(Tree T, int indent){
  if(T){
    int i = 1;
    for( ; i <= indent ; i++)
      printf("    ");
    printf("-(%d)\n",T->info);
    print_ABR(T->sx, indent + 1);
    print_ABR(T->dx, indent + 1);
  }
}

void main210612(){
	srand((unsigned int)time(NULL));

  tern TER = NULL;
  Tree T = randomTree(10);

  print_ABR(T,0);
  if( check_ABR(T,INT_MIN, INT_MAX) )
    printf("T e' un ABR\n");
  else
    printf("T non e' un ABR\n");

  TER = creaTernario(T);
  print_Ternario(TER, 0);
}

// ESERCIZI SUI GRAFI 
 
//main N°3

Graph pesiPari(Graph G, Graph H){
    Graph T=initGraph(G->nodes_count);
    int i=0,peso=0;
    for(i=0;i<G->nodes_count;i++)
    {
        List tmp=G->adj[i];
        List tmp2=H->adj[i];
        while(tmp!=NULL)
        {
            while(tmp2!=NULL)
            {
                peso=tmp->peso+tmp2->peso;
                if((tmp->target==tmp2->target) && ((peso>10)&&(peso%2==0)))
                    addEdge(T,i,tmp->target,peso);

                tmp2=tmp2->next;
            }
            tmp=tmp->next;
        }
    }

    return T;
}
 
void main30619(){
	srand((unsigned int)time(NULL));

    Graph G = graphCreationMenu(0);
    printf("\nPrimo grafo: \n");
    printGraph(G);

    Graph H = graphCreationMenu(0);
    printf("\nSecondo grafo: \n");
    printGraph(H);

    if(G->nodes_count!=H->nodes_count)
    {
        printf("\nNumero nodi diversi\n");
    }
    else
    {
        Graph T=pesiPari(G,H);
        printf("\nGrafo finale:\n");
        printGraph(T);
        free(T);
    }
    freeGraph(H);
    freeGraph(G);
}

//traccia gennaio 2020

// N°1

int isUnion(Graph G3, Graph G1, Graph G2){
    int i = 0;
    int j = 0;
    int ret = 1;

    int n = G1->nodes_count;
    int * isIn = (int *)calloc(n,sizeof(int));
    int * pesi = (int *)calloc(n, sizeof(int));
    List curr = NULL;

    for(; i<n; i++){
        for(curr = G3->adj[i]; curr != NULL; curr = curr->next){
            isIn[curr->target]++;
            pesi[curr->target] = curr->peso;
        }
        for(curr = G1->adj[i]; curr != NULL; curr = curr->next){
            isIn[curr->target]--;
            pesi[curr->target] -= curr->peso;
        }
        for(curr = G2->adj[i]; curr != NULL; curr = curr->next){
            isIn[curr->target]--;
            pesi[curr->target] -= curr->peso;
        }

        for(j=0; j<n; j++){
            // se G3 contiene un nodo non contenuto in G1 o G2 (o entrambi) il valore presente in isIn sarà 1
            // se la somma dei pesi di G1 e G2 è maggiore o minore di quello di G3 avremo un valore != 0
            if((isIn[j]>0) || (pesi[j] != 0)){
                ret = 0;
                break;
            }else{
                // azzero per il prossimo ciclo esterno
                isIn[j] = 0;
                pesi[j] = 0;
            }
        }
        // se sono uscito dal for precedente con ret = 0 posso anche non controllare gli altri vertici
        if(ret == 0)
            break;
    }
    return ret;
}

void menu30120(){
	Graph G1 = graphCreationMenu(0);
    printf("\nPrimo grafo: \n");
    printGraph(G1);

    Graph G2 = graphCreationMenu(0);
    printf("\nSecondo grafo: \n");
    printGraph(G2);
    
    Graph G3 = graphCreationMenu(0);
    printf("\nTerzo grafo: \n");
    printGraph(G3);
    
    isUnion(G3,G1,G2);
    
    freeGraph(G1);
    freeGraph(G2);
    freeGraph(G3);
}

Graph esercizio(Graph G, Graph H){
	Graph T = initGraph(H->nodes_count);
	List g,h;
	int i,j,peso,flag;
	int *aux1 = (int*)calloc(H->nodes_count,sizeof(int));
	int *aux2 = (int*)calloc(H->nodes_count,sizeof(int));
	for(i = 0; i < H->nodes_count; ++i){
		flag = 0;
		if(i < G->nodes_count){
			//g = G->adj;
			flag = 1;
		}
		//h = H->adj;
		while(h){
			aux1[h->target] = h->peso;
			aux2[h->target] = 1;
			h = h->next;
		}
		while(g && flag){
			if(aux2[g->target] == 1){
				printf("aux: %d\n", aux2[g->target]);
				peso = aux1[g->target] > g->peso ? aux1[g->target] : g->peso;
				addEdge(T,i,g->target,peso);
				aux2[g->target] = 0;
			}
			else
				addEdge(T,i,g->target,g->peso);
			g = g->next;
		}
		for(j = 0; j < H->nodes_count; ++j)
			if(aux2[j] == 1){
				addEdge(T,i,j,aux1[j]);
				aux2[j] = 0;
		}
	}
	return T;
}

void main4130616() {
	Graph G = graphCreationMenu(0);
    printf("\nPrimo grafo: \n");
    printGraph(G);

    Graph H = graphCreationMenu(0);
    printf("\nSecondo grafo: \n");
    printGraph(H);
    
	//inserimento T
	Graph T = esercizio(G,H);
	printf("T:\n");
	printGraph(T);
	freeGraph(G);
	freeGraph(H);
	freeGraph(T);
}

void sottrazione (Graph G, Graph H){
            int i=0;
            for(i;i<(G->nodes_count);i++){
                List uno=G->adj[i];
                List due=H->adj[i];
                int  *arr=(int*)calloc(G->nodes_count,(sizeof(int)));
                while(uno!=NULL){
                    arr[uno->target]=uno->peso;
                    uno=uno->next;

                }
                while(due!=NULL){
                    if(arr[due->target]>0){
                        if((arr[due->target]-(due->peso))>0) {
                                removeEdge(G,i,due->target);
                                addEdge(G,i,due->target,(arr[due->target]-(due->peso)));
                        }
                        else removeEdge(G,i,due->target);

                        }
                    due=due->next;
                    }
                }

            }

void main300106(){
	Graph G = graphCreationMenu(0);
  printf("\nPrimo grafo: \n");
  printGraph(G);

  Graph H = graphCreationMenu(0);
  printf("\nSecondo grafo: \n");
  printGraph(H);
  
  sottrazione(G,H);
  
  printf("\nPrimo grafo modificato: \n");
  printGraph(G);
  
  printf("\nSecondo grafo modificato: \n");
  printGraph(H);
  
  freeGraph(G);
  freeGraph(H);
}
