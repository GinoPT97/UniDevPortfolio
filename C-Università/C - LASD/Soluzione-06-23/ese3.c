#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>
#include <limits.h>

//liste di adiacenza
struct el
{
	int key;
	int peso;
	struct el *next;
};
typedef struct el edge;

//struttura grafo
struct elm
{
	int nv;
	edge **adj;
};
typedef struct elm grafo;

int is_empty(grafo *g)
{
	return g==NULL;
}

grafo *new_grafo(int n)
{
	grafo *tmp;
	int i;

	tmp=(grafo *)malloc(sizeof(grafo));
	if(tmp)
	{
		tmp->adj=(edge **)malloc(n*sizeof(edge *));
		if(tmp->adj==NULL)
		{
			printf("Errore, impossibile allocare memoria \n");
			free(tmp);
			tmp=NULL;
		}
		else
		{
			tmp->nv=n;
			for(i=0;i<tmp->nv;i++)
			{
				tmp->adj[i]=NULL;
			}
		}
	}
	else
	{
		printf("Errore, impossibile allocare memoria\n");
	}

	return tmp;
}

void stampa_grafo(grafo *g)
{
	int i;
	int ne=0;
	edge *e;

	if(!(is_empty(g)))
	{
		printf("\nIl grafo ha %d vertici\n", g->nv);
		for(i=0;i<g->nv;i++)
		{
			printf("Nodi adiacenti al nodo %d ->", i);
			e=g->adj[i];
			while(e!=NULL)
			{
				printf("%d (peso =%d) - ", e->key, e->peso);
				ne=ne+1;
				e=e->next;
			}
			printf("\n");
		}
		printf("Il grado ha %d archi", ne);
	}
}

void g_add(grafo *g, int u, int v, int peso)
{
	edge *new, *e;

	new=(edge *)malloc(sizeof(edge));

	if(g!=NULL)
	{
		if(new==NULL)
		{
			printf("Errore impossibile allocare memoria\n");
		}
		else
		{
			new->key=v;
			new->peso=peso;
			new->next=NULL;

			if(g->adj[u]==NULL)
			{
				g->adj[u]=new;
			}
			else
			{
				e=g->adj[u];
				if(e->key==v)
				{
					printf("Errore, larco esiste gia");
					return;
				}
				while(e->next!=NULL)
				{
					e=e->next;
				}
				e->next=new;
			}
		}
	}
}

edge *arco_peso_minimo(grafo *g)
{
	if(g->nv==0 || g->adj==NULL)
	{
		return NULL;
	}

	edge *min_arco=NULL;

	int min_peso=INT_MAX;

	for(int i=0; i<g->nv; i++)
	{
		edge *arco=g->adj[i];
		while(arco!=NULL)
		{
			if(arco->peso < min_peso)
			{
				min_arco=arco;
				min_peso=arco->peso;
			}
			arco=arco->next;
		}
	}

	return min_arco;
}

grafo *g_trasposto(grafo *g)
{
	grafo *trasposto=new_grafo(g->nv);

	for(int i=0; i< g->nv; i++)
	{
		edge *nodo_corrente=g->adj[i];

		while(nodo_corrente!=NULL)
		{
			g_add(trasposto,nodo_corrente->key,i,nodo_corrente->peso);
			nodo_corrente=nodo_corrente->next;
		}
	}

	return trasposto;
}

int main()
{
	grafo *g;
	edge *e_min;

	g=new_grafo(6);

	g_add(g,0,1,2);
	g_add(g,0,2,6);

	g_add(g,1,3,7);

	g_add(g,2,3,5);
	g_add(g,2,4,8);

	g_add(g,3,3,5);
	g_add(g,3,5,9);

	g_add(g,4,5,12);

	stampa_grafo(g);

	printf("\n\n");

	e_min=arco_peso_minimo(g);

	if(e_min!=NULL)
	{
		printf("Larco con peso minore : %d (peso : %d)\n", e_min->key, e_min->peso);
	}
	else
	{
		printf("Nessun arco nel grafo\n");
	}

	printf("\n\n");

	grafo *g_tr=g_trasposto(g);

	stampa_grafo(g_tr);

	edge *e_mintr=arco_peso_minimo(g_tr);

	if(e_mintr!=NULL)
        {
                printf("Larco con peso minore : %d (peso : %d)\n", e_mintr->key, e_mintr->peso);
        }
        else
        {
                printf("Nessun arco nel grafo\n");
        }

	printf("\n\n");

	return 0;
}

