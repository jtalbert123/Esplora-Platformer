#include "LevelElement.h"
#include <stdio.h>
#include <windows.h>
#include "ActiveElement.h"

void makePlatform(LevelElement*, char rep, double x, double y, int tangible, int active);

LevelElement* getLevelElement(char specifier, int x, int y) {
	if (specifier == '.') {
		return NULL;
	}
	printf("Processing '%c'(%d).\n", specifier, specifier);
	if (specifier == '-') {
		printf("\tThe character specified a stable platform.\n");
		LevelElement* platform = malloc(sizeof(LevelElement));
		makePlatform(platform, '-', x, y, 1, 0);
		printf("\tMade the stable platform.\n");
		return platform;
	} else {
		printf("\tThe character specifies a moving platform.\n");
		ActiveElement* platform = malloc(sizeof(ActiveElement));
		makePlatform((LevelElement*)platform, '-', x, y, 1, 1);
		if (specifier == 'L') {
			printf("\tThe character specifies a moving platform (Left).\n");
			//type 2
			//moving platform, moving left
			
		} else if (specifier == 'R') {
			printf("\tThe character specifies a moving platform (Right).\n");
			//type 2
			//moving platform, moving right
			
		} else if (specifier == 'U') {
			printf("\tThe character specifies a moving platform (Up).\n");
			//type 2
			//moving platform, moving up
			
		} else if (specifier == 'D') {
			printf("\tThe character specifies a moving platform (Down).\n");
			//type 2
			//moving platform, moving down
			
		} else {
			printf("\tThe character is unrecognised.\n");
			free(platform);
			return NULL;
		}
		return (LevelElement*)platform;
	}	
}

void makePlatform(LevelElement* platform, char rep, double x, double y, int tangible, int active) {
			platform->representation = rep;
			platform->x = x;
			platform->y = y;
			platform->tangible = tangible;
			platform->is_active = active;
}