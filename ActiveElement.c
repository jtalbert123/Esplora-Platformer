#include "ActiveElement.h"
#include "Utilities.h"
#include <windows.h>

#include <stdio.h>

int updateHorizontalPlatform(ActiveElement* this, Level* level);
int updateVerticalPlatform(ActiveElement* this, Level* level);
void applyMovingPlatform(ActiveElement* this);
LevelElement* itemToTheRight(double x, double y, double x_threshold, double y_threshold, Level* level);
LevelElement* itemToTheLeft(double x, double y, double x_threshold, double y_threshold, Level* level);
LevelElement* itemAbove(double x, double y, double x_threshold, double y_threshold, Level* level);
LevelElement* itemBelow(double x, double y, double x_threshold, double y_threshold, Level* level);
/*
int updateHorizontalPlatform(HMovingPlatform* this, Level* level) {
	LevelElement* basic = (LevelElement*)this;
	double x = basic->x;
	double y = basic->y;
	int dir = (this->double_properties)[0];
	double* new_x = this->double_properties;
	double* new_y = this->double_properties + 1;
	int* new_dir = this->int_properties + 1;
	
	if (dir == 1) {
		ActiveElement* right = itemToTheRight(x, y, 1, 1, level);
		int is_right_collidable = 0;
		is_right_collidable = (right != NULL && right->is_active && (((ActiveElement*)right)->int_properties)[0] != dir);
		if (x >= level->width || is_right_collidable) {
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
		ActiveElement* left = itemToTheLeft(x, y, 1, 1, level);
		int is_left_collidable = 0;
		is_left_collidable = (left != NULL && left->is_active && (((ActiveElement*)left)->int_properties)[0] != dir);
		if (x <= 0 || is_left_collidable) {
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
		if (itemAbove(x, y, 1, 1, level) || y <= 0) {
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
		if (itemBelow(x, y, 1, 1, level) || y >= level->height) {
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
*/
LevelElement* itemToTheRight(double x, double y, double x_threshold, double y_threshold, Level* level) {
	int i;
	for (i = 0; i < level->num_items; i++) {
		LevelElement* item = (level->items)[i];
		if (abs(item->y - y) <= y_threshold) {
			if (item->x > x && abs(item->x - x) <= x_threshold) {
				return item;
			}
		}
	}
	return NULL;
}

LevelElement* itemToTheLeft(double x, double y, double x_threshold, double y_threshold, Level* level) {
	int i;
	for (i = 0; i < level->num_items; i++) {
		LevelElement* item = (level->items)[i];
		if (abs(item->y - y) <= y_threshold) {
			if (item->x < x && abs(item->x - x) <= x_threshold) {
				return item;
			}
		}
	}
	return NULL;
}

LevelElement* itemAbove(double x, double y, double x_threshold, double y_threshold, Level* level) {
	int i;
	for (i = 0; i < level->num_items; i++) {
		LevelElement* item = (level->items)[i];
		if (abs(item->x - x) <= x_threshold) {
			if (item->y < y && abs(item->y - y) <= y_threshold) {
				return item;
			}
		}
	}
	return NULL;
}

LevelElement* itemBelow(double x, double y, double x_threshold, double y_threshold, Level* level) {
	int i;
	for (i = 0; i < level->num_items; i++) {
		LevelElement* item = (level->items)[i];
		if (abs(item->x - x) <= x_threshold) {
			if (item->y > y && abs(item->y - y) <= y_threshold) {
				return item;
			}
		}
	}
	return NULL;
}

void setSpeed(double speed) {
	SPEED = speed;
}