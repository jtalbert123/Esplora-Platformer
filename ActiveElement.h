#pragma once

typedef struct ActiveElement_Struct ActiveElement;

#include "Level.h"
#include "LevelElement.h"

struct ActiveElement_Struct {
	//reference these items by using ((LevelElement*)ActiveElementPointer)->member_variable
	LevelElement basicData;
	
	int type;
	
	//must have a cast where ever it is used (I recommend using it to point to a struct).
	void* properties;
	//returns 0 if the LevelElement/level was updated successfully,
	// another number if there was a non-fatal error.
	int (*update)(ActiveElement* this, Level* level);
	
	void (*apply)(ActiveElement* this);
};

double SPEED;

void setSpeed(double speed);