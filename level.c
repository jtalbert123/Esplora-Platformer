#include <stdio.h>
#include <windows.h>
#include <ncurses.h>

//This file provides utilities to read and deal with level files.
//A level file is a file that defines the level to be played,
// there may also be a random mode, not included in this file.

typedef struct LevelElement LevelElement;

struct LevelElement {
	char representation;
	int type;
	//array of pointers, used to hold data used by the update function.
	//May be NULL, especially when the platform is a standard one.
	void** properties;
	//returns 0 if the LevelElement/level was updated successfully,
	// another number if there was a non-fatal error.
	int (*update)(LevelElement* this, LevelElement*** level);
};

//Reads the given file and returns an array holding pointers to the
// LevelElement items that the file specifies
void*** getLevel(char* fileName);

//Adds the given pointer to the end of the pointer array, resizing as necessary.
//Takes in the address of the list to be resized, the array's length before the
// add operation, and the pointer to be added.
void addPtr(void*** list, int oldLength, void* ptr);

//Gets the level element specified by a given character, NULL if unrecognised.
LevelElement* getLevelElement(char);

//Place holder update method, does nothing
int emptyUpdate(LevelElement* this, LevelElement*** level);

int main() {
	printf("%d", time(NULL));
	return 0;
}
/**/
void*** getLevel(char* fileName) {
	FILE* file = fopen(fileName, "r");
	if (file == NULL)
		return NULL;
	//else is implied
	//so file != NULL, it is readable
	LevelElement*** array = NULL;
	// Keeps track of the current row and column that is being read/set.
	char c;
	int currentRow, currentColumn = 0;
	int rowArraySize;
	LevelElement** rowArray;
	do {
		//Reset the row array reference for use with addPtr.
		rowArray = NULL;
		currentColumn = 0;
		do {
			//read the current character into c.
			//it will be necessary to implement a method to resize and add a pointer to an array.
			fscanf(file, "%c", &c);
			//addPtr(&rowArray, currentColumn, getLevelElement(c));
			currentColumn++;
		} while (c != '\n' && c != EOF);
		currentRow++;
	} while (c != EOF);
}

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
			int i;
			for (i = oldLength - 1; i >= 0; i--) {
				new[i] = (*list)[i];
			}
			new[oldLength] = ptr;
			free(*list);
			*list = new;
		}
		return;
	}
}

LevelElement* getLevelElement(char specifier) {
	LevelElement* element = malloc(sizeof(LevelElement));
	if (specifier == '-') {
		//make a standard platform
		element->representation = '-';
		element->type = 0;
		element->properties = NULL;
		element->update = emptyUpdate;
	//Add other types
	} else {
		free(element);
		return NULL;
	}
	return element;
}

int emptyUpdate(LevelElement* this, LevelElement*** level) {
	return 0;
}