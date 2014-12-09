#pragma once

typedef struct LevelElement_Struct {

	char representation;
	double x;
	double y;
	//does it interact with other objects.
	int tangible;
	//true if this is really a ActiveElement.
	int is_active;
	
} LevelElement;

LevelElement* getLevelElement(char specifier, int x, int y);