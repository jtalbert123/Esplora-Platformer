#include "Level.h"

#ifndef ACTIVE_ELEMENT_HEADER
#define ACTIVE_ELEMENT_HEADER 1

typedef struct ActiveElement_Struct ActiveElement;
struct ActiveElement_Struct {
	char representation;
	double x;
	double y;
	//does it interact with other objects.
	int tangible;
	int is_active;
	
	int type;
	
	double* double_properties;
	int* int_properties;
	//returns 0 if the LevelElement/level was updated successfully,
	// another number if there was a non-fatal error.
	int (*update)(ActiveElement* this, Level* level);
	
	void (*apply)(ActiveElement* this);
};

double SPEED;

void setSpeed(double speed) {
	SPEED = speed;
}

Drawable* getLevelElement(char specifier, int x, int y);

#endif