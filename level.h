#include "Drawable.h"

//Header guard, prevents the header from being processed more than once
#ifndef LEVEL_HEADER
#define LEVEL_HEADER 1

typedef struct Level_Struct {
	double width, height;
	int num_items;
	Drawable** items;
} Level;

//Don't worry about what the void*'s are pointing to, that is handled in level.c
//Constructs the level from the specified file name.
Level* getLevel(char* fileName);

//Updates all elements in the entire given level structure.
void updateLevel(Level* level);

#endif