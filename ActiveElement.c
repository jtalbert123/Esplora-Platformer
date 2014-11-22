#include "ActiveElement.h"
#include "Drawable.h"
#include "Utilities.h"
#include <windows.h>

void makePlatform(Drawable*, char rep, double x, double y, int tangible, int active);
int updateHorizontalPlatform(ActiveElement* this, Level* level);
int updateVerticalPlatform(ActiveElement* this, Level* level);
void applyMovingPlatform(ActiveElement* this);

Drawable* getLevelElement(char specifier, int x, int y) {
	if (specifier == '-') {
		//Drawable
		Drawable* platform = malloc(sizeof(Drawable));
		makePlatform(platform, '-', x, y, 1, 0);
		return platform;
	} else {
		ActiveElement* platform = malloc(sizeof(ActiveElement));
		makePlatform((Drawable*)platform, '-', x, y, 1, 1);
		if (specifier == 'L') {
			//type 2
			//moving platform, moving left
			platform->type = 1;
			int int_p[2] = { -1, -1 };
			double double_p[2] = { x, y };
			platform->double_properties = double_p;
			platform->int_properties = int_p;
			platform->update = updateHorizontalPlatform;
			platform->apply = applyMovingPlatform;
		} else if (specifier == 'R') {
			//type 2
			//moving platform, moving right
			platform->type = 1;
			int int_p[2] = { 1, 1 };
			double double_p[2] = { x, y };
			platform->double_properties = double_p;
			platform->int_properties = int_p;
			platform->update = updateHorizontalPlatform;
			platform->apply = applyMovingPlatform;
		} else if (specifier == 'U') {
			//type 2
			//moving platform, moving up
			platform->type = 2;
			int int_p[2] = { 1, 1 };
			double double_p[2] = { x, y };
			platform->double_properties = double_p;
			platform->int_properties = int_p;
			platform->update = updateVerticalPlatform;
			platform->apply = applyMovingPlatform;
		} else if (specifier == 'D') {
			//type 2
			//moving platform, moving down
			platform->type = 2;
			int int_p[2] = { -1, -1 };
			double double_p[2] = { x, y };
			platform->double_properties = double_p;
			platform->int_properties = int_p;
			platform->update = updateVerticalPlatform;
			platform->apply = applyMovingPlatform;
		} else {
			free(platform);
			return NULL;
		}
	}	
}

void makePlatform(Drawable* platform, char rep, double x, double y, int tangible, int active) {
			platform->representation = rep;
			platform->x = x;
			platform->y = y;
			platform->tangible = tangible;
			platform->is_active = active;
}

int updateHorizontalPlatform(ActiveElement* this, Level* level) {
	double x = this->x;
	double y = this->y;
	int dir = (this->double_properties)[0];
	double* new_x = this->double_properties;
	double* new_y = this->double_properties + 1;
	int* new_dir = this->int_properties + 1;
	
	if (dir == 1) {
		if (isItemToTheRight(x, y, 1, 1, level) || x >= level->width) {
			*new_dir = -1;
			*new_x = x;
			*new_y = y;
			return 1;
		} else {
			*new_x = x + SPEED;
			*new_y = y;
			*new_dir = dir;
			return 0;
		}
	} else {
		if (isItemToTheLeft(x, y, 1, 1, level) || x <= 0) {
			*new_dir = 1;
			*new_x = x;
			*new_y = y;
			return 1;
		} else {
			*new_x = x - SPEED;
			*new_y = y;
			*new_dir = dir;
			return 0;
		}
	}
}

int updateVerticalPlatform(ActiveElement* this, Level* level) {
	double x = this->x;
	double y = this->y;
	int dir = (this->double_properties)[0];
	double* new_x = this->double_properties;
	double* new_y = this->double_properties + 1;
	int* new_dir = this->int_properties + 1;
	
	if (dir == 1) {
		if (isItemAbove(x, y, 1, 1, level) || y <= 0) {
			*new_dir = -1;
			*new_x = x;
			*new_y = y;
			return 1;
		} else {
			*new_x = x + SPEED;
			*new_y = y;
			*new_dir = dir;
			return 0;
		}
	} else {
		if (isItemBelow(x, y, 1, 1, level) || y >= level->height) {
			*new_dir = 1;
			*new_x = x;
			*new_y = y;
			return 1;
		} else {
			*new_x = x - SPEED;
			*new_y = y;
			*new_dir = dir;
			return 0;
		}
	}
}

void applyMovingPlatform(ActiveElement* platform) {
	platform->x = (platform->double_properties)[0];
	platform->y = (platform->double_properties)[1];
	
	(platform->int_properties)[0] = (platform->int_properties)[1];
}

int isItemToTheRight(double x, double y, double x_threshold, double y_threshold, Level* level) {
	int i;
	for (i = 0; i < level->num_items; i++) {
		Drawable* item = (level->items)[i];
		if (abs(item->y - y) <= y_threshold) {
			if (item->x > x && abs(item->x - x) <= x_threshold) {
				return 1;
			}
		}
	}
	return 0;
}

int isItemToTheLeft(double x, double y, double x_threshold, double y_threshold, Level* level) {
	int i;
	for (i = 0; i < level->num_items; i++) {
		Drawable* item = (level->items)[i];
		if (abs(item->y - y) <= y_threshold) {
			if (item->x < x && abs(item->x - x) <= x_threshold) {
				return 1;
			}
		}
	}
	return 0;
}

int isItemAbove(double x, double y, double x_threshold, double y_threshold, Level* level) {
	int i;
	for (i = 0; i < level->num_items; i++) {
		Drawable* item = (level->items)[i];
		if (abs(item->x - x) <= x_threshold) {
			if (item->y < y && abs(item->y - y) <= y_threshold) {
				return 1;
			}
		}
	}
	return 0;
}

int isItemBelow(double x, double y, double x_threshold, double y_threshold, Level* level) {
	int i;
	for (i = 0; i < level->num_items; i++) {
		Drawable* item = (level->items)[i];
		if (abs(item->x - x) <= x_threshold) {
			if (item->y > y && abs(item->y - y) <= y_threshold) {
				return 1;
			}
		}
	}
	return 0;
}