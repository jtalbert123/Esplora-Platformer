#include <windows.h>

void addPtr(void*** list, int oldLength, void* ptr) {
	if (*list == NULL) {
		*list = malloc(sizeof(void*));
		**list = ptr;
		return;
	} else if (oldLength == 0) {
		free(*list);
		*list = malloc(sizeof(void*));
		**list = ptr;
		return;
	} else {
		void* backup = *list;
		*list = realloc(*list, oldLength+1);
		if (list != NULL)
			(*list)[oldLength] = ptr;
		else {
			*list = backup;
			void** new = malloc(sizeof(void*)*oldLength+1);
			for (oldLength = oldLength - 1; oldLength >= 0; oldLength--) {
				new[oldLength] = (*list)[oldLength];
			}
			new[oldLength] = ptr;
			free(*list);
			*list = new;
		}
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