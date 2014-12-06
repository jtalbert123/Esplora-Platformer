#include <windows.h>
#include <stdio.h>

void addPtr(void*** list, int oldLength, void* ptr) {
	if (*list == NULL) {
		printf("\t\tThe List was not initialized.\n");
		*list = malloc(sizeof(void*));
		printf("\t\tInitialized the list.\n");
		(*list)[0] = ptr;
		printf("\t\tAdded the item.\n");
		return;
	} else if (oldLength == 0) {
		printf("\t\tClearing list.\n");
		//free(*list);
		*list = malloc(sizeof(void*));
		printf("\t\tRe-initialized the list.\n");
		(*list)[0] = ptr;
		printf("\t\tAdded the item.\n");
		return;
	} else {
		printf("\t\tCreating a new list and coping items.\n");
		void** new = malloc(sizeof(void*)*oldLength+1);
		printf("\t\tList created.\n");
		for (oldLength = oldLength - 1; oldLength >= 0; oldLength--) {
			new[oldLength] = (*list)[oldLength];
		}
		printf("\t\tItems copied.\n");
		new[oldLength] = ptr;
		//free(*list);
		printf("\t\tfree()'d the old list.\n");
		*list = new;
		printf("\t\tMoved the new list to the location of the old list.\n");
		return;
	}
}
/*
double min(double num1, double num2) {
	if (num1 < num2)
		return num1;
	return num2;
}

double max(double num1, double num2) {
	if (num1 < num2)
		return num2;
	return num1;
}

double abs(double num) {
	if (num < 0)
		return -num;
	return num;
}
*/