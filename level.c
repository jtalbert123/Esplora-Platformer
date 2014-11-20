#include <stdio.h>
#include <windows.h>
#include <ncurses/ncurses.h>

//This file provides utilities to read and deal with level files.
//A level file is a file that defines the level to be played,
// there may also be a random mode, not included in this file.

typedef struct LevelElement LevelElement;
typedef struct Level Level;

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

struct Level {
	int width, height;
	LevelElement*** array;
};

//Reads the given file and returns an array holding pointers to the
// LevelElement items that the file specifies
Level* getLevel(char* fileName);

//Adds the given pointer to the end of the pointer array, resizing as necessary.
//Takes in the address of the list to be resized, the array's length before the
// add operation, and the pointer to be added.
void addPtr(void*** list, int oldLength, void* ptr);

//Gets the level element specified by a given character, NULL if unrecognised.
LevelElement* getLevelElement(char);

//Place holder update method, does nothing
int emptyUpdate(LevelElement* this, LevelElement*** level);

int main() {
	//initscr();
	//refresh();
	//printf("About to read file.\n");
	Level* level = getLevel("/cygdrive/c/Users/james/Desktop/cpre185/Platformer/Esplora-Platformer/level.txt");
	if (level == NULL) {
		printf("getLevel returned NULL.\n");
		return 0;
	}
	//printf("Read file.\n");
	int row;
	int column;
	for (row = 0; row < level->height; row++) {
		for (column = 0; column < level->width; column++) {
			if (((level->array)[row][column]) != NULL)
				printf("%c", ((level->array)[row][column])->representation);
			else
				printf(" ");
			//mvaddch(row, column, ((level->array)[row][column])->representation);
		}
		printf("\n");
	}
	//mvaddch(y,x,char);
	//endwin();
	return 0;
}
/**/
Level* getLevel(char* fileName) {
	//printf("In getLevel.\n");
	FILE* file = fopen(fileName, "r");
	if (file == NULL)
		return NULL;
	//printf("Opened the file.\n");
	//else is implied
	//so file != NULL, it is readable
	LevelElement*** array = NULL;
	// Keeps track of the current row and column that is being read/set.
	char c;
	int currentRow = 0, currentColumn = 0, columnCount = 0;
	int rowArraySize;
	LevelElement** rowArray;
	do {
		//printf("In outer do-while loop.\n");
		//Reset the row array reference for use with addPtr.
		rowArray = NULL;
		currentColumn = 0;
		do {
			//read the current character into c.
			//it will be necessary to implement a method to resize and add a pointer to an array.
			if (feof(file)) {
				break;
			}
			c = getc(file);
			//printf("In inner do-while loop.\n\tc = %d.\n", c);
			if (c > 31) {
				addPtr(&rowArray, currentColumn, getLevelElement(c));
				currentColumn++;
				//printf("Added %c to the row.\n", c);
			}
		} while (c != '\n' && !feof(file));
		//printf("\t\t\tcurrentColumn = %d.\n", currentColumn);
		if (columnCount == 0)
			columnCount = currentColumn;
		else if (currentColumn != columnCount) {
			printf("The rows are not the same length, aborting.\n");
			free(rowArray);
			return NULL;
		}
		addPtr(&array, currentRow, rowArray);
		//printf("Added the row to the level.\n");
		currentRow++;
	} while (!feof(file));
	Level* level = malloc(sizeof(Level));
	level->width = columnCount;
	level->height = currentRow;
	level->array = array;
	return level;
}

void addPtr(void*** list, int oldLength, void* ptr) {
	//printf("In addPtr.\n");
	if (*list == NULL) {
		//printf("The given list was NULL.\n");
		*list = malloc(sizeof(void*));
		**list = ptr;
		return;
	} else if (oldLength == 0) {
		//printf("Resetting list to size 0, then adding.\n");
		free(*list);
		*list = malloc(sizeof(void*));
		**list = ptr;
		return;
	} else {
		//printf("Resizing list.\n");
			void** new = malloc(sizeof(void*)*oldLength+1);
			int i;
			for (i = oldLength - 1; i >= 0; i--) {
				new[i] = (*list)[i];
			}
			new[oldLength] = ptr;
			free(*list);
			*list = new;
		return;
	}
}

LevelElement* getLevelElement(char specifier) {
	//printf("Processing character %c.\n", specifier);
	LevelElement* element = malloc(sizeof(LevelElement));
	if (specifier == '-') {
		//make a standard platform
		element->representation = '-';
		element->type = 0;
		element->properties = NULL;
		element->update = emptyUpdate;
		//printf("Platform processed.\n");
		return element;
	//Add other types
	} else {
		//printf("Returning NULL from getLevelElement(char)\n");
		free(element);
		return NULL;
	}
	return element;
}

int emptyUpdate(LevelElement* this, LevelElement*** level) {
	return 0;
}