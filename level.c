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
	//should be NULL or { x, y, ...}
	void** properties;
	//returns 0 if the LevelElement/level was updated successfully,
	// another number if there was a non-fatal error.
	int (*update)(LevelElement* this, Level* level);
	
	void (*apply)(LevelElement* this, Level* level);
};

struct Level {
	int width, height;
	LevelElement*** array;
	int numActive;
	LevelElement** activeElements;
};

//Reads the given file and returns an array holding pointers to the
// LevelElement items that the file specifies
void* getLevel(char* fileName);

//Adds the given pointer to the end of the pointer array, resizing as necessary.
//Takes in the address of the list to be resized, the array's length before the
// add operation, and the pointer to be added.
void addPtr(void*** list, int oldLength, void* ptr);

//Gets the level element specified by a given character, NULL if unrecognised.
LevelElement* getLevelElement(char, int, int);

//Place holder update method, does nothing
int emptyUpdate(LevelElement* this, Level* level);

//Updates the position and direction of a moving platform.
int updateMovingPlatform(LevelElement* this, Level* level);

//Updates all active elements in the entire given level structure.
void updateAllActive(void*);

//Removes all active items from level->array. Used by updateAllActive
// to avoid conflicts and accidental NULLing of items that should not be NULL.
void applyUpdates(Level* level);

//Draws the given Level, given that initscr() has been called.
void draw(void*);

//Applies the updates to a movingPlatform
void applyMovingPlatform(LevelElement* this, Level* level);

int main() {
	initscr();
	refresh();
	//printf("About to read file.\n");
	Level* level = getLevel("/cygdrive/c/Users/james/Desktop/cpre185/Platformer/Esplora-Platformer/level.txt");
	if (level == NULL) {
		printf("getLevel returned NULL.\n");
		return 0;
	}
	//printf("Read file.\n");
	LevelElement* vertical;
	int i = 0;
	do {
		vertical = (level->activeElements)[i++];
	} while (vertical->type != 2);
	int oldTime = time(NULL);
	while (1) {
		if (time(NULL) > oldTime) {
			mvprintw(40, 0, "%d", time(NULL));
			draw(level);
			refresh();
			updateAllActive(level);
			//updateAllActive(level);
			//updateAllActive(level);
			oldTime = time(NULL);
		}
	}
	//mvaddch(y,x,char);
	endwin();
	return 0;
}

void updateAllActive(void* levelPtr) {
	//clear out the locations with the active items.
	Level* level = (Level*) levelPtr;
	int i;
	for (i = 0; i < level->numActive; i++) {
		LevelElement* this = (level->activeElements)[i];
		if (this != NULL) {
			this->update(this, level);
		}
	}
	applyUpdates(level);
}

void applyUpdates(Level* level) {
	int i;
	for (i = 0; i < level->numActive; i++) {
		LevelElement* this = (level->activeElements)[i];
		if (this != NULL) {
			this->apply(this, level);
		}
	}
}

void draw(void* levelPtr) {
	Level* level = (Level*) levelPtr;
	int row, column;
	for (row = 0; row < level->height; row++) {
		for (column = 0; column < level->width; column++) {
			LevelElement* this = (level->array)[row][column];
			if (this != NULL) {
				mvaddch(row, column, this->representation);
			} else
				mvaddch(row, column, ' ');
		}
	}
}

void* getLevel(char* fileName) {
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
	LevelElement** active = NULL;
	int numActive = 0;
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
				LevelElement* current = getLevelElement(c, currentColumn, currentRow);
				addPtr(((void***)&rowArray), currentColumn, ((void*)current));
				if (current != NULL && (current->type == 1
						|| current->type == 2))
					addPtr(((void***)&active), numActive++, ((void*)current));
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
		addPtr(((void***)&array), currentRow, ((void*)rowArray));
		//printf("Added the row to the level.\n");
		currentRow++;
	} while (!feof(file));
	Level* level = malloc(sizeof(Level));
	level->width = columnCount;
	level->height = currentRow;
	level->array = array;
	level->numActive = numActive;
	level->activeElements = active;
	return (void*)level;
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

