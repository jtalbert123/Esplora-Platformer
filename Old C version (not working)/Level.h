#pragma once

typedef struct Level_Struct Level;

#include "ActiveElement.h"
#include "LevelElement.h"

struct Level_Struct {
	double width, height;
	int num_items;
	LevelElement** items;
};

//Don't worry about what the void*'s are pointing to, that is handled in level.c
//Constructs the level from the specified file name.
Level* getLevel(char* fileName);

//Updates all elements in the entire given level structure.
void updateLevel(Level* level);