LevelElement* getLevelElement(char specifier, int x, int y) {
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
	}
	if (specifier == 'H') {
		element->representation = '-';
		element->type = 1;
		int** prop = malloc(sizeof(int*)*6);
			int *tx, *ty, *dir, *newX, *newY, *newDir;
			tx = malloc(sizeof(int));
			ty = malloc(sizeof(int));
			dir = malloc(sizeof(int));
			
			newX = malloc(sizeof(int));
			newY = malloc(sizeof(int));
			newDir = malloc(sizeof(int));
			
			*tx = x;
			*ty = y;
			*dir = 1;
			prop[0] = tx;
			prop[1] = ty;
			//direction
			prop[2] = dir;
			
			prop[3] = newX;
			prop[4] = newY;
			prop[5] = newDir;
		element->properties = (void**)prop;
		element->update = updateMovingPlatform;
		element->apply = applyMovingPlatform;
		return element;
	}
	if (specifier == 'V') {
		element->representation = '-';
		element->type = 2;
		mvprintw(50, 41, "Type is %d.", element->type);
		int** prop = malloc(sizeof(int*)*6);
			int *tx, *ty, *dir, *newX, *newY, *newDir;
			tx = malloc(sizeof(int));
			ty = malloc(sizeof(int));
			dir = malloc(sizeof(int));
			
			newX = malloc(sizeof(int));
			newY = malloc(sizeof(int));
			newDir = malloc(sizeof(int));
			
			*tx = x;
			*ty = y;
			*dir = 1;
			prop[0] = tx;
			prop[1] = ty;
			//direction
			prop[2] = dir;
			
			prop[3] = newX;
			prop[4] = newY;
			prop[5] = newDir;
		element->properties = (void**)prop;
		element->update = updateMovingPlatform;
		element->apply = applyMovingPlatform;
		return element;
	}
	//printf("Returning NULL from getLevelElement(char)\n");
	free(element);
	return NULL;
}

int emptyUpdate(LevelElement* this, Level* level) {
	return 0;
}

int updateMovingPlatform(LevelElement* this, Level* level) {
	int x = *(int*)(this->properties)[0];
	int y = *(int*)(this->properties)[1];
	int dir = *(int*)(this->properties)[2];
	int* newX = (int*)(this->properties)[3];
	int* newY = (int*)(this->properties)[4];
	int* newDir = (int*)(this->properties)[5];
	if (this->type == 1) {
		//Horizontal platforms
		if (dir == 1) {
			//Right
			if (x < level->width-1) {
				LevelElement* right = (level->array)[y][x+1];
				if (right == NULL || (right->type == 1 && *((int*)(right->properties)[2]) == dir)) {
					if ((level->array)[y][x] == this)
						(level->array)[y][x] = NULL;
					(level->array)[y][x+1] = this;
					(*newX)++;
				} else {
					//(level->array)[y][x] = this;
					*newDir = -1;
					return 1;
				}
			} else {
				//(level->array)[y][x] = this;
				*newDir = -1;
				return 1;
			}
		} else if (dir == -1) {
			//Left
			if (x > 0) {
				LevelElement* left = (level->array)[y][x-1];
				if (left == NULL) {
					if ((level->array)[y][x] == this)
						(level->array)[y][x] = NULL;
					(level->array)[y][x-1] = this;
					(*newX)--;
				}
			} else {
				//(level->array)[y][x] = this;
				*newDir = 1;
				return 1;
			}
		}
	} else if (this->type == 2) {
		//Vertical platforms.
		if (dir == 1) {
			//Up
			if (y > 0) {
				LevelElement* top = (level->array)[y-1][x];
				if (top == NULL) {
					if ((level->array)[y][x] == this)
						(level->array)[y][x] = NULL;
					(level->array)[y-1][x] = this;
					(*newY)--;
				} else {
					//(level->array)[y][x] = this;
					*newDir = -1;
					return 1;
				}
			} else {
				//(level->array)[y][x] = this;
				*newDir = -1;
				return 1;
			}
		} else if (dir == -1) {
			//Down
			if (y < level->height-1) {
				LevelElement* bottom = (level->array)[y+1][x];
				if (bottom == NULL) {
					if ((level->array)[y][x] == this)
						(level->array)[y][x] = NULL;
					(level->array)[y+1][x] = this;
					(*newY)++;
				} else {
				//(level->array)[y][x] = this;
				*newDir = 1;
				return 1;
			}
			} else {
				(level->array)[y][x] = this;
				*newDir = 1;
				return 1;
			}
		}
	}
	return 0;
}

void applyMovingPlatform(LevelElement* this, Level* level) {
	int** prop = (int**)this->properties;
	*(prop[0]) = *(prop[3]);
	*(prop[1]) = *(prop[4]);
	*(prop[2]) = *(prop[5]);
	(level->array)[*(prop[1])][*(prop[0])] == this;
}